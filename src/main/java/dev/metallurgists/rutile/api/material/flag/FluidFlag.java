package dev.metallurgists.rutile.api.material.flag;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.types.IFluidRegistry;
import dev.metallurgists.rutile.api.material.registry.fluid.IMaterialFluid;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class FluidFlag implements IMaterialFlag, IFluidRegistry {
    private final String idPattern;
    private String existingNamespace = "";

    public FluidFlag(String idPattern) {
        this.idPattern = idPattern;
    }

    public FluidFlag(String idPattern, String existingNamespace) {
        this.idPattern = idPattern;
        this.existingNamespace = existingNamespace;
    }

    public abstract FluidEntry<? extends IMaterialFluid> registerFluid(@NotNull Material material, IFluidRegistry flag, @NotNull AbstractRegistrate<?> registrate);
}
