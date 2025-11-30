package dev.metallurgists.rutile.api.material.builder.fluid;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.FlagSources;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.FlagSource;
import dev.metallurgists.rutile.api.material.builder.MaterialFluidBuilder;
import dev.metallurgists.rutile.api.material.builder.MaterialRegistryBuilder;
import dev.metallurgists.rutile.api.material.flag.types.MultiTagHolder;
import dev.metallurgists.rutile.api.material.registry.fluid.FluidBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

public class MoltenFluidBuilder extends MaterialFluidBuilder {

    public MoltenFluidBuilder() {
        super("molten_%s");
    }

    protected MoltenFluidBuilder(String nameFormat) {
        super(nameFormat);
    }

    @Override
    public FlagSource<Fluid> getFlagSource() {
        return FlagSources.MOLTEN;
    }

    @Override
    public void registerAssets(Material material) {}

    @Override
    public MultiTagHolder getTagHolder() {
        var holder = createTagHolder();
        holder.addPatterns("c:molten", "c:molten/%s");
        return new MultiTagHolder(holder);
    }

    @Override
    public ResourceLocation getBuilderName() {
        return Rutile.id("molten");
    }

    @Override
    public MaterialRegistryBuilder<Fluid> getBuilder() {
        return this;
    }

    @Override
    public RegistryEntry<Fluid, ? extends Fluid> build(@NotNull AbstractRegistrate<?> registrate, @NotNull Material material) {
        return new FluidBuilder().build(material, getFlagSource(), registrate);
    }
}
