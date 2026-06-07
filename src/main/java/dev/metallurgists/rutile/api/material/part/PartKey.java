package dev.metallurgists.rutile.api.material.part;

import lombok.Getter;

@Getter
public class PartKey<T> {
    private final Part<T> part;


    public PartKey(Part<T> part) {
        this.part = part;
    }

}
