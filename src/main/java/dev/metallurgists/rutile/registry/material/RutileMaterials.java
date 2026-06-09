package dev.metallurgists.rutile.registry.material;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.RutileRegistrate;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.MaterialEntry;
import dev.metallurgists.rutile.registry.RutileElements;

import java.util.HashMap;
import java.util.Map;

public class RutileMaterials {
    public static final RutileMaterials INSTANCE = new RutileMaterials();

    private final Map<String, Material> materials = new HashMap<>();

    private static final RutileRegistrate registrate = Rutile.getRegistrate();

    public static final RegistryEntry<Material, Material>
            IRON1 = registrate.material("iron", c->
                            c.element(RutileElements.Fe))
                    .reg(),
            IRON11 = registrate.material("iron", c->
                            c.element(RutileElements.Fe))
                    .reg();

    public static final MaterialEntry
            IRON = registrate.material("iron", c->
                            c.element(RutileElements.Fe))
                    .register(),
            COPPER = registrate.material("copper", c->
                            c.element(RutileElements.Cu))
                    .register(),
            GOLD = registrate.material("gold", c->
                            c.element(RutileElements.Au))
                    .register(),
            DIAMOND = registrate.material("diamond", c ->
                            c.element(RutileElements.C))
                    .register(),
            EMERALD = registrate.material("emerald", c ->
                            c.element(RutileElements.Be, 3)
                            .element(RutileElements.Al, 2)
                            .element(RutileElements.Si, 6)
                            .element(RutileElements.O, 18))
                    .register(),
            QUARTZ = registrate.material("quartz", c ->
                            c.element(RutileElements.Si)
                            .element(RutileElements.O, 2)
                            .element(RutileElements.Fe))
                    .register(),
            AMETHYST = registrate.material("amethyst", c ->
                            c.element(RutileElements.Fe))
                    .register();

//    public Material getOrCreate(String name) {
//        return materials.computeIfAbsent(name, Material::new);
//
//    }
}
