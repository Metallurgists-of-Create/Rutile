package dev.metallurgists.rutile.registry.flags;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.ItemFlag;
import dev.metallurgists.rutile.api.material.flag.types.*;
import dev.metallurgists.rutile.api.material.registry.item.IMaterialItem;
import dev.metallurgists.rutile.api.material.registry.item.MaterialItem;
import dev.metallurgists.rutile.registry.RutileFlagKeys;
import dev.metallurgists.rutile.util.helpers.ModelHelpers;
import lombok.Getter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import org.jetbrains.annotations.NotNull;


public class DustFlag extends ItemFlag implements IRecipeHandler, ISpecialLangSuffix {

    @Getter
    private boolean powder;

    public DustFlag(String existingNamespace, boolean powder) {
        super(powder ? "%s_powder" : "%s_dust", existingNamespace);
        this.powder = powder;
    }

    public DustFlag() {
        this("", false);
    }

    public DustFlag(boolean powder) {
        this("", powder);
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
        ModelHelpers.generatedItemModel(material, RutileFlagKeys.DUST);
    }

    @Override
    public FlagKey<? extends IItemRegistry> getKey() {
        return RutileFlagKeys.DUST;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }

    @Override
    public String getLangSuffix() {
        return isPowder() ? "powder" : "";
    }

    @Override
    public void run(@NotNull RecipeOutput output, @NotNull Material material) {

    }

    @Override
    public MultiTagHolder getTagHolder() {
        var holder = new TagHolder<>(Registries.ITEM);
        holder.addPatterns(powder ? "c:powders" : "c:dusts", powder ? "c:powders/%s" : "c:dusts/%s");
        return new MultiTagHolder(holder);
    }
}
