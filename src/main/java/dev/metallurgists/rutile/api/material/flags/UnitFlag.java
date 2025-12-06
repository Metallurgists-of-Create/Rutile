package dev.metallurgists.rutile.api.material.flags;

import com.google.gson.JsonObject;
import lombok.Getter;

public abstract class UnitFlag<T> implements IMaterialFlag {

    @Getter
    private T value;

    public UnitFlag(T value) {
        this.value = value;
    }

    public void setValue(T value) {
        if (!validValue(value)) throw new IllegalArgumentException("Invalid value");
        this.value = value;
    }

    public boolean validValue(T value) {
        return true;
    };

    @Override
    public abstract JsonObject debugJson();
}
