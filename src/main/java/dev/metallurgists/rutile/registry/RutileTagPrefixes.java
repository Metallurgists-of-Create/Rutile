package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import net.minecraft.tags.BlockTags;

import static dev.metallurgists.rutile.api.tag.TagPrefix.Conditions.*;

public class RutileTagPrefixes {

    public static final TagPrefix RawOre = new TagPrefix(Rutile.id("raw"))
            .idPattern("raw_%s")
            .defaultTagPath("raw_materials/%s")
            .unformattedTagPath("raw_materials")
            .langValue("Raw %s")
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(hasFlag(FlagKey.ORE));

    public static final TagPrefix RawOreBlock = new TagPrefix(Rutile.id("raw_ore_block"))
            .idPattern("raw_%s_block")
            .defaultTagPath("storage_blocks/raw_%s")
            .unformattedTagPath("storage_blocks")
            .langValue("Block of Raw %s")
            .miningToolTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .unificationEnabled(true)
            .generateBlock(true)
            .generationCondition(hasFlag(FlagKey.ORE));

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

    public static final TagPrefix Dust = new TagPrefix(Rutile.id("dust"))
            .defaultTagPath("dusts/%s")
            .unformattedTagPath("dusts")
            .materialAmount(TagPrefix.M)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(hasFlag(FlagKey.DUST));

    public static final TagPrefix Block = new TagPrefix(Rutile.id("block"))
            .defaultTagPath("storage_blocks/%s")
            .unformattedTagPath("storage_blocks")
            .langValue("Block of %s")
            .materialAmount(TagPrefix.M * 9)
            .miningToolTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .generateBlock(true)
            .generationCondition(hasAnyFlag(FlagKey.INGOT, FlagKey.GEM))
            .unificationEnabled(true);

    public static void init() {}
}
