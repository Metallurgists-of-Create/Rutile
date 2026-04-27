package dev.metallurgists.rutile.client;

import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import dev.metallurgists.rutile.util.ModelHelpers;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.Set;

public class MaterialBlockRenderer {
    private static final Set<MaterialBlockRenderer> MODELS = new HashSet<>();

    public static void create(Block block, Material material, RegistryModule.Key registerKey) {
        MODELS.add(new MaterialBlockRenderer(block, material, registerKey));
    }

    public static void reinitModels() {
        for (MaterialBlockRenderer model : MODELS) {
            ModelHelpers.blockModel(model.material, model.registerKey);
        }
    }

    private final Block block;
    private final Material material;
    private final RegistryModule.Key registerKey;

    protected MaterialBlockRenderer(Block block, Material material, RegistryModule.Key registerKey) {
        this.block = block;
        this.material = material;
        this.registerKey = registerKey;
    }
}
