package dev.metallurgists.rutile.api.material;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.element.ElementStack;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.material.flags.IMaterialFlag;
import dev.metallurgists.rutile.api.material.flags.MaterialFlags;
import dev.metallurgists.rutile.api.registry.IDisplayedName;
import dev.metallurgists.rutile.api.registry.flags.BurnableFlag;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Material implements IDisplayedName {

    @NotNull
    @Getter
    private final MaterialFlags flags;

    @NotNull
    @Getter
    private final MaterialInfo info;

    private String descriptionId;

    @Setter
    @Getter
    private List<TagKey<Item>> itemTags = new ArrayList<>();

    public Material(@NotNull MaterialInfo info, @NotNull MaterialFlags flags) {
        this.info = info;
        this.flags = flags;
    }

    /**
     * The internal name of this material.
     * This is used for registration, so it MUST be all lowercase with underscores for spaces
     * @return the internal name of this material
     */
    public String getName() {
        return this.getId().getPath();
    }

    /**
     * The modid of the mod that registered this material
     * @return the modid of this material
     */
    public String getModId() {
        return this.getId().getNamespace();
    }

    public ResourceLocation getId() {
        return this.info.resourceLocation;
    }

    @Override
    public String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("material", getId());
        }
        return this.descriptionId;
    }

    public <T extends IMaterialFlag> boolean hasFlag(FlagKey<T> key) {
        return this.flags.hasFlag(key);
    }

    public Composition getComposition() {
        return this.info.composition;
    }

    public <T extends IMaterialFlag> T getFlag(FlagKey<T> key) {
        return this.flags.getFlag(key);
    }

    public int getBlockHarvestLevel() {
        if (!hasFlag(FlagKey.HARVEST_TIER))
            throw new IllegalArgumentException("Material " + info.resourceLocation +
                    " does not have a harvest level! Is probably a Fluid");
        int harvestLevel = getFlag(FlagKey.HARVEST_TIER).getHarvestLevel();
        return harvestLevel > 0 ? harvestLevel - 1 : harvestLevel;
    }

    public static class Builder {
        private final MaterialFlags flags;
        private final MaterialInfo info;

        private Set<TagPrefix> ignoredTagPrefixes = null;

        private List<SubComposition> subCompositions = new ArrayList<>();

        private final List<TagKey<Item>> itemTags = new ArrayList<>();

        public Builder(ResourceLocation resourceLocation) {
            String name = resourceLocation.getPath();
            if (name.charAt(name.length() - 1) == '_')
                throw new IllegalArgumentException("Material name cannot end with a '_'!");
            info = new MaterialInfo(resourceLocation);
            flags = new MaterialFlags();
        }

        public Builder customTags(TagKey<Item> key) {
            this.itemTags.add(key);
            return this;
        }

        public Builder colour(int rgb) {
            info.setColour(rgb);
            return this;
        }

        public <T extends IMaterialFlag> Builder flag(FlagKey<T> key, T value) {
            flags.setFlag(key, value);
            return this;
        }

        public <T extends IMaterialFlag> Builder flag(FlagKey<T> key) {
            flags.ensureSet(key);
            return this;
        }

        public Builder element(String element) {
            return composition("%s %s".formatted(1, element));
        }

        public Builder composition(String... components) {
            List<ElementStack> elementStacks = new ArrayList<>();
            for (String raw : components) {
                String[] split = raw.split(" ", 2);
                int amount = Integer.parseInt(split[0]) <= 0 ? 1 : Integer.parseInt(split[0]);
                String element = split[1];
                if (element.isEmpty()) throw new IllegalArgumentException("Element is invalid or empty");
                ResourceLocation elementKey = Rutile.id(element);
                ElementStack elementStack = new ElementStack(elementKey, amount);
                elementStacks.add(elementStack);
            }
            SubComposition subComposition = new SubComposition(elementStacks, 1);
            subCompositions.add(subComposition);
            return this;
        }

        public Builder ignoredTagPrefixes(TagPrefix... prefixes) {
            if (this.ignoredTagPrefixes == null) {
                this.ignoredTagPrefixes = new HashSet<>();
            }
            this.ignoredTagPrefixes.addAll(Arrays.asList(prefixes));
            return this;
        }

        public Material build() {
            info.setComposition(new Composition(this.subCompositions));
            var mat = new Material(info, flags);
            if (!itemTags.isEmpty()) {
                mat.setItemTags(itemTags);
            }
            if (ignoredTagPrefixes != null) {
                ignoredTagPrefixes.forEach(p -> p.setIgnored(mat));
            }
            return mat;
        }
    }
}
