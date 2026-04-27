package dev.metallurgists.rutile.api.tag;

import com.google.common.base.Preconditions;
import com.google.common.collect.Table;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.material.ItemMaterialData;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.material.flags.IMaterialFlag;
import dev.metallurgists.rutile.api.material.flags.UnitFlag;
import dev.metallurgists.rutile.api.material.registry.MaterialBlock;
import dev.metallurgists.rutile.api.material.registry.MaterialBlockItem;
import dev.metallurgists.rutile.api.material.registry.MaterialItem;
import dev.metallurgists.rutile.api.material.source.AbstractMaterialSource;
import dev.metallurgists.rutile.api.material.stack.MaterialStack;
import dev.metallurgists.rutile.api.memorizer.Memoizer;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import dev.metallurgists.rutile.registry.RutileMaterials;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.common.util.TriPredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import static dev.metallurgists.rutile.api.tag.TagPrefix.Conditions.hasFlag;

@Accessors(chain = true, fluent = true)
public class TagPrefix {
    public static final Codec<TagPrefix> CODEC = ResourceLocation.CODEC.flatXmap(
            str -> Optional.ofNullable(get(str)).map(DataResult::success)
                    .orElseGet(() -> DataResult.error(() -> "invalid TagPrefix: " + str)),
            prefix -> DataResult.success(prefix.id));

    public static void init() {}

    public static TagPrefix get(ResourceLocation id) {
        return RutileRegistries.TAG_PREFIXES.get(id);
    }

    public boolean isEmpty() {
        return this == NULL_PREFIX;
    }

    public static final TagPrefix NULL_PREFIX = new TagPrefix(Rutile.id("null_prefix"));

    public String getModId() {
        return this.id.getNamespace();
    }

    public String getName() {
        return this.id.getPath();
    }

    public static class Conditions {
        public static <T extends IMaterialFlag> BiPredicate<Material, TagPrefix> hasFlag(FlagKey<T> key) {
            return (mat, tag) -> mat.hasFlag(key);
        }
        public static BiPredicate<Material, TagPrefix> hasAnyFlag(FlagKey<?>... keys) {
            return (mat, tag) -> {
                for (FlagKey<?> key : keys) {
                    if (mat.hasFlag(key)) return true;
                }
                return false;
            };
        }
        public static BiPredicate<Material, TagPrefix> hasNoFlag(FlagKey<?>... keys) {
            return (mat, tag) -> {
                boolean hasFlag = false;
                for (FlagKey<?> key : keys) {
                    if (mat.hasFlag(key)) hasFlag = true;
                }
                return !hasFlag;
            };
        }

        public static <V, T extends UnitFlag<V>> BiPredicate<Material, TagPrefix> flagValue(FlagKey<T> key, TriPredicate<Material, TagPrefix, V> valuePredicate) {
            return (mat, tag) -> {
                if (!mat.hasFlag(key)) return true;
                return valuePredicate.test(mat, tag, mat.getFlagValue(key));
            };
        }
    }

    public record BlockAssetProperties(BiFunction<Material, TagPrefix, JsonElement> model, BiFunction<Material, TagPrefix, JsonElement> blockState, BiFunction<Material, TagPrefix, JsonElement> itemModel) {
        static BiFunction<Material, TagPrefix, JsonElement> EMPTY_FUNC = (m, t) -> new JsonObject();
        public static BlockAssetProperties EMPTY = new BlockAssetProperties(EMPTY_FUNC,EMPTY_FUNC,EMPTY_FUNC);

        public boolean isEmpty() {
            return this == EMPTY;
        }

        public boolean hasModel() {
            return model() != EMPTY_FUNC;
        }

        public boolean hasBlockState() {
            return blockState() != EMPTY_FUNC;
        }

        public boolean hasItemModel() {
            return itemModel() != EMPTY_FUNC;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private BiFunction<Material, TagPrefix, JsonElement> model = EMPTY_FUNC;
            private BiFunction<Material, TagPrefix, JsonElement> blockState = EMPTY_FUNC;
            private BiFunction<Material, TagPrefix, JsonElement> itemModel = EMPTY_FUNC;

            public BlockAssetProperties.Builder model(BiFunction<Material, TagPrefix, JsonElement> model) {
                this.model = model;
                return this;
            }

            public BlockAssetProperties.Builder blockState(BiFunction<Material, TagPrefix, JsonElement> blockState) {
                this.blockState = blockState;
                return this;
            }

            public BlockAssetProperties.Builder itemModel(BiFunction<Material, TagPrefix, JsonElement> itemModel) {
                this.itemModel = itemModel;
                return this;
            }

            public BlockAssetProperties build() {
                return new BlockAssetProperties(model, blockState, itemModel);
            }
        }
    }

