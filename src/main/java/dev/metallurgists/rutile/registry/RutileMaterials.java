package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.api.registrate.builder.MaterialEntry;
import dev.metallurgists.rutile.registry.flags.*;

public class RutileMaterials {

    private static final RutileRegistrate registrate = Rutile.registrate();


    public static void init() {

    }

    public static MaterialEntry<Material> Null = registrate.material("null", Material::new)
            .element(RutileElements.NULL)
            .meltingPoint(9999.0)
            .addFlags(
                    new IngotFlag(),
                    new NuggetFlag(),
                    new DustFlag(),
                    new StorageBlockFlag(),
                    new GemFlag()
            ).register();

    // Minecraft Materials
    public static MaterialEntry<Material> Iron = registrate.material("iron", Material::new)
            .element(RutileElements.IRON)
            .meltingPoint(1538.0)
            .addFlags(
                    new IngotFlag("minecraft"),
                    new NuggetFlag("minecraft"),
                    new StorageBlockFlag("minecraft")
            ).register();



    public static MaterialEntry<Material> Copper = registrate.material("copper", Material::new)
            .element(RutileElements.COPPER)
            .meltingPoint(1084.6)
            .addFlags(
                    new IngotFlag("minecraft"),
                    new StorageBlockFlag("minecraft")
            ).register();

    public static MaterialEntry<Material> Gold = registrate.material("gold", Material::new)
            .element(RutileElements.GOLD)
            .meltingPoint(1064.2)
            .addFlags(
                    new IngotFlag("minecraft"),
                    new NuggetFlag("minecraft"),
                    new StorageBlockFlag("minecraft")
            ).register();

    public static MaterialEntry<Material> Diamond = registrate.material("diamond", Material::new)
            .element(RutileElements.CARBON)
            .existingIds(RutileFlagKeys.GEM, "minecraft:diamond")
            .addFlags(
                    new GemFlag("minecraft"),
                    new StorageBlockFlag("minecraft")
            ).register();

    public static MaterialEntry<Material> Emerald = registrate.material("emerald", Material::new)
            .composition(RutileElements.BERYLLIUM, 3, RutileElements.ALUMINUM, 2, RutileElements.SILICON, 6, RutileElements.OXYGEN, 18)
            .existingIds(RutileFlagKeys.GEM, "minecraft:emerald")
            .addFlags(
                    new GemFlag("minecraft"),
                    new StorageBlockFlag("minecraft")
            ).register();

    public static MaterialEntry<Material> Quartz = registrate.material("quartz", Material::new)
            .composition(RutileElements.SILICON, 1, RutileElements.OXYGEN, 2)
            .existingIds(RutileFlagKeys.GEM, "minecraft:quartz")
            .addFlags(
                    new GemFlag("minecraft").small(),
                    new StorageBlockFlag("minecraft")
            ).register();

    public static MaterialEntry<Material> Amethyst = registrate.material("amethyst", Material::new)
            .composition(RutileElements.SILICON, 1, RutileElements.OXYGEN, 2, RutileElements.IRON, 1)
            .existingIds(RutileFlagKeys.GEM, "minecraft:amethyst_shard")
            .addFlags(
                    new GemFlag("minecraft").small(),
                    new StorageBlockFlag("minecraft")
            ).register();
}
