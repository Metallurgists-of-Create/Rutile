package dev.metallurgists.rutile.mixin;

import dev.metallurgists.rutile.util.IRutileTagLoader;
import dev.metallurgists.rutile.util.helpers.MixinHelpers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagLoader;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(value = TagLoader.class, priority = 500)
public class TagLoaderMixin implements IRutileTagLoader {

    @Nullable
    @Unique
    private Registry<?> rutile$storedRegistry;

    @Inject(method = "load", at = @At(value = "RETURN"))
    public void rutile$load(ResourceManager resourceManager,
                                  CallbackInfoReturnable<Map<ResourceLocation, List<TagLoader.EntryWithSource>>> cir) {
        if (rutile$storedRegistry == null) return;
        MixinHelpers.generateDynamicTags(cir.getReturnValue(), rutile$storedRegistry);
    }

    @Override
    public void rutile$setRegistry(Registry<?> registry) {
        this.rutile$storedRegistry = registry;
    }
}
