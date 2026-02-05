package dev.metallurgists.rutile.api.material.flags;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.registry.flags.customisation.*;
import dev.metallurgists.rutile.api.registry.flags.registry.*;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

public class FlagKey<T extends IMaterialFlag> {
    // For Fluids
    public static final FlagKey<FluidFlag> FLUID = new FlagKey<>("fluid", FluidFlag.class);
    public static final FlagKey<ViscosityFlag> VISCOSITY = new FlagKey<>("viscosity", ViscosityFlag.class);
    public static final FlagKey<LuminosityFlag> LUMINOSITY = new FlagKey<>("luminosity", LuminosityFlag.class);

    // For Prefixes
    public static final FlagKey<GemFlag> GEM = new FlagKey<>("gem", GemFlag.class);
    public static final FlagKey<IngotFlag> INGOT = new FlagKey<>("ingot", IngotFlag.class);
    public static final FlagKey<DustFlag> DUST = new FlagKey<>("dust", DustFlag.class);
    public static final FlagKey<OreFlag> ORE = new FlagKey<>("ore", OreFlag.class);

    // For Properties
    public static final FlagKey<HarvestTierFlag> HARVEST_TIER = new FlagKey<>("harvest_tier", HarvestTierFlag.class);
    public static final FlagKey<BurnableFlag> BURNABLE = new FlagKey<>("burnable", BurnableFlag.class);
    public static final FlagKey<StandaloneFlag> WOOD = new FlagKey<>("wood", StandaloneFlag.class);
    public static final FlagKey<StandaloneFlag> DISABLE_RECIPES = new FlagKey<>("disable_recipes", StandaloneFlag.class);

    @Getter
    private final ResourceLocation key;
    @Getter
    private final Class<T> type;

    public FlagKey(String key, Class<T> type) {
        this.key = Rutile.id(key);
        this.type = type;
    }

    protected T constructDefault() {
        try {
            return type.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public T cast(IMaterialFlag flag) {
        return this.type.cast(flag);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FlagKey) {
            return ((FlagKey<?>) o).getKey().equals(key);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return key.toString();
    }
}
