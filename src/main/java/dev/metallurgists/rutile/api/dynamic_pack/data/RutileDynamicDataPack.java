package dev.metallurgists.rutile.api.dynamic_pack.data;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.data.FinishedComposition;
import dev.metallurgists.rutile.api.dynamic_pack.RutileDynamicPackContents;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePluginFinder;
import dev.metallurgists.rutile.config.RutileConfig;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.SharedConstants;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class RutileDynamicDataPack implements PackResources {
    protected static final ObjectSet<String> SERVER_DOMAINS = new ObjectOpenHashSet<>();
    protected static final RutileDynamicPackContents CONTENTS = new RutileDynamicPackContents();

    private final PackLocationInfo info;

    static {
        SERVER_DOMAINS.addAll(Sets.newHashSet(Rutile.ID, "minecraft", "forge", "c"));
    }

    public RutileDynamicDataPack(PackLocationInfo info) {
        this(info, RutilePluginFinder.getModPlugins().stream().map(IRutilePlugin::getPluginNamespace).collect(Collectors.toSet()));
    }

    public RutileDynamicDataPack(PackLocationInfo info, Collection<String> domains) {
        this.info = info;
        SERVER_DOMAINS.addAll(domains);
    }

    public static void clearServer() {
        CONTENTS.clearData();
    }

    private static void addToData(ResourceLocation location, byte[] bytes) {
        CONTENTS.addToData(location, bytes);
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... elements) {
        if (elements.length > 0 && elements[0].equals("pack.png")) {
            return () -> Rutile.class.getResourceAsStream("/icon.png");
        }
        return null;
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
        if (type == PackType.SERVER_DATA) {
            return CONTENTS.getResource(location);
        } else {
            return null;
        }
    }

    @Override
    public void listResources(PackType packType, String namespace, String path, ResourceOutput resourceOutput) {
        if (packType == PackType.SERVER_DATA) {
            CONTENTS.listResources(namespace, path, resourceOutput);
        }
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        return type == PackType.SERVER_DATA ? SERVER_DOMAINS : Set.of();
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable <T> T getMetadataSection(MetadataSectionSerializer<T> metaReader) {
        if (metaReader == PackMetadataSection.TYPE) {
            return (T) new PackMetadataSection(Component.literal("Rutile dynamic data"),
                    SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA));
        }
        return null;
    }

    @Override
    public PackLocationInfo location() {
        return info;
    }

    @Override
    public void close() {
        // NOOP
    }

    @ApiStatus.Internal
    public static void writeJson(ResourceLocation id, @Nullable String subDir, Path parent, byte[] json) {
        try {
            Path file;
            if (subDir != null) {
                // assume JSON
                file = parent.resolve(id.getNamespace()).resolve(subDir).resolve(id.getPath() + ".json");
            } else {
                // assume the file type is also appended if a full path is given.
                file = parent.resolve(id.getNamespace()).resolve(id.getPath());
            }
            Files.createDirectories(file.getParent());
            try (OutputStream output = Files.newOutputStream(file)) {
                output.write(json);
            }
        } catch (IOException e) {
            Rutile.LOGGER.error("Failed to write JSON export for file {}", id, e);
        }
    }

    public static void addRecipe(ResourceLocation recipeId, Recipe<?> recipe, @Nullable AdvancementHolder advancement,
                                 HolderLookup.Provider provider) {
        JsonElement recipeJson = Recipe.CODEC.encodeStart(provider.createSerializationContext(JsonOps.INSTANCE), recipe)
                .getOrThrow();
        byte[] recipeBytes = recipeJson.toString().getBytes(StandardCharsets.UTF_8);
        Path parent = Rutile.getGameDir().resolve("rutile/dumped/data");
        if (RutileConfig.client().dumpRecipes.get()) {
            writeJson(recipeId, "recipes", parent, recipeBytes);
        }
        addToData(getRecipeLocation(recipeId), recipeBytes);
        if (advancement != null) {
            JsonElement advancementJson = Advancement.CODEC
                    .encodeStart(provider.createSerializationContext(JsonOps.INSTANCE), advancement.value())
                    .getOrThrow();
            byte[] advancementBytes = advancementJson.toString().getBytes(StandardCharsets.UTF_8);
            addToData(getAdvancementLocation(advancement.id()), advancementBytes);
        }
    }

    public static void addLootTable(ResourceLocation lootTableId, LootTable table, HolderLookup.Provider provider) {
        JsonElement lootTableJson = LootTable.DIRECT_CODEC
                .encodeStart(provider.createSerializationContext(JsonOps.INSTANCE), table).getOrThrow();
        byte[] lootTableBytes = lootTableJson.toString().getBytes(StandardCharsets.UTF_8);
        Path parent = Rutile.getGameDir().resolve("rutile/dumped/data");
        if (RutileConfig.client().dumpRecipes.get()) {
            writeJson(lootTableId, "loot_table", parent, lootTableBytes);
        }
        if (CONTENTS.getResource(lootTableId) != null) {
            Rutile.LOGGER.error("duplicate loot table: {}", lootTableId);
        }
        addToData(getLootTableLocation(lootTableId), lootTableBytes);
    }

    public static void addComposition(FinishedComposition composition) {
        JsonObject compositionJson = composition.serializeComposition();
        byte[] compositionBytes = compositionJson.toString().getBytes(StandardCharsets.UTF_8);
        ResourceLocation compositionId = composition.getId();
        Path parent = Rutile.getGameDir().resolve("rutile/dumped/data");
        if (RutileConfig.client().dumpCompositions.get()) {
            writeJson(compositionId, "compositions", parent, compositionBytes);
        }
        addToData(getCompositionLocation(compositionId), compositionJson.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static ResourceLocation getRecipeLocation(ResourceLocation recipeId) {
        return recipeId.withPath(path -> "recipe/" + path + ".json");
    }

    public static ResourceLocation getLootTableLocation(ResourceLocation lootTableId) {
        return lootTableId.withPath(path -> "loot_table/" + path + ".json");
    }

    public static ResourceLocation getAdvancementLocation(ResourceLocation advancementId) {
        return advancementId.withPath(path -> "advancement/" + path + ".json");
    }

    public static ResourceLocation getTagLocation(String identifier, ResourceLocation tagId) {
        return tagId.withPath(path -> "tags/" + identifier + "/" + path + ".json");
    }

    public static ResourceLocation getCompositionLocation(ResourceLocation compId) {
        return ResourceLocation.fromNamespaceAndPath(compId.getNamespace(), String.join("", "rutile/composition/material/", compId.getPath(), ".json"));
    }
}
