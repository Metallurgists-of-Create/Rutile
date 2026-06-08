package dev.metallurgists.rutile.api.fluid.attribute;

import dev.metallurgists.rutile.Rutile;
import net.minecraft.network.chat.Component;

public final class FluidAttributes {

    public static final FluidAttribute ACID = new FluidAttribute(Rutile.getResource("acid"),
            list -> list.accept(Component.translatable("rutile.fluid.type_acid.tooltip")),
            list -> list.accept(Component.translatable("rutile.fluid_container.acid_proof")));

    private FluidAttributes() {}
}
