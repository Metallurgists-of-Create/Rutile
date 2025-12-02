package dev.metallurgists.rutile.api.registry.flags;

import com.tterrag.registrate.AbstractRegistrate;
import dev.metallurgists.rutile.api.fluid.FluidBuilder;
import dev.metallurgists.rutile.api.fluid.storage.FluidStorage;
import dev.metallurgists.rutile.api.fluid.storage.FluidStorageImpl;
import dev.metallurgists.rutile.api.fluid.storage.FluidStorageKey;
import dev.metallurgists.rutile.api.fluid.storage.FluidStorageKeys;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.flags.IMaterialFlag;
import dev.metallurgists.rutile.api.material.flags.MaterialFlags;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mezz.jei.neoforge.platform.FluidHelper;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@NoArgsConstructor
public class FluidFlag implements IMaterialFlag, FluidStorage {

    private final FluidStorageImpl storage = new FluidStorageImpl();
    @Getter
    @Setter
    private FluidStorageKey primaryKey = null;
    @Setter
    private @Nullable Fluid solidifyingFluid = null;

    public FluidFlag(@NotNull FluidStorageKey key, @NotNull FluidBuilder builder) {
        enqueueRegistration(key, builder);
    }

    public @NotNull FluidStorage getStorage() {
        return this;
    }

    @ApiStatus.Internal
    public void registerFluids(@NotNull Material material, @NotNull AbstractRegistrate<?> registrate) {
        this.storage.registerFluids(material, registrate);
    }

    @Override
    public void enqueueRegistration(@NotNull FluidStorageKey key, @NotNull FluidBuilder builder) {
        storage.enqueueRegistration(key, builder);
        if (primaryKey == null) {
            primaryKey = key;
        }
    }

    @Override
    public void store(@NotNull FluidStorageKey key, @NotNull Supplier<? extends Fluid> fluid,
                      @Nullable FluidBuilder builder) {
        storage.store(key, fluid, builder);
        if (primaryKey == null) {
            primaryKey = key;
        }
    }

    @Override
    public @Nullable Fluid get(@NotNull FluidStorageKey key) {
        return storage.get(key);
    }

    @Override
    public @Nullable FluidEntry getEntry(@NotNull FluidStorageKey key) {
        return storage.getEntry(key);
    }

    @Override
    public @Nullable FluidBuilder getQueuedBuilder(@NotNull FluidStorageKey key) {
        return storage.getQueuedBuilder(key);
    }

    public @Nullable Fluid solidifiesFrom() {
        if (this.solidifyingFluid == null) {
            this.solidifyingFluid = getStorage().get(FluidStorageKeys.LIQUID);
        }
        return solidifyingFluid;
    }

    public @NotNull FluidStack solidifiesFrom(int amount) {
        Fluid fluid = solidifiesFrom();
        if (fluid == null) {
            return FluidStack.EMPTY;
        }
        return new FluidStack(fluid, amount);
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        if (this.primaryKey == null) {
            throw new IllegalStateException("FluidFlag cannot be empty!");
        }
    }
}
