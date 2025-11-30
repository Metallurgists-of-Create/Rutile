package dev.metallurgists.rutile.client;

import dev.metallurgists.rutile.api.material.FlagRegistryTypes;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialLike;
import dev.metallurgists.rutile.api.material.builder.BlockFlagContainer;
import dev.metallurgists.rutile.api.material.builder.FlagSource;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.Set;

public class MaterialBlockRenderer {
    private static final Set<MaterialBlockRenderer> MODELS = new HashSet<>();

    public static void create(Block block, MaterialLike material, FlagSource<Block> flagSource) {
        MODELS.add(new MaterialBlockRenderer(block, material.asMaterial(), flagSource));
    }

    public static void reinitModels() {
        for (MaterialBlockRenderer model : MODELS) {
            BlockFlagContainer flagContainer = (BlockFlagContainer) model.material.getFlagContainer(FlagRegistryTypes.BLOCK);
            if (flagContainer != null) {
                for (var builder : flagContainer.getBuilders().values()) {
                    builder.registerAssets(model.material);
                }
            }
        }
    }

    private final Block block;
    private final Material material;
    private final FlagSource<Block> flagSource;

    protected MaterialBlockRenderer(Block block, Material material, FlagSource<Block> flagSource) {
        this.block = block;
        this.material = material;
        this.flagSource = flagSource;
    }
}
