package dev.metallurgists.rutile.api.material.builder.item;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.FlagSources;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.FlagSource;
import dev.metallurgists.rutile.api.material.builder.MaterialItemBuilder;
import dev.metallurgists.rutile.api.material.builder.MaterialRegistryBuilder;
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

public class IngotBuilder extends MaterialItemBuilder {

    @Getter
    private boolean machineOnly = false;

    public IngotBuilder() {
        super("%s_ingot");
    }

    public IngotBuilder(String nameFormat) {
        super(nameFormat);
    }

    public IngotBuilder machineOnly() {
        this.machineOnly = true;
        return this;
    }


    @Override
    public ResourceLocation getBuilderName() {
        return Rutile.id("ingot");
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
    public FlagSource<Item> getFlagSource() {
        return FlagSources.INGOT;
    }

    @Override
    public MultiTagHolder getTagHolder() {
        var holder = createTagHolder();
        holder.addPatterns("c:ingots", "c:ingots/%s");
        return new MultiTagHolder(holder);
    }

    @Override
    public void registerRecipes(@NotNull RecipeOutput output, @NotNull Material material) {
        if (material.hasFlag(FlagSources.STORAGE_BLOCK)) {
            ResourceLocation storageBlock = material.getFlag(FlagSources.STORAGE_BLOCK);
            if (MaterialHelpers.namespaceMatch(material, storageBlock)) {
                Block block = MaterialHelpers.getBlock(material, FlagSources.STORAGE_BLOCK);
                Item ingot = MaterialHelpers.getItem(material, FlagSources.INGOT);
                if (!isMachineOnly())
                    RecipeHelpers.craftCompact(output, ingot, block, false, material, "%s_block_from_ingots");
            }
        }
    }
}
