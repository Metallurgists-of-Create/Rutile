package dev.metallurgists.rutile.api.material.module;

import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import dev.metallurgists.rutile.api.material.stack.MaterialStack;
import dev.metallurgists.rutile.registry.RutileModules;
import net.minecraft.core.Holder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondaryMaterialsModule implements MaterialModule<SecondaryMaterialsModule> {
    private final Map<Holder<RegistryModule.Key>, List<MaterialStack>> secondaryMaterials;

    public SecondaryMaterialsModule() {
        secondaryMaterials = new HashMap<>();
    }

    public List<MaterialStack> getSecondaryMaterials(Holder<RegistryModule.Key> registerKey) {
        if (hasSecondaryMaterials(registerKey))
            return secondaryMaterials.get(registerKey);
        return List.of();
    }

    public boolean hasSecondaryMaterials(Holder<RegistryModule.Key> registerKey) {
        return secondaryMaterials.containsKey(registerKey);
    }

    public SecondaryMaterialsModule addSecondaryMaterials(List<MaterialStack> secondaryMaterials, Holder<RegistryModule.Key>... registerKeys) {
        for (Holder<RegistryModule.Key> registerKey : registerKeys) {
            this.secondaryMaterials.put(registerKey, secondaryMaterials);
        }
        return this;
    }

    @Override
    public ModuleHolder<SecondaryMaterialsModule> getType() {
        return RutileModules.SECONDARY_MATERIALS;
    }

    @Override
    public ModuleBuilder<SecondaryMaterialsModule> builder() {
        return new Builder();
    }

    public static class Builder implements ModuleBuilder<SecondaryMaterialsModule> {
        private final Map<Holder<RegistryModule.Key>, List<MaterialStack>> secondaryMaterials = new HashMap<>();

        public Builder add(Holder<RegistryModule.Key> registerKey, List<MaterialStack> materials) {
            secondaryMaterials.put(registerKey, materials);
            return this;
        }

        @Override
        public SecondaryMaterialsModule build() {
            SecondaryMaterialsModule module = new SecondaryMaterialsModule();
            module.secondaryMaterials.putAll(secondaryMaterials);
            return module;
        }
    }
}
