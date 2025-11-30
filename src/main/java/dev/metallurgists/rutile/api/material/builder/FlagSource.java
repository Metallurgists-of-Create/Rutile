package dev.metallurgists.rutile.api.material.builder;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.util.ClientUtil;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public record FlagSource<T>(String modid, String name, FlagRegistryType<T> registryType) {

    public FlagSource(ResourceLocation location, FlagRegistryType<T> registryType) {
        this(location.getNamespace(), location.getPath(), registryType);
    }

    public String getUnlocalizedName(Material material, String nameFormat) {
        ResourceLocation objectKey = ResourceLocation.fromNamespaceAndPath(material.getNamespace(), nameFormat.formatted(material.getName()));
        String matSpecificKey = Util.makeDescriptionId(registryType().resourceKey().registry().getPath(), objectKey);
        if (ClientUtil.langExists(matSpecificKey)) {
            return matSpecificKey;
        }
        return getUnlocalizedName();
    }

    public String getUnlocalizedName() {
        String type = registryType().resourceKey().registry().getPath();
        return "flagSource.%s.%s".formatted(type, rlForm());
    }

    public MutableComponent getLocalizedName(Material material, String nameFormat) {
        return Component.translatable(getUnlocalizedName(material, nameFormat), material.getDisplayName());
    }

    public String rlForm() {
        return modid() + "_" + name();
    }
}
