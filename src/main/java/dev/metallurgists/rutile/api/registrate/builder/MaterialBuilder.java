package dev.metallurgists.rutile.api.registrate.builder;

import com.google.common.base.Preconditions;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.nullness.*;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.ElementData;
import dev.metallurgists.rutile.api.composition.element.ElementStack;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.builder.FlagContainer;
import dev.metallurgists.rutile.api.material.builder.FlagSource;
import dev.metallurgists.rutile.api.material.builder.MaterialFlags;
import dev.metallurgists.rutile.api.material.builder.MaterialRegistryBuilder;
import dev.metallurgists.rutile.api.material.component.MaterialComponentType;
import dev.metallurgists.rutile.api.material.registry.fluid.FluidFlagProperties;
import dev.metallurgists.rutile.util.NonNullTriFunction;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

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

    public <C> MaterialBuilder<T> component(MaterialComponentType<C> type, C value) {
        infoCallback.andThen(i -> i.component(type, value));
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
            ResourceLocation elementKey = Rutile.id(element);
            ElementData elementData = new ElementData(ElementStack.of(elementKey), amount);
            elementDataList.add(elementData);
        }
        ElementData.createFromList(elementDataList).forEach(e -> infoCallback.andThen(i -> i.composition().add(e)));
        return this;
    }

    @SafeVarargs
    public final <R> MaterialBuilder<T> addBuilders(MaterialRegistryBuilder<R>... builders) {
        for (var builder : builders) {
            flagsCallback.andThen(f -> f.addFlag(builder.getFlagSource(), builder.setMaterialKey(id)));
        }
        return this;
    }

    public final <R> MaterialBuilder<T> addFlags(FlagsBuilder<R> flagsBuilder) {
        flagsCallback.andThen(f -> f.addContainer(flagsBuilder, id));
        return this;
    }

    public MaterialBuilder<T> existingIds(Object... o) {
        Preconditions.checkArgument(
                o.length % 2 == 0,
                "Material Existing Ids list malformed!");

        for (int i = 0; i < o.length; i += 2) {
            if (o[i] == null || o[i + 1] == null) {
                throw new IllegalArgumentException(
                        "Existing Id in Existing Ids List is null");
            }
            FlagSource<?> key = (FlagSource<?>) o[i];
            String id = (String) o[i + 1];
            flagsCallback.andThen(l -> l.addFlag(key, ResourceLocation.parse(id)));
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

    public @NonnullType T create() {
        Material.MaterialInfo info = this.initialInfo.get();
        info = infoCallback.apply(info);
        MaterialFlags flags = this.initialFlags.get();
        flags = flagsCallback.apply(flags);
        return factory.apply(info, flags, id);
    }

    public static <T> FlagsBuilder<T> flagsBuilder(FlagContainer<T> flagContainer) {
        return new FlagsBuilder<>(flagContainer);
    }

    public static class FlagsBuilder<T> {
        private final FlagContainer<T> flagContainer;

        private final List<MaterialRegistryBuilder<?>> flagBuilders;
        private final Map<FlagSource<?>, ResourceLocation> flags;

        public FlagsBuilder(FlagContainer<T> flagContainer) {
            this.flagContainer = flagContainer;
            this.flagBuilders = new ArrayList<>();
            this.flags = new HashMap<>();
        }

        public FlagsBuilder<T> addFlag(FlagSource<T> source, ResourceLocation id) {
            flags.put(source, id);
            return this;
        }

        public FlagsBuilder<T> addFlag(FlagSource<T> source, String id) {
            flags.put(source, ResourceLocation.parse(id));
            return this;
        }

        public FlagsBuilder<T> addFlag(MaterialRegistryBuilder<T> builder) {
            flagBuilders.add(builder);
            return this;
        }

        @SuppressWarnings("unchecked")
        public FlagContainer<T> build(ResourceLocation id) {
            FlagContainer<T> flagContainer = this.flagContainer;
            flagBuilders.forEach((builder) -> {
                builder = builder.setMaterialKey(id);
                flagContainer.add((FlagSource<T>)builder.getFlagSource(), (MaterialRegistryBuilder<T>)builder);
                flagContainer.add((FlagSource<T>)builder.getFlagSource(), builder.getObjectId());
            });
            flags.forEach((source, key) -> {
                flagContainer.add((FlagSource<T>)source, key);
            });
            return flagContainer;
        }
    }
}
