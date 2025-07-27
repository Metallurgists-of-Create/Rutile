package dev.metallurgists.rutile.api.material.flag;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

public class FlagKey<T extends IMaterialFlag> {

    @Getter
    private final ResourceLocation id;
    private final Class<T> type;

    public static final FlagKey<EmptyFlag> EMPTY = create("empty", EmptyFlag.class);

    public static <C extends IMaterialFlag> FlagKey<C> create(String key, Class<C> type) {
        return new FlagKey<>(Rutile.id(key), type);
    }

    public FlagKey(ResourceLocation id, Class<T> type) {
        this.id = id;
        this.type = type;
    }

    public T constructDefault() {
        try {
            return type.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public T cast(IMaterialFlag flag) {
        return this.type.cast(flag);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FlagKey) {
            return ((FlagKey<?>) o).getId().equals(id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public static class EmptyFlag implements IMaterialFlag {
        @Override
        public void verifyFlag(MaterialFlags flags) {
            // no-op
        }

        @Override
        public FlagKey<?> getKey() {
            return FlagKey.EMPTY;
        }
    }
}
