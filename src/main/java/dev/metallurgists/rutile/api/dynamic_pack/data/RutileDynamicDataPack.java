package dev.metallurgists.rutile.api.dynamic_pack.data;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.data.FinishedComposition;
import dev.metallurgists.rutile.api.dynamic_pack.RutileDynamicPackContents;
import dev.metallurgists.rutile.api.dynamic_pack.data.recipe.RutileRecipes;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePluginFinder;
import dev.metallurgists.rutile.config.RutileConfig;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.SharedConstants;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class RutileDynamicDataPack implements PackResources {
    protected static final ObjectSet<String> SERVER_DOMAINS = new ObjectOpenHashSet<>();
    protected static final RutileDynamicPackContents CONTENTS = new RutileDynamicPackContents();

    private final String name;

    static {
        SERVER_DOMAINS.addAll(Sets.newHashSet(Rutile.ID, "minecraft", "forge", "c"));
    }

    public RutileDynamicDataPack(String name) {
        this(name, RutilePluginFinder.getModPlugins().stream().map(IRutilePlugin::getPluginNamespace).collect(Collectors.toSet()));
    }

    public RutileDynamicDataPack(String name, Collection<String> domains) {
        this.name = name;
        SERVER_DOMAINS.addAll(domains);
    }

    public static void clearServer() {
        CONTENTS.clearData();
    }

    private static void addToData(ResourceLocation location, byte[] bytes) {
        CONTENTS.addToData(location, bytes);
    }

    @Override
    public @Nullable IoSupplier<InputStream> getRootResource(String... strings) {
        return null;
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(PackType packType, ResourceLocation resourceLocation) {
        if (packType == PackType.SERVER_DATA) {
            return CONTENTS.getResource(resourceLocation);
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
    public @NotNull Set<String> getNamespaces(PackType packType) {
        return packType == PackType.SERVER_DATA ? SERVER_DOMAINS : Set.of();
    }

    @Override
    public @Nullable <T> T getMetadataSection(MetadataSectionSerializer<T> metadataSectionSerializer) {
        if (metadataSectionSerializer == PackMetadataSection.TYPE) {
            return (T) new PackMetadataSection(Component.literal("Rutile dynamic data"),
                    SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA));
        } else if (metadataSectionSerializer.getMetadataSectionName().equals("filter")) {
            JsonObject filter = new JsonObject();
            JsonArray block = new JsonArray();
            RutileRecipes.RECIPE_FILTERS.forEach((id) -> { // Collect removed recipes in here, in the pack filter section.
                JsonObject entry = new JsonObject();
                entry.addProperty("namespace", "^" + id.getNamespace().replaceAll("[\\W]", "\\\\$0") + "$");
                entry.addProperty("path", "^recipes/" + id.getPath().replaceAll("[\\W]", "\\\\$0") + "\\.json" + "$");
                block.add(entry);
            });
            filter.add("block", block);
            return metadataSectionSerializer.fromJson(filter);
        }
        return null;
    }

    @Override
    public @NotNull String packId() {
        return this.name;
    }

    @Override
    public void close() {

    }

    @ApiStatus.Internal
    public static void writeJson(ResourceLocation id, @Nullable String subdir, Path parent, JsonElement json) {
        try {
            Path file;
            if (subdir != null) {
                file = parent.resolve(id.getNamespace()).resolve(subdir).resolve(id.getPath() + ".json"); // assume JSON
            } else {
                file = parent.resolve(id.getNamespace()).resolve(id.getPath()); // assume the file type is also appended
                // if a full path is given.
            }
            Files.createDirectories(file.getParent());
            try (OutputStream output = Files.newOutputStream(file)) {
                output.write(json.toString().getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addRecipe(FinishedRecipe recipe) {
        JsonObject recipeJson = recipe.serializeRecipe();
        ResourceLocation recipeId = recipe.getId();
        Path parent = Rutile.getGameDir().resolve("rutile/dumped/data");
        if (RutileConfig.client().dumpRecipes.get()) {
            writeJson(recipeId, "recipes", parent, recipeJson);
        }
        addToData(getRecipeLocation(recipeId), recipeJson.toString().getBytes(StandardCharsets.UTF_8));
        if (recipe.serializeAdvancement() != null) {
            JsonObject advancement = recipe.serializeAdvancement();
            addToData(getAdvancementLocation(Objects.requireNonNull(recipe.getAdvancementId())), advancement.toString().getBytes(StandardCharsets.UTF_8));
        }
    }

    public static void addTag(String identifier, ResourceLocation tagId, JsonObject tagJson) {
        ResourceLocation l = getTagLocation(identifier, tagId);
        addToData(l, tagJson.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static void addAdvancement(ResourceLocation loc, JsonObject obj) {
        ResourceLocation l = getAdvancementLocation(loc);
        addToData(l, obj.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static void addComposition(FinishedComposition composition) {
        JsonObject compositionJson = composition.serializeComposition();
        ResourceLocation compositionId = composition.getId();
        Path parent = Rutile.getGameDir().resolve("rutile/dumped/data");
        if (RutileConfig.client().dumpCompositions.get()) {
            writeJson(compositionId, "compositions", parent, compositionJson);
        }
        addToData(getCompositionLocation(compositionId), compositionJson.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static ResourceLocation getRecipeLocation(ResourceLocation recipeId) {
        return new ResourceLocation(recipeId.getNamespace(), String.join("", "recipes/", recipeId.getPath(), ".json"));
    }

    public static ResourceLocation getAdvancementLocation(ResourceLocation advancementId) {
        return new ResourceLocation(advancementId.getNamespace(),
                String.join("", "advancements/", advancementId.getPath(), ".json"));
    }

    public static ResourceLocation getTagLocation(String identifier, ResourceLocation tagId) {
        return new ResourceLocation(tagId.getNamespace(),
                String.join("", "tags/", identifier, "/", tagId.getPath(), ".json"));
    }

    public static ResourceLocation getCompositionLocation(ResourceLocation compId) {
        return new ResourceLocation(compId.getNamespace(), String.join("", "rutile_utilities/material_compositions/", compId.getPath(), ".json"));
    }
}
