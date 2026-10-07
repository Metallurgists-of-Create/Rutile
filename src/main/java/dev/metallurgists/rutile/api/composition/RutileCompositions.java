package dev.metallurgists.rutile.api.composition;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.data.manager.composition.AbstractCompositionManager;
import dev.metallurgists.rutile.api.plugin.PluginRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RutileCompositions {
    public static final RutileCompositions INSTANCE = new RutileCompositions();
    private boolean frozen = false;

    private final Map<ResourceLocation, AbstractCompositionManager<?>> managers = new HashMap<>();

    private RutileCompositions() {}

    public <T> void addManager(AbstractCompositionManager<T> manager) {
        if (frozen) {
            throw new IllegalStateException("Cannot register Composition manager '" + manager.getType() + "': registry is frozen.");
        }

        ResourceLocation type = manager.getType();

        if (!managers.containsKey(type)) {
            managers.put(type, manager);
        }
    }

    public void collectManagers() {
        if (frozen) {
            throw new IllegalStateException("Cannot collect Composition managers: registry is frozen");
        }

        PluginRegistry.getInstance().forEach((plugin, config) -> plugin.collectCompositionManagers(this));
        Rutile.LOGGER.info("Registered {} composition managers: {}", this.managers.size(), this.getTypes());
    }

    public void register(AddReloadListenerEvent event) {
        frozen = false;
        for (var manager : managers.values()) {
            event.addListener(manager);
        }
        frozen = true;
    }

    public AbstractCompositionManager<?> getManager(ResourceLocation type) {
        return managers.get(type);
    }

    public List<ResourceLocation> getTypes() {
        return new ArrayList<>(managers.keySet());
    }
}
