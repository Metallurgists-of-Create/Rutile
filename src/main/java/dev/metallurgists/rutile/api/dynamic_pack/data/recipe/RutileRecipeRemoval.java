package dev.metallurgists.rutile.api.dynamic_pack.data.recipe;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class RutileRecipeRemoval {
    public static void init(Consumer<ResourceLocation> registry) {
        generalRemovals(registry);
    }

    private static void generalRemovals(Consumer<ResourceLocation> registry) {

    }
}
