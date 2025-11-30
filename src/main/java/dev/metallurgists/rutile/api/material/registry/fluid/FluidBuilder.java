package dev.metallurgists.rutile.api.material.registry.fluid;

import com.google.common.base.Preconditions;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.metallurgists.rutile.api.material.FlagRegistryTypes;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.FlagSource;
import dev.metallurgists.rutile.api.material.builder.FluidFlagContainer;
import dev.metallurgists.rutile.api.material.builder.MaterialFluidBuilder;
import dev.metallurgists.rutile.api.registrate.RutileClientFluidTypeExtensions;
import dev.metallurgists.rutile.util.ClientUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Accessors(fluent = true, chain = true)
public class FluidBuilder {

    private static final int INFER_TEMPERATURE = -1;
    private static final int INFER_COLOR = 0xFFFFFFFF;
    private static final int INFER_DENSITY = -1;
    private static final int INFER_LUMINOSITY = -1;
    private static final int INFER_VISCOSITY = -1;

    @Setter
    @Nullable
    private String name = null;
    @Setter
    @Nullable
    private String translation = null;

    private int temperature = INFER_TEMPERATURE;
    private int color = INFER_COLOR;
    private boolean isColorEnabled = true;
    @Setter
    private int density = INFER_DENSITY;
    private int luminosity = INFER_LUMINOSITY;
    private int viscosity = INFER_VISCOSITY;

    @Getter
    @Setter(onMethod_ = @ApiStatus.Internal)
    @Nullable
    private ResourceLocation still = null;
    @Getter
    @Setter(onMethod_ = @ApiStatus.Internal)
    @Nullable
    private ResourceLocation flowing = null;
    @Getter
    private boolean hasCustomStill = false;
    @Getter
    private boolean hasCustomFlowing = false;

    private boolean hasFluidBlock = false;
    private boolean hasBucket = true;

    public FluidBuilder() {}

    public @NotNull FluidBuilder temperature(int temperature) {
        Preconditions.checkArgument(temperature > 0, "temperature must be > 0");
        this.temperature = temperature;
        return this;
    }

    public @NotNull FluidBuilder color(int color) {
        this.color = ClientUtil.convertRGBtoARGB(color);
        if (this.color == INFER_COLOR) {
            return disableColor();
        }
        return this;
    }

    public @NotNull FluidBuilder disableColor() {
        this.isColorEnabled = false;
        return this;
    }

    /**
     * @param density the density in g/cm^3
     * @return this
     */
    @Tolerate
    public @NotNull FluidBuilder density(double density) {
        return density(convertToMCDensity(density));
    }

    private static int convertToMCDensity(double density) {
        // conversion formula from GT6
        if (density > 0.001225) {
            return (int) (1000 * density);
        } else if (density < 0.001225) {
            return (int) (-0.1 / density);
        }
        return 0;
    }

    public @NotNull FluidBuilder luminosity(int luminosity) {
        Preconditions.checkArgument(luminosity >= 0 && luminosity < 16, "luminosity must be >= 0 and < 16");
        this.luminosity = luminosity;
        return this;
    }

    public @NotNull FluidBuilder viscosity(int mcViscosity) {
        Preconditions.checkArgument(mcViscosity >= 0, "viscosity must be >= 0");
        this.viscosity = mcViscosity;
        return this;
    }

    /**
     * @param viscosity the viscosity of the fluid in Poise
     * @return this
     */
    public @NotNull FluidBuilder viscosity(double viscosity) {
        return viscosity(convertViscosity(viscosity));
    }

    private static int convertViscosity(double viscosity) {
        return (int) (viscosity * 10000);
    }

    public @NotNull FluidBuilder customStill() {
        return textures(true);
    }

    public @NotNull FluidBuilder textures(boolean hasCustomStill) {
        this.hasCustomStill = hasCustomStill;
        this.isColorEnabled = false;
        return this;
    }

