package dev.metallurgists.rutile.api.material.flag.types;

import dev.metallurgists.rutile.api.material.base.Material;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public interface IFlagRegistry extends IIdPattern {

    String getExistingNamespace();

    ResourceLocation getExistingId(Material material);

    default MutableComponent getLocalizedName(Material material) {
        return Component.translatable(getUnlocalizedName(material), material.getDisplayName());
    }

    String getUnlocalizedName();

    String getUnlocalizedName(Material material);
}
