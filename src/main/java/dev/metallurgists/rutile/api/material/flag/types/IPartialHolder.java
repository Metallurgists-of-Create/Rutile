package dev.metallurgists.rutile.api.material.flag.types;

import com.google.gson.JsonElement;
import dev.metallurgists.rutile.api.material.base.Material;
import net.minecraft.resources.ResourceLocation;

public interface IPartialHolder {
    ResourceLocation getModelLocation(Material material);

    JsonElement createModel(Material material);
}
