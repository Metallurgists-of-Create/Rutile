package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.RutileRegistrate;
import dev.metallurgists.rutile.api.material.MaterialData;
import dev.metallurgists.rutile.api.material.part.PartKey;
import dev.metallurgists.rutile.api.material.part.type.ItemPart;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.RegisterEvent;

public class RutileItems {

    public static void register() {
        RutileRegistrate registrate = Rutile.getRegistrate();

        registrate.simple();
        registrate.item();
        registrate.block();
    }

}
