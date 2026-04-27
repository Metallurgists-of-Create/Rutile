package dev.metallurgists.rutile.api.material.module.registry;

import dev.metallurgists.rutile.RutileClient;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.module.MaterialModule;
import dev.metallurgists.rutile.registry.RutileModules;
import dev.metallurgists.rutile.registry.RutileRegisterKeys;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

public interface RegistryModule<T extends RegistryModule<T>> extends MaterialModule<T> {

    void register(RegisterEvent event, Material material);

    record Key(ResourceLocation loc, String idPattern){
        public String asLangKey(Material material) {
            return Util.makeDescriptionId("material", material.getId().withSuffix("." + loc.getNamespace()+"_"+loc.getPath()));
        }

        public String getLowerCaseName() {
            return RutileClient.toLowerCaseUnder(loc.getPath());
        }

        public String getUnlocalizedName() {
            return loc.toLanguageKey("module");
        }

        public MutableComponent getLocalizedName(Material material) {
            return Component.translatable(getUnlocalizedName(material), getMaterialDisplayName(material));
        }

        public String getUnlocalizedName(Material material) {
            String matSpecificKey = String.format("item.%s.%s", material.getModId(), idPattern().formatted(getMaterialName(material)));
            if (Language.getInstance().has(matSpecificKey)) {
                return matSpecificKey;
            }
            return getUnlocalizedName();
        }

        public String getMaterialName(Material material) {
            var module = material.getModule(RutileModules.NAME_ALTERNATIVE);
            return module.map(alt -> alt.getAlternative(loc).orElse(material.getName())).orElse(material.getName());
        }

        public Component getMaterialDisplayName(Material material) {
            boolean hasAlt = material.getModule(RutileModules.NAME_ALTERNATIVE).map(alt -> alt.hasAlternative(loc)).orElse(false);
            if (hasAlt) {
                return Component.translatable(asLangKey(material));
            }
            return material.getDisplayName();
        }

        public Holder<Key> makeHolder() {
            ResourceLocation loc = RutileRegisterKeys.KEYS_REGISTRY.getKey(this);
            if (loc == null) return null;
            return RutileRegisterKeys.KEYS_REGISTRY.getHolder(loc).orElse(null);
        }
    }
}
