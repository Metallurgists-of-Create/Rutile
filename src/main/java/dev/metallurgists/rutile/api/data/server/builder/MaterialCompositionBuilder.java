package dev.metallurgists.rutile.api.data.server.builder;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.composition.FinishedComposition;
import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.material.Material;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class MaterialCompositionBuilder {
    private final Material material;
    private final Composition composition;

    public MaterialCompositionBuilder(Material material, Composition composition) {
        this.material = material;
        this.composition = composition;
    }

    public static MaterialCompositionBuilder create(Material material, Composition composition) {
        return new MaterialCompositionBuilder(material, composition);
    }

    static ResourceLocation getDefaultId(Material material) {
        return material.getId();
    }

    public void save(Consumer<FinishedComposition> pConsumer) {
        pConsumer.accept(new DataGenResult(this.material, this.composition));
    }

    public static class DataGenResult implements FinishedComposition {
        private final Material material;
        private final Composition composition;

        public DataGenResult(Material material, Composition composition) {
            this.material = material;
            this.composition = composition;
        }

        @Override
        public void serializeData(JsonObject json) {
            SubComposition.CODEC.listOf().encodeStart(JsonOps.INSTANCE, composition.compositions()).ifSuccess(j -> json.add("compositions", j));
        }

        @Override
        public ResourceLocation getId() {
            return material.getId();
        }
    }
}
