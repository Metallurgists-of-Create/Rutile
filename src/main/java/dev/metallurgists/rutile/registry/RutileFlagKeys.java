package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePluginFinder;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.registry.flags.*;
import dev.metallurgists.rutile.registry.flags.fluid.MoltenFlag;
import net.minecraft.resources.ResourceLocation;

public class RutileFlagKeys {

    public static final FlagKey<IngotFlag> INGOT = createFlag("ingot", IngotFlag.class);
    public static final FlagKey<NuggetFlag> NUGGET = createFlag("nugget", NuggetFlag.class);
    public static final FlagKey<DustFlag> DUST = createFlag("dust", DustFlag.class);
    public static final FlagKey<GemFlag> GEM = createFlag("gem",GemFlag.class);

    public static final FlagKey<StorageBlockFlag> STORAGE_BLOCK = createFlag("storage_block", StorageBlockFlag.class);

    public static final FlagKey<MoltenFlag> MOLTEN = createFlag("molten", MoltenFlag.class);

    public static void init() {
        RutilePluginFinder.getModPlugins().forEach(IRutilePlugin::registerFlags);
    }

    public static <C extends IMaterialFlag> FlagKey<C> createFlag(String name, Class<C> type) {
        ResourceLocation location = Rutile.id(name);
        return RutileAPI.registerFlag(location, FlagKey.create(name, type));
    }
}
