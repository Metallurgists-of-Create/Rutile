package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Locale;

public final class RutileTags {
    private RutileTags() {}

    public enum NameSpace {
        MOD(Rutile.ID),
        COMMON("c");

        public final String id;
        NameSpace(String id) {
            this.id = id;
        }
    }

    public enum Items {
        INGOT_IRON(NameSpace.COMMON, "ingots/iron"),
        INGOT_COPPER(NameSpace.COMMON, "ingots/copper"),
        INGOT_GOLD(NameSpace.COMMON, "ingots/gold"),
        ;

        public final TagKey<Item> tag;

        Items() {
            this(NameSpace.MOD);
        }

        Items(NameSpace namespace) {
            this(namespace, null);
        }

        Items(NameSpace namespace, String path) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? name().toLowerCase(Locale.ROOT) : path);
            this.tag = ItemTags.create(id);
        }

        Items(String namespace, String path) {
            this.tag = ItemTags.create(ResourceLocation.fromNamespaceAndPath(namespace, path));
        }
    }

}
