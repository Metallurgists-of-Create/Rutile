package dev.metallurgists.rutile.api.material;

import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.composition.SubComposition;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class MaterialInfo {
    public final ResourceLocation resourceLocation;

    @Getter
    @Setter
    public Composition composition;

    @Getter
    @Setter
    private int colour;

    public MaterialInfo(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

}
