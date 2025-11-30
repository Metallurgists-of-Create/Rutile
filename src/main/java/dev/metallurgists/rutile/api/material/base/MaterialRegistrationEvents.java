package dev.metallurgists.rutile.api.material.base;

import dev.metallurgists.rutile.api.material.component.ModifyMaterialComponentsEvent;
import net.neoforged.fml.ModLoader;

public class MaterialRegistrationEvents {
    private static boolean canModifyComponents;

    public static void modifyComponents() {
        canModifyComponents = true;
        ModLoader.postEvent(new ModifyMaterialComponentsEvent());
        canModifyComponents = false;
    }

    public static boolean canModifyComponents() {
        return canModifyComponents;
    }
}
