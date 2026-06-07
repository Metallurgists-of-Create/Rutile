package dev.metallurgists.rutile.api.material;

import dev.metallurgists.rutile.api.composition.Composition;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@Accessors(chain = true)
public class MaterialData implements FeatureElement {

    @Setter
    private FeatureFlagSet requiredFeatures = FeatureFlagSet.of();

    @Setter @Getter
    private Composition composition = Composition.EMPTY;

    @Override
    public @NotNull FeatureFlagSet requiredFeatures() {
        return requiredFeatures;
    }

    public MaterialData modify(Consumer<MaterialData> consumer) {
        consumer.accept(this);
        return this;
    }
}
