package dev.metallurgists.rutile.api.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.material.base.Material;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("LombokGetterMayBeUsed")
public class RutileAPI {
    public static final String ID = "rutile";

    public static Rutile instance;

    private static IRutileRegistry<Material> materialRegistry;
    private static IRutileRegistry<Element> elementRegistry;

    /**
     * The registry in which all materials are stored
     * @return the material registry
     */
    public static IRutileRegistry<Material> getMaterialRegistry() {
        return materialRegistry;
    }

    /**
     * The registry in which all elements are stored
     * @return the element registry
     */
    public static IRutileRegistry<Element> getElementRegistry() {
        return elementRegistry;
    }

    /**
     * Creates a {@link ResourceLocation} in the `rutile` namespace
     * @param path the path
     * @return the resource location
     */
    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}
