package dev.metallurgists.rutile.api.element;

public interface ElementLike {
    Element asElement();

    default ElementStack asStack() {
        return ElementStack.of(asElement());
    }

    default ElementStack asStack(int amount) {
        return ElementStack.of(asElement(), amount);
    }
}
