package dev.metallurgists.rutile.api.fluid.storage;

import com.tterrag.registrate.AbstractRegistrate;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.fluid.FluidBuilder;
import dev.metallurgists.rutile.api.material.Material;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Supplier;

public class FluidStorageImpl implements FluidStorage {

    private final Map<FluidStorageKey, FluidEntry> map = new Object2ObjectOpenHashMap<>();
    private Map<FluidStorageKey, FluidBuilder> toRegister = new Object2ObjectOpenHashMap<>();

    private boolean registered = false;

    public FluidStorageImpl() {}

    @Override
    public void enqueueRegistration(@NotNull FluidStorageKey key, @NotNull FluidBuilder builder) {
        if (registered) {
            throw new IllegalStateException("Cannot enqueue a builder after registration");
        }

        if (toRegister.containsKey(key)) {
            throw new IllegalArgumentException("FluidStorageKey " + key + " is already queued");
        }
        toRegister.put(key, builder);
    }

    @Override
    public @Nullable FluidBuilder getQueuedBuilder(@NotNull FluidStorageKey key) {
        if (registered) {
            throw new IllegalArgumentException("FluidStorage has already been registered");
        }
        return toRegister.get(key);
    }

    @ApiStatus.Internal
    public void registerFluids(@NotNull Material material, AbstractRegistrate<?> registrate) {
        if (registered) {
            throw new IllegalStateException("FluidStorage has already been registered");
        }

        if (toRegister.isEmpty() && map.isEmpty()) {
            enqueueRegistration(FluidStorageKeys.LIQUID, new FluidBuilder());
        }

        toRegister.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> -e.getKey().getRegistrationPriority()))
                .forEach(entry -> {
                    if (map.containsKey(entry.getKey())) {
                        Rutile.LOGGER.warn("{} already has an associated fluid for material {}", entry.getKey(),
                                material);
                        return;
                    }
                    Supplier<? extends Fluid> fluid = entry.getValue().build(material, entry.getKey(), registrate);
                    if (!storeNoOverwrites(entry.getKey(), fluid, entry.getValue())) {
                        Rutile.LOGGER.error("{} already has an associated fluid for material {}", material, material);
                    }
                });
        toRegister = null;
        registered = true;
    }

    @Override
    public @Nullable Fluid get(@NotNull FluidStorageKey key) {
        return map.containsKey(key) ? map.get(key).getFluid().get() : null;
    }

    public @Nullable FluidEntry getEntry(@NotNull FluidStorageKey key) {
        return map.get(key);
    }

    private boolean storeNoOverwrites(@NotNull FluidStorageKey key, @NotNull Supplier<? extends Fluid> fluid,
                                      @Nullable FluidBuilder builder) {
        if (map.containsKey(key)) {
            return false;
        }
        store(key, fluid, builder);
        return true;
    }

    @Override
    public void store(@NotNull FluidStorageKey key, @NotNull Supplier<? extends Fluid> fluid,
                      @Nullable FluidBuilder builder) {
        if (map.containsKey(key)) {
            throw new IllegalArgumentException(key + " already has an associated fluid");
        }
        if (builder != null) {
            map.put(key, new FluidEntry(fluid, builder, key));
        } else {
            map.put(key, new FluidEntry(fluid, null, key));
        }
    }
}
