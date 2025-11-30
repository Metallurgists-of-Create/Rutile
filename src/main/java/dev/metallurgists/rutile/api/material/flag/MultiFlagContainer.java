package dev.metallurgists.rutile.api.material.flag;

import dev.metallurgists.rutile.api.material.builder.FlagContainer;
import dev.metallurgists.rutile.api.material.builder.FlagRegistryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class MultiFlagContainer implements Iterable<FlagContainer<?>> {

    protected List<ContainerEntry<?>> containers = new ArrayList<>();

    public MultiFlagContainer() {}

    public <T> MultiFlagContainer(FlagContainer<T> container) {
        containers.add(MultiFlagContainer.ContainerEntry.create(container.getRegistryType(), container));
    }

    public <T> MultiFlagContainer set(FlagContainer<T> value) {
        return update(value.getRegistryType(), value);
    }

    public <T> MultiFlagContainer add(FlagContainer<T> container) {
        var registryType = container.getRegistryType();
        if (containers.stream().anyMatch(c->c.test(registryType)))
            throw new IllegalStateException("Flag container already exists for " + registryType.resourceKey().location());
        containers.add(MultiFlagContainer.ContainerEntry.create(registryType, container));
        return this;
    }

    private <T> MultiFlagContainer update(FlagRegistryType<T> registryType, FlagContainer<T> container) {
        for (var c : containers) {
            if (c.test(registryType))
                containers.remove(c);
            containers.add(MultiFlagContainer.ContainerEntry.create(registryType, container));
        }
        return this;
    }

    public <T> MultiFlagContainer computeIfPresent(FlagRegistryType<T> registryType, Consumer<FlagContainer<T>> function) {
        FlagContainer<T> flagContainer = get(registryType);
        if (flagContainer != null) {
            function.accept(flagContainer);
            update(registryType, flagContainer);
        }
        return this;
    }

    public MultiFlagContainer addAll(List<FlagContainer<?>> containers) {
        containers.forEach(this::add);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> FlagContainer<T> get(FlagRegistryType<T> registryType) {
        return (FlagContainer<T>) containers.stream().filter(ent -> ent.test(registryType)).map(MultiFlagContainer.ContainerEntry::container).findFirst().orElse(null);
    }

    public <T> FlagContainer<T> getOrDefault(FlagRegistryType<T> registryType, FlagContainer<T> defaultContainer) {
        FlagContainer<T> flagContainer = get(registryType);
        if (flagContainer != null) {
            return flagContainer;
        }
        return defaultContainer;
    }

    public <T> boolean has(FlagRegistryType<T> registryType) {
        return !containers.stream().filter(ent -> ent.test(registryType)).toList().isEmpty();
    }

    @Override
    public @NotNull Iterator<FlagContainer<?>> iterator() {
        return values().iterator();
    }

    public Collection<FlagContainer<?>> values() {
        List<FlagContainer<?>> list = new ArrayList<>();
        this.containers.forEach(e->list.add(e.container()));
        return list;
    }


    protected record ContainerEntry<T>(FlagRegistryType<T> registryType, FlagContainer<T> container) {
        static <L> ContainerEntry<L> create(FlagRegistryType<L> registryType, FlagContainer<L> container) {
            return new ContainerEntry<>(registryType, container);
        }

        <L> boolean test(FlagRegistryType<L> registryType) {
            return registryType.equals(this.registryType);
        }
    }
}
