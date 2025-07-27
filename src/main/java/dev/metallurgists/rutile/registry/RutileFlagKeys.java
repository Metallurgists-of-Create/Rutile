package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePluginFinder;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.registry.flags.*;
import net.minecraft.resources.ResourceLocation;

public class RutileFlagKeys {

    public static FlagKey<IngotFlag> INGOT = createFlag("ingot", IngotFlag.class);
    public static FlagKey<NuggetFlag> NUGGET = createFlag("nugget", NuggetFlag.class);
    public static FlagKey<DustFlag> DUST = createFlag("dust", DustFlag.class);
    public static FlagKey<GemFlag> GEM = createFlag("gem", GemFlag.class);

    public static FlagKey<StorageBlockFlag> STORAGE_BLOCK = createFlag("storage_block", StorageBlockFlag.class);


    public static void init() {
        RutilePluginFinder.getModPlugins().forEach(IRutilePlugin::registerFlags);
    }

    private static <C extends IMaterialFlag> FlagKey<C> createFlag(String name, Class<C> type) {
        ResourceLocation location = Rutile.id(name);
        return RutileAPI.registerFlag(location, FlagKey.create(name, type));
    }
}
