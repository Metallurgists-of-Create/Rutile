package dev.metallurgists.rutile.api.tag;

import com.google.common.collect.Table;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.RutileApi;
import dev.metallurgists.rutile.api.material.ItemMaterialData;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.material.flags.IMaterialFlag;
import dev.metallurgists.rutile.api.material.registry.MaterialBlock;
import dev.metallurgists.rutile.api.material.registry.MaterialBlockItem;
import dev.metallurgists.rutile.api.material.registry.MaterialItem;
import dev.metallurgists.rutile.api.memorizer.Memorizer;
import dev.metallurgists.rutile.registry.RutileMaterials;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

@Accessors(chain = true, fluent = true)
public class TagPrefix {
    public static final Codec<TagPrefix> CODEC = ResourceLocation.CODEC.flatXmap(
            str -> Optional.ofNullable(get(str)).map(DataResult::success)
                    .orElseGet(() -> DataResult.error(() -> "invalid TagPrefix: " + str)),
            prefix -> DataResult.success(prefix.id));

    public static void init() {}

    public static TagPrefix get(ResourceLocation id) {
        return RutileApi.getTagPrefixRegistry().getById(id);
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
        public static <T extends IMaterialFlag> Predicate<Material> hasFlag(FlagKey<T> key) {
            return mat -> mat.hasFlag(key);
        }
    }

    public record BlockProperties(Supplier<Supplier<RenderType>> renderType,
                                  UnaryOperator<BlockBehaviour.Properties> properties) {}

    @Getter
    public final ResourceLocation id;
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
    private boolean generateItem;
    @Getter
    @Setter
    private ItemConstructor itemConstructor = MaterialItem::new;
    @Setter
    private boolean generateBlock;
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
    private @Nullable Predicate<Material> generationCondition;

    @Setter
    private Supplier<Table<TagPrefix, Material, ? extends Supplier<? extends ItemLike>>> itemTable;

    @Nullable
    @Getter
    @Setter
    private BiConsumer<Material, List<Component>> tooltip;

    private final Map<Material, Collection<Supplier<? extends ItemLike>>> ignoredMaterials = new HashMap<>();
    @Getter
    private final Object2FloatMap<Material> materialAmounts = new Object2FloatOpenHashMap<>();

    @Getter
    @Setter
    private int maxStackSize = 64;

    @Getter
    protected final Set<TagKey<Block>> miningToolTag = new HashSet<>();

    public TagPrefix(ResourceLocation id) {
        this.id = id;
        String lowerCaseUnder = getLowerCaseName();
        this.idPattern = "%s_" + lowerCaseUnder;
        this.langValue = "%s " + RutileClient.toEnglishName(lowerCaseUnder);
        RutileApi.getTagPrefixRegistry().register(this);
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
                (generationCondition == null || generationCondition.test(material)) ||
                (hasItemTable() && this.itemTable.get() != null && getItemFromTable(material) != null);
    }

    public boolean doGenerateBlock() {
        return generateBlock;
    }

    public boolean doGenerateBlock(Material material) {
        return generateBlock && !isIgnored(material) &&
                (generationCondition == null || generationCondition.test(material)) ||
                hasItemTable() && this.itemTable.get() != null && getItemFromTable(material) != null;
    }

    public String getLowerCaseName() {
        return RutileClient.toLowerCaseUnder(this.id.getPath());
    }

    public String getUnlocalizedName() {
        return id().toLanguageKey("materialflag");
    }

    public MutableComponent getLocalizedName(Material material) {
        return Component.translatable(getUnlocalizedName(material), material.getDisplayName());
    }

    public String getUnlocalizedName(Material material) {
        String matSpecificKey = String.format("item.%s.%s", material.getModId(),
                this.idPattern.formatted(material.getName()));
        if (Language.getInstance().has(matSpecificKey)) {
            return matSpecificKey;
        }

        return getUnlocalizedName();
    }

    public boolean isIgnored(Material material) {
        return ignoredMaterials.containsKey(material);
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

    public void setIgnoredBlock(Material material, Block... blocks) {
        this.setIgnored(material, Arrays.stream(blocks)
                .map(block -> Memorizer.memorizeBlockSupplier(() -> block))
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
        return RutileApi.getTagPrefixRegistry().getAll();
    }

    @Override
    public String toString() {
        return id.toString();
    }

    @FunctionalInterface
    public interface ItemConstructor {

        Item create(Item.Properties properties, TagPrefix prefix, Material material);
    }

    @FunctionalInterface
    public interface BlockConstructor {

        Block create(Block.Properties properties, TagPrefix prefix, Material material);
    }

    @FunctionalInterface
    public interface BlockItemConstructor {

        BlockItem create(Block block, Item.Properties properties, TagPrefix prefix, Material material);
    }
}
