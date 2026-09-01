package dev.metallurgists.rutile.datagen;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.data.provider.composition.FluidCompositionProvider;
import dev.metallurgists.rutile.api.data.provider.composition.ItemCompositionProvider;
import dev.metallurgists.rutile.registry.RutileElements;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DebugCompositions {
    public static class Item extends ItemCompositionProvider {
        public Item(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(Rutile.ID, output, registries);
        }

        @Override
        public void generate(HolderLookup.Provider registries) {
            addData(List.of(Items.IRON_INGOT, Items.IRON_NUGGET, Items.IRON_BLOCK, Items.RAW_IRON, Items.RAW_IRON_BLOCK), c -> c.element(RutileElements.Fe));
            addData(List.of(Items.COPPER_INGOT, Items.COPPER_BLOCK, Items.RAW_COPPER, Items.RAW_COPPER_BLOCK), c -> c.element(RutileElements.Cu));
            addData(List.of(Items.GOLD_INGOT, Items.GOLD_NUGGET, Items.GOLD_BLOCK, Items.RAW_GOLD, Items.RAW_GOLD_BLOCK), c -> c.element(RutileElements.Au));
            addData(List.of(Items.DIAMOND, Items.DIAMOND_BLOCK, Items.COAL, Items.COAL_BLOCK), c -> c.element(RutileElements.C));
            addData(List.of(Items.EMERALD, Items.EMERALD_BLOCK), c -> c
                    .element(RutileElements.Be, 3)
                    .element(RutileElements.Al, 2)
                    .element(RutileElements.Si, 6)
                    .element(RutileElements.O, 18));
            addData(List.of(Items.QUARTZ, Items.QUARTZ_BLOCK), c -> c
                    .element(RutileElements.Si)
                    .element(RutileElements.O, 2));
            addData(List.of(Items.AMETHYST_SHARD, Items.AMETHYST_BLOCK), c -> c
                    .element(RutileElements.Si)
                    .element(RutileElements.O, 2)
                    .element(RutileElements.Fe));
            addData(List.of(Items.LAPIS_LAZULI, Items.LAPIS_BLOCK), c -> c
                    .element(RutileElements.Na)
                    .element(RutileElements.Ca).next()
                    .element(RutileElements.Al, 6)
                    .element(RutileElements.Si, 6)
                    .element(RutileElements.O, 24).next()
                    .element(RutileElements.Fe)
                    .element(RutileElements.S, 2)
            );
        }
    }

    public static class Fluid extends FluidCompositionProvider {
        public Fluid(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(Rutile.ID, output, registries);
        }

        @Override
        public void generate(HolderLookup.Provider registries) {
            addData(Fluids.WATER, c -> c
                    .element(RutileElements.H, 2)
                    .element(RutileElements.O));
        }
    }
}
