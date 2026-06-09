package dev.metallurgists.rutile.registry;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.RutileRegistrate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class RutileItems {
    public static RutileRegistrate registrate = Rutile.getRegistrate();

    public static final ItemEntry<Item> ITEM = registrate.item("my_item", Item::new)
            .register();

}
