package dev.metallurgists.rutile;

import dev.metallurgists.rutile.api.element.Element;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.PluginConfig;
import dev.metallurgists.rutile.api.registry.IRutileRegistry;
import dev.metallurgists.rutile.registry.RutileElements;
import dev.metallurgists.rutile.registry.RutileMaterials;

public class RutileCorePlugin implements IRutilePlugin {
    @Override
    public void configure(PluginConfig config) {
        config.setModId(Rutile.ID);
        config.setRegistrate(Rutile.registrate());
    }

    @Override
    public void onRegisterMaterials(IRutileRegistry<Material> registry) {
        registry.register(RutileMaterials.Iron);
        registry.register(RutileMaterials.Gold);
        registry.register(RutileMaterials.Copper);
    }

    @Override
    public void onRegisterElements(IRutileRegistry<Element> registry) {
        RutileElements.init(registry);
    }
}
