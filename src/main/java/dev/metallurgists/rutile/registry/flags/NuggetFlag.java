package dev.metallurgists.rutile.registry.flags;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.ItemFlag;
import dev.metallurgists.rutile.api.material.flag.types.IItemRegistry;
import dev.metallurgists.rutile.api.material.flag.types.IRecipeHandler;
import dev.metallurgists.rutile.api.material.flag.types.ISpecialLangSuffix;
import dev.metallurgists.rutile.api.material.registry.item.IMaterialItem;
import dev.metallurgists.rutile.api.material.registry.item.MaterialItem;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.util.helpers.RecipeHelpers;
import dev.metallurgists.rutile.registry.RutileFlagKeys;
import dev.metallurgists.rutile.util.helpers.MaterialHelpers;
import dev.metallurgists.rutile.util.helpers.ModelHelpers;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class NuggetFlag extends ItemFlag implements IRecipeHandler, ISpecialLangSuffix {
    @Getter
    private boolean requiresCompacting = false;
    @Getter
    private boolean shard;

    @Getter
    private int amountToCraft = 9; // Default to 9 nuggets per crafting recipe

    public NuggetFlag(String existingNamespace, boolean shard) {
        super(shard ? "%s_shard" :"%s_nugget", existingNamespace);
        this.setTagPatterns(List.of(shard ? "c:/shards" :"c:nuggets", shard ? "c:/shards/%s" : "c:nuggets/%s"));
    }

    public NuggetFlag(boolean shard) {
        this("", shard);
    }

    public NuggetFlag(String existingNamespace) {
        this(existingNamespace, false);
    }

    public NuggetFlag() {
        this("", false);
    }

    public NuggetFlag requiresCompacting() {
        this.requiresCompacting = true;
        return this;
    }

    @Override
    public ItemEntry<? extends IMaterialItem> registerItem(@NotNull Material material, IItemRegistry flag, @NotNull RutileRegistrate registrate) {
        return registrate
                .item(flag.getIdPattern().formatted(material.getName()), (p) -> new MaterialItem(p, material, flag))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .register();
    }

    @Override
    public void registerItemAssets(Material material) {
        boolean texturePresent = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/item/materials/" + material.getName() + "/nugget.png")).isPresent();
        String texture = texturePresent ? material.getNamespace() + ":item/materials/" + material.getName() + "/nugget" : "rutile:item/materials/null/nugget";
        RutileDynamicResourcePack.addItemModel(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), ModelHelpers.simpleGeneratedModel("minecraft:item/generated", texture));
    }

    @Override
    public FlagKey<?> getKey() {
        return RutileFlagKeys.NUGGET;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }

    @Override
    public void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        if (MaterialHelpers.hasExternalId(material, getKey())) return;
        if (!isShard() && material.hasFlag(RutileFlagKeys.INGOT)) {
            Item ingot = MaterialHelpers.getItem(material, RutileFlagKeys.INGOT);
            Item nugget = MaterialHelpers.getItem(material, RutileFlagKeys.NUGGET);
            if (!isRequiresCompacting()) {
                RecipeHelpers.craftCompact(provider, nugget, ingot, false, material, "%s_ingot_from_nuggets");
                RecipeHelpers.craftDecompact(provider, ingot, nugget, 9, material, "%s_nuggets_from_ingot");
            }
        }
        if (isShard() && material.hasFlag(RutileFlagKeys.GEM)) {
            Item ingot = MaterialHelpers.getItem(material, RutileFlagKeys.GEM);
            Item shard = MaterialHelpers.getItem(material, RutileFlagKeys.NUGGET);
            if (!isRequiresCompacting()) {
                RecipeHelpers.craftCompact(provider, shard, ingot, 8, material, "%s_gem_from_shards");
                RecipeHelpers.craftDecompact(provider, ingot, shard, 8, material, "%s_shards_from_gem");
            }
        }
    }

    @Override
    public String getLangSuffix() {
        return isShard() ? "shard" : "";
    }
}
