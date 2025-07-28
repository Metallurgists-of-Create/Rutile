package dev.metallurgists.rutile.registry.flags;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.BlockFlag;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IBlockRegistry;
import dev.metallurgists.rutile.api.material.registry.block.AxisMaterialBlock;
import dev.metallurgists.rutile.api.material.registry.block.IMaterialBlock;
import dev.metallurgists.rutile.api.material.registry.block.MaterialBlock;
import dev.metallurgists.rutile.api.material.registry.block.MaterialBlockItem;
import dev.metallurgists.rutile.registry.RutileFlagKeys;
import dev.metallurgists.rutile.util.helpers.ModelHelpers;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StorageBlockFlag extends BlockFlag {
    @Getter
    private boolean requiresDecompacting;
    @Getter
    private boolean useColumnModel;

    public StorageBlockFlag(String existingNamespace) {
        super("%s_block", existingNamespace);
        this.setTagPatterns(List.of("c:storage_blocks", "c:storage_blocks/%s", "minecraft:mineable/pickaxe"));
        this.setItemTagPatterns(List.of("c:storage_blocks", "c:storage_blocks/%s"));
    }

    public StorageBlockFlag() {
        this("");
    }

    public StorageBlockFlag requiresDecompacting() {
        this.requiresDecompacting = true;
        return this;
    }

    public StorageBlockFlag useColumnModel() {
        this.useColumnModel = true;
        return this;
    }

    @Override
    public BlockEntry<? extends IMaterialBlock> registerBlock(@NotNull Material material, IBlockRegistry flag, @NotNull AbstractRegistrate<?> registrate) {
        NonNullFunction<BlockBehaviour.Properties, MaterialBlock> factory = useColumnModel ? p -> new AxisMaterialBlock(p, material, flag)
                : p -> new MaterialBlock(p, material, flag);
        return registrate.block(getIdPattern().formatted(material.getName()), factory)
                .initialProperties(() -> Blocks.ANDESITE)
                .properties(p -> p.sound(SoundType.METAL))
                .setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .item(MaterialBlockItem::create)
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .build()
                .register();
    }

    @Override
    public void registerBlockAssets(Material material) {
        if (isUseColumnModel()) {
            boolean sidePresent = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/block/materials/" + material.getName() + "/storage_block_side.png")).isPresent();
            boolean endPresent = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/block/materials/" + material.getName() + "/storage_block_end.png")).isPresent();
            String sideTexture = sidePresent ? material.getNamespace() + ":block/materials/" + material.getName() + "/storage_block_side" : "metallurgica:block/materials/null/storage_block_side";
            String endTexture = endPresent ? material.getNamespace() + ":block/materials/" + material.getName() + "/storage_block_end" : "metallurgica:block/materials/null/storage_block_end";
            RutileDynamicResourcePack.addBlockModel(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), ModelHelpers.simplePillar(endTexture, sideTexture));
            RutileDynamicResourcePack.addBlockState(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), ModelHelpers.simpleAxisBlockstate("metallurgica:block/" + getIdPattern().formatted(material.getName())));
        } else {
            boolean texturePresent = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/block/materials/" + material.getName() + "/storage_block.png")).isPresent();
            String texture = texturePresent ? material.getNamespace() + ":block/materials/" + material.getName() + "/storage_block" : "metallurgica:block/materials/null/storage_block";
            RutileDynamicResourcePack.addBlockModel(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), ModelHelpers.simpleCubeAll(texture));
            RutileDynamicResourcePack.addBlockState(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), ModelHelpers.singleVariantBlockstate(material.getNamespace() + ":block/" + getIdPattern().formatted(material.getName())));
        }
        RutileDynamicResourcePack.addItemModel(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), ModelHelpers.simpleParentedModel(material.getNamespace() + ":block/" + getIdPattern().formatted(material.getName())));
    }

    @Override
    public boolean shouldHaveComposition() {
        return true;
    }

    @Override
    public FlagKey<?> getKey() {
        return RutileFlagKeys.STORAGE_BLOCK;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
