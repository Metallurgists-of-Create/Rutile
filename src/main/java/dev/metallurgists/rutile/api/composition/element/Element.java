package dev.metallurgists.rutile.api.composition.element;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.IDisplayedName;
import lombok.Getter;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class Element implements IDisplayedName, ElementLike {
    public static final Element NULL = new Element("?", 0xffbf4cd2, Rutile.id("null"));

    private String descriptionId;

    @Getter
    private final ResourceLocation id;

    @Getter
    private final String symbol;

    @Getter
    private final int color;

    public Element(String symbol, int color, ResourceLocation id) {
        this.symbol = symbol;
        this.color = color;
        this.id = id;
    }

    /**
     * The internal name of this element.
     * This is used for registration, so it MUST be all lowercase with underscores for spaces
     * @return the internal name of this element
     */
    public String getName() {
        return this.getId().getPath();
    }

    /**
     * The modid of the mod that registered this element
     * @return the modid of this element
     */
    public String getModId() {
        return this.getId().getNamespace();
    }

    @Override
    public String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("element", getId());
        }
        return this.descriptionId;
    }

    @Override
    public Element asElement() {
        return this;
    }

    public boolean isNull() {
        return this == NULL;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Element other && this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public String toString() {
        return this.id.toString();
    }
}
