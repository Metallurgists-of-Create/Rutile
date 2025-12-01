package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.tag.TagPrefix;

import static dev.metallurgists.rutile.api.tag.TagPrefix.Conditions.hasFlag;

public class RutileTagPrefixes {

    public static TagPrefix Ingot = new TagPrefix(Rutile.id("ingot"))
            .defaultTagPath("ingots/%s")
            .unformattedTagPath("ingots")
            .materialAmount(TagPrefix.M)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(hasFlag(FlagKey.INGOT))
            ;

    public static final TagPrefix Gem = new TagPrefix(Rutile.id("gem"))
            .defaultTagPath("gems/%s")
            .unformattedTagPath("gems")
            .langValue("%s")
            .materialAmount(TagPrefix.M)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(hasFlag(FlagKey.GEM))
            ;
}
