package dev.metallurgists.rutile.api.data.manager.composition;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.Composition;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class ItemCompositionManager extends AbstractCompositionManager<Item> {
    public static ItemCompositionManager INSTANCE = new ItemCompositionManager();

    public ItemCompositionManager() {
        super(Rutile.id("item"), Registries.ITEM, BuiltInRegistries.ITEM);
    }

    public static ItemCompositionManager getInstance() {
        return INSTANCE;
    }
}