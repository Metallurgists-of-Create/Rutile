package dev.metallurgists.rutile.api.registry.material;

import com.tterrag.registrate.AbstractRegistrate;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePluginFinder;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.api.registry.RutileRegistry;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public abstract class MaterialRegistry extends RutileRegistry.String<Material> {

    @Getter
    private final AbstractRegistrate<?> registrate;

    public MaterialRegistry(java.lang.String modId) {
        super(new ResourceLocation(modId, "material"));
        IRutilePlugin plugin = RutilePluginFinder.getPlugin(modId);
        this.registrate = plugin != null ? plugin.getRegistrate() :
                Rutile.ID.equals(modId) ? Rutile.registrate() : RutileRegistrate.create(modId);
    }

    public abstract void register(Material material);

    @NotNull
    public abstract Collection<Material> getAllMaterials();

    public abstract void setFallbackMaterial(@NotNull Material material);

    @NotNull
    public abstract Material getFallbackMaterial();

    public abstract int getNetworkId();

    @NotNull
    public abstract java.lang.String getModid();
}
