package dev.metallurgists.rutile.api.plugin;

import dev.metallurgists.rutile.api.composition.RutileCompositions;
import dev.metallurgists.rutile.api.material.RutileMaterial;

public interface IRutilePlugin {
    /**
     * Override this method to configure plugin wide settings
     * @param config the plugin config
     */
    default void configure(PluginConfig config) { }

    /**
     * Override this method to add custom Composition managers
     */
    default void collectCompositionManagers(RutileCompositions handler) { }

    default void modifyMaterials(RutileMaterial materials) { }
}
