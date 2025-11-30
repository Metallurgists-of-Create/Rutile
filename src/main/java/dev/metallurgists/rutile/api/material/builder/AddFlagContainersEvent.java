package dev.metallurgists.rutile.api.material.builder;

import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AddFlagContainersEvent extends Event {

    private final List<FlagContainer<?>> flagContainers = new ArrayList<>();

    public AddFlagContainersEvent() {

    }

    public <R> void addFlagContainer(FlagContainer<R> flagContainer) {
        this.flagContainers.add(flagContainer);
    }

    @NotNull
    public List<FlagContainer<?>> getFlagContainers() {
        return flagContainers;
    }
}
