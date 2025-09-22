package dev.metallurgists.rutile.api.registrate.builder;

import com.google.common.base.Preconditions;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.*;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.ElementData;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;
import dev.metallurgists.rutile.api.material.flag.types.IFlagRegistry;
import dev.metallurgists.rutile.api.material.registry.fluid.FluidFlagProperties;
import dev.metallurgists.rutile.api.registry.CustomRutileRegistries;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.registry.RutileRegistries;
import dev.metallurgists.rutile.util.NonNullTriFunction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MaterialBuilder<T extends Material> {

    public static <T extends Material> MaterialBuilder<T> create(String name, NonNullTriFunction<Material.MaterialInfo, MaterialFlags, ResourceLocation, T> factory) {
        return new MaterialBuilder<>(name, factory);
    }

    public static <T extends Material> MaterialBuilder<T> create(ResourceLocation name, NonNullTriFunction<Material.MaterialInfo, MaterialFlags, ResourceLocation, T> factory) {
        return new MaterialBuilder<>(name.toString(), factory);
    }

    private final ResourceLocation id;
    private final NonNullTriFunction<Material.MaterialInfo, MaterialFlags, ResourceLocation, T> factory;

    private NonNullSupplier<Material.MaterialInfo> initialInfo = Material.MaterialInfo::new;
    private NonNullFunction<Material.MaterialInfo, Material.MaterialInfo> infoCallback = NonNullUnaryOperator.identity();

    private NonNullSupplier<MaterialFlags> initialFlags = MaterialFlags::new;
    private NonNullFunction<MaterialFlags, MaterialFlags> flagsCallback = NonNullUnaryOperator.identity();


    protected MaterialBuilder(String name, NonNullTriFunction<Material.MaterialInfo, MaterialFlags, ResourceLocation, T> factory) {
        this.id = Rutile.id(name);
        this.factory = factory;
    }

    public MaterialBuilder<T> info(NonNullUnaryOperator<Material.MaterialInfo> func) {
        infoCallback = infoCallback.andThen(func);
        return this;
    }

    public MaterialBuilder<T> initialInfo(NonNullSupplier<Material.MaterialInfo> info) {
        initialInfo = info;
        return this;
    }

    public MaterialBuilder<T> flags(NonNullUnaryOperator<MaterialFlags> func) {
        flagsCallback = flagsCallback.andThen(func);
        return this;
    }

    public MaterialBuilder<T> initialFlags(NonNullSupplier<MaterialFlags> flags) {
        initialFlags = flags;
        return this;
    }

    public MaterialBuilder<T> meltingPoint(double temp) {
        infoCallback.andThen(i -> i.meltingPoint(temp));
        return this;
    }

    public MaterialBuilder<T> element(String element) {
        return composition("%s %s".formatted(1, element));
    }

    public MaterialBuilder<T> composition(String... components) {
        List<ElementData> elementDataList = new ArrayList<>();
        for (String raw : components) {
            String[] split = raw.split(" ", 2);
            int amount = Integer.parseInt(split[0]) <= 0 ? 1 : Integer.parseInt(split[0]);
            String element = split[1];
            if (element.isEmpty()) throw new IllegalArgumentException("Element is invalid or empty");
            ResourceKey<Element> elementKey = ResourceKey.create(CustomRutileRegistries.ELEMENT_REGISTRY, Rutile.id(element));
            ElementData elementData = new ElementData(CustomRutileRegistries.ELEMENTS.get(elementKey), amount);
            elementDataList.add(elementData);
        }
        ElementData.createFromList(elementDataList).forEach(e -> infoCallback.andThen(i -> i.composition().add(e)));
        return this;
    }

    public MaterialBuilder<T> addFlags(IMaterialFlag... flags) {
        for (var flag : flags) {
            if (flag instanceof IFlagRegistry reg && !Objects.equals(reg.getExistingNamespace(), "")) {
                flagsCallback.andThen(f -> f.noRegister(flag.getKey()));
            }
            flagsCallback.andThen(f -> f.setFlag(flag.getKey(), flag));
        }
        return this;
    }

    @SafeVarargs
    public final MaterialBuilder<T> noRegister(FlagKey<? extends IMaterialFlag>... matFlags) {
        flagsCallback.andThen(f -> f.noRegister(matFlags));
        return this;
    }

    public MaterialBuilder<T> existingIds(Object... components) {
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

    public MaterialBuilder<T> nameAlternatives(Object... components) {
        Preconditions.checkArgument(
                components.length % 2 == 0,
                "Material Name Alternatives list malformed!");
        for (int i = 0; i < components.length; i += 2) {
            if (components[i] == null || components[i + 1] == null) {
                throw new IllegalArgumentException(
                        "Name Alternative in Name Alternatives List is null");
            }
            FlagKey<?> key = (FlagKey<?>) components[i];
            String name = (String) components[i + 1];
            infoCallback.andThen(l -> l.withNameAlternative(key, name));
        }
        return this;
    }

    public MaterialBuilder<T> fluidProperty(FluidFlagProperties... properties) {
        Arrays.asList(properties).forEach(p -> flagsCallback.andThen(f -> f.addFluidProperties(p)));
        return this;
    }

    public MaterialBuilder<T> fluidProperty(FluidFlagProperties.Builder... properties) {
        Arrays.asList(properties).forEach(p -> flagsCallback.andThen(f -> f.addFluidProperties(p.build())));
        return this;
    }

    public @NonnullType T createAndRegister() {
        Material.MaterialInfo info = this.initialInfo.get();
        info = infoCallback.apply(info);
        MaterialFlags flags = this.initialFlags.get();
        flags = flagsCallback.apply(flags);
        T material = factory.apply(info, flags, id);
        RutileAPI.materialRegistry.register(material);
        return material;
    }
}
