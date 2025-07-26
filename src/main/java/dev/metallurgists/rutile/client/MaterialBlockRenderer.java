package dev.metallurgists.rutile.client;

import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.registry.block.AxisMaterialBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.Set;

public class MaterialBlockRenderer {
    private static final Set<MaterialBlockRenderer> MODELS = new HashSet<>();

    public static void create(Block block, Material material, FlagKey<?> flagKey) {
        if (block instanceof AxisMaterialBlock) {
            PillarMaterialBlockRenderer.create(block, material, flagKey);
            return;
        }
        MODELS.add(new MaterialBlockRenderer(block, material, flagKey));
    }

    public static void reinitModels() {
        for (MaterialBlockRenderer model : MODELS) {
            String namespace = model.material.getNamespace();
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(model.block);
            String blockName = model.flagKey.toString();
            boolean texturePresent = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(namespace, "textures/block/materials/" + model.material.getName() + "/%s.png".formatted(blockName))).isPresent();
            String texture = texturePresent ? namespace+":block/materials/" + model.material.getName() + "/" + blockName : "rutile:block/materials/null/" + blockName;
            ResourceLocation modelId = blockId.withPrefix("block/");
            RutileDynamicResourcePack.addBlockModel(blockId, RutileModels.simpleCubeAll(texture));
            RutileDynamicResourcePack.addBlockState(blockId, BlockModelGenerators.createSimpleBlock(model.block, modelId));
            RutileDynamicResourcePack.addItemModel(BuiltInRegistries.ITEM.getKey(model.block.asItem()), new DelegatedModel(ModelLocationUtils.getModelLocation(model.block)));
        }
    }

    private final Block block;
    private final Material material;
    private final FlagKey<?> flagKey;

    protected MaterialBlockRenderer(Block block, Material material, FlagKey<?> flagKey) {
        this.block = block;
        this.material = material;
        this.flagKey = flagKey;
    }
}