    public record BlockProperties(Supplier<Supplier<RenderType>> renderType,
                                  UnaryOperator<BlockBehaviour.Properties> properties) {}

    @Getter
    public final ResourceLocation id;
    @Getter
    @Setter
    public ResourceLocation langAlias;
    @Getter
    @Setter
    private String idPattern;


    protected final List<TagType> tags = new ArrayList<>();

    @Setter
    @Getter
    public String langValue;

    @Getter
    @Setter
    private long materialAmount = -1;

    @Setter
    @Getter
    private boolean unificationEnabled;

    @Setter
    boolean generateItem;
    @Getter
    @Setter
    private ItemConstructor itemConstructor = MaterialItem::new;
    @Setter
    boolean generateBlock;
    @Getter
    @Setter
    private BlockConstructor blockConstructor = MaterialBlock::new;

    @Getter
    @Setter
    private BlockItemConstructor blockItemConstructor = MaterialBlockItem::new;
    @Getter
    @Setter
    private BlockProperties blockProperties = new BlockProperties(() -> RenderType::translucent,
            UnaryOperator.identity());

    @Getter
    @Setter
    private BlockAssetProperties blockAssetProperties = BlockAssetProperties.EMPTY;

    @Getter
    @Setter
    @Nullable BiPredicate<Material, TagPrefix> generationCondition;

    @Setter
    Supplier<Table<TagPrefix, Material, ? extends Supplier<? extends ItemLike>>> itemTable;

    @Nullable
    @Getter
    @Setter
    private BiConsumer<Material, List<Component>> tooltip;

    private final Map<Material, Collection<Supplier<? extends ItemLike>>> ignoredMaterials = new HashMap<>();

    private final Map<Material, BlockConstructor> specialBlockConstructors = new HashMap<>();

    private final Map<Material, ItemConstructor> specialItemConstructors = new HashMap<>();

    private final Map<Material, BlockAssetProperties> specialBlockAssets = new HashMap<>();

    private Map<Material, String> nameAlternatives = new HashMap<>();

    @Getter
    private final Object2FloatMap<Material> materialAmounts = new Object2FloatOpenHashMap<>();

    @Getter
    @Setter
    private int maxStackSize = 64;

    @Getter
    private final List<MaterialStack> secondaryMaterials = new ArrayList<>();

    @Getter
    protected final Set<TagKey<Block>> miningToolTag = new HashSet<>();

    public TagPrefix(ResourceLocation id) {
        this.id = id;
        this.langAlias = id;
        String lowerCaseUnder = getLowerCaseName();
        this.idPattern = "%s_" + lowerCaseUnder;
        this.langValue = "%s " + RutileClient.toEnglishName(lowerCaseUnder);
        RutileRegistries.register(RutileRegistries.TAG_PREFIXES, id, this);
    }

    public static TagPrefix oreTagPrefix(ResourceLocation id, TagKey<Block> miningToolTag) {
        return new TagPrefix(id)
                .defaultTagPath("ores/%s")
                .prefixOnlyTagPath("ores_in_ground/%s")
                .unformattedTagPath("ores")
                .miningToolTag(miningToolTag)
                .unificationEnabled(true)
                .generationCondition(hasFlag(FlagKey.ORE));
    }

    public void addSecondaryMaterial(MaterialStack secondaryMaterial) {
        Preconditions.checkNotNull(secondaryMaterial, "secondaryMaterial");
        secondaryMaterials.add(secondaryMaterial);
    }

    public TagPrefix defaultTagPath(String path) {
        return this.defaultTagPath(path, false);
    }

    public TagPrefix defaultTagPath(String path, boolean isVanilla) {
        this.tags.add(TagType.withDefaultFormatter(path, isVanilla));
        return this;
    }

    public TagPrefix prefixTagPath(String path) {
        this.tags.add(TagType.withPrefixFormatter(path));
        return this;
    }

    public TagPrefix prefixOnlyTagPath(String path) {
        this.tags.add(TagType.withPrefixOnlyFormatter(path));
        return this;
    }

    public TagPrefix unformattedTagPath(String path) {
        return unformattedTagPath(path, false);
    }

