package dev.metallurgists.rutile.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.client.LangKubeEvent;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.StringUtilsWrapper;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.PluginConfig;
import dev.metallurgists.rutile.api.plugin.RutilePlugin;
import dev.metallurgists.rutile.compat.kubejs.event.RutileKubeEvents;
import dev.metallurgists.rutile.compat.kubejs.registry.KubeElementBuilder;
import dev.metallurgists.rutile.compat.kubejs.registry.KubeRegistrate;
import dev.metallurgists.rutile.registry.RutileElements;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedList;

@RutilePlugin("kubejs")
public final class RutileKubePlugin implements IRutilePlugin, KubeJSPlugin {
    private static LinkedList<ResourceLocation> toGenerateLang = new LinkedList<>();

    // Rutile

    @Override
    public void configure(PluginConfig config) {
        config.setModId(KubeJS.MOD_ID);
        config.setRegistrate(KubeRegistrate.INSTANCE);
    }

    @Override
    public void registerRegistries() {
        if (RutileKubeEvents.REGISTER_ELEMENTS.hasListeners()) {
            KubeElementBuilder builder = new KubeElementBuilder();
            RutileKubeEvents.REGISTER_ELEMENTS.post(ScriptType.STARTUP, builder);
            for (KubeElementBuilder.Built built : builder.getBuiltElements()) {
                toGenerateLang.add(built.id());
                RutileElements.create(built.id(), built.symbol(), built.color());
            }
        }
    }

    // Kube

    @Override
    public void generateLang(LangKubeEvent event) {
        for (ResourceLocation id : toGenerateLang) {
            event.add("element." + id.getNamespace() + "." + id.getPath(), StringUtilsWrapper.snakeCaseToTitleCase(id.getPath()));
        }
        toGenerateLang = null;
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(RutileKubeEvents.GROUP);
    }
}
