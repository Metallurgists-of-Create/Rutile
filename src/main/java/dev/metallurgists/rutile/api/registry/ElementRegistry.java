package dev.metallurgists.rutile.api.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.element.Element;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.plugin.PluginRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.*;

public class ElementRegistry implements IRutileRegistry<Element> {
    private static final ElementRegistry INSTANCE = new ElementRegistry();

    private final Set<String> usedNamespaces = new HashSet<>();
    private final Map<ResourceLocation, Element> elements = new LinkedHashMap<>();

    @Override
    public Set<String> getUsedNamespaces() {
        return usedNamespaces;
    }

    @Override
    public void register(Element element) {
        if (this.elements.values().stream().noneMatch(c -> c.getId().equals(element.getId()))) {
            this.elements.put(element.getId(), element);
            this.usedNamespaces.add(element.getModId());
        } else {
            Rutile.LOGGER.info("{} tried to register a duplicate element with id {}, skipping", element.getModId(), element.getId());
        }
    }

    @Override
    public List<Element> getAll() {
        return List.copyOf(this.elements.values());
    }

    @Override
    public Element getById(ResourceLocation id) {
        return this.elements.get(id);
    }

    @Override
    public Element getByName(String name) {
        return this.elements.values().stream().filter(m -> name.equals(m.getName())).findFirst().orElse(null);
    }

    public static ElementRegistry getInstance() {
        return INSTANCE;
    }

    public void onLoadComplete() {
        Rutile.LOGGER.info("Loaded {} elements", this.elements.size());
    }

    @Override
    public Optional<Element> getOptional(ResourceLocation key) {
        return Optional.ofNullable(getById(key));
    }

    public void onRegisterObjects(RegisterEvent event) {
        PluginRegistry.getInstance().forEach((plugin, config) -> plugin.onRegisterElements(this));

        initializeElementObjects(event);

        PluginRegistry.getInstance().forEach((plugin, config) -> plugin.onPostRegisterElements(this));
    }

    public void initializeElementObjects(RegisterEvent event) {

    }
}
