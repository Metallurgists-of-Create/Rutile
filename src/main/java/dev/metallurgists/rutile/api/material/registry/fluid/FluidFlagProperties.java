package dev.metallurgists.rutile.api.material.registry.fluid;

import com.google.common.base.Preconditions;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.metallurgists.rutile.api.material.builder.FlagSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.level.material.Fluid;

@RequiredArgsConstructor
public class FluidFlagProperties {
    @Getter
    final FlagSource<Fluid> flagSource;

    /**
     * Example: "%s/%s"<p>%s(1) -> Material path</p>%s(2) -> Flag Key name (_flow appended to end)
     */
    @Getter
    String textureFormat = "%s/%s";
    private FluidFlagProperties setTextureFormat(String texture) {
        this.textureFormat = texture;
        return this;
    }
    /**
     * density in g/cm^3
     */
    @Getter
    double density = 1;
    private FluidFlagProperties setDensity(double density) {
        this.density = density;
        return this;
    }
    /**
     * viscosity of the fluid in Poise
     */
    @Getter
    double viscosity = 0.01;
    private FluidFlagProperties setViscosity(double viscosity) {
        this.viscosity = viscosity;
        return this;
    }
    /**
     * luminosity in Light Levels<p>min -> 0</p>max -> 15
     */
    @Getter
    int luminosity = 0;
    private FluidFlagProperties setLuminosity(int luminosity) {
        this.luminosity = luminosity;
        return this;
    }
    /**
     * colour in RGB.<p>If left as-is, will use Material colour</p>
     */
    @Getter
    int color = 0xFFFFFF;
    private FluidFlagProperties setColor(int color) {
        this.color = color;
        return this;
    }

    @Getter
    double temperature = 23 * 12.7391304348;
    private FluidFlagProperties setTemperature(double temperature) {
        this.temperature = temperature;
        return this;
    }
    /**
     * Ticks the bucket will burn for when used as furnace fuel.
     */
    @Getter
    int burnTime = 0;
    private FluidFlagProperties setBurnTime(int burnTime) {
        this.burnTime = burnTime;
        return this;
    }

    public static Builder b(FlagSource<Fluid> flagSource) {
        return new Builder(flagSource);
    }

    @RequiredArgsConstructor
    public static class Builder {
        @Getter
        final FlagSource<Fluid> flagSource;

        NonNullFunction<FlagSource<Fluid>, FluidFlagProperties> propsCallback = FluidFlagProperties::new;

        public Builder texture(String texture) {
            propsCallback.andThen(p -> p.setTextureFormat(texture));
            return this;
        }
        public Builder density(double density) {
            propsCallback.andThen(p -> p.setDensity(density));
            return this;
        }
        public Builder viscosity(double viscosity) {
            propsCallback.andThen(p -> p.setViscosity(viscosity));
            return this;
        }
        public Builder luminosity(int luminosity) {
            Preconditions.checkArgument(luminosity >= 0 && luminosity < 16, "luminosity must be >= 0 And < 16");
            propsCallback.andThen(p -> p.setLuminosity(luminosity));
            return this;
        }
        public Builder color(int color) {
            propsCallback.andThen(p -> p.setColor(color));
            return this;
        }
        public Builder temperature(double temperature) {
            propsCallback.andThen(p -> p.setTemperature(temperature));
            return this;
        }
        public Builder burnTime(int burnTime) {
            propsCallback.andThen(p -> p.setBurnTime(burnTime));
            return this;
        }

        public FluidFlagProperties build() {
            return propsCallback.apply(flagSource);
        }
    }


}
