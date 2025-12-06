package dev.metallurgists.rutile.api.material.flags;

import com.google.gson.JsonObject;
import lombok.Getter;

public abstract class BiUnitFlag<A, B> implements IMaterialFlag {

    @Getter
    private A firstValue;

    @Getter
    private B secondValue;

    public BiUnitFlag(A first, B second) {
        this.firstValue = first;
        this.secondValue = second;
    }

    public void setValue(A first, B second) {
        this.firstValue = validateFirst(first);
        this.secondValue = validateSecond(second);
    }

    public A validateFirst(A first) {
        return first;
    }

    public B validateSecond(B second) {
        return second;
    }

    @Override
    public abstract JsonObject debugJson();
}
