package dev.metallurgists.rutile.api.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.material.base.Material;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.RegistryBuilder;

public class RutileRegistries {

    public static final ResourceKey<Registry<Element>> ELEMENT_KEY = Rutile.registrate().makeRegistry("element", () -> new RegistryBuilder<Element>().hasTags().allowModification().setDefaultKey(Rutile.asResource("null")));
    public static final ResourceKey<Registry<Material>> MATERIAL_KEY = Rutile.registrate().makeRegistry("material", () -> new RegistryBuilder<Material>().hasTags().setDefaultKey(Rutile.asResource("null")));

    public static void init() {
    }
}
