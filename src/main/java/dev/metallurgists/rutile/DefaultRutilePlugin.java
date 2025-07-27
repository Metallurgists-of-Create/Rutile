package dev.metallurgists.rutile;

import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePlugin;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;

@RutilePlugin
public class DefaultRutilePlugin implements IRutilePlugin {

    @Override
    public String getPluginNamespace() {
        return Rutile.ID;
    }

    public RutileRegistrate getRegistrate() {
        return Rutile.registrate();
    }
}
