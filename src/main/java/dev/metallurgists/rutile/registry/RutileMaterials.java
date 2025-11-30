package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.api.material.FlagSources;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.BlockFlagContainer;
import dev.metallurgists.rutile.api.material.builder.FluidFlagContainer;
import dev.metallurgists.rutile.api.material.builder.ItemFlagContainer;
import dev.metallurgists.rutile.api.material.builder.block.StorageBlockBuilder;
import dev.metallurgists.rutile.api.material.builder.fluid.MoltenFluidBuilder;
import dev.metallurgists.rutile.api.material.builder.item.DustBuilder;
import dev.metallurgists.rutile.api.material.builder.item.GemBuilder;
import dev.metallurgists.rutile.api.material.builder.item.IngotBuilder;
import dev.metallurgists.rutile.api.material.builder.item.NuggetBuilder;
import dev.metallurgists.rutile.api.material.registry.fluid.FluidFlagProperties;
import dev.metallurgists.rutile.api.registrate.builder.MaterialBuilder;
import dev.metallurgists.rutile.api.registry.IRutileRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import static dev.metallurgists.rutile.api.registrate.builder.MaterialBuilder.flagsBuilder;

public class RutileMaterials {
    public static MaterialBuilder.FlagsBuilder<Item> itemBuilder() {return flagsBuilder(new ItemFlagContainer());}
    public static MaterialBuilder.FlagsBuilder<Block> blockBuilder() { return flagsBuilder(new BlockFlagContainer());}
    public static MaterialBuilder.FlagsBuilder<Fluid> fluidBuilder() { return flagsBuilder(new FluidFlagContainer());}

    public static Material Null = MaterialBuilder.create("null", Material::new)
            .element("null")
            .meltingPoint(9999.0)
            .fluidProperty(FluidFlagProperties.b(FlagSources.MOLTEN).temperature(9999.0))
            .flags(flags ->
                    flags.addFlag(FlagSources.INGOT, new IngotBuilder()))
            .addFlags(itemBuilder()
                    .addFlag(new IngotBuilder())
                    .addFlag(new GemBuilder())
                    .addFlag(new DustBuilder())
                    .addFlag(new NuggetBuilder())
            ).addFlags(blockBuilder()
                    .addFlag(new StorageBlockBuilder())
            ).addFlags(fluidBuilder()
                    .addFlag(new MoltenFluidBuilder())
            )
            .create();

    // Minecraft Materials
    public static Material Iron = MaterialBuilder.create("iron", Material::new)
            .element("iron")
            .meltingPoint(1538.0)
            .addFlags(itemBuilder()
                    .addFlag(FlagSources.INGOT, "iron_ingot")
                    .addFlag(FlagSources.NUGGET, "iron_nugget")
            ).addFlags(blockBuilder()
                    .addFlag(FlagSources.STORAGE_BLOCK, "iron_block")
            ).create();

    public static Material Copper = MaterialBuilder.create("copper", Material::new)
            .element("copper")
            .meltingPoint(1084.6)
            .addFlags(itemBuilder()
                    .addFlag(FlagSources.INGOT, "copper_ingot")
            ).addFlags(blockBuilder()
                    .addFlag(FlagSources.STORAGE_BLOCK, "copper_block")
            ).create();

    public static Material Gold = MaterialBuilder.create("gold", Material::new)
            .element("gold")
            .meltingPoint(1064.2)
            .addFlags(itemBuilder()
                    .addFlag(FlagSources.INGOT, "gold_ingot")
                    .addFlag(FlagSources.NUGGET, "gold_nugget")
            ).addFlags(blockBuilder()
                    .addFlag(FlagSources.STORAGE_BLOCK, "gold_block")
            ).create();

    public static Material Diamond = MaterialBuilder.create("diamond", Material::new)
            .element("carbon")
            .addFlags(itemBuilder()
                    .addFlag(FlagSources.GEM, "diamond")
            ).addFlags(blockBuilder()
                    .addFlag(FlagSources.STORAGE_BLOCK, "diamond_block")
            ).create();

    public static Material Emerald = MaterialBuilder.create("emerald", Material::new)
            .composition("3 beryllium", "2 aluminum", "6 silicon", "18 oxygen")
            .addFlags(itemBuilder()
                    .addFlag(FlagSources.GEM, "emerald")
            ).addFlags(blockBuilder()
                    .addFlag(FlagSources.STORAGE_BLOCK, "emerald_block")
            ).create();

    public static Material Quartz = MaterialBuilder.create("quartz", Material::new)
            .composition("1 silicon", "2 oxygen")
            .addFlags(itemBuilder()
                    .addFlag(FlagSources.GEM, "quartz")
            ).addFlags(blockBuilder()
                    .addFlag(FlagSources.STORAGE_BLOCK, "quartz_block")
            ).create();

    public static Material Amethyst = MaterialBuilder.create("amethyst", Material::new)
            .composition("1 silicon", "2 oxygen", "1 iron")
            .addFlags(itemBuilder()
                    .addFlag(FlagSources.GEM, "amethyst_shard")
            ).addFlags(blockBuilder()
                    .addFlag(FlagSources.STORAGE_BLOCK, "amethyst_block")
            ).create();

    public static void init(IRutileRegistry<Material> registry) {
        registry.register(Null);
        registry.register(Iron);
        registry.register(Copper);
        registry.register(Gold);
        registry.register(Diamond);
        registry.register(Emerald);
        registry.register(Quartz);
        registry.register(Amethyst);
    }
}
