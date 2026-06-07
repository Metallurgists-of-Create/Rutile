package dev.metallurgists.rutile.api.material.part;

import com.tterrag.registrate.builders.AbstractBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public abstract class Part<T> {

    abstract <I extends T, P, S extends AbstractBuilder<T, I, P, S>> AbstractBuilder<T, I, P, S>  getBuilder();

    abstract ResourceKey<Registry<T>> registryResourceKey();
}
