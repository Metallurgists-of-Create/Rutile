package dev.metallurgists.rutile.api.composition;

import com.mojang.serialization.DataResult;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.composition.element.ElementStack;
import dev.metallurgists.rutile.registry.RutileElements;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class FormulaParser {

    public static DataResult<SubComposition> parse(String formula) {
        try {
            Cursor cursor = new Cursor(formula);
            SubComposition composition = parseSubComposition(cursor);
            if (cursor.hasNext()) {
                return DataResult.error(() -> "Unexpected character at pos " + cursor.pos + ": '" + cursor.peek() + "'");
            }
            return DataResult.success(composition);
        } catch (Exception e) {
            return DataResult.error(() -> "Error processing formula '" + formula + "': " + e.getMessage());
        }
    }

    private static SubComposition parseSubComposition(Cursor cursor) {
        Deque<ParseFrame> stack = new ArrayDeque<>();
        stack.push(new ParseFrame(false, 1));

        int prefixAmount = 1;

        while (cursor.hasNext()) {
            cursor.skipWhitespace();
            if (!cursor.hasNext()) break;

            char ch = cursor.peek();

            if (ch == '+') {
                cursor.next();
                cursor.skipWhitespace();
                continue;
            }

            if (ch == '(') {
                cursor.next();
                stack.push(new ParseFrame(false, prefixAmount));
                prefixAmount = 1;
            } else if (ch == ')') {
                cursor.next();
                closeTopDotFrameIfNeeded(stack);

                if (stack.size() <= 1) {
                    throw new IllegalArgumentException("Unexpected closing ')'");
                }

                ParseFrame parenFrame = stack.pop();
                int suffixAmount = parseNumber(cursor);
                int totalAmount = parenFrame.amount * suffixAmount;

                SubComposition child = new SubComposition(parenFrame.elements, totalAmount, parenFrame.nested);
                stack.peek().nested.add(child);
            } else if (isDotChar(ch)) {
                cursor.next();
                cursor.skipWhitespace();

                closeTopDotFrameIfNeeded(stack);

                int amount = parseNumber(cursor);
                cursor.skipWhitespace();

                stack.push(new ParseFrame(true, amount));
            } else if (Character.isDigit(ch)) {
                int num = parseNumber(cursor);
                cursor.skipWhitespace();
                
                if (stack.peek().elements.isEmpty() && stack.peek().nested.isEmpty()) {
                    stack.peek().amount = num;
                } else {
                    prefixAmount = num;
                }
            } else if (ch == '^' || Character.isUpperCase(ch)) {
                int mass = -1;
                if (ch == '^') {
                    cursor.next();
                    mass = parseNumber(cursor);
                    cursor.skipWhitespace();
                    if (!cursor.hasNext() || !Character.isUpperCase(cursor.peek())) {
                        throw new IllegalArgumentException("Expected element symbol after '^" + mass + "'");
                    }
                }

                String symbol = parseSymbol(cursor);
                int suffixAmount = parseNumber(cursor);
                int totalAmount = prefixAmount * suffixAmount;
                prefixAmount = 1;

                Element element = RutileElements.getBySymbol(symbol);
                if (element == null) {
                    throw new IllegalArgumentException("Unregistered element: " + symbol);
                }

                ElementStack elementStack = (mass > 0)
                        ? ElementStack.of(element, (double) mass, totalAmount)
                        : ElementStack.of(element, totalAmount);

                stack.peek().elements.add(elementStack);
            } else {
                throw new IllegalArgumentException("Invalid character at pos " + cursor.pos + ": '" + ch + "'");
            }
        }

        closeTopDotFrameIfNeeded(stack);

        if (stack.size() > 1) {
            throw new IllegalArgumentException("Expected closing ')'");
        }

        ParseFrame rootFrame = stack.pop();
        return new SubComposition(rootFrame.elements, rootFrame.amount, rootFrame.nested);
    }

    private static void closeTopDotFrameIfNeeded(Deque<ParseFrame> stack) {
        if (stack.peek().isDotFrame) {
            ParseFrame dotFrame = stack.pop();
            SubComposition child = new SubComposition(dotFrame.elements, dotFrame.amount, dotFrame.nested);
            child.setHydrate(true);
            stack.peek().nested.add(child);
        }
    }

    public static String toFormulaString(SubComposition composition) {
        if (composition == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        if (composition.getAmount() > 1) {
            sb.append(composition.getAmount());
        }

        Deque<StringifyFrame> stack = new ArrayDeque<>();
        stack.push(new StringifyFrame(composition));

        while (!stack.isEmpty()) {
            StringifyFrame frame = stack.peek();

            if (!frame.headerProcessed) {
                for (ElementStack item : frame.node.getElements()) {
                    if (item.getMass() != item.getElement().getMass()) {
                        sb.append("^").append((long) item.getMass());
                    }
                    sb.append(item.getElement().getSymbol());
                    if (item.getAmount() > 1) {
                        sb.append(item.getAmount());
                    }
                }
                frame.headerProcessed = true;
            }

            if (frame.childIndex >= frame.node.getNested().size()) {
                stack.pop();
                if (!stack.isEmpty() && !frame.node.isHydrate()) {
                    sb.append(")");
                    if (frame.node.getAmount() > 1) {
                        sb.append(frame.node.getAmount());
                    }
                }
                continue;
            }

            SubComposition child = frame.node.getNested().get(frame.childIndex++);
            if (child.isHydrate()) {
                sb.append(" * ");
                if (child.getAmount() > 1) {
                    sb.append(child.getAmount());
                }
            } else {
                sb.append("(");
            }
            stack.push(new StringifyFrame(child));
        }

        return sb.toString();
    }

    private static boolean isDotChar(char ch) {
        return ch == '.' || ch == '*';
    }

    private static String parseSymbol(Cursor cursor) {
        StringBuilder sb = new StringBuilder();
        sb.append(cursor.next());
        while (cursor.hasNext() && Character.isLowerCase(cursor.peek())) {
            sb.append(cursor.next());
        }
        return sb.toString();
    }

    private static int parseNumber(Cursor cursor) {
        if (!cursor.hasNext() || !Character.isDigit(cursor.peek())) {
            return 1;
        }

        int num = 0;
        while (cursor.hasNext() && Character.isDigit(cursor.peek())) {
            num = num * 10 + (cursor.next() - '0');
        }

        return num;
    }

    private static class ParseFrame {
        final List<ElementStack> elements = new ArrayList<>();
        final List<SubComposition> nested = new ArrayList<>();
        final boolean isDotFrame;
        int amount;

        ParseFrame(boolean isDotFrame, int amount) {
            this.isDotFrame = isDotFrame;
            this.amount = amount;
        }
    }

    private static class StringifyFrame {
        final SubComposition node;
        int childIndex = 0;
        boolean headerProcessed = false;

        StringifyFrame(SubComposition node) {
            this.node = node;
        }
    }

    private static class Cursor {
        private final String text;
        private int pos = 0;

        public Cursor(String text) {
            this.text = text;
        }

        public boolean hasNext() {
            return pos < text.length();
        }

        public char peek() {
            return text.charAt(pos);
        }

        public char next() {
            return text.charAt(pos++);
        }

        public void skipWhitespace() {
            while (hasNext() && Character.isWhitespace(peek())) {
                pos++;
            }
        }
    }
}