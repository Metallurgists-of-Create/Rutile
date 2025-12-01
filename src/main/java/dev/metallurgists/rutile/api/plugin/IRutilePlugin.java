package dev.metallurgists.rutile.api.plugin;

import dev.metallurgists.rutile.api.element.Element;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.registry.IRutileRegistry;
import dev.metallurgists.rutile.api.tag.TagPrefix;

public interface IRutilePlugin {
    /**
     * Override this method to configure plugin wide settings
     * @param config the plugin config
     */
    default void configure(PluginConfig config) { }

    /**
     * Override this method and use the supplied registry to register all of your materials
     * @param registry the material registry
     */
    default void onRegisterMaterials(IRutileRegistry<Material> registry) { }

    /**
     * Override this method and use the supplied registry to modify materials from the material registry
     * @param registry the material registry
     */
    default void onPostRegisterMaterials(IRutileRegistry<Material> registry) { }

    /**
     * Override this method and use the supplied registry to register all of your elements
     * @param registry the element registry
     */
    default void onRegisterElements(IRutileRegistry<Element> registry) { }

    /**
     * Override this method and use the supplied registry to modify elements from the element registry
     * @param registry the element registry
     */
    default void onPostRegisterElements(IRutileRegistry<Element> registry) { }

    default void onRegisterTagPrefixes(IRutileRegistry<TagPrefix> registry) { }

    default void onPostRegisterTagPrefixes(IRutileRegistry<TagPrefix> registry) { }
}
