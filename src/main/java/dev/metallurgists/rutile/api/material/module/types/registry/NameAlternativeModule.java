package dev.metallurgists.rutile.api.material.module.types.registry;

import dev.metallurgists.rutile.api.material.module.MaterialModule;
import dev.metallurgists.rutile.api.material.module.ModuleHolder;
import dev.metallurgists.rutile.api.material.part.PartKey;
import dev.metallurgists.rutile.registry.RutileModules;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

public class NameAlternativeModule implements MaterialModule<NameAlternativeModule> {

    private final Map<PartKey<?>, String> linkedNames;

    public NameAlternativeModule() {
        this.linkedNames = new HashMap<>();
    }

    public Optional<String> getAlternative(PartKey<?> partKey) {
        if (hasAlternative(partKey))
            return Optional.of(linkedNames.get(partKey));
        return Optional.empty();
    }

    public boolean hasAlternative(PartKey<?> partKey) {
        return linkedNames.containsKey(partKey);
    }

    public NameAlternativeModule add(PartKey<?> partKey, String name) {
        linkedNames.put(partKey, name);
        return this;
    }

    public NameAlternativeModule remove(PartKey<?> partKey) {
        linkedNames.remove(partKey);
        return this;
    }

    public NameAlternativeModule removeIf(BiPredicate<PartKey<?>, String> predicate) {
        for (var entry : linkedNames.entrySet()) {
            if (predicate.test(entry.getKey(), entry.getValue()))
                linkedNames.remove(entry.getKey());
        }
        return this;
    }

    @Override
    public ModuleHolder<NameAlternativeModule> getType() {
        return RutileModules.NAME_ALTERNATIVE;
    }

    @Override
    public ModuleBuilder<NameAlternativeModule> builder() {
        return new Builder();
    }

    public static class Builder implements ModuleBuilder<NameAlternativeModule> {
        private final Map<PartKey<?>, String> linkedNames = new HashMap<>();

        public Builder add(PartKey<?> partKey, String name) {
            linkedNames.put(partKey, name);
            return this;
        }

        @Override
        public NameAlternativeModule build() {
            NameAlternativeModule module = new NameAlternativeModule();
            module.linkedNames.putAll(linkedNames);
            return module;
        }
    }
}
