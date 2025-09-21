package dev.metallurgists.rutile.api.registry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.data.ItemComposition;
import dev.metallurgists.rutile.api.composition.data.MaterialComposition;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.registry.material.MaterialRegistry;
import dev.metallurgists.rutile.registry.RutileRegistries;
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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public class CustomRutileRegistries {
    private static final LinkedHashMap<ResourceLocation, Registry<?>> LOAD_ORDER = new LinkedHashMap<>();

    public static final ResourceKey<Registry<Element>> ELEMENT_REGISTRY = makeRegistryKey(Rutile.id("element"));
    public static final ResourceKey<Registry<ItemComposition>> ITEM_COMPOSITION_REGISTRY = makeRegistryKey(Rutile.id("composition/item"));
    public static final ResourceKey<Registry<MaterialComposition>> MATERIAL_COMPOSITION_REGISTRY = makeRegistryKey(Rutile.id("composition/material"));
    public static final ResourceKey<Registry<Material>> MATERIAL_REGISTRY = makeRegistryKey(Rutile.id("material"));


    public static final Registry<Element> ELEMENTS = makeRegistry(ELEMENT_REGISTRY);
    public static final MaterialRegistry MATERIALS = makeMaterialRegistry();

    public static <T> ResourceKey<Registry<T>> makeRegistryKey(ResourceLocation registryId) {
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

    private static MaterialRegistry makeMaterialRegistry() {
        MaterialRegistry registry = new MaterialRegistry(MATERIAL_REGISTRY);
        LOAD_ORDER.put(MATERIAL_REGISTRY.location(), registry);
        return registry;
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

    // ignore the generics and hope the registered objects are still correctly typed :3
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
        eventBus.addListener(EventPriority.HIGHEST, CustomRutileRegistries::onUnfreeze);
        eventBus.addListener(EventPriority.LOW, CustomRutileRegistries::actuallyRegister);
        NeoForge.EVENT_BUS.addListener(CustomRutileRegistries::onFreeze);
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

    /**
     * You shouldn't call it, you should probably not even look at it just to be extra safe
     *
     * @param registryAccess the new value to set to the frozen registry access
     */
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
