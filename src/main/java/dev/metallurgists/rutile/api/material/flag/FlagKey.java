package dev.metallurgists.rutile.api.material.flag;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.registry.RutileRegistries;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

public class FlagKey<T extends IMaterialFlag> {

    private final Class<T> type;
    @Getter
    private final ResourceLocation id;

    public static final FlagKey<EmptyFlag> EMPTY = create("empty", EmptyFlag.class);

    public static <C extends IMaterialFlag> FlagKey<C> create(String name, Class<C> type) {
        return new FlagKey<>(name, type);
    }

    public FlagKey(String key, Class<T> type) {
        this.type = type;
        this.id = Rutile.id(key);
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

    public boolean isEmpty() {
        return this.type.isInstance(EMPTY);
    }

    @Override
    public String toString() {
        return getId().toString();
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
