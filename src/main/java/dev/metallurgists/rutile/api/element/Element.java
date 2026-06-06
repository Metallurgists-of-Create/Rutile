package dev.metallurgists.rutile.api.element;

import dev.metallurgists.rutile.api.registry.IDisplayedName;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;

public class Element implements IDisplayedName, FeatureElement, ElementLike {
    private String descriptionId;
    /**
     * -- GETTER --
     *  The id of this element, the modid is taken from the namespace for
     * ,
     *  and the path is used for
     *
     * @return the id of this element
     */
    @Getter
    private final ResourceLocation id;

    @Getter
    @Setter
    private String symbol;

    @Getter
    @Setter
    private int color;

    @Getter
    @Setter
    private FeatureFlagSet requiredFeatures;

    public Element(String symbol, int color, ResourceLocation id) {
        this.symbol = symbol;
        this.color = color;
        this.id = id;
        this.requiredFeatures = FeatureFlags.VANILLA_SET;
    }

    public Element(String symbol, int color, ResourceLocation id, FeatureFlag... requiredFeatures) {
        this.symbol = symbol;
        this.color = color;
        this.id = id;
        this.requiredFeatures = FeatureFlags.REGISTRY.subset(requiredFeatures);
    }

    /**
     * The internal name of this element.
     * This is used for registration, so it MUST be all lowercase with underscores for spaces
     * @return the internal name of this element
     */
    public String getName() {
        return this.getId().getPath();
    }

    /**
     * The modid of the mod that registered this element
     * @return the modid of this element
     */
    public String getModId() {
        return this.getId().getNamespace();
    }

    @Override
    public String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("element", getId());
        }
        return this.descriptionId;
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return this.requiredFeatures;
    }

    @Override
    public Element asElement() {
        return this;
    }
}
