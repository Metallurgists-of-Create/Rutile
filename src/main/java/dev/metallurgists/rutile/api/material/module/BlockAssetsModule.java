package dev.metallurgists.rutile.api.material.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import dev.metallurgists.rutile.registry.RutileModules;
import net.minecraft.core.Holder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class BlockAssetsModule implements MaterialModule<BlockAssetsModule> {
    private final Map<Holder<RegistryModule.Key>, Properties> assets;

    public BlockAssetsModule() {
        this.assets = new HashMap<>();
    }

    public Optional<Properties> getProperties(Holder<RegistryModule.Key> registerKey) {
        if (hasProperty(registerKey))
            return Optional.of(assets.get(registerKey));
        return Optional.empty();
    }

    public boolean hasProperty(Holder<RegistryModule.Key> registerKey) {
        return assets.containsKey(registerKey);
    }

    @Override
    public ModuleHolder<BlockAssetsModule> getType() {
        return RutileModules.BLOCK_ASSETS;
    }

    @Override
    public ModuleBuilder<BlockAssetsModule> builder() {
        return new Builder();
    }

    public static class Builder implements ModuleBuilder<BlockAssetsModule> {
        private final Map<Holder<RegistryModule.Key>, Properties> assets = new HashMap<>();

        public Builder add(Holder<RegistryModule.Key> registerKey, Properties properties) {
            assets.put(registerKey, properties);
            return this;
        }

        @Override
        public BlockAssetsModule build() {
            BlockAssetsModule module = new BlockAssetsModule();
            module.assets.putAll(assets);
            return module;
        }
    }

    public record Properties(BiFunction<Material, Holder<RegistryModule.Key>, JsonElement> model, BiFunction<Material, Holder<RegistryModule.Key>, JsonElement> blockState, BiFunction<Material, Holder<RegistryModule.Key>, JsonElement> itemModel) {
        static BiFunction<Material, Holder<RegistryModule.Key>, JsonElement> EMPTY_FUNC = (m, t) -> new JsonObject();
        public static Properties EMPTY = new Properties(EMPTY_FUNC,EMPTY_FUNC,EMPTY_FUNC);

        public boolean isEmpty() {
            return this == EMPTY;
        }

        public boolean hasModel() {
            return model() != EMPTY_FUNC;
        }

        public boolean hasBlockState() {
            return blockState() != EMPTY_FUNC;
        }

        public boolean hasItemModel() {
            return itemModel() != EMPTY_FUNC;
        }

        public static Properties.Builder builder() {
            return new Properties.Builder();
        }

        public static class Builder {
            private BiFunction<Material, Holder<RegistryModule.Key>, JsonElement> model = EMPTY_FUNC;
            private BiFunction<Material, Holder<RegistryModule.Key>, JsonElement> blockState = EMPTY_FUNC;
            private BiFunction<Material, Holder<RegistryModule.Key>, JsonElement> itemModel = EMPTY_FUNC;

            public Properties.Builder model(BiFunction<Material, Holder<RegistryModule.Key>, JsonElement> model) {
                this.model = model;
                return this;
            }

            public Properties.Builder blockState(BiFunction<Material, Holder<RegistryModule.Key>, JsonElement> blockState) {
                this.blockState = blockState;
                return this;
            }

            public Properties.Builder itemModel(BiFunction<Material, Holder<RegistryModule.Key>, JsonElement> itemModel) {
                this.itemModel = itemModel;
                return this;
            }

            public Properties build() {
                return new Properties(model, blockState, itemModel);
            }
        }
    }
}
