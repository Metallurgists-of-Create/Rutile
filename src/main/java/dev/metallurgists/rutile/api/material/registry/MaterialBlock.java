package dev.metallurgists.rutile.api.material.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import dev.metallurgists.rutile.client.MaterialBlockRenderer;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.Block;

public class MaterialBlock extends Block {
    public final Holder<RegistryModule.Key> registerKey;
    public final Material material;

    public MaterialBlock(Properties properties, Holder<RegistryModule.Key> registerKey, Material material, boolean registerModel) {
        super(properties);
        this.material = material;
        this.registerKey = registerKey;
        if (registerModel && Rutile.isClientSide()) {
            MaterialBlockRenderer.create(this, material, registerKey.value());
        }
    }

    public MaterialBlock(Properties properties, Holder<RegistryModule.Key> registerKey, Material material) {
        this(properties, registerKey, material, true);
    }

    // This should only be enabled if the material is
    @Override
    public boolean isEnabled(FeatureFlagSet enabledFeatures) {
        return material.isEnabled(enabledFeatures) && super.isEnabled(enabledFeatures);
    }

    @Override
    public String getDescriptionId() {
        return registerKey.value().getUnlocalizedName(material);
    }

    @Override
    public MutableComponent getName() {
        return registerKey.value().getLocalizedName(material);
    }
}
