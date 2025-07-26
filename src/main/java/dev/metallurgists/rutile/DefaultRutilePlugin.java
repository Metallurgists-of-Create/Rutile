package dev.metallurgists.rutile;

import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePlugin;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.registry.RutileElements;
import dev.metallurgists.rutile.registry.RutileFlagKeys;
import dev.metallurgists.rutile.registry.RutileMaterials;

@RutilePlugin
public class DefaultRutilePlugin implements IRutilePlugin {

    @Override
    public String getPluginNamespace() {
        return Rutile.ID;
    }

    public RutileRegistrate getRegistrate() {
        return Rutile.registrate();
    }

    @Override
    public void registerMaterials() {
        RutileMaterials.register(getRegistrate());
    }

    @Override
    public void registerElements() {
        RutileElements.register(getRegistrate());
    }

    @Override
    public void registerObjects() {
        IRutilePlugin.super.registerObjects();
    }

    @Override
    public void registerFlags() {
        RutileFlagKeys.register(getRegistrate());
    }
}
