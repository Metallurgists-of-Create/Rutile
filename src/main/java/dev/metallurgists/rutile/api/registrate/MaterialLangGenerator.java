package dev.metallurgists.rutile.api.registrate;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.metallurgists.rutile.api.registry.RutileAPI;

import static dev.metallurgists.rutile.util.ClientUtil.toEnglishName;

public class MaterialLangGenerator {
    public static void generate(RegistrateLangProvider provider, final String modId) {
        RutileAPI.getMaterialRegistry().getAll().stream()
                .filter(mat -> mat.getNamespace().equals(modId))
                .forEach(material -> provider.add(material.getDescriptionId(), toEnglishName(material.getName())));
    }
}
