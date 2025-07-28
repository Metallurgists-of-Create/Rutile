package dev.metallurgists.rutile;

import com.tterrag.registrate.AbstractRegistrate;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePlugin;

@RutilePlugin
public class DefaultRutilePlugin implements IRutilePlugin {

    @Override
    public String getPluginNamespace() {
        return Rutile.ID;
    }

    public AbstractRegistrate<?> getRegistrate() {
        return Rutile.registrate();
    }
}
