package dev.metallurgists.rutile.client;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IBlockRegistry;
import dev.metallurgists.rutile.api.material.registry.block.AxisMaterialBlock;
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
            var flag = model.material.getFlag(model.flagKey);
            if (flag instanceof IBlockRegistry blockRegistry) blockRegistry.registerBlockAssets(model.material);
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
