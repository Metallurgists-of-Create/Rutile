package dev.metallurgists.rutile.api.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Set;

public interface IRutileRegistry<T> extends Iterable<T> {
    /**
     * Get a list of all used namespaces
     * @return a set of all used namespaces
     */
    Set<String> getUsedNamespaces();

    /**
     * Register something in this registry
     * @param object the object to register
     */
    T register(T object);

    /**
     * Get an unmodifiable list of all the registered objects
     * @return a list of registered objects
     */
    List<T> getAll();

    /**
     * Get the object with the specified internal id from this registry
     * @param id the resource location id of the object
     * @return the object for this id
     */
    T getByKey(ResourceLocation id);

    /**
     * Get the key of the object.
     * @param object the object
     * @return the object's resource key
     */
    ResourceLocation getKey(T object);

    boolean isFrozen();

    void onLoadComplete();

}
