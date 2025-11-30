package dev.metallurgists.rutile.api.material.component.types;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.FlagSource;
import dev.metallurgists.rutile.api.material.builder.MaterialRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.*;

public class ItemRegistryComponent extends RegistryMaterialComponent<Item> {

    private final Map<FlagSource<Item>, MaterialRegistryBuilder<Item>> builders = new HashMap<>();
    private final Map<FlagSource<Item>, ResourceKey<Item>> objects = new HashMap<>();

    protected ItemRegistryComponent(Material material) {
        super(material);
    }

    @Override
    public ResourceKey<Registry<Item>> getRegistryKey() {
        return Registries.ITEM;
    }

    @Override
    public Collection<MaterialRegistryBuilder<Item>> getBuilders() {
        return builders.values();
    }

    @Override
    public Collection<ResourceKey<Item>> getObjectKeys() {
        return objects.values();
    }

    @Override
    public MaterialRegistryBuilder<Item> getBuilder(FlagSource<Item> flagSource) {
        return builders.get(flagSource);
    }

    @Override
    public void add(FlagSource<Item> flagSource, MaterialRegistryBuilder<Item> builder) {
        builder.setMaterialKey(this.getMaterialKey());
        ResourceKey<Item> key = ResourceKey.create(getRegistryKey(), builder.getObjectId());
        this.builders.put(flagSource, builder);
        this.objects.put(flagSource, key);
    }

    @Override
    public void add(FlagSource<Item> flagSource, ResourceKey<Item> key) {
        this.objects.put(flagSource, key);
    }
}
