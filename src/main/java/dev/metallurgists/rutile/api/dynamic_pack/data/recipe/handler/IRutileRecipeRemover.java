package dev.metallurgists.rutile.api.dynamic_pack.data.recipe.handler;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public interface IRutileRecipeRemover {
    void removals(Consumer<ResourceLocation> registry);
}
