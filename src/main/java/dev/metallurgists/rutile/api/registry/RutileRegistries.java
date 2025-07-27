package dev.metallurgists.rutile.api.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.element.Element;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jetbrains.annotations.ApiStatus;

public class RutileRegistries {
    public static final RutileRegistry.RL<Element> ELEMENTS = new RutileRegistry.RL<>(Rutile.id("element"));

    public static void init() {
    }

    private static final RegistryAccess BLANK = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
    private static RegistryAccess FROZEN = BLANK;

    @ApiStatus.Internal
    public static void updateFrozenRegistry(RegistryAccess registryAccess) {
        FROZEN = registryAccess;
    }

    public static RegistryAccess builtinRegistry() {
        if (Rutile.isClientThread()) {
            return ClientHelpers.getClientRegistries();
        }
        return FROZEN;
    }

    private static class ClientHelpers {

        private static RegistryAccess getClientRegistries() {
            if (Minecraft.getInstance().getConnection() != null) {
                return Minecraft.getInstance().getConnection().registryAccess();
            } else {
                return FROZEN;
            }
        }
    }
}
