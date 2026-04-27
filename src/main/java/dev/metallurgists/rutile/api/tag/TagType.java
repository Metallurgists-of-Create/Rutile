package dev.metallurgists.rutile.api.tag;

import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import lombok.Getter;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public class TagType {
    private final String tagPath;
    @Getter
    private boolean isParentTag = false;
    // this is now memoized because creating tag keys interns them and that's slow
    private BiFunction<Holder<RegistryModule.Key>, Material, TagKey<Item>> formatter;
    private Predicate<Material> filter;

    private TagType(String tagPath) {
        this.tagPath = tagPath;
    }

    public static TagType withDefaultFormatter(String tagPath, boolean isVanilla) {
        TagType type = new TagType(tagPath);
        type.formatter = Util
                .memoize((keyHolder, mat) -> TagUtil.createItemTag(type.tagPath.formatted(keyHolder.value().getMaterialName(mat)), isVanilla));
        return type;
    }

    public static TagType withPrefixFormatter(String tagPath) {
        TagType type = new TagType(tagPath);
        type.formatter = Util.memoize((keyHolder, mat) -> TagUtil.createItemTag(
                type.tagPath.formatted(keyHolder.value().getLowerCaseName(), keyHolder.value().getMaterialName(mat))));
        return type;
    }

    public static TagType withPrefixOnlyFormatter(String tagPath) {
        TagType type = new TagType(tagPath);
        type.formatter = Util.memoize((keyHolder, mat) -> TagUtil
                .createItemTag(type.tagPath.formatted(keyHolder.value().getLowerCaseName())));
        type.isParentTag = true;
        return type;
    }

    public static TagType withNoFormatter(String tagPath, boolean isVanilla) {
        TagType type = new TagType(tagPath);
        type.formatter = Util.memoize((keyHolder, material) -> TagUtil.createItemTag(type.tagPath, isVanilla));
        type.isParentTag = true;
        return type;
    }

    public static TagType withCustomFormatter(String tagPath, BiFunction<Holder<RegistryModule.Key>, Material, TagKey<Item>> formatter) {
        TagType type = new TagType(tagPath);
        type.formatter = Util.memoize(formatter);
        return type;
    }

    public static TagType withCustomFilter(String tagPath, boolean isVanilla, Predicate<Material> filter) {
        TagType type = new TagType(tagPath);
        type.filter = filter;
        type.formatter = Util.memoize((keyHolder, material) -> TagUtil.createItemTag(type.tagPath, isVanilla));
        return type;
    }

    @Nullable
    public TagKey<Item> getTag(Holder<RegistryModule.Key> keyHolder, @NotNull Material material) {
        if (filter != null && !filter.test(material)) return null;
        return formatter.apply(keyHolder, material);
    }
}