    public TagPrefix unformattedTagPath(String path, boolean isVanilla) {
        this.tags.add(TagType.withNoFormatter(path, isVanilla));
        return this;
    }

    public TagPrefix customTagPath(String path, BiFunction<TagPrefix, Material, TagKey<Item>> formatter) {
        this.tags.add(TagType.withCustomFormatter(path, formatter));
        return this;
    }

    public TagPrefix customTagPredicate(String path, boolean isVanilla, Predicate<Material> materialPredicate) {
        this.tags.add(TagType.withCustomFilter(path, isVanilla, materialPredicate));
        return this;
    }

    public TagPrefix miningToolTag(TagKey<Block> tag) {
        this.miningToolTag.add(tag);
        return this;
    }

    public TagPrefix blockProperties(Supplier<Supplier<RenderType>> renderType,
                                     UnaryOperator<BlockBehaviour.Properties> properties) {
        return this.blockProperties(new BlockProperties(renderType, properties));
    }

    public static final long M = 3628800;

    public long getMaterialAmount(@NotNull Material material) {
        if (!isAmountModified(material)) {
            return this.materialAmount;
        }
        return (long) (M * materialAmounts.getFloat(material));
    }

    @Unmodifiable
    public List<TagKey<Item>> getItemParentTags() {
        return tags.stream()
                .filter(TagType::isParentTag)
                .map(type -> type.getTag(this, RutileMaterials.Null))
                .toList();
    }

    @Unmodifiable
    public List<TagKey<Item>> getItemTags(@NotNull Material mat) {
        return tags.stream()
                .filter(type -> !type.isParentTag())
                .map(type -> type.getTag(this, mat))
                .filter(Objects::nonNull)
                .toList();
    }

    @Unmodifiable
    public List<TagKey<Item>> getAllItemTags(@NotNull Material mat) {
        return tags.stream()
                .map(type -> type.getTag(this, mat))
                .filter(Objects::nonNull)
                .toList();
    }

    @Unmodifiable
    public List<TagKey<Block>> getBlockTags(@NotNull Material mat) {
        return tags.stream()
                .filter(type -> !type.isParentTag())
                .map(type -> type.getTag(this, mat)).filter(Objects::nonNull)
                .map(itemTagKey -> TagKey.create(Registries.BLOCK, itemTagKey.location()))
                .toList();
    }

    @Unmodifiable
    public List<TagKey<Block>> getAllBlockTags(@NotNull Material mat) {
        return tags.stream()
                .map(type -> type.getTag(this, mat)).filter(Objects::nonNull)
                .map(itemTagKey -> TagKey.create(Registries.BLOCK, itemTagKey.location()))
                .toList();
    }

    public boolean hasItemTable() {
        return itemTable != null;
    }

    @SuppressWarnings("unchecked")
    public Supplier<ItemLike> getItemFromTable(Material material) {
        return (Supplier<ItemLike>) itemTable.get().get(this, material);
    }

    public boolean doGenerateItem() {
        return generateItem;
    }

    public boolean doGenerateItem(Material material) {
        return generateItem && !isIgnored(material) &&
                (generationCondition == null || generationCondition.test(material, this)) ||
                (hasItemTable() && this.itemTable.get() != null && getItemFromTable(material) != null);
    }

    public boolean doGenerateBlock() {
        return generateBlock;
    }

    public boolean doGenerateBlock(Material material) {
        return generateBlock && !isIgnored(material) &&
                (generationCondition == null || generationCondition.test(material, this)) ||
                hasItemTable() && this.itemTable.get() != null && getItemFromTable(material) != null;
    }

    public String getLowerCaseName() {
        return RutileClient.toLowerCaseUnder(this.id.getPath());
    }

    public String getUnlocalizedName() {
        return langAlias().toLanguageKey("materialflag");
    }

    public MutableComponent getLocalizedName(Material material) {
        return Component.translatable(getUnlocalizedName(material), getMaterialDisplayName(material));
    }

    public String getUnlocalizedName(Material material) {
        String matSpecificKey = String.format("item.%s.%s", material.getModId(), this.idPattern.formatted(getMaterialName(material)));
        if (Language.getInstance().has(matSpecificKey)) {
            return matSpecificKey;
        }

        return getUnlocalizedName();
    }

    public boolean isIgnored(Material material) {
        return ignoredMaterials.containsKey(material);
    }

