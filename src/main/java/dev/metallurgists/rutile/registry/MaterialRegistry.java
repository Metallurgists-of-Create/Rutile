package dev.metallurgists.rutile.registry;

import com.google.common.collect.Multimaps;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.plugin.PluginConfig;
import dev.metallurgists.rutile.api.plugin.PluginRegistry;
import dev.metallurgists.rutile.api.registrate.MaterialLangGenerator;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.api.registry.IRutileRegistry;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.mixin.registrate.AbstractRegistrateAccessor;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.*;

public final class MaterialRegistry implements IRutileRegistry<Material> {
    private static final MaterialRegistry INSTANCE = new MaterialRegistry();

    private final Set<String> usedNamespaces = new HashSet<>();
    private Map<ResourceLocation, Material> materials = new LinkedHashMap<>();

    @Setter
    private boolean allowRegistration = false;
    private PluginConfig currentPluginConfig = null;

    @Override
    public Set<String> getUsedNamespaces() {
        return usedNamespaces;
    }

    @Override
    public void register(Material material) {
        if (this.allowRegistration) {
            if (this.materials.values().stream().noneMatch(c -> c.getName().equals(material.getName()))) {
                this.materials.put(material.getId(), material);
                this.usedNamespaces.add(material.getNamespace());
            } else {
                Rutile.LOGGER.info("{} tried to register a duplicate material with name {}, skipping", material.getNamespace(), material.getName());
            }
        } else {
            Rutile.LOGGER.error("{} tried to register material {} outside of onRegisterMaterials, skipping", material.getNamespace(), material.getName());
        }
    }

    @Override
    public List<Material> getAll() {
        return List.copyOf(this.materials.values());
    }

    @Override
    public Material getById(ResourceLocation id) {
        return this.materials.get(id);
    }

    @Override
    public Material getByName(String name) {
        return this.materials.values().stream().filter(m -> name.equals(m.getName())).findFirst().orElse(null);
    }

    public static MaterialRegistry getInstance() {
        return INSTANCE;
    }

    public void onRegisterObjects(RegisterEvent event) {
        PluginRegistry.getInstance().forEach((plugin, config) -> {
            this.currentPluginConfig = config;

            plugin.onRegisterMaterials(this);
        });

        PluginRegistry.getInstance().forEach((plugin, config) -> plugin.onPostRegisterMaterials(this));

        var materials = this.materials.values();

        for (Material material : materials) {
            for (var container : material.getFlags().getFlagContainers()) {
                if (container.getRegistryType().resourceKey().equals(event.getRegistryKey())) {
                    container.getBuilders().forEach((f, b) ->
                            b.register(currentPluginConfig.getRegistrate(), material));
                }
            }
        }

        postInitMaterials();
        this.currentPluginConfig = null;
    }

    @Override
    public Optional<Material> getOptional(ResourceLocation key) {
        Material material = getById(key);
        return Optional.ofNullable(material);
    }

    private static void postInitMaterials() {
        // Register all material manager registries, for materials with mod ids.
        RutileAPI.getMaterialRegistry().getUsedNamespaces().forEach(namespace -> {
            // Force the material lang generator to be at index 0, so that addons' lang generators can override it.
            RutileRegistrate registrate = RutileRegistrate.createIgnoringListenerErrors(namespace);
            AbstractRegistrateAccessor accessor = (AbstractRegistrateAccessor) registrate;
            if (accessor.getDoDatagen().get()) {
                List<NonNullConsumer<? extends RegistrateProvider>> providers = Multimaps.asMap(accessor.getDatagens())
                        .get(ProviderType.LANG);
                if (providers != null)
                    providers.addFirst(
                            (provider) -> MaterialLangGenerator.generate((RegistrateLangProvider) provider, namespace));
            }

            ModList.get().getModContainerById(namespace)
                    .map(ModContainer::getEventBus)
                    .ifPresent(registrate::registerEventListeners);
        });
    }

    public void onCommonSetup() {
        Rutile.LOGGER.info("Loaded {} materials", this.materials.size());
    }
}
