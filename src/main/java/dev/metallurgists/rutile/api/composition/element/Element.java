package dev.metallurgists.rutile.api.composition.element;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Accessors(chain = true, fluent = true)
public class Element {
    @NotNull @Getter
    private final String symbol;
    @Getter
    private final int color;
    @NotNull
    private final ResourceLocation resourceLocation;

    public Element(@NotNull String symbol, int color, @NotNull ResourceLocation resourceLocation) {
        this.symbol = symbol;
        this.color = color;
        this.resourceLocation = resourceLocation;
    }

    public ResourceLocation getId() {
        return resourceLocation;
    }

    public Component getDisplayName() {
        String key = getId().toLanguageKey("element");
        return Component.translatable(key);
    }

    public String getNamespace() {
        return getId().getNamespace();
    }

    public String getName() {
        return getId().getPath();
    }
}
