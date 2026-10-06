package dev.metallurgists.rutile.util;

import com.google.common.base.CaseFormat;
import dev.metallurgists.rutile.RutileClient;
import net.createmod.catnip.utility.lang.LangBuilder;
import net.createmod.catnip.utility.lang.LangNumberFormat;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class StringFormatUtil {
    private static final int SMALL_DOWN_NUMBER_BASE = '₀'; //₀
    private static final int SMALL_UP_NUMBER_BASE = '⁰'; //⁰
    private static final int SMALL_UP_NUMBER_TWO = '²'; //²
    private static final int SMALL_UP_NUMBER_THREE = '³'; //³
    private static final int NUMBER_BASE = '0';

    public static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.##");

    public static String toSmallUpNumbers(String string) {
        return checkNumbers(string, SMALL_UP_NUMBER_BASE, true);
    }

    public static String toSmallDownNumbers(String string) {
        return checkNumbers(string, SMALL_DOWN_NUMBER_BASE, false);
    }

    @NotNull
    private static String checkNumbers(String string, int smallUpNumberBase, boolean isUp) {
        char[] charArray = string.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            int relativeIndex = charArray[i] - NUMBER_BASE;
            if (relativeIndex >= 0 && relativeIndex <= 9) {
                if (isUp) {
                    if (relativeIndex == 2 ) {
                        charArray[i] = SMALL_UP_NUMBER_TWO;
                        continue;
                    } else if (relativeIndex == 3) {
                        charArray[i] = SMALL_UP_NUMBER_THREE;
                        continue;
                    }
                }
                int newChar = smallUpNumberBase + relativeIndex;
                charArray[i] = (char) newChar;
            }
        }
        return new String(charArray);
    }

    public static String toLowerCaseUnder(String string) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, string);
    }

    public static String toEnglishName(Object internalName) {
        return Arrays.stream(internalName.toString().toLowerCase(Locale.ROOT).split("_"))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(" "));
    }

    public static LangBuilder number(double d) {
        return RutileClient.getLang().text(LangNumberFormat.format(d));
    }

    public static int convertRGBtoARGB(int colorValue) {
        return convertRGBtoARGB(colorValue, 0xFF);
    }

    public static int convertRGBtoARGB(int colorValue, int opacity) {
        // preserve existing opacity if present
        if (((colorValue >> 24) & 0xFF) != 0) return colorValue;
        return opacity << 24 | colorValue;
    }

    public static String toUpperNumbers(long number) {
        String str = String.valueOf(number);
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            switch (c) {
                case '0' -> sb.append('⁰');
                case '1' -> sb.append('¹');
                case '2' -> sb.append('²');
                case '3' -> sb.append('³');
                case '4' -> sb.append('⁴');
                case '5' -> sb.append('⁵');
                case '6' -> sb.append('⁶');
                case '7' -> sb.append('⁷');
                case '8' -> sb.append('⁸');
                case '9' -> sb.append('⁹');
                default -> sb.append(c);
            }
        }
        return sb.toString();
    }
}
