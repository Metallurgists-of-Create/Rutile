package dev.metallurgists.rutile.api.fluid;

import com.google.common.base.Preconditions;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.fluid.attribute.FluidAttribute;
import dev.metallurgists.rutile.api.fluid.storage.FluidStorageKey;
import dev.metallurgists.rutile.api.fluid.storage.FluidStorageKeys;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static dev.metallurgists.rutile.api.fluid.FluidConstants.*;

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

    private final Collection<FluidAttribute> attributes = new ArrayList<>();

    @Setter
    private FluidState state = FluidState.LIQUID;
    private int temperature = INFER_TEMPERATURE;
    private int color = INFER_COLOR;
    private boolean isColorEnabled = true;
    @Setter
    private int density = INFER_DENSITY;
    private int luminosity = INFER_LUMINOSITY;
    private int viscosity = INFER_VISCOSITY;
    @Setter
    private int burnTime = 0;

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
        this.temperature = temperature;
        return this;
    }

    public @NotNull FluidBuilder color(int color) {
        this.color = RutileClient.convertRGBtoARGB(color);
        if (this.color == INFER_COLOR) {
            return disableColor();
        }
        return this;
    }

    public @NotNull FluidBuilder disableColor() {
        this.isColorEnabled = false;
        return this;
    }

    @Tolerate
    public @NotNull FluidBuilder density(double density) {
        return density(convertToMCDensity(density));
    }

    private static int convertToMCDensity(double density) {
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

    public @NotNull FluidBuilder viscosity(double viscosity) {
        return viscosity(convertViscosity(viscosity));
    }

    private static int convertViscosity(double viscosity) {
        return (int) (viscosity * 10000);
    }

    public @NotNull FluidBuilder attribute(@NotNull FluidAttribute attribute) {
        this.attributes.add(attribute);
        return this;
    }

    public @NotNull FluidBuilder attributes(@NotNull FluidAttribute @NotNull... attributes) {
        Collections.addAll(this.attributes, attributes);
        return this;
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
    public @NotNull Supplier<? extends Fluid> build(Material material, @NotNull FluidStorageKey key,
                                                    AbstractRegistrate<?> registrate) {
        determineName(material, key);
        determineTextures(material, key);

        if (name == null) {
            throw new IllegalStateException("Could not determine fluid name");
        }

        if (state == null) {
            if (key.getDefaultFluidState() != null) {
                state = key.getDefaultFluidState();
            } else {
                state = FluidState.LIQUID; // default fallback
            }
        }

        determineTemperature(material);
        determineColor(material);
        determineDensity();
        determineLuminosity(material, key);
        determineViscosity(material, key);

        final String langKey = this.translation != null ? this.translation : key.getTranslationKeyFor(material);

        var builder = registrate.fluid(this.name, this.still, this.flowing,
                        (p, $1, $2) -> makeFluidType(registrate, p, material, key, langKey),
                        (p) -> new MaterialFluid.Flowing(this.state, this.burnTime, p))
                .source((p) -> new MaterialFluid.Source(this.state, this.burnTime, p))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop());
        if (this.hasFluidBlock) {
            builder.block()
                    .blockstate((ctx, prov) -> prov
                            .simpleBlock(ctx.getEntry(), prov.models().getBuilder(this.name)
                                    .texture("particle", this.still)))
                    .color(() -> () -> (state, level, pos, index) -> {
                        return IClientFluidTypeExtensions.of(state.getFluidState())
                                .getTintColor(state.getFluidState(), level, pos);
                    })
                    .register();
        } else {
            // noinspection DataFlowIssue
            builder.noBlock().fluidProperties(p -> p.block(null));
        }
        if (this.hasBucket) {
            builder.bucket((fluid, properties) -> new MaterialBucketItem(fluid, properties, material, langKey))
                    .properties(p -> p.craftRemainder(Items.BUCKET).stacksTo(1))
                    .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                    .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                    .color(() -> () -> MaterialBucketItem::color)
                    .register();
        } else {
            // noinspection DataFlowIssue
            builder.noBucket().fluidProperties(p -> p.bucket(null));
        }
        builder.onRegister(fluid -> {
            if (fluid.getSource() instanceof MaterialFluid matSource) attributes.forEach(matSource::addAttribute);
            if (fluid.getFlowing() instanceof MaterialFluid matFlowing) attributes.forEach(matFlowing::addAttribute);
        });
        return builder.register()::getSource;
    }

    private void determineName(@NotNull Material material, @Nullable FluidStorageKey key) {
        if (name != null) return;
        if (key == null) throw new IllegalArgumentException("Fluid must have a name");
        name = key.getRegistryNameFor(material);
    }

    private final List<String> icaNamespaces = List.of("rutile", "metallurgica");

    @ApiStatus.Internal
    public void determineTextures(@NotNull Material material, @NotNull FluidStorageKey key) {
        var keyLoc = key.getResourceLocation();
        String keyPath = (icaNamespaces.contains(keyLoc.getNamespace()) ? "" : (keyLoc.getNamespace() + "_")) + keyLoc.getPath();
        if (hasCustomStill) {
            still = ResourceLocation.fromNamespaceAndPath(material.getModId(), "fluid/" + name);
        } else {
            still = ResourceLocation.fromNamespaceAndPath(keyLoc.getNamespace(), "fluid/" + keyPath);
        }
        if (hasCustomFlowing) {
            flowing = ResourceLocation.fromNamespaceAndPath(material.getModId(), "fluid/" + name + "_flow");
        } else {
            flowing = ResourceLocation.fromNamespaceAndPath(keyLoc.getNamespace(), "fluid/" + keyPath + "_flow");
        }
    }

    private void determineTemperature(@NotNull Material material) {
        if (temperature != INFER_TEMPERATURE) return;
        temperature = switch (state) {
            case LIQUID, GAS -> ROOM_TEMPERATURE;
            case PLASMA -> {
                if (material.hasFluid() && material.getFluidBuilder() != null &&
                        material.getFluidBuilder() != material.getFluidBuilder(FluidStorageKeys.PLASMA)) {
                    yield BASE_PLASMA_TEMPERATURE + material.getFluidBuilder().temperature;
                }
                yield BASE_PLASMA_TEMPERATURE;
            }
        };
    }

    private void determineColor(@NotNull Material material) {
        if (color != INFER_COLOR) return;
        if (isColorEnabled) {
            color = RutileClient.convertRGBtoARGB(material.getInfo().getColour());
        }
    }

    private void determineDensity() {
        if (density != INFER_DENSITY) return;
        density = switch (state) {
            case LIQUID -> DEFAULT_LIQUID_DENSITY;
            case GAS -> DEFAULT_GAS_DENSITY;
            case PLASMA -> DEFAULT_PLASMA_DENSITY;
        };
    }

    private void determineLuminosity(@NotNull Material material, @NotNull FluidStorageKey key) {
        if (luminosity != INFER_LUMINOSITY) return;
        if (state == FluidState.PLASMA) {
            luminosity = 15;
        } else if (state == FluidState.LIQUID && material.hasFlag(FlagKey.LUMINOSITY)) {
            int flaggedViscosity = material.getFlag(FlagKey.LUMINOSITY).getLuminosity(key);
            if (flaggedViscosity != INFER_VISCOSITY) {
                luminosity = flaggedViscosity;
            }
        } else {
            luminosity = 0;
        }
    }

    private void determineViscosity(@NotNull Material material, @NotNull FluidStorageKey key) {
        if (viscosity != INFER_VISCOSITY) return;
        viscosity = switch (state) {
            case LIQUID -> {
                if (material.hasFlag(FlagKey.VISCOSITY)) {
                    int flaggedViscosity = material.getFlag(FlagKey.VISCOSITY).getViscosity(key);
                    if (flaggedViscosity != INFER_VISCOSITY) {
                        yield flaggedViscosity;
                    }
                }
                yield DEFAULT_LIQUID_VISCOSITY;
            }
            case GAS -> DEFAULT_GAS_VISCOSITY;
            case PLASMA -> DEFAULT_PLASMA_VISCOSITY;
        };
    }

    private FluidType makeFluidType(AbstractRegistrate<?> owner, FluidType.Properties properties,
                                    Material material, FluidStorageKey key, String langKey) {
        properties.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .temperature(this.temperature)
                .density(this.density)
                .lightLevel(this.luminosity)
                .viscosity(this.viscosity);
        FluidType type = new FluidType(properties) {

            @Override
            public String getDescriptionId() {
                return material.getDescriptionId();
            }

            @Override
            public Component getDescription() {
                return Component.translatable(langKey, material.getDisplayName());
            }

            @Override
            public Component getDescription(FluidStack stack) {
                return this.getDescription();
            }
        };
        OneTimeEventReceiver.addModListener(owner, RegisterClientExtensionsEvent.class, event -> {
            final int color = isColorEnabled ? this.color : INFER_COLOR;
            if (still == null || flowing == null) {
                this.determineTextures(material, key);
            }
            event.registerFluidType(new RutileClientFluidTypeExtensions(still, flowing, color), type);
        });
        return type;
    }
}
