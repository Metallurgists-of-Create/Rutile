package dev.metallurgists.rutile.config.client;

import net.createmod.catnip.config.ConfigBase;

public class RutileClientConfig extends ConfigBase {

    public final ConfigBase.ConfigGroup chemicalCompositions = this.group(1, "chemicalCompositions", "Chemical Compositions");
    public final ConfigBase.ConfigInt tooltipColor = this.i(0xFFFFFF, "tooltipColor", "Color of the tooltip text for chemical compositions");
    public final ConfigBase.ConfigBool elementColorForTooltip = this.b(true, "elementColorForTooltip", "Should each element in the tooltip use the colour of the element.", "Overrides tooltipColor");

    @Override
    public String getName() {
        return "client";
    }
}
