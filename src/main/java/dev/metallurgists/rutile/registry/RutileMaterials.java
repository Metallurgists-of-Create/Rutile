package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.element.Element;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.registry.IRutileRegistry;
import dev.metallurgists.rutile.api.registry.flags.BurnableFlag;
import dev.metallurgists.rutile.api.registry.flags.HarvestTierFlag;
import dev.metallurgists.rutile.api.registry.flags.PillarModelFlag;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import static dev.metallurgists.rutile.registry.RutileTagPrefixes.*;
import static dev.metallurgists.rutile.registry.RutileTagPrefixes.Nugget;

public class RutileMaterials {

    public static Material Null = new Material.Builder(Rutile.id("null"))
            .element("null")
            .colour(0xffbf4cd2)
            .flag(FlagKey.INGOT)
            .fluid()
            .flag(FlagKey.HARVEST_TIER)
            .flag(FlagKey.BURNABLE, new BurnableFlag(60))
            .flag(FlagKey.PILLAR_MODEL, new PillarModelFlag(RutileTagPrefixes.Block, RutileTagPrefixes.BlockPillar))
            .build();

    public static Material Iron = new Material.Builder(Rutile.id("iron"))
            .element("iron")
            .colour(0xff949496)
            .flag(FlagKey.INGOT)
            .flag(FlagKey.HARVEST_TIER, new HarvestTierFlag(1))
            .build();

    public static Material Copper = new Material.Builder(Rutile.id("copper"))
            .element("copper")
            .colour(0xffdcb491)
            .flag(FlagKey.INGOT)
            .flag(FlagKey.HARVEST_TIER, new HarvestTierFlag(1))
            .build();

    public static Material Gold = new Material.Builder(Rutile.id("gold"))
            .element("gold")
            .colour(0xffd1c186)
            .flag(FlagKey.INGOT)
            .flag(FlagKey.HARVEST_TIER, new HarvestTierFlag(2))
            .build();

    public static Material Diamond = new Material.Builder(Rutile.id("diamond"))
            .element("carbon")
            .colour(0xffa1fbe8)
            .flag(FlagKey.GEM)
            .flag(FlagKey.HARVEST_TIER, new HarvestTierFlag(2))
            .build();

    public static Material Emerald = new Material.Builder(Rutile.id("emerald"))
            .composition("3 beryllium", "2 aluminum", "6 silicon", "18 oxygen")
            .colour(0xff41f384)
            .flag(FlagKey.GEM)
            .flag(FlagKey.HARVEST_TIER, new HarvestTierFlag(2))
            .build();

    public static Material Quartz = new Material.Builder(Rutile.id("quartz"))
            .composition("1 silicon", "2 oxygen")
            .colour(0xffd4caba)
            .flag(FlagKey.GEM)
            .flag(FlagKey.HARVEST_TIER, new HarvestTierFlag(0))
            .build();

    public static Material Amethyst = new Material.Builder(Rutile.id("amethyst"))
            .composition("1 silicon", "2 oxygen", "1 iron")
            .colour(0xffcfa0f3)
            .flag(FlagKey.GEM)
            .flag(FlagKey.HARVEST_TIER, new HarvestTierFlag(0))
            .build();

    public static void init() {
        configurePrefixes();
    }

    public static void configurePrefixes() {
        Ingot.setIgnored(RutileMaterials.Iron, Items.IRON_INGOT);
        Ingot.setIgnored(RutileMaterials.Gold, Items.GOLD_INGOT);
        Ingot.setIgnored(RutileMaterials.Copper, Items.COPPER_INGOT);
        Gem.setIgnored(RutileMaterials.Diamond, Items.DIAMOND);
        Gem.setIgnored(RutileMaterials.Emerald, Items.EMERALD);
        Gem.setIgnored(RutileMaterials.Quartz, Items.QUARTZ);
        Gem.setIgnored(RutileMaterials.Amethyst, Items.AMETHYST_SHARD);
        Nugget.setIgnored(RutileMaterials.Iron, Items.IRON_NUGGET);
        Nugget.setIgnored(RutileMaterials.Gold, Items.GOLD_NUGGET);
        Nugget.setIgnored(RutileMaterials.Copper);

        Block.setIgnored(RutileMaterials.Iron, Blocks.IRON_BLOCK);
        Block.setIgnored(RutileMaterials.Gold, Blocks.GOLD_BLOCK);
        Block.setIgnored(RutileMaterials.Copper, Blocks.COPPER_BLOCK);
        Block.setIgnored(RutileMaterials.Diamond, Blocks.DIAMOND_BLOCK);
        Block.setIgnored(RutileMaterials.Emerald, Blocks.EMERALD_BLOCK);
        Block.setIgnored(RutileMaterials.Quartz, Blocks.QUARTZ_BLOCK);
        Block.setIgnored(RutileMaterials.Amethyst, Blocks.AMETHYST_BLOCK);
        Block.modifyMaterialAmount(RutileMaterials.Quartz, TagPrefix.M * 4);
        Block.modifyMaterialAmount(RutileMaterials.Amethyst, TagPrefix.M * 4);
    }
}
