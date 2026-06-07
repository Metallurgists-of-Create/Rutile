package dev.metallurgists.rutile.util;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.element.DeferredElements;
import dev.metallurgists.rutile.api.element.Element;
import dev.metallurgists.rutile.api.material.module.DeferredModules;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;
import java.util.function.Function;

public class RegistryHelper {
    private static final Consumer<?> NO_ACTION = (a) -> {};

    public static ResourceLocation location(String path) {
        return Rutile.id(path);
    }

    public static <DR extends DeferredRegister<T>, T> DR createRegister(Function<String, DR> factory) {
        return registerToBus(factory.apply(Rutile.ID));
    }

    public static <T> DeferredRegister<T> createRegister(ResourceKey<Registry<T>> registry) {
        return registerToBus(DeferredRegister.create(registry, Rutile.ID));
    }

    public static DeferredElements createElements(String modid) {
        return registerToBus(DeferredElements.create(modid));
    }

    public static DeferredModules createModules(String modid) {
        return registerToBus(DeferredModules.create(modid));
    }

    private static <DR extends DeferredRegister<T>, T> DR registerToBus(DR deferredRegister) {
        deferredRegister.register(Rutile.getEventBus());
        return deferredRegister;
    }

    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> noAction() {
        return ((Consumer<T>) NO_ACTION);
    }
}
