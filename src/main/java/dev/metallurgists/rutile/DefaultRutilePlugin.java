package dev.metallurgists.rutile;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePlugin;
import dev.metallurgists.rutile.api.registrate.builder.MaterialBuilder;
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

        public static Material Dirt;

        public static void register() {
            Dirt = MaterialBuilder.create("dirt", Material::new)
                    .composition("3 americium", "19 bismuth")
                    .addFlags(
                            new IngotFlag()
                    ).createAndRegister();
        }

    }
}
