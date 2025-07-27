package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.registry.flags.*;

public class RutileMaterials {

    public static void init() {

    }

    public static Material Null = new Material.Builder(Rutile.id("null"))
            .element(RutileElements.NULL)
            .meltingPoint(9999.0)
            .addFlags(
                    new IngotFlag(),
                    new NuggetFlag(),
                    new DustFlag(),
                    new StorageBlockFlag(),
                    new GemFlag()
            ).buildAndRegister();

    // Minecraft Materials
    public static Material Iron = new Material.Builder(Rutile.id("iron"))
            .element(RutileElements.IRON)
            .meltingPoint(1538.0)
            .addFlags(
                    new IngotFlag("minecraft"),
                    new NuggetFlag("minecraft"),
                    new StorageBlockFlag("minecraft")
            ).buildAndRegister();

    public static Material Copper = new Material.Builder(Rutile.id("copper"))
            .element(RutileElements.COPPER)
            .meltingPoint(1084.6)
            .addFlags(
                    new IngotFlag("minecraft"),
                    new StorageBlockFlag("minecraft")
            ).buildAndRegister();

    public static Material Gold = new Material.Builder(Rutile.id("gold"))
            .element(RutileElements.GOLD)
            .meltingPoint(1064.2)
            .addFlags(
                    new IngotFlag("minecraft"),
                    new NuggetFlag("minecraft"),
                    new StorageBlockFlag("minecraft")
            ).buildAndRegister();

    public static Material Diamond = new Material.Builder(Rutile.id("diamond"))
            .element(RutileElements.CARBON)
            .addFlags(
                    new GemFlag("minecraft"),
                    new StorageBlockFlag("minecraft")
            ).buildAndRegister();

    public static Material Emerald = new Material.Builder(Rutile.id("emerald"))
            .composition(RutileElements.BERYLLIUM, 3, RutileElements.ALUMINUM, 2, RutileElements.SILICON, 6, RutileElements.OXYGEN, 18)
            .addFlags(
                    new GemFlag("minecraft"),
                    new StorageBlockFlag("minecraft")
            ).buildAndRegister();

    public static Material Quartz = new Material.Builder(Rutile.id("quartz"))
            .composition(RutileElements.SILICON, 1, RutileElements.OXYGEN, 2)
            .addFlags(
                    new GemFlag("minecraft").small(),
                    new StorageBlockFlag("minecraft")
            ).buildAndRegister();

    public static Material Amethyst = new Material.Builder(Rutile.id("amethyst"))
            .composition(RutileElements.SILICON, 1, RutileElements.OXYGEN, 2, RutileElements.IRON, 1)
            .addFlags(
                    new GemFlag("minecraft").small(),
                    new StorageBlockFlag("minecraft")
            ).buildAndRegister();
}
