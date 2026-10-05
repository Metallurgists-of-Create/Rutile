package dev.metallurgists.rutile.datagen;

import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.composition.element.ElementLike;
import dev.metallurgists.rutile.api.data.provider.composition.FluidCompositionProvider;
import dev.metallurgists.rutile.api.data.provider.composition.ItemCompositionProvider;
import dev.metallurgists.rutile.registry.RutileElements;
import dev.metallurgists.rutile.registry.RutileTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DefaultCompositions {
    public static class Item extends ItemCompositionProvider {
        public Item(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(Rutile.ID, output, registries);
        }

        @Override
        public void generate(HolderLookup.Provider registries) {
            addData("iron",
                    List.of(Items.IRON_NUGGET, Items.IRON_BLOCK, Items.RAW_IRON, Items.RAW_IRON_BLOCK),
                    List.of(ItemTags.IRON_ORES, RutileTags.Items.INGOT_IRON.tag),
                    c -> c
                            .element(RutileElements.Fe)
            );
            addData("copper",
                    List.of(Items.COPPER_BLOCK, Items.RAW_COPPER, Items.RAW_COPPER_BLOCK),
                    List.of(ItemTags.COPPER_ORES, RutileTags.Items.INGOT_COPPER.tag),
                    c -> c
                            .element(RutileElements.Cu)
            );
            addData("gold",
                    List.of(Items.GOLD_NUGGET, Items.GOLD_BLOCK, Items.RAW_GOLD, Items.RAW_GOLD_BLOCK),
                    List.of(ItemTags.GOLD_ORES, RutileTags.Items.INGOT_GOLD.tag),
                    c -> c
                            .element(RutileElements.Au)
            );
            addData("coal",
                    List.of(Items.COAL, Items.COAL_BLOCK, Items.CHARCOAL),
                    c -> c
                            .element(RutileElements.C)
            );
            addData("diamond",
                    List.of(Items.DIAMOND, Items.DIAMOND_BLOCK),
                    c -> c
                            .element(RutileElements.C)
            );
            addData("emerald",
                    List.of(Items.EMERALD, Items.EMERALD_BLOCK),
                    c -> c
                            .element(RutileElements.Be, 3)
                            .element(RutileElements.Al, 2)
                            .element(RutileElements.Si, 6)
                            .element(RutileElements.O, 18)
            );
            addData("quartz",
                    List.of(Items.QUARTZ, Items.QUARTZ_BLOCK), c -> c
                            .element(RutileElements.Si)
                            .element(RutileElements.O, 2)
            );
            addData("amethyst",
                    List.of(Items.AMETHYST_SHARD, Items.AMETHYST_BLOCK),
                    c -> c
                            .element(RutileElements.Si)
                            .element(RutileElements.O, 2)
                            .element(RutileElements.Fe)
            );
            addData("lapis",
                    List.of(Items.LAPIS_LAZULI, Items.LAPIS_BLOCK),
                    c -> c
                            .element(RutileElements.Na)
                            .element(RutileElements.Ca).next()
                            .element(RutileElements.Al, 6)
                            .element(RutileElements.Si, 6)
                            .element(RutileElements.O, 24).next()
                            .element(RutileElements.Fe)
                            .element(RutileElements.S, 2)
            );

            // Integration Compositions
            compat("lead", RutileElements.Pb, "storage_blocks/", "ingots/", "nuggets/", "plates/", "ores/", "raw_materials/", "storage_blocks/raw_");
            compat("nickel", RutileElements.Ni, "storage_blocks/", "ingots/", "nuggets/", "plates/", "ores/", "raw_materials/", "storage_blocks/raw_");
            compat("lithium", RutileElements.Li, "storage_blocks/", "ingots/", "nuggets/", "plates/", "ores/", "raw_materials/", "storage_blocks/raw_");
            compat("aluminum", RutileElements.Al, "storage_blocks/", "ingots/", "nuggets/", "plates/", "ores/", "raw_materials/", "storage_blocks/raw_");
            compat("zinc", RutileElements.Zn, "storage_blocks/", "ingots/", "nuggets/", "plates/", "ores/", "raw_materials/", "storage_blocks/raw_");
            compat("tin", RutileElements.Sn, "storage_blocks/", "ingots/", "nuggets/", "plates/", "ores/", "raw_materials/", "storage_blocks/raw_");
            compat("osmium", RutileElements.Os, "storage_blocks/", "ingots/", "nuggets/", "plates/", "ores/", "raw_materials/", "storage_blocks/raw_");
            compat("uranium", RutileElements.U, "storage_blocks/", "ingots/", "nuggets/", "plates/", "ores/", "raw_materials/", "storage_blocks/raw_");
            compat("tungsten", RutileElements.W, "storage_blocks/", "ingots/", "nuggets/", "plates/", "ores/", "raw_materials/", "storage_blocks/raw_");
            compat("platinum", RutileElements.Pt, "storage_blocks/", "ingots/", "nuggets/", "plates/", "ores/", "raw_materials/", "storage_blocks/raw_");
            compat("silicon", RutileElements.Si, "storage_blocks/", "ingots/", "nuggets/", "plates/", "ores/", "raw_materials/", "storage_blocks/raw_");
            // Alloys
            compat("steel", RutileElements.Fe, "storage_blocks/", "ingots/", "nuggets/", "plates/", "ores/", "raw_materials/", "storage_blocks/raw_");
            compat("brass", c -> c.element(RutileElements.Cu).element(RutileElements.Zn), "storage_blocks/", "ingots/", "nuggets/", "plates/", "ores/", "raw_materials/", "storage_blocks/raw_");
            compat("constantan", c -> c.element(RutileElements.Cu).element(RutileElements.Ni), "storage_blocks/", "ingots/", "nuggets/", "plates/", "ores/", "raw_materials/", "storage_blocks/raw_");
            compat("bronze", c -> c.element(RutileElements.Cu).element(RutileElements.Sn), "storage_blocks/", "ingots/", "nuggets/", "plates/", "ores/", "raw_materials/", "storage_blocks/raw_");
            // Misc
            compat("sulfur", RutileElements.S, "dusts/");

        }

        private void compat(String material, ElementLike element, String... tags) {
            List<TagKey<net.minecraft.world.item.Item>> tagKeys = Arrays.stream(tags).map((p) -> ItemTags.create(Rutile.cId(p + material))).toList();
            addTags(material, tagKeys, c -> c.element(element));
        }

        private void compat(String material, NonNullFunction<Composition.Builder<net.minecraft.world.item.Item>, Composition.Builder<net.minecraft.world.item.Item>> composition, String... tags) {
            List<TagKey<net.minecraft.world.item.Item>> tagKeys = Arrays.stream(tags).map((p) -> ItemTags.create(Rutile.cId(p + material))).toList();
            addTags(material, tagKeys, composition);
        }
    }

    public static class Fluid extends FluidCompositionProvider {
        public Fluid(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(Rutile.ID, output, registries);
        }

        @Override
        public void generate(HolderLookup.Provider registries) {
            addData("water", Fluids.WATER, c -> c
                    .element(RutileElements.H, 2)
                    .element(RutileElements.O)
            );
        }
    }
}
