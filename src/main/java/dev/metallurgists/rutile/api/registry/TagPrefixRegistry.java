package dev.metallurgists.rutile.api.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.plugin.PluginRegistry;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.*;

public class TagPrefixRegistry implements IRutileRegistry<TagPrefix> {
    private static final TagPrefixRegistry INSTANCE = new TagPrefixRegistry();

    private final Set<String> usedNamespaces = new HashSet<>();
    private final Map<ResourceLocation, TagPrefix> tagPrefixes = new LinkedHashMap<>();

    @Override
    public Set<String> getUsedNamespaces() {
        return usedNamespaces;
    }

    @Override
    public void register(TagPrefix tagPrefix) {
        if (this.tagPrefixes.values().stream().noneMatch(c -> c.id().equals(tagPrefix.id()))) {
            this.tagPrefixes.put(tagPrefix.id(), tagPrefix);
            this.usedNamespaces.add(tagPrefix.getModId());
        } else {
            Rutile.LOGGER.info("{} tried to register a duplicate tag prefix with id {}, skipping", tagPrefix.getModId(), tagPrefix.id());
        }
    }

    @Override
    public List<TagPrefix> getAll() {
        return List.copyOf(this.tagPrefixes.values());
    }

    @Override
    public TagPrefix getById(ResourceLocation id) {
        return this.tagPrefixes.get(id);
    }

    @Override
    public TagPrefix getByName(String name) {
        return this.tagPrefixes.values().stream().filter(m -> name.equals(m.getName())).findFirst().orElse(null);
    }

    public static TagPrefixRegistry getInstance() {
        return INSTANCE;
    }

    public void onLoadComplete() {
        Rutile.LOGGER.info("Loaded {} tag prefixes", this.tagPrefixes.size());
    }

    public void onRegisterObjects(RegisterEvent event) {
        PluginRegistry.getInstance().forEach((plugin, config) -> plugin.onRegisterTagPrefixes(this));


        PluginRegistry.getInstance().forEach((plugin, config) -> plugin.onPostRegisterTagPrefixes(this));
    }

    @Override
    public Optional<TagPrefix> getOptional(ResourceLocation key) {
        return Optional.ofNullable(getById(key));
    }
}
