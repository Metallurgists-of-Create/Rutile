package dev.metallurgists.rutile.api.part;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.part.constructor.Constructor;
import dev.metallurgists.rutile.registry.RutileModules;
import dev.metallurgists.rutile.util.StringFormatUtil;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;

public abstract class Part<T> {

    public abstract <S> Constructor<T, S> constructor();

    public abstract String idPattern(Material data);

    public abstract ResourceKey<Registry<T>> registryResourceKey();

    public abstract PartKey<T> getKey();

    public String asLangKey(Material data) {
        return Util.makeDescriptionId("material", Rutile.getResource(data.getName()).withSuffix("." + getKey().getName()));
    }

    public String getLowerCaseName() {
        return StringFormatUtil.toLowerCaseUnder(getKey().getName());
    }

    public String getUnlocalizedName() {
        return Rutile.getResource(getKey().getName()).toLanguageKey("module");
    }

    public MutableComponent getLocalizedName(Material data) {
        return Component.translatable(getUnlocalizedName(data), getMaterialDisplayName(data));
    }

    public String getUnlocalizedName(Material data) {
        String matSpecificKey = String.format("item.rutile.%s", idPattern(data).formatted(getMaterialName(data)));
        if (Language.getInstance().has(matSpecificKey)) {
            return matSpecificKey;
        }
        return getUnlocalizedName();
    }

    public String getMaterialName(Material data) {
        var module = data.getProperties().getModule(RutileModules.NAME_ALTERNATIVE);
        return module.map(alt -> alt.getAlternative(getKey()).orElse(data.getName())).orElse(data.getName());
    }

    public Component getMaterialDisplayName(Material data) {
        boolean hasAlt = data.getProperties().getModule(RutileModules.NAME_ALTERNATIVE).map(alt -> alt.hasAlternative(getKey())).orElse(false);
        if (hasAlt) {
            return Component.translatable(asLangKey(data));
        }
        return data.getDisplayName();
    }
}
