package dev.metallurgists.rutile.api.material.builder;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.types.MultiTagHolder;
import dev.metallurgists.rutile.api.material.flag.types.TagHolder;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public abstract class MaterialRegistryBuilder<T> {
    private final ResourceKey<Registry<T>> registryKey;

    private final String nameFormat;

    private ResourceLocation materialKey;

    protected MaterialRegistryBuilder(ResourceKey<Registry<T>> registryKey, String nameFormat) {
        this.registryKey = registryKey;
        this.nameFormat = nameFormat;
    }

    @ApiStatus.Internal
    public MaterialRegistryBuilder<T> setMaterialKey(ResourceLocation materialKey) {
        this.materialKey = materialKey;
        return this;
    }

    /**
     * Gets the ID the object would have if it were registered under this builder's provided material.
     * @apiNote DO NOT call this unless you have already given the builder a materialKey
     * @return the ID the object would have
     */
    public ResourceLocation getObjectId() {
        if (materialKey == null) throw new IllegalStateException("No material key provided");
        return materialKey.withPath(nameFormat().formatted(materialKey.getPath()));
    }

    public ResourceLocation getKey() {
        return getBuilderName().withPrefix(this.registryKey.registry().getPath()+"/");
    }

    public abstract FlagSource<T> getFlagSource();

    public void registerRecipes(@NotNull RecipeOutput output, @NotNull Material material) {

    }

    public TagHolder<T> createTagHolder() {
        return new TagHolder<>(this.registryKey);
    }

    public <T1> TagHolder<T1> createTagHolder(ResourceKey<Registry<T1>> registryKey) {
        return new TagHolder<>(registryKey);
    }

    public abstract void registerAssets(Material material);

    public abstract MultiTagHolder getTagHolder();

    public String nameFormat() {
        return this.nameFormat;
    }

    public abstract ResourceLocation getBuilderName();

    public abstract MaterialRegistryBuilder<T> getBuilder();

    public final RegistryEntry<T, ? extends T> register(@NotNull AbstractRegistrate<?> registrate, @NotNull Material material) {
        return build(registrate, material);
    }

    public abstract RegistryEntry<T, ? extends T>  build(@NotNull AbstractRegistrate<?> registrate, @NotNull Material material);
}
