package dev.metallurgists.rutile.registry;

import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.api.registrate.material.MaterialEntry;
import dev.metallurgists.rutile.registry.flags.IngotFlag;

import static dev.metallurgists.rutile.util.ClientUtil.toEnglishName;

public class RutileMaterials {
    public static MaterialEntry<Material> NULL;


    public static void register(RutileRegistrate registrate) {
        NULL = createMaterial("null", b -> b
                        .element(RutileElements.NULL)
                        .meltingPoint(9999.0)
                        .addFlags(
                            new IngotFlag().requiresCompacting()
                        )
                , registrate);
    }

    static MaterialEntry<Material> createMaterial(String name, NonNullUnaryOperator<Material.Builder> builder, RutileRegistrate registrate) {
        return registrate.material(name, Material.Builder::buildAndRegister).builder(builder).lang(toEnglishName(name)).register();
    }
}
