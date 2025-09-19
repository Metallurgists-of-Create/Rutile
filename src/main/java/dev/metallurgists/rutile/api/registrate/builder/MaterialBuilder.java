package dev.metallurgists.rutile.api.registrate.builder;

import com.google.common.base.Preconditions;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.*;
import dev.metallurgists.rutile.api.composition.ElementData;
import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;
import dev.metallurgists.rutile.api.material.flag.types.IFlagRegistry;
import dev.metallurgists.rutile.registry.RutileRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MaterialBuilder<T extends Material, P> extends AbstractBuilder<Material, T, P, MaterialBuilder<T, P>> {

    public static <T extends Material, P> MaterialBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullBiFunction<Material.MaterialInfo, MaterialFlags, T> factory) {
        return new MaterialBuilder<>(owner, parent, name, callback, factory).defaultLang();
    }

    private final NonNullBiFunction<Material.MaterialInfo, MaterialFlags, T> factory;

    private NonNullSupplier<Material.MaterialInfo> initialInfo = Material.MaterialInfo::new;
    private NonNullFunction<Material.MaterialInfo, Material.MaterialInfo> infoCallback = NonNullUnaryOperator.identity();

    private NonNullSupplier<MaterialFlags> initialFlags = MaterialFlags::new;
    private NonNullFunction<MaterialFlags, MaterialFlags> flagsCallback = NonNullUnaryOperator.identity();


    protected MaterialBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullBiFunction<Material.MaterialInfo, MaterialFlags, T> factory) {
        super(owner, parent, name, callback, RutileRegistries.MATERIAL);
        this.factory = factory;
    }

    public MaterialBuilder<T, P> info(NonNullUnaryOperator<Material.MaterialInfo> func) {
        infoCallback = infoCallback.andThen(func);
        return this;
    }

    public MaterialBuilder<T, P> initialInfo(NonNullSupplier<Material.MaterialInfo> info) {
        initialInfo = info;
        return this;
    }

    public MaterialBuilder<T, P> flags(NonNullUnaryOperator<MaterialFlags> func) {
        flagsCallback = flagsCallback.andThen(func);
        return this;
    }

    public MaterialBuilder<T, P> initialFlags(NonNullSupplier<MaterialFlags> flags) {
        initialFlags = flags;
        return this;
    }

    public MaterialBuilder<T, P> meltingPoint(double temp) {
        infoCallback.andThen(i -> i.meltingPoint(temp));
        return this;
    }

    public MaterialBuilder<T, P> element(Element element) {
        ElementData elementData = new ElementData(element.getId(), 1);
        infoCallback.andThen(i -> i.composition().add(new SubComposition(List.of(elementData), 1)));
        return this;
    }

    public MaterialBuilder<T, P> element(ElementEntry<? extends Element> element) {
        ElementData elementData = new ElementData(element.get().getId(), 1);
        infoCallback.andThen(i -> i.composition().add(new SubComposition(List.of(elementData), 1)));
        return this;
    }

    public MaterialBuilder<T, P> composition(Object... components) {
        Preconditions.checkArgument(
                components.length % 2 == 0,
                "Material Composition list malformed!");
        List<ElementData> elementDataList = new ArrayList<>();
        for (int i = 0; i < components.length; i += 2) {
            if (components[i] == null) {
                throw new IllegalArgumentException(
                        "ElementData in Compositions List is null");
            }
            ResourceLocation element = components[i] instanceof Element entry ? entry.getId() : components[i] instanceof ElementEntry<? extends Element> entry ? entry.get().getId() : ((Element) components[i]).getId();
            ElementData elementData = new ElementData(element, ((Number) components[i + 1]).intValue());
            elementDataList.add(elementData);
        }
        ElementData.createFromList(elementDataList).forEach(sc -> infoCallback.andThen(i -> i.composition().add(sc)));
        return this;
    }

    public MaterialBuilder<T, P> addFlags(IMaterialFlag... flags) {
        for (var flag : flags) {
            if (flag instanceof IFlagRegistry reg && !Objects.equals(reg.getExistingNamespace(), "")) {
                flagsCallback.andThen(f -> f.noRegister(flag.getKey()));
            }
            flagsCallback.andThen(f -> f.setFlag(flag.getKey(), flag));
        }
        return this;
    }

    public MaterialBuilder<T, P> existingIds(Object... components) {
        Preconditions.checkArgument(
                components.length % 2 == 0,
                "Material Existing Ids list malformed!");

        for (int i = 0; i < components.length; i += 2) {
            if (components[i] == null || components[i + 1] == null) {
                throw new IllegalArgumentException(
                        "Existing Id in Existing Ids List is null");
            }
            FlagKey<?> key = (FlagKey<?>) components[i];
            String id = (String) components[i + 1];
            infoCallback.andThen(l -> l.withExistingId(key, id));
            flagsCallback.andThen(f -> f.noRegister(key));
        }
        return this;
    }

    public MaterialBuilder<T, P> defaultLang() {
        return lang(Material::getDescriptionId);
    }

    public MaterialBuilder<T, P> lang(String name) {
        return lang(Material::getDescriptionId, name);
    }

    @Override
    protected @NonnullType T createEntry() {
        Material.MaterialInfo info = this.initialInfo.get();
        info = infoCallback.apply(info);
        MaterialFlags flags = this.initialFlags.get();
        flags = flagsCallback.apply(flags);
        return factory.apply(info, flags);
    }

    @Override
    protected RegistryEntry<Material, T> createEntryWrapper(@NotNull DeferredHolder<Material, T> delegate) {
        return new MaterialEntry<>(getOwner(), delegate);
    }

    @Override
    public MaterialEntry<T> register() {
        return (MaterialEntry<T>) super.register();
    }
}
