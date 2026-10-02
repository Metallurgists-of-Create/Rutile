package dev.metallurgists.rutile.compat.kubejs.registry;

import dev.latvian.mods.kubejs.KubeJS;
import dev.metallurgists.rutile.RutileRegistrate;

public final class KubeRegistrate {
    public static final RutileRegistrate INSTANCE;

    static {
        INSTANCE = RutileRegistrate.create(KubeJS.MOD_ID);
    }

    private KubeRegistrate() {}

}