package dev.metallurgists.rutile.datagen;

import dev.metallurgists.rutile.api.tag.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class RutileTags {

    public static final TagKey<Fluid> MOLTEN_FLUIDS = TagUtil.createFluidTag("molten");
    public static final TagKey<Fluid> LIQUID_FLUIDS = TagUtil.createFluidTag("liquid");
    public static final TagKey<Fluid> PLASMA_FLUIDS = TagUtil.createFluidTag("plasmatic");
}
