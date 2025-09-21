package dev.metallurgists.rutile.registry.flags.fluid;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.FluidFlag;
import dev.metallurgists.rutile.api.material.flag.types.IFluidRegistry;
import dev.metallurgists.rutile.api.material.registry.fluid.FluidBuilder;
import dev.metallurgists.rutile.api.material.registry.fluid.IMaterialFluid;
import dev.metallurgists.rutile.registry.RutileFlagKeys;
import org.jetbrains.annotations.NotNull;

public class MoltenFlag extends FluidFlag {

    public MoltenFlag(String existingNamespace) {
        super("molten_%s", existingNamespace);
    }

    public MoltenFlag() {
        this("");
    }

    @Override
    public FluidEntry<? extends IMaterialFluid> registerFluid(@NotNull Material material, IFluidRegistry flag, @NotNull AbstractRegistrate<?> registrate) {
        return new FluidBuilder().build(material, getKey(), registrate);
    }

    @Override
    public FlagKey<? extends IFluidRegistry> getKey() {
        return RutileFlagKeys.MOLTEN;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
