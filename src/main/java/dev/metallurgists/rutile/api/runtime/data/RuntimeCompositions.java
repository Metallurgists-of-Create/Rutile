package dev.metallurgists.rutile.api.runtime.data;

import dev.metallurgists.rutile.api.RutileApi;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.composition.FinishedComposition;
import dev.metallurgists.rutile.api.data.server.builder.MaterialCompositionBuilder;
import dev.metallurgists.rutile.api.material.Material;

import java.util.function.Consumer;

public class RuntimeCompositions {
    public static void compositionAddition(Consumer<FinishedComposition> originalConsumer) {
        for (Material material : RutileApi.getMaterialRegistry().getAll()) {
            if (material.getComposition() != null) {
                createComposition(originalConsumer, material, material.getComposition());
            }
        }
    }

    protected static void createComposition(Consumer<FinishedComposition> pFinishedCompositionConsumer, Material material, Composition subCompositions) {
        MaterialCompositionBuilder.create(material, subCompositions).save(pFinishedCompositionConsumer);
    }
}
