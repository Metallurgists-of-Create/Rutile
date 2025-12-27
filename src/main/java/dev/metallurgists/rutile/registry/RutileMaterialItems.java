package dev.metallurgists.rutile.registry;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.RutileRegistrate;
import dev.metallurgists.rutile.api.material.ItemMaterialData;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.stack.MaterialEntry;
import dev.metallurgists.rutile.api.memorizer.Memorizer;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RutileMaterialItems {
    static ImmutableTable.Builder<TagPrefix, Material, ItemEntry<? extends Item>> MATERIAL_ITEMS_BUILDER = ImmutableTable.builder();

    public static final Map<MaterialEntry, Supplier<? extends ItemLike>> toUnify = new HashMap<>();

    public static Table<TagPrefix, Material, ItemEntry<? extends Item>> MATERIAL_ITEMS;

    public static void generateMaterialItems() {
        Rutile.LOGGER.info("Generating material items...");
        for (TagPrefix tagPrefix : RutileRegistries.TAG_PREFIXES) {
            if (tagPrefix.doGenerateItem()) {
                for (Material material : RutileRegistries.MATERIALS) {
                    RutileRegistrate registrate = RutileRegistrate.createIgnoringListenerErrors(material.getModId());
                    if (tagPrefix.doGenerateItem(material)) {
                        generateMaterialItem(tagPrefix, material, registrate);
                    }
                }
            }
        }
        MATERIAL_ITEMS = MATERIAL_ITEMS_BUILDER.build();
        Rutile.LOGGER.info("Generated {} material items", MATERIAL_ITEMS.size());
    }

    private static void generateMaterialItem(TagPrefix tagPrefix, Material material, RutileRegistrate registrate) {
        MATERIAL_ITEMS_BUILDER.put(tagPrefix, material, registrate
                .item(tagPrefix.idPattern().formatted(material.getName()),
                        properties -> tagPrefix.itemConstructor().create(properties, tagPrefix, material))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .transform(unificationItem(tagPrefix, material))
                .properties(p -> p.stacksTo(tagPrefix.maxStackSize()))
                .model(NonNullBiConsumer.noop())
                .register());
    }

    public static <P, T extends Item,
            S2 extends ItemBuilder<T, P>> NonNullFunction<S2, S2> unificationItem(@NotNull TagPrefix tagPrefix,
                                                                                  @NotNull Material mat) {
        return builder -> {
            builder.onRegister(item -> {
                Supplier<ItemLike> supplier = Memorizer.memorize(() -> item);
                MaterialEntry entry = new MaterialEntry(tagPrefix, mat);
                toUnify.put(entry, supplier);
                ItemMaterialData.registerMaterialEntry(supplier, entry);
            });
            return builder;
        };
    }
}
