package dev.metallurgists.rutile.api.material.flag.types;

import dev.metallurgists.rutile.api.material.base.Material;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagHolder<T> {

    @Getter @Setter
    public List<String> tagPatterns = new ArrayList<>();

    public final ResourceKey<Registry<T>> tagPath;

    public TagHolder(ResourceKey<Registry<T>> tagPath) {
        this.tagPath = tagPath;
    }

    public void addPatterns(String... patterns) {
        tagPatterns.addAll(Arrays.asList(patterns));
    }

    public void addPatterns(ResourceLocation... patterns) {
        Arrays.stream(patterns).map(ResourceLocation::toString).forEach(this.tagPatterns::add);
    }

    public List<TagKey<T>> getTags(Material material) {
        List<TagKey<T>> tags = new ArrayList<>();
        this.tagPatterns.forEach((pattern) -> {
            if (pattern.contains("%s")) {
                tags.add(TagKey.create(this.tagPath, ResourceLocation.parse(pattern.formatted(material))));
            } else  {
                tags.add(TagKey.create(this.tagPath, ResourceLocation.parse(pattern)));
            }
        });
        return tags;
    }
}
