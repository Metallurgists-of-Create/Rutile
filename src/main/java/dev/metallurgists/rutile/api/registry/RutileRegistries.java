package dev.metallurgists.rutile.api.registry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.element.Element;
import net.minecraft.client.Minecraft;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.IdMappingEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.function.Function;

public class RutileRegistries {

    private static final LinkedHashMap<ResourceLocation, Registry<?>> LOAD_ORDER = new LinkedHashMap<>();
    private static final Map<ResourceLocation, RutileRegistry<?>> RUTILE_REGISTRIES = new HashMap<>();

    public static final ResourceKey<Registry<Element>> ELEMENT_REGISTRY = makeKey(Rutile.id("element"));

    public static final Registry<Element> ELEMENTS = makeRegistry(ELEMENT_REGISTRY);

    public static <T> ResourceKey<Registry<T>> makeKey(ResourceLocation registryId) {
        return ResourceKey.createRegistryKey(registryId);
    }

    public static <T> MappedRegistry<T> makeRegistry(ResourceKey<Registry<T>> key) {
        return makeRegistry(key, true);
    }

    public static <T> MappedRegistry<T> makeRegistry(ResourceKey<Registry<T>> key, boolean sync) {
        MappedRegistry<T> registry = (MappedRegistry<T>) new RegistryBuilder<>(key)
                .sync(sync)
                .create();
        LOAD_ORDER.put(key.location(), registry);
        return registry;
    }

    private static <T> RutileRegistry<T> makeRutileRegistry(ResourceKey<Registry<T>> key, Function<ResourceKey<Registry<T>>, RutileRegistry<T>> registryConstructor) {
        RutileRegistry<T> registry = registryConstructor.apply(key);
        LOAD_ORDER.put(key.location(), registry);
        RUTILE_REGISTRIES.put(key.location(), registry);
        return registry;
    }

    public static List<RutileRegistry<?>> getRutileRegistries() {
        return new ArrayList<>(RUTILE_REGISTRIES.values());
    }

    private static final Table<Registry<?>, ResourceLocation, Object> TO_REGISTER = HashBasedTable.create();
    private static boolean isFrozen = true;

    public static <V, T extends V> T register(Registry<V> registry, ResourceLocation name, T value) {
        if (!isFrozen) {
            Registry.register(registry, name, value);
        } else {
            TO_REGISTER.put(registry, name, value);
        }
        return value;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void actuallyRegister(RegisterEvent event) {
        for (Registry reg : TO_REGISTER.rowKeySet()) {
            event.register(reg.key(), helper -> {
                TO_REGISTER.row(reg).forEach(helper::register);
            });
        }
        TO_REGISTER.clear();
    }

    private static void onUnfreeze(RegisterEvent event) {
        isFrozen = false;
    }

    private static void onFreeze(IdMappingEvent event) {
        isFrozen = event.isFrozen();
    }

    public static void init(IEventBus eventBus) {
        eventBus.addListener(EventPriority.HIGHEST, RutileRegistries::onUnfreeze);
        eventBus.addListener(EventPriority.LOW, RutileRegistries::actuallyRegister);
        NeoForge.EVENT_BUS.addListener(RutileRegistries::onFreeze);
    }

    @UnmodifiableView
    public static List<ResourceLocation> getRegistrationOrder() {
        return List.copyOf(LOAD_ORDER.keySet());
    }

    @UnmodifiableView
    public static Collection<Registry<?>> getRegistries() {
        return LOAD_ORDER.values();
    }

    private static final RegistryAccess BLANK = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
    private static RegistryAccess FROZEN = BLANK;

    @ApiStatus.Internal
    public static void updateFrozenRegistry(RegistryAccess registryAccess) {
        FROZEN = registryAccess;
    }

    public static RegistryAccess builtinRegistry() {
        if (Rutile.isClientThread()) {
            return ClientHelpers.getClientRegistries();
        }
        return FROZEN;
    }

    private static class ClientHelpers {

        private static RegistryAccess getClientRegistries() {
            if (Minecraft.getInstance().getConnection() != null) {
                return Minecraft.getInstance().getConnection().registryAccess();
            } else {
                return FROZEN;
            }
        }
    }
}
