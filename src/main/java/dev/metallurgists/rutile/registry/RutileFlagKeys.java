package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.registry.flags.IngotFlag;
import net.minecraft.resources.ResourceLocation;

public class RutileFlagKeys {

    public static FlagKey<IngotFlag> INGOT;


    public static void register(RutileRegistrate registrate) {
        INGOT = createFlag("ingot", IngotFlag.class, registrate);
    }

    private static <C extends IMaterialFlag> FlagKey<C> createFlag(String name, Class<C> type, RutileRegistrate registrate) {
        ResourceLocation location = new ResourceLocation(registrate.getModid(), name);
        return RutileAPI.registerFlag(location, FlagKey.create(name, type));
    }
}
