package dev.metallurgists.rutile.api.data.manager.composition;

import dev.metallurgists.rutile.api.RutileApi;
import dev.metallurgists.rutile.api.composition.Composition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCompositionManager extends AbstractCompositionManager<Item> {
    public static ItemCompositionManager INSTANCE = new ItemCompositionManager();

    public Map<Item, Composition> compositions = new HashMap<>();
    public List<Item> composed = new ArrayList<>();

    public ItemCompositionManager() {
        super("item");
    }

    public static ItemCompositionManager getInstance() {
        return INSTANCE;
    }

    @Override
    public Map<Item, Composition> getCompositions() {
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
    public void putComposition(Item composed, Composition composition) {
        this.compositions.put(composed, composition);
        this.composed.add(composed);
    }

    @Override
    public Item getFromKey(ResourceLocation key) {
        return BuiltInRegistries.ITEM.get(key);
    }

    public static void register(AddReloadListenerEvent event) {
        event.addListener(RutileApi.getItemCompositionManager());
    }
}
