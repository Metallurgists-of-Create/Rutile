package dev.metallurgists.rutile.config;

import net.createmod.catnip.config.ConfigBase;

public class RClient extends ConfigBase {

    public final ConfigBool dumpRecipes = this.b(false,"dump_recipes", "Dump Runtime Generated Recipes to the local files");
    public final ConfigBool dumpCompositions = this.b(false,"dump_compositions", "Dump Runtime Generated Compositions to the local files");
    public final ConfigBool dumpAssets = this.b(false,"dump_assets", "Dump Runtime Generated Assets to the local files");

    public final ConfigBase.ConfigGroup chemicalCompositions = this.group(1, "chemicalCompositions", "Chemical Compositions");
    public final ConfigBase.ConfigInt tooltipColor = this.i(0xFFFFFF, "tooltipColor", "Color of the tooltip text for chemical compositions");

    @Override
    public String getName() {
        return "client";
    }
}
