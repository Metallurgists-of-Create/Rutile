package dev.metallurgists.rutile.api.registry;

import com.google.common.collect.Multimaps;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.RutileRegistrate;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.registrate.MaterialLangGenerator;
import dev.metallurgists.rutile.mixin.registrate.AbstractRegistrateAccessor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;

import java.util.*;

public class MaterialRegistry extends RutileRegistry<Material> {

    public MaterialRegistry(ResourceKey<? extends Registry<Material>> key) {
        super(key);
    }

    @Override
    public Material register(Material material) {
        return register(material.getId(), material);
    }

    @Override
    public Material getByKey(ResourceLocation id) {
        return get(id);
    }

    public void onLoadComplete() {
        Rutile.LOGGER.info("Loaded {} materials", this.getAll().size());
    }

    private static void postInitMaterials() {
        // Register all material manager registries, for materials with mod ids.
        RutileRegistries.MATERIALS.getUsedNamespaces().forEach(namespace -> {
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
}
