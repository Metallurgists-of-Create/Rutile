package dev.metallurgists.rutile.api.material;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Pair;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.Composition;
import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.element.ElementStack;
import dev.metallurgists.rutile.api.fluid.FluidBuilder;
import dev.metallurgists.rutile.api.fluid.FluidState;
import dev.metallurgists.rutile.api.fluid.storage.FluidStorageKey;
import dev.metallurgists.rutile.api.fluid.storage.FluidStorageKeys;
import dev.metallurgists.rutile.api.material.flags.*;
import dev.metallurgists.rutile.api.registry.IDisplayedName;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import dev.metallurgists.rutile.api.registry.flags.registry.FluidFlag;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import dev.metallurgists.rutile.api.tag.TagUtil;
import dev.metallurgists.rutile.registry.RutileMaterials;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class Material implements IDisplayedName, FeatureElement {

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

    public <T extends IMaterialFlag> void addFlag(FlagKey<T> key, T value) {
        if (RutileRegistries.MATERIALS.isFrozen()) {
            throw new IllegalStateException("Cannot add flag to material when registry is frozen!");
        }
        this.flags.setFlag(key, value);
    }

    public <T extends IMaterialFlag> boolean hasFlag(FlagKey<T> key) {
        return this.flags.hasFlag(key);
    }

    public Composition getComposition() {
        return this.info.composition;
    }

    public boolean isElement() {
        Composition composition = this.getComposition();
        if (composition != null) {
            List<SubComposition> subCompositions = composition.compositions();
            if (subCompositions.size() < 2) {
                List<ElementStack> elements = composition.compositions.getFirst().getElements();
                if (elements.size() < 2) {
                    ElementStack element = elements.getFirst();
                    return element.getAmount() == 1;
                }
            }
        }
        return false;
    }



    public <T extends IMaterialFlag> T getFlag(FlagKey<T> key) {
        return this.flags.getFlag(key);
    }

    public <V, T extends UnitFlag<V>> V getFlagValue(FlagKey<T> key) {
        if (!this.flags.hasFlag(key)) {
            return null;
        }
        return this.flags.getFlag(key).getValue();
    }

    public <A, B, T extends BiUnitFlag<A, B>> Pair<A, B> getFlagValues(FlagKey<T> key) {
        if (!this.flags.hasFlag(key)) {
            return null;
        }
        BiUnitFlag<A, B> flag = this.flags.getFlag(key);
        return Pair.of(flag.getFirstValue(), flag.getSecondValue());
    }

    public <K, V, T extends MapFlag<K, V>> V getFlagValue(FlagKey<T> key, K mapKey) {
        if (!this.flags.hasFlag(key)) {
            return null;
        }
        return this.flags.getFlag(key).getValue().get(mapKey);
    }

    public boolean shouldGenerateRecipesFor(@NotNull TagPrefix prefix) {
        return (!this.hasFlag(FlagKey.DISABLE_RECIPES)) && !MaterialHelper.get(prefix, this).isEmpty();
    }

    public int getBlockHarvestLevel() {
        if (!hasFlag(FlagKey.HARVEST_TIER))
            throw new IllegalArgumentException("Material " + info.resourceLocation +
                    " does not have a harvest level! Is probably a Fluid");
        int harvestLevel = getFlagValue(FlagKey.HARVEST_TIER);
        return harvestLevel > 0 ? harvestLevel - 1 : harvestLevel;
    }

    public Fluid getFluid() {
        FluidFlag flag = getFlag(FlagKey.FLUID);
        if (flag == null) {
            throw new IllegalArgumentException("Material " + getId() + " does not have a Fluid!");
        }

        Fluid fluid = flag.get(flag.getPrimaryKey());
        if (fluid != null) return fluid;

        fluid = getFluid(FluidStorageKeys.LIQUID);
        if (fluid != null) return fluid;

        return getFluid(FluidStorageKeys.GAS);
    }

    public Fluid getFluid(@NotNull FluidStorageKey key) {
        FluidFlag flag = getFlag(FlagKey.FLUID);
        if (flag == null) {
            throw new IllegalArgumentException("Material " + getId() + " does not have a Fluid!");
        }

        return flag.get(key);
    }

    public FluidStack getFluid(int amount) {
        return new FluidStack(getFluid(), amount);
    }

    public FluidStack getFluid(@NotNull FluidStorageKey key, int amount) {
        return new FluidStack(getFluid(key), amount);
    }

    public TagKey<Fluid> getFluidTag() {
        return TagUtil.createFluidTag(this.getName());
    }

    public SizedFluidIngredient asFluidIngredient(int amount) {
        return SizedFluidIngredient.of(getFluidTag(), amount);
    }

    public SizedFluidIngredient asSingleFluidIngredient(int amount) {
        return SizedFluidIngredient.of(getFluid(), amount);
    }

    public FluidBuilder getFluidBuilder() {
        FluidFlag flag = getFlag(FlagKey.FLUID);
        if (flag == null) {
            throw new IllegalArgumentException("Material " + getId() + " does not have a Fluid!");
        }

        FluidStorageKey key = flag.getPrimaryKey();
        FluidBuilder fluid = null;

        if (key != null) fluid = flag.getStorage().getQueuedBuilder(key);
        if (fluid != null) return fluid;

        fluid = getFluidBuilder(FluidStorageKeys.LIQUID);
        if (fluid != null) return fluid;

        return getFluidBuilder(FluidStorageKeys.GAS);
    }

    public FluidBuilder getFluidBuilder(@NotNull FluidStorageKey key) {
        FluidFlag flag = getFlag(FlagKey.FLUID);
        if (flag == null) {
            throw new IllegalArgumentException("Material " + getId() + " does not have a Fluid!");
        }

        return flag.getStorage().getQueuedBuilder(key);
    }

    public Item getBucket() {
        Fluid fluid = getFluid();
        return fluid.getBucket();
    }

    public boolean isSolid() {
        return hasFlag(FlagKey.INGOT) || hasFlag(FlagKey.GEM);
    }

    public boolean hasFluid() {
        return hasFlag(FlagKey.FLUID);
    }

    protected void register() {
        RutileRegistries.register(RutileRegistries.MATERIALS, this.getId(), this);
    }

    public boolean isNull() {
        return this == RutileMaterials.Null;
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return getInfo().getRequiredFeatures();
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

        public Builder fluid() {
            fluid(FluidStorageKeys.LIQUID, new FluidBuilder());
            return this;
        }

        public Builder fluid(Function<FluidBuilder, FluidBuilder> builder) {
            fluid(FluidStorageKeys.LIQUID, builder.apply(new FluidBuilder()));
            return this;
        }

        public Builder fluid(@NotNull FluidStorageKey key, @NotNull FluidState state) {
            return fluid(key, new FluidBuilder().state(state));
        }

        public Builder fluid(@NotNull FluidStorageKey key, @NotNull FluidBuilder builder) {
            flags.ensureSet(FlagKey.FLUID);
            FluidFlag flag = flags.getFlag(FlagKey.FLUID);
            flag.enqueueRegistration(key, builder);
            return this;
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

        public Builder components(Object... components) {
            Preconditions.checkArgument(
                    components.length % 2 == 0,
                    "Material Components list malformed!");
            for (int i = 0; i < components.length; i += 2) {
                if (components[i] == null) {
                    throw new IllegalArgumentException(
                            "Material in Components List is null for Material " + this.info.resourceLocation);
                }
                Material material = components[i] instanceof CharSequence chars ? RutileRegistries.MATERIALS.get(Rutile.id(chars.toString())) :
                        (Material) components[i];
                int amount = (Integer) components[i + 1];
                SubComposition.Builder subCompositionBuilder = SubComposition.builder();
                for (var subComp : material.getComposition().compositions()) {
                    subComp.getElements().forEach(subCompositionBuilder::element);
                }
                subCompositionBuilder.setAmount(amount);
                subCompositions.add(subCompositionBuilder.build());
            }
            return this;
        }

        public Builder ignoredTagPrefixes(TagPrefix... prefixes) {
            if (this.ignoredTagPrefixes == null) {
                this.ignoredTagPrefixes = new HashSet<>();
            }
            this.ignoredTagPrefixes.addAll(Arrays.asList(prefixes));
            return this;
        }

        public Builder requiredFeatures(FeatureFlag... requiredFeatures) {
            FeatureFlagSet flagSet = FeatureFlags.REGISTRY.subset(requiredFeatures);
            info.setRequiredFeatures(flagSet);
            return this;
        }

        public Material build() {
            info.setComposition(new Composition(this.subCompositions));
            var mat = new Material(info, flags);
            if (!itemTags.isEmpty()) {
                mat.setItemTags(itemTags);
            }
            mat.register();
            if (ignoredTagPrefixes != null) {
                ignoredTagPrefixes.forEach(p -> p.setIgnored(mat));
            }
            return mat;
        }
    }
}
