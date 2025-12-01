package dev.metallurgists.rutile;

import dev.metallurgists.rutile.api.element.Element;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.PluginConfig;
import dev.metallurgists.rutile.api.registry.IRutileRegistry;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import dev.metallurgists.rutile.registry.RutileElements;
import dev.metallurgists.rutile.registry.RutileMaterials;
import dev.metallurgists.rutile.registry.RutileTagPrefixes;
import net.minecraft.world.item.Items;

import static dev.metallurgists.rutile.registry.RutileTagPrefixes.*;

public class RutileCorePlugin implements IRutilePlugin {
    @Override
    public void configure(PluginConfig config) {
        config.setModId(Rutile.ID);
        config.setRegistrate(Rutile.registrate());
    }

    @Override
    public void onRegisterMaterials(IRutileRegistry<Material> registry) {
        registry.register(RutileMaterials.Null);
        registry.register(RutileMaterials.Iron);
        registry.register(RutileMaterials.Gold);
        registry.register(RutileMaterials.Copper);
        registry.register(RutileMaterials.Diamond);
        registry.register(RutileMaterials.Emerald);
        registry.register(RutileMaterials.Quartz);
        registry.register(RutileMaterials.Amethyst);

        Ingot.setIgnored(RutileMaterials.Iron, Items.IRON_INGOT);
        Ingot.setIgnored(RutileMaterials.Gold, Items.GOLD_INGOT);
        Ingot.setIgnored(RutileMaterials.Copper, Items.COPPER_INGOT);
        Gem.setIgnored(RutileMaterials.Diamond, Items.DIAMOND);
        Gem.setIgnored(RutileMaterials.Emerald, Items.EMERALD);
        Gem.setIgnored(RutileMaterials.Quartz, Items.QUARTZ);
        Gem.setIgnored(RutileMaterials.Amethyst, Items.AMETHYST_SHARD);
    }

    @Override
    public void onRegisterElements(IRutileRegistry<Element> registry) {
        RutileElements.init(registry);
    }
}
