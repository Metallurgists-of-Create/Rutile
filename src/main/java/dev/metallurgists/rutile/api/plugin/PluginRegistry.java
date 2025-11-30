package dev.metallurgists.rutile.api.plugin;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.RutileCorePlugin;
import net.neoforged.fml.ModList;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class PluginRegistry {
    private static final PluginRegistry INSTANCE = new PluginRegistry();
    private final Map<IRutilePlugin, PluginConfig> plugins = new LinkedHashMap<>();

    public void loadPlugins() {
        this.plugins.put(new RutileCorePlugin(), new PluginConfig());
        Rutile.LOGGER.info("Registered plugin: {}", RutileCorePlugin.class.getName());

        ModList.get().getAllScanData().forEach(data -> {
            data.getAnnotations().forEach(annotation -> {
                if (annotation.annotationType().getClassName().equals(RutilePlugin.class.getName())) {
                    try {
                        Class<?> clazz = Class.forName(annotation.memberName());
                        if (IRutilePlugin.class.isAssignableFrom(clazz)) {
                            IRutilePlugin plugin = (IRutilePlugin) clazz.newInstance();
                            this.plugins.put(plugin, new PluginConfig());
                            Rutile.LOGGER.info("Registered plugin: {}", annotation.memberName());
                        }
                    } catch (Exception e) {
                        Rutile.LOGGER.error("Error loading plugin: {}", annotation.memberName(), e);
                    }
                }
            });
        });

        this.forEach(IRutilePlugin::configure);

        Rutile.LOGGER.info("Loaded {} plugins", this.plugins.size());
    }

    public void forEach(BiConsumer<IRutilePlugin, PluginConfig> action) {
        this.plugins.forEach(action);
    }

    public <T> Stream<T> map(BiFunction<IRutilePlugin, PluginConfig, T> function) {
        Function<Map.Entry<IRutilePlugin, PluginConfig>, T> entryFunction = next -> function.apply(next.getKey(), next.getValue());
        return this.plugins.entrySet().stream().map(entryFunction);
    }

    public static PluginRegistry getInstance() {
        return INSTANCE;
    }
}
