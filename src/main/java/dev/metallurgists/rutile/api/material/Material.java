package dev.metallurgists.rutile.api.material;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.registry.IDisplayedName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.Util;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@SuppressWarnings("unchecked")
@Accessors(chain = true)
public class Material implements FeatureElement, IDisplayedName {

    @Setter
    private FeatureFlagSet requiredFeatures = FeatureFlagSet.of();

    @Getter
    private final Composition composition;

    @Getter
    private @NotNull MaterialProperties properties;

    @Getter
    private final String name;

    private String descriptionId;

    public Material(String name, Composition comp, MaterialProperties prop) {
        this.name = name;
        properties = prop;
        composition = comp;
    }

    @Override
    public @NotNull FeatureFlagSet requiredFeatures() {
        return requiredFeatures;
    }

    public Material modify(Consumer<Material> consumer) {
        consumer.accept(this);
        return this;
    }

    @Override
    public String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("material", Rutile.getResource(getName()));
        }
        return this.descriptionId;
    }
}
