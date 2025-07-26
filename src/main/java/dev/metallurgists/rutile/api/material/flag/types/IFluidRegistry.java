package dev.metallurgists.rutile.api.material.flag.types;

import com.tterrag.registrate.util.entry.FluidEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.registry.fluid.IMaterialFluid;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.client.RutileModels;
import dev.metallurgists.rutile.util.ClientUtil;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface IFluidRegistry extends IFlagRegistry {

    FluidEntry<? extends IMaterialFluid> registerFluid(@NotNull Material material, IFluidRegistry flag, @NotNull RutileRegistrate registrate);

    @Override
    default String getUnlocalizedName(Material material) {
        String matSpecificKey = String.format("fluid.%s.%s", material.getNamespace(), getIdPattern().formatted(material.getName()));
        if (ClientUtil.langExists(matSpecificKey)) {
            return matSpecificKey;
        }

        return getUnlocalizedName();
    }

    @Override
    default String getUnlocalizedName() {
        return "materialflag." + (this instanceof ISpecialLangSuffix suffix ? suffix.getLangSuffix() : RutileModels.getFlagName(getKey()));
    }

    @Override
    default ResourceLocation getExistingId(Material material) {
        String nameAlternative = material.materialInfo().nameAlternatives().get(getKey());
        String namespace = getExistingNamespace() != null ? getExistingNamespace() : material.getNamespace();
        if (nameAlternative != null) {
            return new ResourceLocation(namespace, getIdPattern().formatted(nameAlternative));
        }
        return new ResourceLocation(namespace, getIdPattern().formatted(material.getName()));
    }

    FlagKey<?> getKey();
}
