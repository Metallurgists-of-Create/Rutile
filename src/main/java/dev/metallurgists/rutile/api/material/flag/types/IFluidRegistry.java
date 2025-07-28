package dev.metallurgists.rutile.api.material.flag.types;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.registry.fluid.IMaterialFluid;
import dev.metallurgists.rutile.util.ClientUtil;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public interface IFluidRegistry extends IFlagRegistry {

    FluidEntry<? extends IMaterialFluid> registerFluid(@NotNull Material material, IFluidRegistry flag, @NotNull AbstractRegistrate<?> registrate);

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
        String flag = Util.makeDescriptionId("materialflag", getKey().getId());
        if (this instanceof ISpecialLangSuffix suffix && !Objects.equals(suffix.getLangSuffix(), "")) {
            flag += "." + suffix.getLangSuffix();
        }
        return flag;
    }

    @Override
    default ResourceLocation getExistingId(Material material) {
        ResourceLocation existingId = material.materialInfo().existingIds().get(getKey());
        if (existingId != null) return existingId;
        String nameAlternative = material.materialInfo().nameAlternatives().get(getKey());
        String namespace = !Objects.equals(getExistingNamespace(), "") ? getExistingNamespace() : material.getNamespace();
        String path = nameAlternative != null ? nameAlternative : material.getName();
        return new ResourceLocation(namespace, getIdPattern().formatted(path));
    }

    FlagKey<?> getKey();
}
