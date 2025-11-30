package dev.metallurgists.rutile.registry;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.FlagContainer;
import dev.metallurgists.rutile.api.material.builder.FlagSource;
import dev.metallurgists.rutile.api.material.builder.MaterialRegistryBuilder;
import dev.metallurgists.rutile.api.material.flag.types.IPartialHolder;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import net.minecraft.resources.ResourceLocation;

public class RutilePartialModels {
    public static ImmutableTable.Builder<FlagSource<?>, Material, PartialModel> MATERIAL_PARTIALS_BUILDER = ImmutableTable.builder();
    public static Table<FlagSource<?>, Material, PartialModel> MATERIAL_PARTIALS;

    public static void generateMaterialPartials() {
        for (Material material : RutileAPI.getMaterialRegistry().getAll()) {
            for (FlagContainer<?> flagContainer : material.getFlags().getFlagContainers()) {
                for (MaterialRegistryBuilder<?> builder : flagContainer.getBuilders().values()) {
                    if (builder instanceof IPartialHolder partialHolder) {
                        generatePartialModel(partialHolder.getModelLocation(material), builder.getFlagSource(), material);
                    }
                }
            }
        }
        MATERIAL_PARTIALS = MATERIAL_PARTIALS_BUILDER.build();
    }

    private static void generatePartialModel(ResourceLocation location, FlagSource<?> flagSource, Material material) {
        PartialModel model = block(location);
        MATERIAL_PARTIALS_BUILDER.put(flagSource, material, model);
    }

    private static PartialModel block(ResourceLocation location) {
        return PartialModel.of(ResourceLocation.fromNamespaceAndPath(location.getNamespace(), String.join("", "block/", location.getPath())));
    }

    public static PartialModel getPartial(Material material, FlagSource<?> flagSource) {
        if (!MATERIAL_PARTIALS.contains(flagSource, material)) throw new IllegalArgumentException("No such partial model is present");
        return MATERIAL_PARTIALS.get(flagSource, material);
    }

    public static void clientInit() {
        generateMaterialPartials();
    }
}
