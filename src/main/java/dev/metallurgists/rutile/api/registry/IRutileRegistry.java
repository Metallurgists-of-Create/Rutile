package dev.metallurgists.rutile.api.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IRutileRegistry<T> {
    /**
     * Get a list of all used namespaces
     * @return a set of all used namespaces
     */
    Set<String> getUsedNamespaces();

    /**
     * Register something in this registry
     * @param object the object to register
     */
    void register(T object);

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
    T getById(ResourceLocation id);

    /**
     * Gets the object with the specified internal name from this registry
     * @param name the internal name of this object
     * @return the object for this name
     */
    T getByName(String name);

    void onLoadComplete();

    Optional<T> getOptional(ResourceLocation key);
}
