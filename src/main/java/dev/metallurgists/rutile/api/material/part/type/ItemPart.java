package dev.metallurgists.rutile.api.material.part.type;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.metallurgists.rutile.RutileRegistrate;
import dev.metallurgists.rutile.api.material.MaterialData;
import dev.metallurgists.rutile.api.material.objects.MaterialItem;
import dev.metallurgists.rutile.api.material.part.Part;
import dev.metallurgists.rutile.api.material.part.constructor.ItemConstructor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public abstract class ItemPart extends Part<Item> {

    public ItemConstructor constructor() {
        return MaterialItem::new;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends Item> RegistryEntry<Item, I> register(MaterialData data, RutileRegistrate registrate) {
        String name = idPattern(data).formatted(data.getName());
        var itemBuilder = registrate.item(name, (p) -> constructor().create(p, getKey(), data))
                .properties(p -> p)
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop());

        return (RegistryEntry<Item, I>) itemBuilder.register();
    }

    @Override
    public ResourceKey<Registry<Item>> registryResourceKey() {
        return Registries.ITEM;
    }
}
