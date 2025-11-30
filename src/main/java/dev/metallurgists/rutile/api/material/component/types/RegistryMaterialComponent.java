package dev.metallurgists.rutile.api.material.component.types;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.FlagSource;
import dev.metallurgists.rutile.api.material.builder.MaterialRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class RegistryMaterialComponent<T> extends MaterialComponent {

    protected RegistryMaterialComponent(Material material) {
        super(material);
    }

    public abstract ResourceKey<Registry<T>> getRegistryKey();

    public abstract Collection<MaterialRegistryBuilder<T>> getBuilders();
    public abstract Collection<ResourceKey<T>> getObjectKeys();

    public abstract MaterialRegistryBuilder<T> getBuilder(FlagSource<T> flagSource);

    public abstract void add(FlagSource<T> flagSource, MaterialRegistryBuilder<T> builder);
    public abstract void add(FlagSource<T> flagSource, ResourceKey<T> key);

    public  Collection<ResourceKey<T>> getKeys() {
        List<ResourceKey<T>> keys = new ArrayList<>();
        for (var builder : getBuilders()) {
            builder.setMaterialKey(this.getMaterialKey());
            ResourceKey<T> key = ResourceKey.create(getRegistryKey(), builder.getObjectId());
            keys.add(key);
        }
        keys.addAll(getObjectKeys());
        return keys;
    }
}
