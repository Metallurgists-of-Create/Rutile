package dev.metallurgists.rutile.api.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;
import dev.metallurgists.rutile.api.registry.material.MaterialRegistry;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class RutileAPI {
    @Getter
    private static final Map<ResourceLocation, FlagKey<?>> registeredFlags = new HashMap<>();

    public static Rutile instance;

    public static MaterialRegistry materialRegistry;

    public static <C extends IMaterialFlag> FlagKey<C> registerFlag(ResourceLocation key, FlagKey<C> entry) {
        registeredFlags.put(key, entry);
        return entry;
    }
}
