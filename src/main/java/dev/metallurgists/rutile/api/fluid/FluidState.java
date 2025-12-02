package dev.metallurgists.rutile.api.fluid;

import dev.metallurgists.rutile.datagen.RutileTags;
import lombok.Getter;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

public enum FluidState {
    LIQUID("rutile.fluid_state.liquid", RutileTags.LIQUID_FLUIDS),
    GAS("gtceu.fluid_state.gas", Tags.Fluids.GASEOUS),
    PLASMA("gtceu.fluid_state.plasma", RutileTags.PLASMA_FLUIDS),
    ;

    @Getter
    private final String translationKey;
    @Getter
    private final TagKey<Fluid> tagKey;

    FluidState(@NotNull String translationKey, @NotNull TagKey<Fluid> tagKey) {
        this.translationKey = translationKey;
        this.tagKey = tagKey;
    }
}
