package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.material.flags.MaterialFlags;
import dev.metallurgists.rutile.api.material.registry.AxisMaterialBlock;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import dev.metallurgists.rutile.util.ModelHelpers;
import net.minecraft.tags.BlockTags;

import static dev.metallurgists.rutile.api.tag.TagPrefix.Conditions.*;

public class RutileTagPrefixes {

    public static TagPrefix Ingot = new TagPrefix(Rutile.id("ingot"))
            .defaultTagPath("ingots/%s")
            .unformattedTagPath("ingots")
            .materialAmount(TagPrefix.M)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(hasFlag(FlagKey.INGOT));

    public static final TagPrefix Nugget = new TagPrefix(Rutile.id("nugget"))
            .defaultTagPath("nuggets/%s")
            .unformattedTagPath("nuggets")
            .materialAmount(TagPrefix.M / 9)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(hasFlag(FlagKey.INGOT));

    public static final TagPrefix Gem = new TagPrefix(Rutile.id("gem"))
            .defaultTagPath("gems/%s")
            .unformattedTagPath("gems")
            .langValue("%s")
            .materialAmount(TagPrefix.M)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(hasFlag(FlagKey.GEM));

    public static final TagPrefix Block = new TagPrefix(Rutile.id("block"))
            .defaultTagPath("storage_blocks/%s")
            .unformattedTagPath("storage_blocks")
            .langValue("Block of %s")
            .materialAmount(TagPrefix.M * 9)
            .miningToolTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .generateBlock(true)
            .generationCondition(hasAnyFlag(FlagKey.INGOT, FlagKey.GEM).and(
                    flagValue(FlagKey.PILLAR_MODEL, (m, t, v) -> (!v.containsKey(t) || v.get(t) == false))))
            .unificationEnabled(true);

    public static final TagPrefix BlockPillar = new TagPrefix(Rutile.id("block_pillar"))
            .defaultTagPath("storage_blocks/%s")
            .unformattedTagPath("storage_blocks")
            .langAlias(Rutile.id("block"))
            .idPattern("%s_block")
            .langValue("Block of %s")
            .materialAmount(TagPrefix.M * 9)
            .miningToolTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .blockConstructor(AxisMaterialBlock::new)
            .blockAssetProperties(TagPrefix.BlockAssetProperties.builder()
                    .model(ModelHelpers.BlockModel::pillar)
                    .blockState(ModelHelpers.BlockState::axis)
                    .build())
            .generateBlock(true)
            .generationCondition(hasAnyFlag(FlagKey.INGOT, FlagKey.GEM).and(
                    flagValue(FlagKey.PILLAR_MODEL, (m, t, v) -> (v.containsKey(t) && v.get(t) == true))))
            .unificationEnabled(true);
}
