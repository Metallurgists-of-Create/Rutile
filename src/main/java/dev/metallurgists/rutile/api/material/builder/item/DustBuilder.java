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
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class DustBuilder extends MaterialItemBuilder {

    @Getter
    private boolean powder = false;

    private final String customName;

    public DustBuilder() {
        super("");
        this.customName = null;
    }

    public DustBuilder(String nameFormat) {
        super(nameFormat);
        this.customName = nameFormat;
    }

    public DustBuilder powder() {
        this.powder = true;
        return this;
    }

    @Override
    public FlagSource<Item> getFlagSource() {
        return FlagSources.DUST;
    }

    @Override
    public MultiTagHolder getTagHolder() {
        var holder = createTagHolder();
        holder.addPatterns(this.powder ? "c:powders" : "c:dusts", this.powder ? "c:/powders/%s" : "c:dusts/%s");
        return new MultiTagHolder(holder);
    }

    @Override
    public String nameFormat() {
        if (this.customName != null) {
            return this.customName;
        }
        return this.powder ? "%s_powder" : "%s_dust";
    }

    @Override
    public ResourceLocation getBuilderName() {
        return Rutile.id("dust");
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
}
