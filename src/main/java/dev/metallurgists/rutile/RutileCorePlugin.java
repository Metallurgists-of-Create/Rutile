package dev.metallurgists.rutile;

import dev.metallurgists.rutile.api.composition.RutileCompositions;
import dev.metallurgists.rutile.api.data.server.manager.composition.FluidCompositionManager;
import dev.metallurgists.rutile.api.data.server.manager.composition.ItemCompositionManager;
import dev.metallurgists.rutile.api.data.server.manager.composition.MaterialCompositionManager;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.PluginConfig;

public class RutileCorePlugin implements IRutilePlugin {
    @Override
    public void configure(PluginConfig config) {
        config.setModId(Rutile.ID);
        config.setRegistrate(Rutile.registrate());
    }

    @Override
    public void collectCompositionManagers(RutileCompositions handler) {
        handler.addManager(MaterialCompositionManager.getInstance());
        handler.addManager(ItemCompositionManager.getInstance());
        handler.addManager(FluidCompositionManager.getInstance());
    }
}
