package dev.metallurgists.rutile.client;

import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import dev.metallurgists.rutile.util.ModelHelpers;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.Set;

public class MaterialBlockRenderer {
    private static final Set<MaterialBlockRenderer> MODELS = new HashSet<>();

    public static void create(Block block, Material material, TagPrefix tagPrefix) {
        MODELS.add(new MaterialBlockRenderer(block, material, tagPrefix));
    }

    public static void reinitModels() {
        for (MaterialBlockRenderer model : MODELS) {
            if (model.tagPrefix.doGenerateBlock(model.material)) {
                ModelHelpers.cubeAllBlockModel(model.material, model.tagPrefix);
            }
        }
    }

    private final Block block;
    private final Material material;
    private final TagPrefix tagPrefix;

    protected MaterialBlockRenderer(Block block, Material material, TagPrefix tagPrefix) {
        this.block = block;
        this.material = material;
        this.tagPrefix = tagPrefix;
    }
}
