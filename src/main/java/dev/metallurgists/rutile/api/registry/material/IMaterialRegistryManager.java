package dev.metallurgists.rutile.api.registry.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.metallurgists.rutile.api.material.base.Material;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface IMaterialRegistryManager {

    @NotNull
    MaterialRegistry createRegistry(@NotNull String modid);

    @NotNull
    MaterialRegistry getRegistry(@NotNull String modid);

    @NotNull
    MaterialRegistry getRegistry(int networkId);

    @NotNull
    Collection<MaterialRegistry> getRegistries();

    @NotNull
    Collection<Material> getRegisteredMaterials();

    Material getMaterial(String name);

    ResourceLocation getKey(Material material);

    @NotNull
    Phase getPhase();

    default boolean canModifyMaterials() {
        return this.getPhase() != Phase.FROZEN && this.getPhase() != Phase.PRE;
    }

    default Codec<Material> codec() {
        return ResourceLocation.CODEC
                .flatXmap(
                        id -> Optional.ofNullable(this.getRegistry(id.getNamespace()).get(id.getPath()))
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult
                                        .error(() -> "Unknown registry key in material registry: " + id)),
                        obj -> DataResult.success(obj.getId()));
    }

    enum Phase {
        PRE,
        OPEN,
        CLOSED,
        FROZEN
    }
}
