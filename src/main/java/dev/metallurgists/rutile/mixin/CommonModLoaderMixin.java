package dev.metallurgists.rutile.mixin;

import dev.metallurgists.rutile.api.material.base.MaterialRegistrationEvents;
import net.neoforged.fml.ModLoader;
import net.neoforged.neoforge.internal.CommonModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.Executor;

@Mixin(CommonModLoader.class)
public class CommonModLoaderMixin {

    @Inject(method = "load(Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)V", at = @At("TAIL"))
    private static void load(Executor syncExecutor, Executor parallelExecutor, CallbackInfo ci) {
        Runnable periodicTask = () -> {};

        ModLoader.runInitTask("Material Registration events", syncExecutor, periodicTask, MaterialRegistrationEvents::modifyComponents);
    }
}
