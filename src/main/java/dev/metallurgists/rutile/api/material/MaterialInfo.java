package dev.metallurgists.rutile.api.material;

import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.composition.SubComposition;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;

import java.util.List;

public class MaterialInfo {
    public final ResourceLocation resourceLocation;

    @Getter
    @Setter
    public Composition composition;

    @Getter
    @Setter
    private int colour;

    @Getter
    @Setter
    private FeatureFlagSet requiredFeatures;

    public MaterialInfo(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
        this.requiredFeatures = FeatureFlags.VANILLA_SET;
    }

}