    public BlockConstructor getBlockConstructor(Material material) {
        return specialBlockConstructors.getOrDefault(material, blockConstructor());
    }

    public ItemConstructor getItemConstructor(Material material) {
        return specialItemConstructors.getOrDefault(material, itemConstructor());
    }

    public BlockAssetProperties getBlockAssetProperties(Material material) {
        return specialBlockAssets.getOrDefault(material, blockAssetProperties());
    }

    public String getMaterialName(Material material) {
        return nameAlternatives.getOrDefault(material, material.getName());
    }

    public Component getMaterialDisplayName(Material material) {
        if (nameAlternatives.containsKey(material)) {
            return Component.translatable(Util.makeDescriptionId("material", material.getId().withSuffix("." + getName())));
        }
        return material.getDisplayName();
    }

    @SafeVarargs
    public final void setIgnored(Material material, Supplier<? extends ItemLike>... items) {
        setIgnored(material, Arrays.asList(items));
    }

    public void setIgnored(Material material, Collection<Supplier<? extends ItemLike>> items) {
        ignoredMaterials.computeIfAbsent(material, m -> new HashSet<>()).addAll(items);
        if (!items.isEmpty()) {
            ItemMaterialData.registerMaterialEntries(items, this, material);
        }
    }

    public void setIgnored(Material material, ItemLike... items) {
        // go through setIgnoredBlock to wrap if this is a block prefix
        if (this.doGenerateBlock()) {
            this.setIgnoredBlock(material, Arrays.stream(items)
                    .filter(Block.class::isInstance)
                    .map(Block.class::cast)
                    .toArray(Block[]::new));
            // also add possible item-only entries
            this.setIgnored(material, Arrays.stream(items)
                    .filter(item -> !(item instanceof Block))
                    .map(item -> (Supplier<ItemLike>) () -> item)
                    .collect(Collectors.toSet()));
        } else {
            this.setIgnored(material, Arrays.stream(items)
                    .map(item -> (Supplier<ItemLike>) () -> item)
                    .collect(Collectors.toSet()));
        }
    }

    public void setBlockConstructor(Material material, BlockConstructor blockConstructor) {
        if (this.doGenerateBlock()) {
            specialBlockConstructors.put(material, blockConstructor);
        }
    }

    public void setItemConstructor(Material material, ItemConstructor itemConstructor) {
        if (this.doGenerateItem()) {
            specialItemConstructors.put(material, itemConstructor);
        }
    }

    public void setBlockAssets(Material material, BlockAssetProperties assetProperties) {
        specialBlockAssets.put(material, assetProperties);
    }

    public void setMaterialName(Material material, String name) {
        nameAlternatives.put(material, name);
    }

    public void setIgnoredBlock(Material material, Block... blocks) {
        this.setIgnored(material, Arrays.stream(blocks)
                .map(block -> Memoizer.memoizeBlockSupplier(() -> block))
                .collect(Collectors.toSet()));
    }

    public void setIgnored(Material material) {
        ignoredMaterials.computeIfAbsent(material, m -> new HashSet<>());
    }

    public void removeIgnored(Material material) {
        ignoredMaterials.remove(material);
    }

    @UnmodifiableView
    public Map<Material, Collection<Supplier<? extends ItemLike>>> getIgnored() {
        return Collections.unmodifiableMap(ignoredMaterials);
    }

    public boolean isAmountModified(Material material) {
        return materialAmounts.containsKey(material);
    }

    public void modifyMaterialAmount(@NotNull Material material, float amount) {
        materialAmounts.put(material, amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagPrefix tagPrefix = (TagPrefix) o;
        return id.equals(tagPrefix.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static Iterable<TagPrefix> values() {
        return RutileRegistries.TAG_PREFIXES;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    @FunctionalInterface
    public interface ItemConstructor {

        <T, P extends AbstractMaterialSource<? extends T, ?>> Item create(Item.Properties properties, AbstractMaterialSource<T, P> source, Material material);
    }

    @FunctionalInterface
    public interface BlockConstructor {

        Block create(Block.Properties properties, AbstractMaterialSource<? extends Block, ? extends AbstractMaterialSource<?, ?>> source, Material material);
    }

    @FunctionalInterface
    public interface BlockItemConstructor {

        BlockItem create(Block block, Item.Properties properties, AbstractMaterialSource<? extends Block, ? extends AbstractMaterialSource<?, ?>> source, Material material);
    }
}
