package dev.metallurgists.rutile.api.registrate;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.metallurgists.rutile.api.RutileApi;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class MaterialLangGenerator {
    public static void generate(RegistrateLangProvider provider, final String modId) {
        RutileApi.getMaterialRegistry().getAll().stream()
                .filter(mat -> mat.getModId().equals(modId))
                .forEach(material -> provider.add(material.getDescriptionId(), toEnglishName(material.getName())));
    }

    public static String toEnglishName(Object internalName) {
        return Arrays.stream(internalName.toString().toLowerCase(Locale.ROOT).split("_"))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(" "));
    }
}
