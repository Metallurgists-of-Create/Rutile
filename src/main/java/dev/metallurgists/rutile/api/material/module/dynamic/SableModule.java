package dev.metallurgists.rutile.api.material.module.dynamic;

import com.google.gson.JsonObject;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.MaterialHelper;
import dev.metallurgists.rutile.api.material.data.MaterialEntry;
import dev.metallurgists.rutile.api.material.module.ModuleHolder;
import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import dev.metallurgists.rutile.api.runtime.data.RutileDynamicDataPack;
import lombok.experimental.Accessors;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class SableModule implements DynamicPackModule<SableModule> {
    private double unitMass;
    private final Map<Holder<RegistryModule.Key>, PhysicsProperties> specificProperties;

    public SableModule(double unitMass) {
        this.unitMass = unitMass;
        this.specificProperties = new HashMap<>();
    }

    public SableModule() {
        this(0);
    }

    public SableModule setProperty(Holder<RegistryModule.Key> key, PhysicsProperties value) {
        this.specificProperties.put(key, value);
        return this;
    }

    public SableModule setMass(double unitMass) {
        this.unitMass = unitMass;
        return this;
    }

    public void addData(Material material, Holder<RegistryModule.Key> key) {
        RutileDynamicDataPack.addData(createData(material, key), material.getId().withPath(path -> path + "/" + key.value().loc().getPath()), "physics_block_properties");
    }

    private JsonObject createData(Material material, Holder<RegistryModule.Key> key) {
        JsonObject data = new JsonObject();
        //MaterialHelper.get(new MaterialEntry(key, material));

        return data;
    }

    @Override
    public ModuleHolder<SableModule> getType() {
        return null;
    }

    @Override
    public ModuleBuilder<SableModule> builder() {
        return new Builder();
    }

    public static class Builder implements ModuleBuilder<SableModule> {
        private double mass = 1.0;
        private final Map<Holder<RegistryModule.Key>, PhysicsProperties> specificProperties = new HashMap<>();

        public Builder mass(double mass) {
            this.mass = mass;
            return this;
        }

        public Builder property(Holder<RegistryModule.Key> key, PhysicsProperties value) {
            this.specificProperties.put(key, value);
            return this;
        }

        @Override
        public SableModule build() {
            SableModule module = new SableModule(this.mass);
            module.specificProperties.putAll(this.specificProperties);
            return module;
        }
    }


    @Accessors(chain = true, fluent = true)
    public static class PhysicsProperties {
        private double mass = 1.0;
        private Vec3 inertia = null;
        private double volume = 1.0;
        private double restitution = 0.0;
        private double friction = 1.0;
        private boolean fragile = false;
        private ResourceLocation floatingMaterial = null;
        private double floatingScale = 1.0;
    }
}
