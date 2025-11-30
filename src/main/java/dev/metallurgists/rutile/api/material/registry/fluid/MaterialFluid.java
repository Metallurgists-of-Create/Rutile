package dev.metallurgists.rutile.api.material.registry.fluid;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.MaterialFluidBuilder;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import org.jetbrains.annotations.NotNull;

public class MaterialFluid extends BaseFlowingFluid implements IMaterialFluid {
    public final Material material;
    public final MaterialFluidBuilder fluidBuilder;

    public MaterialFluid(Properties properties, Material material, MaterialFluidBuilder fluidBuilder) {
        super(properties);
        this.material = material;
        this.fluidBuilder = fluidBuilder;
    }

    @Override
    public boolean isSource(FluidState fluidState) {
        return true;
    }

    @Override
    public int getAmount(FluidState fluidState) {
        return 8;
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public MaterialFluidBuilder getBuilder() {
        return this.fluidBuilder;
    }

    public static class Source extends MaterialFluid {

        public Source(Properties properties, Material material, MaterialFluidBuilder fluidBuilder) {
            super(properties, material, fluidBuilder);
        }

        @Override
        public int getAmount(net.minecraft.world.level.material.FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(net.minecraft.world.level.material.FluidState state) {
            return true;
        }
    }

    public static class Flowing extends MaterialFluid {

        public Flowing(Properties properties, Material material, MaterialFluidBuilder fluidBuilder) {
            super(properties, material, fluidBuilder);
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        }

        protected void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }
}
