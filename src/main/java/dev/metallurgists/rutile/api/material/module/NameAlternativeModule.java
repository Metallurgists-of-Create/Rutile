package dev.metallurgists.rutile.api.material.module;

import dev.metallurgists.rutile.registry.RutileModules;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

public class NameAlternativeModule implements MaterialModule<NameAlternativeModule> {

    private final Map<ResourceLocation, String> linkedNames;

    public NameAlternativeModule() {
        this.linkedNames = new HashMap<>();
    }

    public Optional<String> getAlternative(ResourceLocation registerKey) {
        if (hasAlternative(registerKey))
            return Optional.of(linkedNames.get(registerKey));
        return Optional.empty();
    }

    public boolean hasAlternative(ResourceLocation registerKey) {
        return linkedNames.containsKey(registerKey);
    }

    public NameAlternativeModule add(ResourceLocation registerKey, String name) {
        linkedNames.put(registerKey, name);
        return this;
    }

    public NameAlternativeModule remove(ResourceLocation registerKey) {
        linkedNames.remove(registerKey);
        return this;
    }

    public NameAlternativeModule removeIf(BiPredicate<ResourceLocation, String> predicate) {
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
        private final Map<ResourceLocation, String> linkedNames = new HashMap<>();

        public Builder add(ResourceLocation registerKey, String name) {
            linkedNames.put(registerKey, name);
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
