package dev.metallurgists.rutile.api.material.flags;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;


@FunctionalInterface
public interface IMaterialFlag {

    void verifyFlag(MaterialFlags flags);

    /**
     * Use this to add information from the flag to the debug json
     * @return a new {@link JsonObject}
     */
    default JsonObject debugJson() {
        return new JsonObject();
    }
}
