package dev.metallurgists.rutile.api;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.data.ItemCompositionManager;
import dev.metallurgists.rutile.api.data.MaterialCompositionManager;
import dev.metallurgists.rutile.api.element.Element;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.registry.IRutileRegistry;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

public class RutileApi {
    public static final String ID = "rutile";

    public static Rutile instance;

    /**
     * -- GETTER --
     *  The registry in which all materials are stored
     *
     * @return the material registry
     */
    @Getter
    private static IRutileRegistry<Material> materialRegistry;
    /**
     * -- GETTER --
     *  The registry in which all elements are stored
     *
     * @return the element registry
     */
    @Getter
    private static IRutileRegistry<Element> elementRegistry;
    @Getter
    private static ItemCompositionManager itemCompositionManager;
    @Getter
    private static MaterialCompositionManager materialCompositionManager;
    /**
     * Creates a {@link ResourceLocation} in the `rutile` namespace
     * @param path the path
     * @return the resource location
     */
    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}
