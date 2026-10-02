package dev.metallurgists.rutile.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJS;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.PluginConfig;
import dev.metallurgists.rutile.api.plugin.RutilePlugin;
import org.jetbrains.annotations.ApiStatus;

@RutilePlugin("kubejs")
@ApiStatus.Internal
public final class InternalRutileKubePlugin implements IRutilePlugin {
    @Override
    public void configure(PluginConfig config) {
        config.setModId(KubeJS.MOD_ID);
    }
}
