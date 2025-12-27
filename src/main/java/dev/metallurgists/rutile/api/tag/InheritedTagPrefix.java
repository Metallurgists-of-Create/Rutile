package dev.metallurgists.rutile.api.tag;

import dev.metallurgists.rutile.api.registry.RutileRegistries;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.resources.ResourceLocation;

@Accessors(chain = true, fluent = true)
public class InheritedTagPrefix extends TagPrefix {

    @Getter
    public final ResourceLocation parentId;

    @Getter
    @Setter
    private boolean replaceParent;


    public InheritedTagPrefix(TagPrefix parent, String id) {
        super(parent.id().withSuffix("/" + id), parent);
        this.parentId = parent.id();
    }

    private TagPrefix getParent() {
        return RutileRegistries.TAG_PREFIXES.get(parentId());
    }

    public InheritedTagPrefix clearTags() {
        this.tags.clear();
        return this;
    }
}
