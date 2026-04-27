package dev.metallurgists.rutile.api.material.module.registry;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.metallurgists.rutile.RutileRegistrate;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.data.ItemMaterialData;
import dev.metallurgists.rutile.api.material.data.MaterialEntry;
import dev.metallurgists.rutile.api.material.module.ModuleHolder;
import dev.metallurgists.rutile.api.material.module.registry.constructor.BlockConstructor;
import dev.metallurgists.rutile.api.material.module.registry.constructor.BlockItemConstructor;
import dev.metallurgists.rutile.api.material.module.registry.constructor.ItemConstructor;
import dev.metallurgists.rutile.api.material.registry.MaterialBlock;
import dev.metallurgists.rutile.api.material.registry.MaterialBlockItem;
import dev.metallurgists.rutile.api.material.registry.MaterialItem;
import dev.metallurgists.rutile.api.memorizer.Memoizer;
import dev.metallurgists.rutile.registry.RutileModules;
import dev.metallurgists.rutile.registry.RutileRegisterKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Function;
import java.util.function.Supplier;

public class MetalModule implements RegistryModule<MetalModule> {

    private final ItemConstructor ingotConstructor;
    private final ItemConstructor nuggetConstructor;
    private final BlockConstructor blockConstructor;
    private final BlockItemConstructor blockItemConstructor;

    private Function<BlockBehaviour.Properties, BlockBehaviour.Properties> blockPropertiesCallback = (p) -> p;
    private Function<Item.Properties, Item.Properties> blockItemPropertiesCallback = (p) -> p;

    public MetalModule(ItemConstructor ingotConstructor, ItemConstructor nuggetConstructor, BlockConstructor blockConstructor, BlockItemConstructor blockItemConstructor) {
        this.ingotConstructor = ingotConstructor;
        this.nuggetConstructor = nuggetConstructor;
        this.blockConstructor = blockConstructor;
        this.blockItemConstructor = blockItemConstructor;
    }

    public MetalModule() {
        this.ingotConstructor = MaterialItem::new;
        this.nuggetConstructor = MaterialItem::new;
        this.blockConstructor = MaterialBlock::new;
        this.blockItemConstructor = MaterialBlockItem::new;
    }

    public MetalModule blockProperties(Function<BlockBehaviour.Properties, BlockBehaviour.Properties> blockPropertiesCallback) {
        this.blockPropertiesCallback = blockPropertiesCallback;
        return this;
    }

    public MetalModule blockItemProperties(Function<Item.Properties, Item.Properties> blockItemPropertiesCallback) {
        this.blockItemPropertiesCallback = blockItemPropertiesCallback;
        return this;
    }

    @Override
    public void register(RegisterEvent event, Material material) {
        RutileRegistrate registrate = RutileRegistrate.createIgnoringListenerErrors(material.getModId());
        if (event.getRegistryKey() == Registries.BLOCK) {
            registerBlock(material, registrate);
        }
        if (event.getRegistryKey() == Registries.ITEM) {
            registerItem(material, registrate, RutileRegisterKeys.Ingot, ingotConstructor);
            registerItem(material, registrate, RutileRegisterKeys.Nugget, nuggetConstructor);
        }
    }

    public void registerBlock(Material material, RutileRegistrate registrate) {
        var key = RutileRegisterKeys.StorageBlock;
        BlockBuilder<? extends Block, ?> blockBuilder = registrate
                .block(key.get().idPattern().formatted(key.get().getMaterialName(material)), properties -> blockConstructor.create(properties, key, material))
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(blockPropertiesCallback::apply)
                .setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.LOOT, NonNullBiConsumer.noop())
                .item((b, p) -> blockItemConstructor.create(b, p, key, material))
                .properties(blockItemPropertiesCallback::apply)
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .build();

        blockBuilder.onRegister(block -> {
            Supplier<Block> supplier = Memoizer.memoize(() -> block);
            //MaterialEntry entry = new MaterialEntry(this, material);
            // BlockMaterialData.registerMaterialEntry would need to be implemented
        });

        blockBuilder.register();
    }

    public void registerItem(Material material, RutileRegistrate registrate, Holder<Key> key, ItemConstructor constructor) {
        ItemBuilder<? extends Item, ?> itemBuilder = registrate
                .item(key.value().idPattern().formatted(key.value().getMaterialName(material)), properties -> constructor.create(properties, key, material))
                .properties(p -> p)
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop());

        itemBuilder.onRegister(item -> {
            Supplier<Item> supplier = Memoizer.memoize(() -> item);
            MaterialEntry entry = new MaterialEntry(key, material);
            ItemMaterialData.registerMaterialEntry(supplier, entry);
        });

        itemBuilder.register();
    }

    @Override
    public ModuleHolder<MetalModule> getType() {
        return RutileModules.METAL;
    }

    @Override
    public ModuleBuilder<MetalModule> builder() {
        return new Builder();
    }

    public static class Builder implements ModuleBuilder<MetalModule> {
        private ItemConstructor ingot = MaterialItem::new;
        private ItemConstructor nugget = MaterialItem::new;
        private BlockConstructor block = MaterialBlock::new;
        private BlockItemConstructor blockItem = MaterialBlockItem::new;

        public Builder ingot(ItemConstructor ingot) {
            this.ingot = ingot;
            return this;
        }

        public Builder nugget(ItemConstructor nugget) {
            this.nugget = nugget;
            return this;
        }

        public Builder block(BlockConstructor block) {
            this.block = block;
            return this;
        }

        public Builder blockItem(BlockItemConstructor blockItem) {
            this.blockItem = blockItem;
            return this;
        }

        @Override
        public MetalModule build() {
            return new MetalModule(ingot, nugget, block, blockItem);
        }
    }
}
