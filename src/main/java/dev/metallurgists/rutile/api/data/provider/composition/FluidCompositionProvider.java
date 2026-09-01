package dev.metallurgists.rutile.api.data.provider.composition;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateProvider;
import dev.metallurgists.rutile.datagen.RutileDataGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.fml.LogicalSide;

import java.util.concurrent.CompletableFuture;

public class FluidCompositionProvider extends AbstractCompositionProvider<Fluid> {

    public FluidCompositionProvider(String modId, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(modId, "fluid", output, registries);
    }

    @Override
    ResourceLocation getKey(Fluid value) {
        return BuiltInRegistries.FLUID.getKey(value);
    }

    public static class Registrate extends FluidCompositionProvider implements RegistrateProvider {
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
            this.parent.genData(RutileDataGen.FLUID_COMPOSITION, this);
        }
    }
}
