package dev.metallurgists.rutile.registry;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IPartialHolder;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.material.MaterialRegistry;
import net.minecraft.resources.ResourceLocation;

public class RutilePartialModels {
    public static ImmutableTable.Builder<FlagKey<?>, Material, PartialModel> MATERIAL_PARTIALS_BUILDER = ImmutableTable.builder();
    public static Table<FlagKey<?>, Material, PartialModel> MATERIAL_PARTIALS;

    public static void generateMaterialPartials() {
        for (var flagKey : RutileAPI.getRegisteredFlags().values()) {
            if (flagKey.constructDefault() instanceof IPartialHolder) {
                for (MaterialRegistry registry : RutileAPI.materialManager.getRegistries()) {
                    for (Material material : registry.getAllMaterials()) {
                        var flag = material.getFlag(flagKey);
                        if (flag instanceof IPartialHolder partialHolder) {
                            generatePartialModel(partialHolder.getModelLocation(material), flagKey, material);
                        }
                    }
                }
            }
        }
        MATERIAL_PARTIALS = MATERIAL_PARTIALS_BUILDER.build();
    }

    private static void generatePartialModel(ResourceLocation location, FlagKey<?> flagKey, Material material) {
        PartialModel model = block(location);
        MATERIAL_PARTIALS_BUILDER.put(flagKey, material, model);
    }

    private static PartialModel block(ResourceLocation location) {
        return PartialModel.of(new ResourceLocation(location.getNamespace(), String.join("", "block/", location.getPath())));
    }

    public static PartialModel getPartial(Material material, FlagKey<?> flagKey) {
        if (!MATERIAL_PARTIALS.contains(flagKey, material)) throw new IllegalArgumentException("No such partial model is present");
        return MATERIAL_PARTIALS.get(flagKey, material);
    }

    public static void clientInit() {
        generateMaterialPartials();
    }
}
