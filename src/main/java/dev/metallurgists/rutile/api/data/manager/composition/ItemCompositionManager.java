package dev.metallurgists.rutile.api.data.manager.composition;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.Composition;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCompositionManager extends AbstractCompositionManager<Item> {
    public static ItemCompositionManager INSTANCE = new ItemCompositionManager();

    public Map<Item, Composition<Item>> compositions = new HashMap<>();
    public List<Item> composed = new ArrayList<>();

    public ItemCompositionManager() {
        super(Rutile.id("item"), Registries.ITEM, Composition.codec(Registries.ITEM, BuiltInRegistries.ITEM.byNameCodec()));
    }

    public static ItemCompositionManager getInstance() {
        return INSTANCE;
    }

    @Override
    public Registry<Item> getRegistry() {
        return BuiltInRegistries.ITEM;
    }

    @Override
    public Map<Item, Composition<Item>> getCompositions() {
        return this.compositions;
    }

    @Override
    public List<Item> getComposed() {
        return this.composed;
    }

    @Override
    public void clearData() {
        this.compositions.clear();
        this.composed.clear();
    }

    @Override
    public void putComposition(Item composed, Composition<Item> composition) {
        this.compositions.put(composed, composition);
        this.composed.add(composed);
    }
}