    public @NotNull FluidBuilder textures(boolean hasCustomStill, boolean hasCustomFlowing) {
        this.hasCustomStill = hasCustomStill;
        this.hasCustomFlowing = hasCustomFlowing;
        this.isColorEnabled = false;
        return this;
    }

    public @NotNull FluidBuilder block() {
        this.hasFluidBlock = true;
        return this;
    }

    public @NotNull FluidBuilder disableBucket() {
        this.hasBucket = false;
        return this;
    }

    @SuppressWarnings("UnstableApiUsage")
    public @NotNull FluidEntry<? extends IMaterialFluid> build(Material material, @NotNull FlagSource<Fluid> flagSource,
                                                               AbstractRegistrate<?> owner) {
        determineName(material, flagSource);
        determineTextures(material, flagSource);

        if (name == null) {
            throw new IllegalStateException("Could not determine fluid name");
        }

        determineTemperature(material, flagSource);
        determineColor(material, flagSource);
        determineDensity(material, flagSource);
        determineLuminosity(material, flagSource);
        determineViscosity(material, flagSource);

        FluidFlagContainer flagContainer = (FluidFlagContainer) material.getFlagContainer(FlagRegistryTypes.FLUID);
        MaterialFluidBuilder mfBuilder = (MaterialFluidBuilder) flagContainer.getBuilders().get(flagSource);
        if (mfBuilder == null) throw new NullPointerException("Fluid has no builder");

        final String langKey = this.translation != null ? this.translation : flagSource.getUnlocalizedName(material, mfBuilder.nameFormat());
        // noinspection DataFlowIssue
        var builder = owner.fluid(this.name, this.still, this.flowing,
                        (p, $1, $2) -> makeFluidType(owner, p, material, flagSource, langKey),
                        (p) -> new MaterialFluid.Flowing(p, material, mfBuilder))
                .source((p) -> new MaterialFluid.Source(p, material, mfBuilder))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop());
        if (this.hasFluidBlock) {
            builder.block()
                    .blockstate((ctx, prov) -> prov
                            .simpleBlock(ctx.getEntry(), prov.models().getBuilder(this.name)
                                    .texture("particle", this.still)))
                    .color(() -> () -> (state, level, pos, index) ->
                            IClientFluidTypeExtensions.of(state.getFluidState())
                            .getTintColor(state.getFluidState(), level, pos))
                    .register();
        } else {
            // noinspection DataFlowIssue
            builder.noBlock().fluidProperties(p -> p.block(null));
        }
        if (this.hasBucket) {
            builder.bucket((fluid, properties) -> new MaterialBucketItem(fluid, properties, material, mfBuilder))
                    .properties(p -> p.craftRemainder(Items.BUCKET).stacksTo(1))
                    .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                    .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                    .color(() -> () -> MaterialBucketItem::color)
                    .register();
        } else {
            // noinspection DataFlowIssue
            builder.noBucket().fluidProperties(p -> p.bucket(null));
        }
        return builder.register();
    }

    private void determineName(@NotNull Material material, @Nullable FlagSource<Fluid> flagSource) {
        if (name != null) return;
        if (flagSource == null) throw new IllegalArgumentException("Fluid must have a name");
        FluidFlagContainer flagContainer = (FluidFlagContainer) material.getFlagContainer(FlagRegistryTypes.FLUID);
        MaterialFluidBuilder builder = (MaterialFluidBuilder) flagContainer.getBuilders().get(flagSource);
        if (builder == null) throw new NullPointerException("Fluid has no builder");
        name = builder.nameFormat().formatted(material.getName());
    }

    @ApiStatus.Internal
    public void determineTextures(@NotNull Material material, @NotNull FlagSource<Fluid> flagSource) {
        FluidFlagContainer flagContainer = (FluidFlagContainer) material.getFlagContainer(FlagRegistryTypes.FLUID);
        MaterialFluidBuilder builder = (MaterialFluidBuilder) flagContainer.getBuilders().get(flagSource);
        if (builder == null) throw new NullPointerException("Fluid has no builder");
        if (hasCustomStill) {
            still = ResourceLocation.fromNamespaceAndPath(material.getNamespace(), "block/fluids/" + name);
        } else {
            String path = material.getFlags().getPropertiesFor(flagSource).getTextureFormat().formatted(material.getName(), flagSource.rlForm());
            still = ResourceLocation.fromNamespaceAndPath(material.getNamespace(), "block/fluids/" + path);
        }

        if (hasCustomFlowing) {
            flowing = ResourceLocation.fromNamespaceAndPath(material.getNamespace(), "block/fluids/" + name + "_flow");
        } else {
            String path = material.getFlags().getPropertiesFor(flagSource).getTextureFormat().formatted(material.getName(), flagSource.rlForm() + "_flow");
            flowing = ResourceLocation.fromNamespaceAndPath(material.getNamespace(), "block/fluids/" + path);
        }
    }

    private void determineTemperature(@NotNull Material material, FlagSource<Fluid> flagSource) {
        if (temperature != INFER_TEMPERATURE) return;
        double temp = material.getFlags().getPropertiesFor(flagSource).getTemperature();
        temperature = Math.toIntExact(Math.round(temp));
    }

    private void determineColor(@NotNull Material material, FlagSource<Fluid> flagSource) {
        if (color != INFER_COLOR) return;
        int col = material.getFlags().getPropertiesFor(flagSource).getColor();
        if (col == INFER_COLOR) color = material.materialInfo().colour();
        if (isColorEnabled) {
            color = ClientUtil.convertRGBtoARGB(col);
        }
    }

    private void determineDensity(@NotNull Material material, FlagSource<Fluid> flagSource) {
        if (density != INFER_DENSITY) return;
        density = FluidBuilder.convertToMCDensity(material.getFlags().getPropertiesFor(flagSource).getDensity());
    }

    private void determineLuminosity(@NotNull Material material, FlagSource<Fluid> flagSource) {
        if (luminosity != INFER_LUMINOSITY) return;
        int light = material.getFlags().getPropertiesFor(flagSource).getLuminosity();
        luminosity = Math.min(Math.max(light, 0), 15);
    }

    private void determineViscosity(@NotNull Material material, FlagSource<Fluid> flagSource) {
        if (viscosity != INFER_VISCOSITY) return;
        viscosity = FluidBuilder.convertViscosity(material.getFlags().getPropertiesFor(flagSource).getViscosity());
    }

    private FluidType makeFluidType(AbstractRegistrate<?> owner, FluidType.Properties properties,
                                    Material material, FlagSource<Fluid> flagSource, String langKey) {
        properties.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .temperature(this.temperature)
                .density(this.density)
                .lightLevel(this.luminosity)
                .viscosity(this.viscosity);
        FluidType type = new FluidType(properties) {
            FluidFlagContainer flagContainer = (FluidFlagContainer) material.getFlagContainer(FlagRegistryTypes.FLUID);
            MaterialFluidBuilder builder = (MaterialFluidBuilder) flagContainer.getBuilders().get(flagSource);
            @Override
            public String getDescriptionId() {
                if (builder == null) throw new NullPointerException("Fluid has no builder");
                return flagSource.getUnlocalizedName(material, builder.nameFormat());
            }

            @Override
            public Component getDescription() {
                if (builder == null) throw new NullPointerException("Fluid has no builder");
                return Component.translatable(langKey, flagSource.getLocalizedName(material, builder.nameFormat()));
            }

            @Override
            public Component getDescription(FluidStack stack) {
                return this.getDescription();
            }
        };
        OneTimeEventReceiver.addModListener(owner, RegisterClientExtensionsEvent.class, event -> {
            final int color = isColorEnabled ? this.color : INFER_COLOR;
            if (still == null || flowing == null) {
                this.determineTextures(material, flagSource);
            }
            event.registerFluidType(new RutileClientFluidTypeExtensions(still, flowing, color), type);
        });
        return type;
    }
}
