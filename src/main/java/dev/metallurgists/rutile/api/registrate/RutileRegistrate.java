package dev.metallurgists.rutile.api.registrate;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import dev.metallurgists.rutile.mixin.registrate.AbstractRegistrateAccessor;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class RutileRegistrate extends AbstractRegistrate<RutileRegistrate> {

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

    public void registerRegistrate() {
        registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @Override
    public RutileRegistrate registerEventListeners(IEventBus bus) {
        if (!registered.getAndSet(true)) {
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
}
