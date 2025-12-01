package dev.metallurgists.rutile.registry;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.RutileRegistrate;
import dev.metallurgists.rutile.api.RutileApi;
import dev.metallurgists.rutile.api.material.ItemMaterialData;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.stack.MaterialEntry;
import dev.metallurgists.rutile.api.memorizer.Memorizer;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class RutileMaterialBlocks {

    static ImmutableTable.Builder<TagPrefix, Material, BlockEntry<? extends Block>> MATERIAL_BLOCKS_BUILDER = ImmutableTable
            .builder();

    public static Table<TagPrefix, Material, BlockEntry<? extends Block>> MATERIAL_BLOCKS;

    public static void generateMaterialBlocks() {
        Rutile.LOGGER.info("Generating material blocks...");
        for (TagPrefix tagPrefix : RutileApi.getTagPrefixRegistry().getAll()) {
            if (tagPrefix.doGenerateBlock()) {
                for (Material material : RutileApi.getMaterialRegistry().getAll()) {
                    RutileRegistrate registrate = RutileRegistrate.createIgnoringListenerErrors(material.getModId());
                    if (tagPrefix.doGenerateBlock(material)) {
                        registerMaterialBlock(tagPrefix, material, registrate);
                    }
                }
            }
        }
        MATERIAL_BLOCKS = MATERIAL_BLOCKS_BUILDER.build();
        Rutile.LOGGER.info("Generated {} material blocks", MATERIAL_BLOCKS.size());
    }

    private static void registerMaterialBlock(TagPrefix tagPrefix, Material material, RutileRegistrate registrate) {
        MATERIAL_BLOCKS_BUILDER.put(tagPrefix, material, registrate
                .block(tagPrefix.idPattern().formatted(material.getName()),
                        properties -> tagPrefix.blockConstructor().create(properties, tagPrefix, material))
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> tagPrefix.blockProperties().properties().apply(p).noLootTable())
                .transform(unificationBlock(tagPrefix, material))
                .addLayer(tagPrefix.blockProperties().renderType())
                .setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.LOOT, NonNullBiConsumer.noop())
                .item((b, p) -> tagPrefix.blockItemConstructor().create(b, p, tagPrefix, material))
                .model(NonNullBiConsumer.noop())
                .build()
                .register());
    }

    public static <P, T extends Block,
            S2 extends BlockBuilder<T, P>> NonNullFunction<S2, S2> unificationBlock(@NotNull TagPrefix tagPrefix,
                                                                                    @NotNull Material mat) {
        return builder -> {
            builder.onRegister(block -> {
                Supplier<Block> blockSupplier = Memorizer.memorizeBlockSupplier(() -> block);
                MaterialEntry entry = new MaterialEntry(tagPrefix, mat);
                RutileMaterialItems.toUnify.put(entry, blockSupplier);
                ItemMaterialData.registerMaterialEntry(blockSupplier, entry);
            });
            return builder;
        };
    }
}
