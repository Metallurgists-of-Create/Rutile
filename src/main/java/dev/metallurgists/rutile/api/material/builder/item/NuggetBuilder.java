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
import org.jetbrains.annotations.NotNull;

public class NuggetBuilder extends MaterialItemBuilder {
    @Getter
    private boolean machineOnly = false;
    @Getter
    private boolean shard = false;

    @Getter
    private int amountToCraft = 9;

    private final String customName;

    public NuggetBuilder() {
        super("");
        this.customName = null;
    }

    public NuggetBuilder(String nameFormat) {
        super(nameFormat);
        this.customName = nameFormat;
    }

    public NuggetBuilder shard() {
        this.shard = true;
        return this;
    }

    public NuggetBuilder machineOnly() {
        this.machineOnly = true;
        return this;
    }

    public NuggetBuilder amountToCraft(int amountToCraft) {
        this.amountToCraft = amountToCraft;
        return this;
    }

    @Override
    public FlagSource<Item> getFlagSource() {
        return FlagSources.NUGGET;
    }

    @Override
    public MultiTagHolder getTagHolder() {
        var holder = createTagHolder();
        holder.addPatterns(this.shard ? "c:/shards" : "c:nuggets", this.shard ? "c:/shards/%s" : "c:nuggets/%s");
        return new MultiTagHolder(holder);
    }

    @Override
    public String nameFormat() {
        if (this.customName != null) {
            return this.customName;
        }
        return this.shard ? "%s_shard" :"%s_nugget";
    }

    @Override
    public ResourceLocation getBuilderName() {
        return Rutile.id("nugget");
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
        if (!isShard() && material.hasFlag(FlagSources.INGOT)) {
            if (MaterialHelpers.namespaceMatch(material, material.getFlag(FlagSources.INGOT))) {
                Item ingot = MaterialHelpers.getItem(material, FlagSources.INGOT);
                Item nugget = MaterialHelpers.getItem(material, FlagSources.NUGGET);
                if (!isMachineOnly()) {
                    RecipeHelpers.craftCompact(output, nugget, ingot, false, material, "%s_ingot_from_nuggets");
                    RecipeHelpers.craftDecompact(output, ingot, nugget, 9, material, "%s_nuggets_from_ingot");
                }
            }
        }
        if (isShard() && material.hasFlag(FlagSources.GEM)) {
            if (MaterialHelpers.namespaceMatch(material, material.getFlag(FlagSources.GEM))) {
                Item gem = MaterialHelpers.getItem(material, FlagSources.GEM);
                Item shard = MaterialHelpers.getItem(material, FlagSources.NUGGET);
                if (!isMachineOnly()) {
                    RecipeHelpers.craftCompact(output, shard, gem, 8, material, "%s_gem_from_shards");
                    RecipeHelpers.craftDecompact(output, gem, shard, 8, material, "%s_shards_from_gem");
                }
            }
        }
    }
}
