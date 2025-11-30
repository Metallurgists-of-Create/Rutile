package dev.metallurgists.rutile.api.material.builder.block;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.FlagSources;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.FlagSource;
import dev.metallurgists.rutile.api.material.builder.MaterialBlockBuilder;
import dev.metallurgists.rutile.api.material.builder.MaterialRegistryBuilder;
import dev.metallurgists.rutile.api.material.builder.item.DustBuilder;
import dev.metallurgists.rutile.api.material.flag.types.MultiTagHolder;
import dev.metallurgists.rutile.api.material.flag.types.TagHolder;
import dev.metallurgists.rutile.api.material.registry.block.MaterialBlock;
import dev.metallurgists.rutile.api.material.registry.block.MaterialBlockItem;
import lombok.Getter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

public class StorageBlockBuilder extends MaterialBlockBuilder {

    @Getter
    private boolean machineOnly;

    public StorageBlockBuilder() {
        super("%s_block");
    }

    public StorageBlockBuilder(String nameFormat) {
        super(nameFormat);
    }

    public StorageBlockBuilder machineOnly() {
        this.machineOnly = true;
        return this;
    }

    @Override
    public FlagSource<Block> getFlagSource() {
        return FlagSources.STORAGE_BLOCK;
    }

    @Override
    public MultiTagHolder getTagHolder() {
        var blockHolder = createTagHolder();
        blockHolder.addPatterns("c:storage_blocks", "c:storage_blocks/%s", "minecraft:mineable/pickaxe");
        var itemHolder = createTagHolder(Registries.ITEM);
        itemHolder.addPatterns("c:storage_blocks", "c:storage_blocks/%s");
        return new MultiTagHolder(blockHolder, itemHolder);
    }

    @Override
    public ResourceLocation getBuilderName() {
        return Rutile.id("storage_block");
    }

    @Override
    public MaterialRegistryBuilder<Block> getBuilder() {
        return this;
    }

    @Override
    public RegistryEntry<Block, ? extends Block> build(@NotNull AbstractRegistrate<?> registrate, @NotNull Material material) {
        return registrate.block(nameFormat().formatted(material.getName()), p -> new MaterialBlock(p, material, this))
                .initialProperties(() -> Blocks.ANDESITE)
                .properties(p -> p.sound(SoundType.METAL))
                .setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .item(MaterialBlockItem::create)
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .build()
                .register();
    }
}
