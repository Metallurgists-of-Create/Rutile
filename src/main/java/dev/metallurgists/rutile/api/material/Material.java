package dev.metallurgists.rutile.api.material;

import dev.metallurgists.rutile.api.registry.IDisplayedName;
import lombok.Getter;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

public class Material implements IDisplayedName {
    private String descriptionId;
    /**
     * -- GETTER --
     *  The id of this material, the modid is taken from the namespace for
     * ,
     *  and the path is used for
     *
     * @return the id of this material
     */
    @Getter
    private final ResourceLocation id;

    public Material(ResourceLocation id) {
        this.id = id;
    }

    /**
     * The internal name of this material.
     * This is used for registration, so it MUST be all lowercase with underscores for spaces
     * @return the internal name of this material
     */
    public String getName() {
        return this.getId().getPath();
    }

    /**
     * The modid of the mod that registered this material
     * @return the modid of this material
     */
    public String getModId() {
        return this.getId().getNamespace();
    }

    @Override
    public String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("material", getId());
        }
        return this.descriptionId;
    }
}
