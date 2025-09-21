package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.registry.fluid.FluidFlagProperties;
import dev.metallurgists.rutile.api.registrate.builder.MaterialBuilder;
import dev.metallurgists.rutile.registry.flags.*;
import dev.metallurgists.rutile.registry.flags.fluid.MoltenFlag;

public class RutileMaterials {

    public static void init() {

    }

    public static Material Null = MaterialBuilder.create("null", Material::new)
            .element("null")
            .meltingPoint(9999.0)
            .fluidProperty(FluidFlagProperties.b(RutileFlagKeys.MOLTEN).temperature(9999.0))
            .addFlags(
                    new IngotFlag(),
                    new NuggetFlag(),
                    new DustFlag(),
                    new StorageBlockFlag(),
                    new GemFlag(),
                    new MoltenFlag()
            ).createAndRegister();

    // Minecraft Materials
    public static Material Iron = MaterialBuilder.create("iron", Material::new)
            .element("iron")
            .meltingPoint(1538.0)
            .addFlags(
                    new IngotFlag("minecraft"),
                    new NuggetFlag("minecraft"),
                    new StorageBlockFlag("minecraft")
            ).createAndRegister();

    public static Material Copper = MaterialBuilder.create("copper", Material::new)
            .element("copper")
            .meltingPoint(1084.6)
            .addFlags(
                    new IngotFlag("minecraft"),
                    new StorageBlockFlag("minecraft")
            ).createAndRegister();

    public static Material Gold = MaterialBuilder.create("gold", Material::new)
            .element("gold")
            .meltingPoint(1064.2)
            .addFlags(
                    new IngotFlag("minecraft"),
                    new NuggetFlag("minecraft"),
                    new StorageBlockFlag("minecraft")
            ).createAndRegister();

    public static Material Diamond = MaterialBuilder.create("diamond", Material::new)
            .element("carbon")
            .existingIds(RutileFlagKeys.GEM, "minecraft:diamond")
            .addFlags(
                    new GemFlag("minecraft"),
                    new StorageBlockFlag("minecraft")
            ).createAndRegister();

    public static Material Emerald = MaterialBuilder.create("emerald", Material::new)
            .composition("3 beryllium", "2 aluminum", "6 silicon", "18 oxygen")
            .existingIds(RutileFlagKeys.GEM, "minecraft:emerald")
            .addFlags(
                    new GemFlag("minecraft"),
                    new StorageBlockFlag("minecraft")
            ).createAndRegister();

    public static Material Quartz = MaterialBuilder.create("quartz", Material::new)
            .composition("1 silicon", "2 oxygen")
            .existingIds(RutileFlagKeys.GEM, "minecraft:quartz")
            .addFlags(
                    new GemFlag("minecraft").small(),
                    new StorageBlockFlag("minecraft")
            ).createAndRegister();

    public static Material Amethyst = MaterialBuilder.create("amethyst", Material::new)
            .composition("1 silicon", "2 oxygen", "1 iron")
            .existingIds(RutileFlagKeys.GEM, "minecraft:amethyst_shard")
            .addFlags(
                    new GemFlag("minecraft").small(),
                    new StorageBlockFlag("minecraft")
            ).createAndRegister();
}
