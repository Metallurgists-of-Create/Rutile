package dev.metallurgists.rutile.api.material.base;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import dev.metallurgists.rutile.api.IHasDescriptionId;
import dev.metallurgists.rutile.api.composition.ElementData;
import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.material.builder.*;
import dev.metallurgists.rutile.api.material.component.*;
import dev.metallurgists.rutile.registry.RutileElements;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.Util;
import net.minecraft.core.component.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.internal.RegistrationEvents;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

@Accessors(chain = true, fluent = true)
public class Material implements Comparable<Material>, IHasDescriptionId, MaterialLike {
    private String descriptionId;

    @NotNull
    @Getter
    private final MaterialInfo materialInfo;

    @NotNull
    private final MaterialFlags flags;

    private MaterialComponentMap components;

    @NotNull
    private final ResourceLocation resourceLocation;

    public Material(@NotNull MaterialInfo materialInfo, @NotNull MaterialFlags flags, @NotNull ResourceLocation resourceLocation) {
        this.materialInfo = materialInfo;
        this.flags = flags;
        this.components = materialInfo.buildAndValidateComponents();
        this.resourceLocation = resourceLocation;
        verifyMaterial();
    }

    public <T> FlagContainer<T> getFlagContainer(FlagRegistryType<T> type) {
        return getFlags().getFlagContainer(type);
    }

    public String getName() {
        return getId().getPath();
    }

    public String getNamespace() {
        return getId().getNamespace();
    }

    public ResourceLocation getId() {
        return resourceLocation;
    }

    public ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(getNamespace(), path);
    }

    public String asResourceString(String path) {
        return asResource(path).toString().replace(":", "/");
    }

    public boolean shouldRegister() {
        return true;
    }

    public List<SubComposition> getComposition() {
        if (materialInfo.composition.isEmpty()) {
            return List.of(new SubComposition(List.of(ElementData.create(RutileElements.NULL)), 1));
        }
        return materialInfo.composition;
    }

    public <T> boolean hasFlag(FlagSource<T> flagSource) {
        return flags.getFlagContainer(flagSource.registryType()).getObjects().containsKey(flagSource);
    }

    public <T> ResourceLocation getFlag(FlagSource<T> flagSource) {
        return flags.getFlagContainer(flagSource.registryType()).get(flagSource);
    }

    public void setMeltingPoint(double temperature) {
        materialInfo.meltingPoint(temperature);
    }

    public void setColor(int color) {
        materialInfo.withColour(color);
    }

    public @NotNull MaterialFlags getFlags() {
        return flags;
    }

    public void verifyMaterial() {

    }

    @Override
    public int compareTo(@NotNull Material material) {
        return toString().compareTo(material.toString());
    }

    @Override
    public String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("material", getId());
        }
        return this.descriptionId;
    }

    @Override
    public Material asMaterial() {
        return this;
    }

    public MaterialComponentMap components() {
        return this.components;
    }

    public void modifyDefaultComponentsFrom(MaterialComponentPatch patch) {
        if (!MaterialRegistrationEvents.canModifyComponents()) {
            throw new IllegalStateException("Default components cannot be modified now!");
        } else {
            MaterialComponentMap.Builder builder = MaterialComponentMap.builder().addAll(this.components);
            patch.entrySet().forEach((entry) -> builder.set((MaterialComponentType)entry.getKey(), (entry.getValue()).orElse(null)));
            this.components = MaterialInfo.COMPONENT_INTERNER.intern(MaterialInfo.validateComponents(builder.build()));
        }
    }

    @Accessors(chain = true)
    public static class MaterialInfo {
        private static final Interner<MaterialComponentMap> COMPONENT_INTERNER = Interners.newStrongInterner();
        @Getter
        public List<SubComposition> composition = new ArrayList<>();
        @Getter
        private int colour;
        @Nullable
        private MaterialComponentMap.Builder components;
        @Getter
        private double meltingPoint;

        public MaterialInfo() {

        }

        public MaterialInfo withColour(int rgb) {
            colour = rgb;
            return this;
        }

        public MaterialInfo meltingPoint(double mp) {
            this.meltingPoint = mp;
            return this;
        }

        public <T> MaterialInfo component(MaterialComponentType<T> component, T value) {
            if (this.components == null) {
                this.components = MaterialComponentMap.builder();
            }

            this.components.set(component, value);
            return this;
        }

        MaterialComponentMap buildAndValidateComponents() {
            MaterialComponentMap componentMap = this.buildComponents();
            return validateComponents(componentMap);
        }

        public static MaterialComponentMap validateComponents(MaterialComponentMap componentMap) {
            if (false) {
                throw new IllegalStateException("Item cannot have both durability and be stackable");
            } else {
                return componentMap;
            }
        }

        private MaterialComponentMap buildComponents() {
            return this.components == null ? MaterialComponentMap.EMPTY : COMPONENT_INTERNER.intern(this.components.build());
        }

        private void verifyInfo(MaterialFlags flags) {
            // no-op
        }
    }
}
