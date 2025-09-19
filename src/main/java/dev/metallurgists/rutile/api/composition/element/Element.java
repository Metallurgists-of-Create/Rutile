package dev.metallurgists.rutile.api.composition.element;

import com.tterrag.registrate.AbstractRegistrate;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.IHasDescriptionId;
import dev.metallurgists.rutile.registry.RutileRegistries;
import lombok.Getter;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

@Getter
public class Element implements IHasDescriptionId {
    private String descriptionId;

    private final String symbol;
    private final int color;

    public Element(Properties properties) {
        this.symbol = properties.symbol;
        this.color = properties.color;
    }

    public String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("element", RutileRegistries.ELEMENT_REGISTRY.getKey(this));
        }

        return this.descriptionId;
    }

    public ResourceLocation getId() {
        return RutileRegistries.ELEMENT_REGISTRY.getKey(this);
    }

    public static class Properties {
        private String symbol = "?";
        private int color = 0x818181; // Default color

        public Properties() {
        }

        public Properties symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Properties color(int color) {
            this.color = color;
            return this;
        }
    }
}
