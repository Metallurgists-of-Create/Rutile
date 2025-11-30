package dev.metallurgists.rutile.api.material.builder;

import dev.metallurgists.rutile.api.registrate.builder.MaterialBuilder;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;

import java.util.function.Consumer;

@Getter
public class ModifyFlagBuilderEvent<T> extends Event {
    private final MaterialBuilder.FlagsBuilder<T> flagsBuilder;
    private final ResourceLocation materialKey;

    public ModifyFlagBuilderEvent(MaterialBuilder.FlagsBuilder<T> flagsBuilder, ResourceLocation materialKey) {
        this.flagsBuilder = flagsBuilder;
        this.materialKey = materialKey;
    }

    public void modify(Consumer<MaterialBuilder.FlagsBuilder<T>> consumer) {
        consumer.accept(flagsBuilder);
    }

    public FlagContainer<T> build() {
        return flagsBuilder.build(materialKey);
    }
}
