package dev.metallurgists.rutile.registry.flags;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.ItemFlag;
import dev.metallurgists.rutile.api.material.flag.types.IItemRegistry;
import dev.metallurgists.rutile.api.material.flag.types.IRecipeHandler;
import dev.metallurgists.rutile.api.material.registry.item.IMaterialItem;
import dev.metallurgists.rutile.api.material.registry.item.MaterialItem;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.registry.RutileFlagKeys;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

import static dev.metallurgists.rutile.client.RutileModels.simpleGeneratedModel;

public class IngotFlag extends ItemFlag implements IRecipeHandler {
    @Getter
    private boolean requiresCompacting = false;

    public IngotFlag(String existingNamespace) {
        super("%s_ingot", existingNamespace);
        this.setTagPatterns(List.of("c:ingots", "c:ingots/%s"));
    }

    public IngotFlag() {
        this("metallurgica");
    }

    public IngotFlag requiresCompacting() {
        this.requiresCompacting = true;
        return this;
    }

    @Override
    public ItemEntry<? extends IMaterialItem> registerItem(@NotNull Material material, IItemRegistry flag, @NotNull RutileRegistrate registrate) {
        return registrate
                .item(flag.getIdPattern().formatted(material.getName()), (p) -> new MaterialItem(p, material, flag))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .register();
    }

    @Override
    public void registerItemAssets(Material material) {
        boolean texturePresent = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/item/materials/" + material.getName() + "/ingot.png")).isPresent();
        String texture = texturePresent ? material.getNamespace() + ":item/materials/" + material.getName() + "/ingot" : "rutile:item/materials/null/ingot";
        RutileDynamicResourcePack.addItemModel(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), simpleGeneratedModel("minecraft:item/generated", texture));
    }

    @Override
    public FlagKey<?> getKey() {
        return RutileFlagKeys.INGOT;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }

    @Override
    public void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {

    }
}
