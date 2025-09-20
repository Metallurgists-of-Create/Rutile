package dev.metallurgists.rutile.registry.flags;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.ItemFlag;
import dev.metallurgists.rutile.api.material.flag.types.IItemRegistry;
import dev.metallurgists.rutile.api.material.flag.types.IRecipeHandler;
import dev.metallurgists.rutile.api.material.flag.types.MultiTagHolder;
import dev.metallurgists.rutile.api.material.flag.types.TagHolder;
import dev.metallurgists.rutile.api.material.registry.item.IMaterialItem;
import dev.metallurgists.rutile.api.material.registry.item.MaterialItem;
import dev.metallurgists.rutile.util.helpers.RecipeHelpers;
import dev.metallurgists.rutile.registry.RutileFlagKeys;
import dev.metallurgists.rutile.util.helpers.MaterialHelpers;
import dev.metallurgists.rutile.util.helpers.ModelHelpers;
import lombok.Getter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class IngotFlag extends ItemFlag implements IRecipeHandler {
    @Getter
    private boolean requiresCompacting = false;

    public IngotFlag(String existingNamespace) {
        super("%s_ingot", existingNamespace);
    }

    public IngotFlag() {
        this("");
    }

    public IngotFlag requiresCompacting() {
        this.requiresCompacting = true;
        return this;
    }

    @Override
    public ItemEntry<? extends IMaterialItem> registerItem(@NotNull Material material, IItemRegistry flag, @NotNull AbstractRegistrate<?> registrate) {
        return registrate
                .item(flag.getIdPattern().formatted(material.getName()), (p) -> new MaterialItem(p, material, flag))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .register();
    }

    @Override
    public void registerItemAssets(Material material) {
        ModelHelpers.generatedItemModel(material, RutileFlagKeys.INGOT.get());
    }

    @Override
    public FlagKey<?> getKey() {
        return RutileFlagKeys.INGOT.get();
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }

    //TODO: Fix flags creating duplicate recipes
    @Override
    public void run(@NotNull RecipeOutput output, @NotNull Material material) {
        if (material.hasFlag(RutileFlagKeys.STORAGE_BLOCK)) {
            var storageBlockFlag = material.getFlag(RutileFlagKeys.STORAGE_BLOCK);
            if (MaterialHelpers.hasExternalId(material, getKey())) return;
            Block block = MaterialHelpers.getBlock(material, RutileFlagKeys.STORAGE_BLOCK.get());
            Item ingot = MaterialHelpers.getItem(material, RutileFlagKeys.INGOT.get());
            if (!isRequiresCompacting())
                RecipeHelpers.craftCompact(output, ingot, block, false, material, "%s_block_from_ingots");
            //if (!storageBlockFlag.isRequiresDecompacting())
            //    RecipeHelpers.craftDecompact(output, block, ingot, 9, material, "%s_ingots_from_block");
        }
    }

    @Override
    public MultiTagHolder getTagHolder() {
        var holder = new TagHolder<>(Registries.ITEM);
        holder.addPatterns("c:ingots", "c:ingots/%s");
        return new MultiTagHolder(holder);
    }
}
