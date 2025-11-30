package dev.metallurgists.rutile.api.material.builder.item;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.FlagRegistryTypes;
import dev.metallurgists.rutile.api.material.FlagSources;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.FlagSource;
import dev.metallurgists.rutile.api.material.builder.MaterialItemBuilder;
import dev.metallurgists.rutile.api.material.builder.MaterialRegistryBuilder;
import dev.metallurgists.rutile.api.material.builder.block.StorageBlockBuilder;
import dev.metallurgists.rutile.api.material.flag.types.MultiTagHolder;
import dev.metallurgists.rutile.api.material.registry.item.MaterialItem;
import dev.metallurgists.rutile.util.helpers.MaterialHelpers;
import dev.metallurgists.rutile.util.helpers.RecipeHelpers;
import lombok.Getter;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class GemBuilder extends MaterialItemBuilder {

    @Getter
    private boolean small = false;
    @Getter
    private boolean machineOnly = false;

    public GemBuilder() {
        super("%s_gem");
    }

    public GemBuilder(String nameFormat) {
        super(nameFormat);
    }

    public GemBuilder machineOnly() {
        this.machineOnly = true;
        return this;
    }

    public GemBuilder small() {
        this.small = true;
        return this;
    }

    @Override
    public FlagSource<Item> getFlagSource() {
        return FlagSources.GEM;
    }

    @Override
    public MultiTagHolder getTagHolder() {
        var holder = createTagHolder();
        holder.addPatterns("c:gems", "c:gems/%s");
        return new MultiTagHolder(holder);
    }

    @Override
    public ResourceLocation getBuilderName() {
        return Rutile.id("gem");
    }

    @Override
    public MaterialRegistryBuilder<Item> getBuilder() {
        return this;
    }

    @Override
    public RegistryEntry<Item, ? extends Item> build(@NotNull AbstractRegistrate<?> registrate, @NotNull Material material) {
        return registrate
                .item(nameFormat().formatted(material.getName()), (p) -> new MaterialItem(p, material, this))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .register();
    }

    @Override
    public void registerRecipes(@NotNull RecipeOutput output, @NotNull Material material) {
        if (material.hasFlag(FlagSources.STORAGE_BLOCK)) {
            ResourceLocation storageBlock = material.getFlag(FlagSources.STORAGE_BLOCK);
            if (MaterialHelpers.namespaceMatch(material, storageBlock)) {
                Block block = MaterialHelpers.getBlock(material, FlagSources.STORAGE_BLOCK);
                Item gem = MaterialHelpers.getItem(material, FlagSources.GEM);
                if (!isMachineOnly())
                    RecipeHelpers.craftCompact(output, gem, block, isSmall(), material, "%s_block_from_gems");
                StorageBlockBuilder sbBuilder = (StorageBlockBuilder) material.getFlagContainer(FlagRegistryTypes.BLOCK).getBuilder(FlagSources.STORAGE_BLOCK);
                if (sbBuilder != null && !sbBuilder.isMachineOnly())
                    RecipeHelpers.craftDecompact(output, block, gem, isSmall() ? 4 : 9, material, "%s_gems_from_block");
            }
        }
    }
}
