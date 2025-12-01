package dev.metallurgists.rutile.util;

import it.unimi.dsi.fastutil.Hash;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface ItemStackHashStrategy extends Hash.Strategy<ItemStack> {

    static ItemStackHashStrategyBuilder builder() {
        return new ItemStackHashStrategyBuilder();
    }

    static ItemStackHashStrategy comparingAll() {
        return ItemStackHashStrategyBuilder.ALL;
    }

    static ItemStackHashStrategy comparingAllButCount() {
        return ItemStackHashStrategyBuilder.ITEM_AND_TAG;
    }

    static ItemStackHashStrategy comparingItem() {
        return ItemStackHashStrategyBuilder.ITEM;
    }

    class ItemStackHashStrategyBuilder {
        private static final ItemStackHashStrategy ALL = builder().compareItem(true)
                .compareCount(true)
                .compareComponents(true)
                .build();
        private static final ItemStackHashStrategy ITEM_AND_TAG = builder().compareItem(true)
                .compareComponents(true)
                .build();
        private static final ItemStackHashStrategy ITEM = builder().compareItem(true).build();

        private boolean item, count, components;

        public ItemStackHashStrategyBuilder compareItem(boolean choice) {
            item = choice;
            return this;
        }

        public ItemStackHashStrategyBuilder compareCount(boolean choice) {
            count = choice;
            return this;
        }

        public ItemStackHashStrategyBuilder compareComponents(boolean choice) {
            components = choice;
            return this;
        }

        public ItemStackHashStrategy build() {
            return new ItemStackHashStrategy() {

                @Override
                public int hashCode(@Nullable ItemStack o) {
                    return o == null || o.isEmpty() ? 0 : Objects.hash(
                            item ? o.getItem() : null,
                            count ? o.getCount() : null,
                            components ? o.getComponents() : null);
                }

                @Override
                public boolean equals(@Nullable ItemStack a, @Nullable ItemStack b) {
                    if (a == null || a.isEmpty()) return b == null || b.isEmpty();
                    if (b == null || b.isEmpty()) return false;

                    return (!item || a.getItem() == b.getItem()) &&
                            (!count || a.getCount() == b.getCount()) &&
                            (!components || Objects.equals(a.getComponents(), b.getComponents()));
                }
            };
        }
    }
}
