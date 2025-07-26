package dev.metallurgists.rutile.api.plugin;

import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.dynamic_pack.data.recipe.handler.*;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.registry.block.RutileMaterialBlocks;
import dev.metallurgists.rutile.api.material.registry.fluid.RutileMaterialFluids;
import dev.metallurgists.rutile.api.material.registry.item.RutileMaterialItems;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.api.registry.RutileAPI;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface IRutilePlugin {

    String getPluginNamespace();

    default IRutileRecipeHandler getRuntimeRecipes() { return new EmptyRecipeHandler(); };

    default IRutileMaterialRecipeHandler getRuntimeMaterialRecipes() { return  new EmptyMaterialRecipeHandler(); };

    default IRutileRecipeRemover getRuntimeRecipeRemover() { return new EmptyRecipeRemover(); };

    /**
        Register this in you mod container
        @see dev.metallurgists.rutile.api.registrate.RutileRegistrate#registerEventListeners(net.minecraftforge.eventbus.api.IEventBus)
     */
    default RutileRegistrate getRegistrate() {
        return RutileRegistrate.create(getPluginNamespace());
    }

    default void registerMaterials() {

    }

    default void registerElements() {

    }

    default void registerFlags() {

    }

    default void registerObjects() {
        // Items
        RutileMaterialItems.generateMaterialItems(getRegistrate(), getPluginNamespace());
        RutileMaterialItems.MATERIAL_ITEMS = RutileMaterialItems.MATERIAL_ITEMS_BUILDER.build();
        RutileMaterialItems.MATERIAL_ITEMS_BUILDER = null;
        // Blocks
        RutileMaterialBlocks.generateMaterialBlocks(getRegistrate(), getPluginNamespace());
        RutileMaterialBlocks.MATERIAL_BLOCKS = RutileMaterialBlocks.MATERIAL_BLOCKS_BUILDER.build();
        RutileMaterialBlocks.MATERIAL_BLOCKS_BUILDER = null;
        // Fluids
        RutileMaterialFluids.generateMaterialFluids(getRegistrate(), getPluginNamespace());
        RutileMaterialFluids.MATERIAL_FLUIDS = RutileMaterialFluids.MATERIAL_FLUIDS_BUILDER.build();
        RutileMaterialFluids.MATERIAL_FLUIDS_BUILDER = null;
    }

    default List<Material> getMaterials() {
        return RutileAPI.getRegisteredMaterials().entrySet().stream().filter(e -> e.getKey().getNamespace().equals(getPluginNamespace())).map(Map.Entry::getValue).collect(Collectors.toList());
    }

    default List<Element> getElements() {
        return RutileAPI.getRegisteredElements().entrySet().stream().filter(e -> e.getKey().getNamespace().equals(getPluginNamespace())).map(Map.Entry::getValue).collect(Collectors.toList());
    }

    default List<FlagKey<?>> getFlags() {
        return RutileAPI.getRegisteredFlags().entrySet().stream().filter(e -> e.getKey().getNamespace().equals(getPluginNamespace())).map(Map.Entry::getValue).collect(Collectors.toList());
    }
}
