package dev.metallurgists.rutile.api.material.part;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.RutileRegistrate;
import dev.metallurgists.rutile.api.material.MaterialData;
import dev.metallurgists.rutile.registry.RutileModules;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;

public abstract class Part<T> {

    public abstract String idPattern(MaterialData data);

    public abstract <I extends T> RegistryEntry<T, I> register(MaterialData data, RutileRegistrate registrate);

    public abstract ResourceKey<Registry<T>> registryResourceKey();

    public abstract PartKey<T> getKey();

    public String asLangKey(MaterialData data) {
        return Util.makeDescriptionId("material", Rutile.id(data.getName()).withSuffix("." + getKey().getName()));
    }

    public String getLowerCaseName() {
        return RutileClient.toLowerCaseUnder(getKey().getName());
    }

    public String getUnlocalizedName() {
        return Rutile.id(getKey().getName()).toLanguageKey("module");
    }

    public MutableComponent getLocalizedName(MaterialData data) {
        return Component.translatable(getUnlocalizedName(data), getMaterialDisplayName(data));
    }

    public String getUnlocalizedName(MaterialData data) {
        String matSpecificKey = String.format("item.rutile.%s", idPattern(data).formatted(getMaterialName(data)));
        if (Language.getInstance().has(matSpecificKey)) {
            return matSpecificKey;
        }
        return getUnlocalizedName();
    }

    public String getMaterialName(MaterialData data) {
        var module = data.getModule(RutileModules.NAME_ALTERNATIVE);
        return module.map(alt -> alt.getAlternative(getKey()).orElse(data.getName())).orElse(data.getName());
    }

    public Component getMaterialDisplayName(MaterialData data) {
        boolean hasAlt = data.getModule(RutileModules.NAME_ALTERNATIVE).map(alt -> alt.hasAlternative(getKey())).orElse(false);
        if (hasAlt) {
            return Component.translatable(asLangKey(data));
        }
        return data.getDisplayName();
    }
}
