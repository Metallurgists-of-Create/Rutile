package dev.metallurgists.rutile.api.data.provider.composition;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateProvider;
import dev.metallurgists.rutile.datagen.RutileDataGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.fml.LogicalSide;

import java.util.concurrent.CompletableFuture;

public class ItemCompositionProvider extends AbstractCompositionProvider<Item> {

    public ItemCompositionProvider(String modId, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(modId, "item", output, registries);
    }

    @Override
    ResourceLocation getKey(Item value) {
        return BuiltInRegistries.ITEM.getKey(value);
    }

    public static class Registrate extends ItemCompositionProvider implements RegistrateProvider {
        private final AbstractRegistrate<?> parent;

        public Registrate(AbstractRegistrate<?> parent, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(parent.getModid(), output, registries);
            this.parent = parent;
        }

        @Override
        public LogicalSide getSide() {
            return LogicalSide.SERVER;
        }

        public void generate(HolderLookup.Provider registries) {
            this.parent.genData(RutileDataGen.ITEM_COMPOSITION, this);
        }
    }
}
