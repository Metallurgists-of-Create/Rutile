package dev.metallurgists.rutile.api.composition;

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

    private final Map<ResourceLocation, AbstractCompositionManager<?>> managers = new HashMap<>();

    private RutileCompositions() {}

    public <T> boolean addManager(AbstractCompositionManager<T> manager) {
        ResourceLocation type = manager.getType();
        if (!managers.containsKey(type)) {
            managers.put(type, manager);
            return true;
        }
        return false;
    }

    public void register(AddReloadListenerEvent event) {
        PluginRegistry.getInstance().forEach((plugin, config) -> plugin.collectCompositionManagers(this));
        managers.forEach((type, manager) -> event.addListener(manager));
    }

    public AbstractCompositionManager<?> getManager(ResourceLocation type) {
        return managers.get(type);
    }

    public List<ResourceLocation> getTypes() {
        return new ArrayList<>(managers.keySet());
    }
}
