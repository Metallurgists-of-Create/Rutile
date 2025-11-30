package dev.metallurgists.rutile.api.material.builder;

import dev.metallurgists.rutile.api.material.flag.MultiFlagContainer;
import dev.metallurgists.rutile.api.material.registry.fluid.FluidFlagProperties;
import dev.metallurgists.rutile.api.registrate.builder.MaterialBuilder;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.NeoForge;

import java.util.*;

public class MaterialFlags {

    @Getter
    private Map<FlagSource<Fluid>, FluidFlagProperties> fluidFlagProperties = new HashMap<>();

    private MultiFlagContainer flagContainers = new MultiFlagContainer();

    public MaterialFlags() {
    }

    public <T> FlagContainer<T> getFlagContainer(FlagRegistryType<T> type) {
        return this.flagContainers.get(type);
    }

    public <T> MaterialFlags addContainer(MaterialBuilder.FlagsBuilder<T> flagsBuilder, ResourceLocation id) {
        this.flagContainers = this.flagContainers.add(modifyFlagsBuilder(flagsBuilder, id));
        return this;
    }

    private <T> FlagContainer<T> modifyFlagsBuilder(MaterialBuilder.FlagsBuilder<T> flagsBuilder, ResourceLocation id) {
        ModifyFlagBuilderEvent<T> event = NeoForge.EVENT_BUS.post(new ModifyFlagBuilderEvent<>(flagsBuilder, id));
        return event.build();
    }

    public <T> Collection<MaterialRegistryBuilder<T>> getFlagBuilders(FlagRegistryType<T> type) {
        return getFlagContainer(type).getBuilders().values();
    }

    public Collection<MaterialRegistryBuilder<?>> getFlagBuilders() {
        List<MaterialRegistryBuilder<?>> builders = new ArrayList<>();
        for (var c : getFlagContainers()) {
            builders.addAll(c.getBuilders().values());
        }
        return builders;
    }

    public Collection<FlagContainer<?>> getFlagContainers() {
        return this.flagContainers.values();
    }

    public <T> MaterialFlags addFlag(FlagSource<T> flagSource, ResourceLocation value) {
        this.flagContainers = this.flagContainers.computeIfPresent(flagSource.registryType(), (container) -> {
            container.add(flagSource, value);
        });
        return this;
    }

    public <T> MaterialFlags addFlag(FlagSource<T> flagSource, MaterialRegistryBuilder<T> builder) {
        this.flagContainers = this.flagContainers.computeIfPresent(flagSource.registryType(), (container) -> {
            container.add(flagSource, builder);
            container.add(flagSource, builder.getObjectId());
        });
        return this;
    }

    public MaterialFlags addFluidProperties(FluidFlagProperties properties) {
        FlagSource<Fluid> flagSource = properties.getFlagSource();
        if (fluidFlagProperties.containsKey(flagSource)) {
            throw new IllegalArgumentException("Fluid Flag " + flagSource.name() + " already registered!");
        }
        fluidFlagProperties.put(flagSource, properties);
        return this;
    }

    public FluidFlagProperties getPropertiesFor(FlagSource<Fluid> flagSource) {
        return fluidFlagProperties.get(flagSource);
    }
}
