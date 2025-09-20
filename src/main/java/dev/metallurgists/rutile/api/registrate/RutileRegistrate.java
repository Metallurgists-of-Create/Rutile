package dev.metallurgists.rutile.api.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;
import dev.metallurgists.rutile.api.registrate.builder.ElementBuilder;
import dev.metallurgists.rutile.api.registrate.builder.FlagKeyBuilder;
import dev.metallurgists.rutile.api.registrate.builder.MaterialBuilder;
import dev.metallurgists.rutile.mixin.registrate.AbstractRegistrateAccessor;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class RutileRegistrate extends AbstractRegistrate<RutileRegistrate> {

    private static final Map<String, RutileRegistrate> EXISTING_REGISTRATES = new Object2ObjectOpenHashMap<>();

    private final AtomicBoolean registered = new AtomicBoolean(false);

    /**
     * Construct a new Registrate for the given mod ID.
     *
     * @param modid The mod ID for which objects will be registered
     */
    protected RutileRegistrate(String modid) {
        super(modid);
    }

    public static RutileRegistrate create(String modid) {
        return new RutileRegistrate(modid);
    }

    @ApiStatus.Internal
    public static RutileRegistrate createIgnoringListenerErrors(String modId) {
        return innerCreate(modId, false);
    }

    private static RutileRegistrate innerCreate(String modId, boolean strict) {
        if (EXISTING_REGISTRATES.containsKey(modId)) {
            return EXISTING_REGISTRATES.get(modId);
        }
        var registrate = new RutileRegistrate(modId);
        Optional<IEventBus> modEventBus = ModList.get().getModContainerById(modId).map(ModContainer::getEventBus);
        if (strict) {
            modEventBus.ifPresentOrElse(registrate::registerEventListeners, () -> {
                String message = "# [RutileRegistrate] Failed to register eventListeners for mod " + modId +
                        ", This should be reported to this mod's dev #";
                String hashtags = "#".repeat(message.length());
                Rutile.LOGGER.error(hashtags);
                Rutile.LOGGER.error(message);
                Rutile.LOGGER.error(hashtags);
            });
        } else {
            registrate.registerEventListeners(modEventBus.orElse(Rutile.getEventBus()));
        }
        EXISTING_REGISTRATES.put(modId, registrate);
        return registrate;
    }

    @Override
    public RutileRegistrate registerEventListeners(IEventBus bus) {
        if (!registered.getAndSet(true)) {
            if (((AbstractRegistrateAccessor) this).getModEventBus() == null) {
                ((AbstractRegistrateAccessor) this).setModEventBus(bus);
            }
            // recreate the super method so we can register the event listener with LOW priority.
            Consumer<RegisterEvent> onRegister = this::onRegister;
            Consumer<RegisterEvent> onRegisterLate = this::onRegisterLate;
            bus.addListener(EventPriority.LOW, onRegister);
            bus.addListener(EventPriority.LOWEST, onRegisterLate);

            // Fired multiple times when ever tabs need contents rebuilt (changing op tab perms for example)
            bus.addListener(this::onBuildCreativeModeTabContents);
            // Register events fire multiple times, so clean them up on common setup
            OneTimeEventReceiver.addModListener(this, FMLCommonSetupEvent.class, $ -> {
                OneTimeEventReceiver.unregister(this, onRegister, RegisterEvent.class);
                OneTimeEventReceiver.unregister(this, onRegisterLate, RegisterEvent.class);
            });
            if (((AbstractRegistrateAccessor) this).getDoDatagen().get()) {
                OneTimeEventReceiver.addModListener(this, GatherDataEvent.class, this::onData);
            }
        }
        return this;
    }

    // Elements
    public <T extends Element> ElementBuilder<T, RutileRegistrate> element(NonNullFunction<Element.Properties, T> factory) {
        return element(self(), factory);
    }

    public <T extends Element> ElementBuilder<T, RutileRegistrate> element(String name, NonNullFunction<Element.Properties, T> factory) {
        return element(self(), name, factory);
    }

    public <T extends Element, P> ElementBuilder<T, P> element(P parent, NonNullFunction<Element.Properties, T> factory) {
        return element(parent, currentName(), factory);
    }

    public <T extends Element, P> ElementBuilder<T, P> element(P parent, String name, NonNullFunction<Element.Properties, T> factory) {
        return entry(name, callback -> ElementBuilder.create(this, parent, name, callback, factory));
    }

    // Flag Keys
    public <C extends IMaterialFlag, T extends FlagKey<? extends IMaterialFlag>> FlagKeyBuilder<C, T, RutileRegistrate> flagKey(NonNullFunction<Class<C>, T> factory, Class<C> type) {
        return flagKey(self(), factory, type);
    }

    public <C extends IMaterialFlag, T extends FlagKey<? extends IMaterialFlag>> FlagKeyBuilder<C, T, RutileRegistrate> flagKey(String name, NonNullFunction<Class<C>, T> factory, Class<C> type) {
        return flagKey(self(), name, factory, type);
    }

    public <C extends IMaterialFlag, T extends FlagKey<? extends IMaterialFlag>, P> FlagKeyBuilder<C, T, P> flagKey(P parent, NonNullFunction<Class<C>, T> factory, Class<C> type) {
        return flagKey(parent, currentName(), factory, type);
    }

    public <C extends IMaterialFlag, T extends FlagKey<? extends IMaterialFlag>, P> FlagKeyBuilder<C, T, P> flagKey(P parent, String name, NonNullFunction<Class<C>, T> factory, Class<C> type) {
        return entry(name, callback -> FlagKeyBuilder.create(this, parent, name, callback, factory, type));
    }

    // Materials
    public <T extends Material> MaterialBuilder<T, RutileRegistrate> material(NonNullBiFunction<Material.MaterialInfo, MaterialFlags, T> factory) {
        return material(self(), factory);
    }

    public <T extends Material> MaterialBuilder<T, RutileRegistrate> material(String name, NonNullBiFunction<Material.MaterialInfo, MaterialFlags, T> factory) {
        return material(self(), name, factory);
    }

    public <T extends Material, P> MaterialBuilder<T, P> material(P parent, NonNullBiFunction<Material.MaterialInfo, MaterialFlags, T> factory) {
        return material(parent, currentName(), factory);
    }

    public <T extends Material, P> MaterialBuilder<T, P> material(P parent, String name, NonNullBiFunction<Material.MaterialInfo, MaterialFlags, T> factory) {
        return entry(name, callback -> MaterialBuilder.create(this, parent, name, callback, factory));
    }
}
