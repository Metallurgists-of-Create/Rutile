package dev.metallurgists.rutile.api.material.module.dynamic;

import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.module.MaterialModule;
import dev.metallurgists.rutile.api.material.module.ModuleHolder;
import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import dev.metallurgists.rutile.api.tag.TagType;
import dev.metallurgists.rutile.registry.RutileMaterials;
import dev.metallurgists.rutile.registry.RutileModules;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

@SuppressWarnings("DuplicatedCode")
public class TagsModule implements MaterialModule<TagsModule> {
    private final Map<Holder<RegistryModule.Key>, List<TagType>> tags;

    public TagsModule() {
        this.tags = new HashMap<>();
    }

    public TagsModule defaultTagPath(Holder<RegistryModule.Key> key, String path) {
        return this.defaultTagPath(key, path, false);
    }

    public TagsModule defaultTagPath(Holder<RegistryModule.Key> key, String path, boolean isVanilla) {
        this.tags.compute(key, (k, v) -> {
            var tag = TagType.withDefaultFormatter(path, isVanilla);
            List<TagType> list = v == null ? new ArrayList<>() : v;
            list.add(tag);
            return list;
        });
        return this;
    }

    public TagsModule prefixTagPath(Holder<RegistryModule.Key> key, String path) {
        this.tags.compute(key, (k, v) -> {
            var tag = TagType.withPrefixFormatter(path);
            List<TagType> list = v == null ? new ArrayList<>() : v;
            list.add(tag);
            return list;
        });
        return this;
    }

    public TagsModule prefixOnlyTagPath(Holder<RegistryModule.Key> key, String path) {
        this.tags.compute(key, (k, v) -> {
            var tag = TagType.withPrefixOnlyFormatter(path);
            List<TagType> list = v == null ? new ArrayList<>() : v;
            list.add(tag);
            return list;
        });
        return this;
    }

    public TagsModule unformattedTagPath(Holder<RegistryModule.Key> key, String path) {
        return unformattedTagPath(key, path, false);
    }

    public TagsModule unformattedTagPath(Holder<RegistryModule.Key> key, String path, boolean isVanilla) {
        this.tags.compute(key, (k, v) -> {
            var tag = TagType.withNoFormatter(path, isVanilla);
            List<TagType> list = v == null ? new ArrayList<>() : v;
            list.add(tag);
            return list;
        });
        return this;
    }

    public TagsModule customTagPath(Holder<RegistryModule.Key> key, String path, BiFunction<Holder<RegistryModule.Key>, Material, TagKey<Item>> formatter) {
        this.tags.compute(key, (k, v) -> {
            var tag = TagType.withCustomFormatter(path, formatter);
            List<TagType> list = v == null ? new ArrayList<>() : v;
            list.add(tag);
            return list;
        });
        return this;
    }

    public TagsModule customTagPredicate(Holder<RegistryModule.Key> key, String path, boolean isVanilla, Predicate<Material> materialPredicate) {
        this.tags.compute(key, (k, v) -> {
            var tag = TagType.withCustomFilter(path, isVanilla, materialPredicate);
            List<TagType> list = v == null ? new ArrayList<>() : v;
            list.add(tag);
            return list;
        });
        return this;
    }

    @Override
    public ModuleHolder<TagsModule> getType() {
        return RutileModules.TAGS;
    }

    @Override
    public ModuleBuilder<TagsModule> builder() {
        return new Builder();
    }

    @Unmodifiable
    public List<TagKey<Item>> getItemParentTags(Holder<RegistryModule.Key> registryKey) {
        return tags.get(registryKey).stream()
                .filter(TagType::isParentTag)
                .map(type -> type.getTag(registryKey, RutileMaterials.Null))
                .toList();
    }

    @Unmodifiable
    public List<TagKey<Item>> getItemTags(Holder<RegistryModule.Key> registryKey, @NotNull Material mat) {
        return tags.get(registryKey).stream()
                .filter(type -> !type.isParentTag())
                .map(type -> type.getTag(registryKey, mat))
                .filter(Objects::nonNull)
                .toList();
    }

    public static class Builder implements ModuleBuilder<TagsModule> {
        private final Map<Holder<RegistryModule.Key>, List<TagType>> tags = new HashMap<>();

        public Builder defaultTagPath(Holder<RegistryModule.Key> key, String path) {
            return this.defaultTagPath(key, path, false);
        }

        public Builder defaultTagPath(Holder<RegistryModule.Key> key, String path, boolean isVanilla) {
            this.tags.compute(key, (k, v) -> {
                var tag = TagType.withDefaultFormatter(path, isVanilla);
                List<TagType> list = v == null ? new ArrayList<>() : v;
                list.add(tag);
                return list;
            });
            return this;
        }

        public Builder prefixTagPath(Holder<RegistryModule.Key> key, String path) {
            this.tags.compute(key, (k, v) -> {
                var tag = TagType.withPrefixFormatter(path);
                List<TagType> list = v == null ? new ArrayList<>() : v;
                list.add(tag);
                return list;
            });
            return this;
        }

        public Builder prefixOnlyTagPath(Holder<RegistryModule.Key> key, String path) {
            this.tags.compute(key, (k, v) -> {
                var tag = TagType.withPrefixOnlyFormatter(path);
                List<TagType> list = v == null ? new ArrayList<>() : v;
                list.add(tag);
                return list;
            });
            return this;
        }

        public Builder unformattedTagPath(Holder<RegistryModule.Key> key, String path) {
            return unformattedTagPath(key, path, false);
        }

        public Builder unformattedTagPath(Holder<RegistryModule.Key> key, String path, boolean isVanilla) {
            this.tags.compute(key, (k, v) -> {
                var tag = TagType.withNoFormatter(path, isVanilla);
                List<TagType> list = v == null ? new ArrayList<>() : v;
                list.add(tag);
                return list;
            });
            return this;
        }

        public Builder customTagPath(Holder<RegistryModule.Key> key, String path, BiFunction<Holder<RegistryModule.Key>, Material, TagKey<Item>> formatter) {
            this.tags.compute(key, (k, v) -> {
                var tag = TagType.withCustomFormatter(path, formatter);
                List<TagType> list = v == null ? new ArrayList<>() : v;
                list.add(tag);
                return list;
            });
            return this;
        }

        public Builder customTagPredicate(Holder<RegistryModule.Key> key, String path, boolean isVanilla, Predicate<Material> materialPredicate) {
            this.tags.compute(key, (k, v) -> {
                var tag = TagType.withCustomFilter(path, isVanilla, materialPredicate);
                List<TagType> list = v == null ? new ArrayList<>() : v;
                list.add(tag);
                return list;
            });
            return this;
        }

        @Override
        public TagsModule build() {
            TagsModule tagsModule = new TagsModule();
            tagsModule.tags.putAll(this.tags);
            return tagsModule;
        }
    }
}
