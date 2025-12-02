package dev.metallurgists.rutile.api.fluid.attribute;

import dev.metallurgists.rutile.api.fluid.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

public interface IAttributedFluid {

    @NotNull
    @Unmodifiable
    Collection<FluidAttribute> getAttributes();

    void addAttribute(@NotNull FluidAttribute attribute);

    @NotNull
    FluidState getState();
}
