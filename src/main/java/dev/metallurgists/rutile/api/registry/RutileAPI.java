package dev.metallurgists.rutile.api.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;
import dev.metallurgists.rutile.api.material.modifier.MaterialGroup;
import dev.metallurgists.rutile.api.material.modifier.MaterialModifier;
import dev.metallurgists.rutile.api.registry.material.IMaterialRegistryManager;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.fml.event.IModBusEvent;

import java.util.HashMap;
import java.util.Map;

public class RutileAPI {
    @Getter
    private static final Map<ResourceLocation, FlagKey<?>> registeredFlags = new HashMap<>();

    public static Rutile instance;

    public static IMaterialRegistryManager materialManager;

    public static <C extends IMaterialFlag> FlagKey<C> registerFlag(ResourceLocation key, FlagKey<C> entry) {
        registeredFlags.put(key, entry);
        return entry;
    }

    public static class RegisterEvent<K, V> extends GenericEvent<V> implements IModBusEvent {

        private final RutileRegistry<K, V> registry;

        public RegisterEvent(RutileRegistry<K, V> registry, Class<V> clazz) {
            super(clazz);
            this.registry = registry;
        }

        public void register(K key, V value) {
            if (registry != null) registry.register(key, value);
        }

        public static class RL<V> extends RegisterEvent<ResourceLocation, V> {

            public RL(RutileRegistry<ResourceLocation, V> registry, Class<V> clazz) {
                super(registry, clazz);
            }
        }

        public static class String<V> extends RegisterEvent<java.lang.String, V> {

            public String(RutileRegistry<java.lang.String, V> registry, Class<V> clazz) {
                super(registry, clazz);
            }
        }
    }
}
