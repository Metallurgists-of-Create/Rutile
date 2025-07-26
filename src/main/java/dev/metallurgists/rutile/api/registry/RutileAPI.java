package dev.metallurgists.rutile.api.registry;

import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class RutileAPI {
    @Getter
    private static final Map<ResourceLocation, Material> registeredMaterials = new HashMap<>();
    @Getter
    private static final Map<ResourceLocation, FlagKey<?>> registeredFlags = new HashMap<>();
    @Getter
    private static final Map<ResourceLocation, Element> registeredElements = new HashMap<>();


    public static <T extends Material> void registerMaterial(ResourceLocation key, T entry) {
        registeredMaterials.put(key, entry);
    }

    public static <C extends IMaterialFlag> FlagKey<C> registerFlag(ResourceLocation key, FlagKey<C> entry) {
        registeredFlags.put(key, entry);
        return entry;
    }

    public static <T extends Element> void registerElement(ResourceLocation key, T entry) {
        registeredElements.put(key, entry);
    }


}
