package dev.metallurgists.rutile.api.material.builder;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.util.helpers.ModelHelpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public abstract class MaterialItemBuilder extends MaterialRegistryBuilder<Item> {

    protected MaterialItemBuilder(String nameFormat) {
        super(Registries.ITEM, nameFormat);
    }

    @Override
    public void registerAssets(Material material) {
        ModelHelpers.generatedItemModel(material, getBuilderName(), nameFormat());
    }
}
