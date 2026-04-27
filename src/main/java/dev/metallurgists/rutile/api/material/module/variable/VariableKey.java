package dev.metallurgists.rutile.api.material.module.variable;

import net.minecraft.resources.ResourceLocation;

public record VariableKey<T>(ResourceLocation key, Class<T> clazz, T defaultValue) { }
