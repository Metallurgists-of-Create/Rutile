package dev.metallurgists.rutile.api.dynamic_pack.data;

import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.composition.data.FinishedComposition;
import dev.metallurgists.rutile.api.composition.data.MaterialCompositionBuilder;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.material.MaterialRegistry;

import java.util.List;
import java.util.function.Consumer;

public class RuntimeCompositions {
    public static void compositionAddition(Consumer<FinishedComposition> originalConsumer) {
        for (MaterialRegistry registry : RutileAPI.materialManager.getRegistries()) {
            for (Material material : registry.getAllMaterials()) {
                createComposition(originalConsumer, material, material.getComposition());
            }
        }

    }

    protected static void createComposition(Consumer<FinishedComposition> pFinishedCompositionConsumer, Material material, List<SubComposition> subCompositions) {
        MaterialCompositionBuilder.create(material, subCompositions).save(pFinishedCompositionConsumer);
    }
}
