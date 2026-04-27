package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.material.module.CompositionModule;
import dev.metallurgists.rutile.api.material.module.UnitSizeModule;
import dev.metallurgists.rutile.api.material.registry.AxisMaterialBlock;
import dev.metallurgists.rutile.api.registry.flags.customisation.HarvestTierFlag;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import dev.metallurgists.rutile.util.ModelHelpers;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import static dev.metallurgists.rutile.registry.RutileBlockSources.*;
import static dev.metallurgists.rutile.registry.RutileItemSources.*;

public class RutileMaterials {

    public static Material Null = new Material.Builder(Rutile.id("null"))
            .addModule(RutileModules.COMPOSITION, CompositionModule.INSTANCE.builder().element("null"))
            .addVariable(RutileVariableKeys.COLOUR, 0xffbf4cd2)
            .build();

    public static Material Iron = new Material.Builder(Rutile.id("iron"))
            .addModule(RutileModules.COMPOSITION, CompositionModule.INSTANCE.builder().element("iron"))
            .addVariable(RutileVariableKeys.COLOUR, 0xff949496)
            .addVariable(RutileVariableKeys.HARVEST_TIER, 1)
            .flag(FlagKey.INGOT)
            .flag(FlagKey.ORE)
            .build();

    public static Material Copper = new Material.Builder(Rutile.id("copper"))
            .addModule(RutileModules.COMPOSITION, CompositionModule.INSTANCE.builder().element("copper"))
            .addVariable(RutileVariableKeys.COLOUR, 0xffdcb491)
            .addVariable(RutileVariableKeys.HARVEST_TIER, 1)
            .flag(FlagKey.INGOT)
            .flag(FlagKey.ORE)
            .build();

    public static Material Gold = new Material.Builder(Rutile.id("gold"))
            .addModule(RutileModules.COMPOSITION, CompositionModule.INSTANCE.builder().element("gold"))
            .addVariable(RutileVariableKeys.COLOUR, 0xffd1c186)
            .addVariable(RutileVariableKeys.HARVEST_TIER, 2)
            .flag(FlagKey.INGOT)
            .flag(FlagKey.ORE)
            .build();

    public static Material Diamond = new Material.Builder(Rutile.id("diamond"))
            .addModule(RutileModules.COMPOSITION, CompositionModule.INSTANCE.builder().element("carbon"))
            .addVariable(RutileVariableKeys.COLOUR, 0xffa1fbe8)
            .addVariable(RutileVariableKeys.HARVEST_TIER, 2)
            .flag(FlagKey.GEM)
            .flag(FlagKey.ORE)
            .build();

    public static Material Emerald = new Material.Builder(Rutile.id("emerald"))
            .addModule(RutileModules.COMPOSITION, CompositionModule.INSTANCE.builder().composition("3 beryllium", "2 aluminum", "6 silicon", "18 oxygen"))
            .addVariable(RutileVariableKeys.COLOUR, 0xff41f384)
            .addVariable(RutileVariableKeys.HARVEST_TIER, 2)
            .flag(FlagKey.GEM)
            .flag(FlagKey.ORE)
            .build();

    public static Material Quartz = new Material.Builder(Rutile.id("quartz"))
            .addModule(RutileModules.COMPOSITION, CompositionModule.INSTANCE.builder().composition("1 silicon", "2 oxygen"))
            .addVariable(RutileVariableKeys.COLOUR, 0xffd4caba)
            .addVariable(RutileVariableKeys.HARVEST_TIER, 0)
            .flag(FlagKey.GEM)
            .flag(FlagKey.ORE)
            .build();

    public static Material Amethyst = new Material.Builder(Rutile.id("amethyst"))
            .addModule(RutileModules.COMPOSITION, CompositionModule.INSTANCE.builder().composition("1 silicon", "2 oxygen", "1 iron"))
            .addVariable(RutileVariableKeys.COLOUR, 0xffcfa0f3)
            .addVariable(RutileVariableKeys.HARVEST_TIER, 0)
            .flag(FlagKey.GEM)
            .build();

    public static void init() {
        configurePrefixes();
    }

    public static void configurePrefixes() {
        Iron.addModule(RutileModules.ITEM_REDIRECT, (module) -> module
                .redirect(RutileRegisterKeys.Ingot, () -> Items.IRON_INGOT)
                .redirect(RutileRegisterKeys.Nugget, () -> Items.IRON_NUGGET)
                .redirect(RutileRegisterKeys.RawOre, () -> Items.RAW_IRON)
                .redirect(RutileRegisterKeys.StorageBlock, () -> Items.IRON_BLOCK)
                .redirect(RutileRegisterKeys.RawOreBlock, () -> Items.RAW_IRON_BLOCK));
        Gold.addModule(RutileModules.ITEM_REDIRECT, (module) -> module
                .redirect(RutileRegisterKeys.Ingot, () -> Items.GOLD_INGOT)
                .redirect(RutileRegisterKeys.Nugget, () -> Items.GOLD_NUGGET)
                .redirect(RutileRegisterKeys.RawOre, () -> Items.RAW_GOLD)
                .redirect(RutileRegisterKeys.StorageBlock, () -> Items.GOLD_BLOCK)
                .redirect(RutileRegisterKeys.RawOreBlock, () -> Items.RAW_GOLD_BLOCK));
        Copper.addModule(RutileModules.ITEM_REDIRECT, (module) -> module
                .redirect(RutileRegisterKeys.Ingot, () -> Items.COPPER_INGOT)
                .redirect(RutileRegisterKeys.RawOre, () -> Items.RAW_COPPER)
                .redirect(RutileRegisterKeys.StorageBlock, () -> Items.COPPER_BLOCK)
                .redirect(RutileRegisterKeys.RawOreBlock, () -> Items.RAW_COPPER_BLOCK))
                .addModule(RutileModules.IGNORE, (module) -> module.ignore(RutileRegisterKeys.Nugget));
        Diamond.addModule(RutileModules.ITEM_REDIRECT, (module) -> module
                .redirect(RutileRegisterKeys.Gem, () -> Items.DIAMOND)
                .redirect(RutileRegisterKeys.StorageBlock, () -> Items.DIAMOND_BLOCK));
        Emerald.addModule(RutileModules.ITEM_REDIRECT, (module) -> module
                .redirect(RutileRegisterKeys.Gem, () -> Items.EMERALD)
                .redirect(RutileRegisterKeys.StorageBlock, () -> Items.EMERALD_BLOCK));
        Quartz.addModule(RutileModules.ITEM_REDIRECT, (module) -> module
                .redirect(RutileRegisterKeys.Gem, () -> Items.QUARTZ)
                .redirect(RutileRegisterKeys.StorageBlock, () -> Items.QUARTZ_BLOCK))
                .addModule(RutileModules.UNIT_SIZE, (module) -> module.setSize(RutileRegisterKeys.StorageBlock, UnitSizeModule.UNIT * 4));
        Amethyst.addModule(RutileModules.ITEM_REDIRECT, (module) -> module
                .redirect(RutileRegisterKeys.Gem, () -> Items.AMETHYST_SHARD)
                .redirect(RutileRegisterKeys.StorageBlock, () -> Items.AMETHYST_BLOCK))
                .addModule(RutileModules.UNIT_SIZE, (module) -> module.setSize(RutileRegisterKeys.StorageBlock, UnitSizeModule.UNIT * 4));
    }
}
