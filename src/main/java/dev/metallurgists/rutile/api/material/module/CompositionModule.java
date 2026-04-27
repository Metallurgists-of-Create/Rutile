package dev.metallurgists.rutile.api.material.module;

import com.google.common.base.Preconditions;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.element.ElementStack;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import dev.metallurgists.rutile.registry.RutileModules;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record CompositionModule(Composition composition) implements MaterialModule<CompositionModule> {

    @Override
    public ModuleHolder<CompositionModule> getType() {
        return RutileModules.COMPOSITION;
    }

    public static final CompositionModule INSTANCE = new CompositionModule(Composition.builder().build());

    public Builder builder() {
        return new Builder();
    }

    public static class Builder implements ModuleBuilder<CompositionModule> {
        private List<SubComposition> subCompositions = new ArrayList<>();

        public Builder element(String element) {
            return composition("%s %s".formatted(1, element));
        }

        public Builder composition(String... components) {
            List<ElementStack> elementStacks = new ArrayList<>();
            subCompositions.clear();
            for (String raw : components) {
                String[] split = raw.split(" ", 2);
                int amount = Integer.parseInt(split[0]) <= 0 ? 1 : Integer.parseInt(split[0]);
                String element = split[1];
                if (element.isEmpty()) throw new IllegalArgumentException("Element is invalid or empty");
                ResourceLocation elementKey = Rutile.id(element);
                ElementStack elementStack = new ElementStack(elementKey, amount);
                elementStacks.add(elementStack);
            }
            SubComposition subComposition = new SubComposition(elementStacks, 1);
            subCompositions.add(subComposition);
            return this;
        }

        public Builder components(Object... components) {
            Preconditions.checkArgument(
                    components.length % 2 == 0,
                    "Composition Components list malformed!");
            subCompositions.clear();
            for (int i = 0; i < components.length; i += 2) {
                if (components[i] == null) {
                    throw new IllegalArgumentException(
                            "Material in Components List is null");
                }
                Material material = components[i] instanceof CharSequence chars ? RutileRegistries.MATERIALS.get(Rutile.id(chars.toString())) : (Material) components[i];
                if (material == null) {
                    throw new IllegalArgumentException("Material in Components List is null");
                }
                int amount = (Integer) components[i + 1];
                SubComposition.Builder subCompositionBuilder = SubComposition.builder();
                for (var subComp : material.getComposition().compositions()) {
                    subComp.getElements().forEach(subCompositionBuilder::element);
                }
                subCompositionBuilder.setAmount(amount);
                subCompositions.add(subCompositionBuilder.build());
            }
            return this;
        }

        public CompositionModule build() {
            return new CompositionModule(new Composition(this.subCompositions));
        }
    }
}
