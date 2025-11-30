package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.composition.element.ElementStack;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.plugin.PluginConfig;
import dev.metallurgists.rutile.api.plugin.PluginRegistry;
import dev.metallurgists.rutile.api.registry.IRutileRegistry;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.*;

public final class ElementRegistry implements IRutileRegistry<Element> {
    private static final ElementRegistry INSTANCE = new ElementRegistry();

    private final Set<String> usedNamespaces = new HashSet<>();
    private Map<ResourceLocation, Element> elements = new LinkedHashMap<>();

    @Setter
    private boolean allowRegistration = false;
    private PluginConfig currentPluginConfig = null;

    @Override
    public Set<String> getUsedNamespaces() {
        return usedNamespaces;
    }

    @Override
    public void register(Element element) {
        if (this.allowRegistration) {
            if (this.elements.values().stream().noneMatch(c -> c.getName().equals(element.getName()))) {
                this.elements.put(element.getId(), element);
                this.usedNamespaces.add(element.getNamespace());
            }
        } else {
            Rutile.LOGGER.error("{} tried to register element {} outside of onRegisterElements, skipping", element.getNamespace(), element.getName());
        }
    }

    @Override
    public List<Element> getAll() {
        return List.copyOf(this.elements.values());
    }

    public List<ElementStack> getAllAsStacks() {
        List<ElementStack> elementStacks = new ArrayList<>();
        for (Element element : this.elements.values()) {
            elementStacks.add(new ElementStack(element.getId()));
        }
        return elementStacks;
    }

    @Override
    public Element getById(ResourceLocation id) {
        return this.elements.get(id);
    }

    @Override
    public Element getByName(String name) {
        return this.elements.values().stream().filter(m -> name.equals(m.getName())).findFirst().orElse(null);
    }

    @Override
    public Optional<Element> getOptional(ResourceLocation key) {
        Element element = getById(key);
        return Optional.ofNullable(element);
    }

    public static ElementRegistry getInstance() {
        return INSTANCE;
    }

    public void onRegisterObjects(RegisterEvent event) {
        PluginRegistry.getInstance().forEach((plugin, config) -> {
            this.currentPluginConfig = config;

            plugin.onRegisterElements(this);
        });

        PluginRegistry.getInstance().forEach((plugin, config) -> plugin.onPostRegisterElements(this));

        this.currentPluginConfig = null;
    }

    @Override
    public void onCommonSetup() {
        Rutile.LOGGER.info("Loaded {} elements", this.elements.size());
    }
}
