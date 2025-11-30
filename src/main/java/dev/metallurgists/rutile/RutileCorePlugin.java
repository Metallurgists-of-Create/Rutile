package dev.metallurgists.rutile;

import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.PluginConfig;
import dev.metallurgists.rutile.api.registry.IRutileRegistry;
import dev.metallurgists.rutile.registry.RutileElements;
import dev.metallurgists.rutile.registry.RutileMaterials;

public class RutileCorePlugin implements IRutilePlugin {

    @Override
    public void configure(PluginConfig config) {
        config.setModId(Rutile.ID);
    }

    @Override
    public void onRegisterMaterials(IRutileRegistry<Material> registry) {
        RutileMaterials.init(registry);
    }

    @Override
    public void onRegisterElements(IRutileRegistry<Element> registry) {
        RutileElements.init(registry);
    }

}
