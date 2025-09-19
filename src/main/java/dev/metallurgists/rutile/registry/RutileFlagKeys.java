package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePluginFinder;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.api.registrate.builder.FlagKeyEntry;
import dev.metallurgists.rutile.registry.flags.*;

public class RutileFlagKeys {

    private static final RutileRegistrate registrate = Rutile.registrate();

    public static FlagKeyEntry<FlagKey<IngotFlag>> INGOT = createFlag("ingot", IngotFlag.class);
    public static FlagKeyEntry<FlagKey<NuggetFlag>> NUGGET = createFlag("nugget", NuggetFlag.class);
    public static FlagKeyEntry<FlagKey<DustFlag>> DUST = createFlag("dust", DustFlag.class);
    public static FlagKeyEntry<FlagKey<GemFlag>> GEM = createFlag("gem",GemFlag.class);

    public static FlagKeyEntry<FlagKey<StorageBlockFlag>> STORAGE_BLOCK = createFlag("storage_block", StorageBlockFlag.class);


    public static void init() {
        RutilePluginFinder.getModPlugins().forEach(IRutilePlugin::registerFlags);
    }

    private static <C extends IMaterialFlag> FlagKeyEntry<FlagKey<C>> createFlag(String name, Class<C> type) {
        return registrate.flagKey(name, FlagKey::new, type).register();
    }
}
