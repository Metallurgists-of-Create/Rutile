package dev.metallurgists.rutile.mixin;

import com.google.gson.JsonElement;
import dev.metallurgists.rutile.api.dynamic_pack.data.recipe.RutileRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = RecipeManager.class, priority = 500)
public abstract class RecipeManagerEarlyMixin {

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At("HEAD"))
    private void rutile$removeRecipes(Map<ResourceLocation, JsonElement> map, ResourceManager pResourceManager,
                                      ProfilerFiller pProfiler, CallbackInfo ci) {
        RutileRecipes.recipeRemoval(map::remove);
    }}
