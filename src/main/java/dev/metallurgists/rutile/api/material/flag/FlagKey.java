package dev.metallurgists.rutile.api.material.flag;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import net.minecraft.resources.ResourceLocation;

public class FlagKey<T extends IMaterialFlag> {

    private final String key;
    private final Class<T> type;

    public static final FlagKey<EmptyFlag> EMPTY = create("empty", EmptyFlag.class);

    public static <C extends IMaterialFlag> FlagKey<C> create(String key, Class<C> type) {
        FlagKey<C> flag = new  FlagKey<>(key, type);
        ResourceLocation rl = new ResourceLocation(Rutile.ID, key);
        return RutileAPI.registerFlag(rl, flag);
    }

    public FlagKey(String key, Class<T> type) {
        this.key = key;
        this.type = type;
    }

    protected String getKey() {
        return key;
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
            return ((FlagKey<?>) o).getKey().equals(key);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return key;
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
