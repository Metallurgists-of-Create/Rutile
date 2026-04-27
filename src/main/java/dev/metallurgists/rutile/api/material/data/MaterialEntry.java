package dev.metallurgists.rutile.api.material.data;

import com.google.common.base.Preconditions;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import dev.metallurgists.rutile.registry.RutileMaterials;
import dev.metallurgists.rutile.registry.RutileModules;
import dev.metallurgists.rutile.registry.RutileRegisterKeys;
import lombok.experimental.Accessors;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.NotNull;

@Accessors(chain = true, fluent = true)
public record MaterialEntry(@NotNull Holder<RegistryModule.Key> key, @NotNull Material material) {
    public MaterialEntry(Holder<RegistryModule.Key> key, Material material) {
        this.key = Preconditions.checkNotNull(key, "Entry RegisterKey cannot be null!");
        this.material = Preconditions.checkNotNull(material, "Entry Material cannot be null!");
    }

    public static final MaterialEntry NULL_ENTRY = new MaterialEntry(RutileRegisterKeys.Null, RutileMaterials.Null);

    public MaterialEntry(Holder<RegistryModule.Key> key) {
        this(key, RutileMaterials.Null);
    }

    public boolean isEmpty() {
        return this == NULL_ENTRY || material() == RutileMaterials.Null;
    }

    public boolean isIgnored() {
        return material().getModule(RutileModules.IGNORE).map(module -> module.isIgnored(key())).orElse(false);
    }

    @Override
    public String toString() {
        return key.value().loc() + "/" + material.getName();
    }
}
