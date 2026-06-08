package dev.metallurgists.rutile.api.material;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.material.module.MaterialModule;
import dev.metallurgists.rutile.api.material.module.ModuleHolder;
import dev.metallurgists.rutile.api.registry.IDisplayedName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.Util;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unchecked")
@Accessors(chain = true)
public class MaterialData implements FeatureElement, IDisplayedName {

    @Setter
    private FeatureFlagSet requiredFeatures = FeatureFlagSet.of();

    @Setter @Getter
    private Composition composition = Composition.EMPTY;

    @NotNull
    private final Map<ModuleHolder<?>, MaterialModule<?>> modules = new HashMap<>();

    @Getter
    private final String name;

    private String descriptionId;

    public MaterialData(String name) {
        this.name = name;
    }

    @Override
    public @NotNull FeatureFlagSet requiredFeatures() {
        return requiredFeatures;
    }

    public MaterialData modify(Consumer<MaterialData> consumer) {
        consumer.accept(this);
        return this;
    }

    public <T extends MaterialModule<T>> Optional<T> getModule(ModuleHolder<T> module) {
        if (this.modules.containsKey(module)) {
            return Optional.ofNullable((T) this.modules.get(module));
        }
        return Optional.empty();
    }

    public <T extends MaterialModule<T>> MaterialData addModule(ModuleHolder<T> module, Function<T, T> action) {
        if (this.modules.containsKey(module)) {
            T oldModule = (T)this.modules.get(module);
            this.modules.put(module, action.apply(oldModule));
        } else this.modules.put(module, action.apply(module.get()));
        return this;
    }

    public <T extends MaterialModule<T>> MaterialData modifyModule(ModuleHolder<T> module, Function<T, T> action) {
        if (this.modules.containsKey(module)) {
            T oldModule = (T)this.modules.get(module);
            this.modules.put(module, action.apply(oldModule));
        }
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
