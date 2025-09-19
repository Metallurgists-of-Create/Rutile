package dev.metallurgists.rutile;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePlugin;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.api.registrate.builder.MaterialEntry;
import dev.metallurgists.rutile.registry.RutileElements;
import dev.metallurgists.rutile.registry.flags.IngotFlag;

@RutilePlugin
public class DefaultRutilePlugin implements IRutilePlugin {

    @Override
    public String getPluginNamespace() {
        return "testplugin";
    }

    @Override
    public void registerMaterials() {
        TestMaterials.register();
    }

    static class TestMaterials {
        private static final RutileRegistrate registrate = Rutile.registrate();

        public static MaterialEntry<Material> Dirt;

        public static void register() {
            Dirt = registrate.material("dirt", Material::new)
                    .composition(RutileElements.AMERICIUM, 3, RutileElements.BISMUTH, 19)
                    .addFlags(
                            new IngotFlag()
                    ).register();
        }

    }
}
