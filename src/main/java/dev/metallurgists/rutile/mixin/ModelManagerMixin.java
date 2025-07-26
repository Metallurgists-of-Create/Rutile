package dev.metallurgists.rutile.mixin;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.client.MaterialBlockRenderer;
import dev.metallurgists.rutile.client.PillarMaterialBlockRenderer;
import dev.metallurgists.rutile.client.RutileModels;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fml.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(value = ModelManager.class)
public abstract class ModelManagerMixin {

    @Inject(method = "reload", at = @At(value = "HEAD"))
    private void rutile$loadDynamicModels(PreparableReloadListener.PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        if (!ModLoader.isLoadingStateValid()) return;

        long startTime = System.currentTimeMillis();
        MaterialBlockRenderer.reinitModels();
        PillarMaterialBlockRenderer.reinitModels();
        RutileModels.registerMaterialAssets();
        Rutile.LOGGER.info("Rutile Model loading took {}ms", System.currentTimeMillis() - startTime);
    }
}
