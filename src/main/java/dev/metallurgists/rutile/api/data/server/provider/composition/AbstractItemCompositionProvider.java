package dev.metallurgists.rutile.api.data.server.provider.composition;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractItemCompositionProvider extends AbstractCompositionProvider<Item> {

    public AbstractItemCompositionProvider(String modId, PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(modId, "item", output, fileHelper, registries);
    }

    @Override
    ResourceLocation getKey(Item value) {
        return BuiltInRegistries.ITEM.getKey(value);
    }
}
