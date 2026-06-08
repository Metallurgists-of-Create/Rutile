package dev.metallurgists.rutile;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.metallurgists.rutile.api.material.MaterialData;
import dev.metallurgists.rutile.api.material.part.Part;
import dev.metallurgists.rutile.api.material.part.type.BlockPart;
import dev.metallurgists.rutile.api.material.part.type.ItemPart;
import dev.metallurgists.rutile.mixin.registrate.AbstractRegistrateAccessor;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
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

    public <T, I extends T, S, R extends AbstractBuilder<T, I, RutileRegistrate, R>> RegistryEntry<T, I> part(
            Part<T> part,
            MaterialData data,
            NonNullBiFunction<String, NonNullFunction<S, T>, R> func, ProviderType<? extends RegistrateProvider>... dataProviders) {
        String name = part.idPattern(data).formatted(data.getName());
        AbstractBuilder<T, I, RutileRegistrate, ?> builder = func.apply(name, (S properties) -> part.constructor().create(properties, part.getKey(), data));
        for (ProviderType<? extends RegistrateProvider> provider : dataProviders) {
            builder.setData(provider, NonNullBiConsumer.noop());
        }
        return builder.register();
    }

    public <I extends Item> RegistryEntry<Item, I> part(ItemPart part, MaterialData data) {
        NonNullBiFunction<String, NonNullFunction<Item.Properties, Item>, ItemBuilder> func = this::item;
        return part(part, data, func, ProviderType.LANG, ProviderType.ITEM_MODEL);
    }

    public <I extends Block> RegistryEntry<Block, I> part(BlockPart part, MaterialData data) {
        NonNullBiFunction<String, NonNullFunction<BlockBehaviour.Properties, Block>, BlockBuilder> func = this::block;
        return part(part, data, func);
    }

}
