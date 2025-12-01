package dev.metallurgists.rutile.api.runtime.data.recipe.handler;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public interface IRutileRecipeRemover {
    void removals(Consumer<ResourceLocation> registry);

    class Empty implements IRutileRecipeRemover {
        @Override
        public void removals(Consumer<ResourceLocation> registry) {}
    }
}
