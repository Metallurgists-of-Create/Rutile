package dev.metallurgists.rutile.api.material.base;

import com.google.common.base.Preconditions;
import dev.metallurgists.rutile.api.IHasDescriptionId;
import dev.metallurgists.rutile.api.composition.ElementData;
import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;
import dev.metallurgists.rutile.api.material.flag.types.IFlagRegistry;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.registry.RutileElements;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Accessors(chain = true, fluent = true)
public class Material implements Comparable<Material>, IHasDescriptionId {
    private String descriptionId;

    @NotNull
    @Getter
    private final MaterialInfo materialInfo;

    @NotNull
    private final MaterialFlags flags;

    public Material(@NotNull MaterialInfo materialInfo, @NotNull MaterialFlags flags) {
        this.materialInfo = materialInfo;
        this.flags = flags;
        this.flags.setMaterial(this);
        verifyMaterial();
    }

    protected void registerMaterial() {
        RutileAPI.materialManager.getRegistry(getNamespace()).register(this);
    }

    public String getName() {
        return materialInfo.resourceLocation.getPath();
    }

    public String getNamespace() {
        return materialInfo.resourceLocation.getNamespace();
    }

    public ResourceLocation getId() {
        return materialInfo.resourceLocation;
    }

    public ResourceLocation asResource(String path) {
        return new ResourceLocation(getNamespace(), path);
    }

    public String asResourceString(String path) {
        return asResource(path).toString().replace(":", "/");
    }

    public boolean noRegister(FlagKey<? extends IMaterialFlag> flag) {
        if (!shouldRegister()) {
            return true;
        }
        return flags.getNoRegister().contains(flag);
    }

    public boolean shouldRegister() {
        return true;
    }

    public List<SubComposition> getComposition() {
        if (materialInfo.composition.isEmpty()) {
            return List.of(new SubComposition(List.of(new ElementData(RutileElements.NULL.getId(), 1)), 1));
        }
        return materialInfo.composition;
    }

    public <T extends IMaterialFlag> boolean hasFlag(FlagKey<T> key) {
        return getFlag(key) != null;
    }

    public <T extends IMaterialFlag> T getFlag(FlagKey<T> key) {
        return flags.getFlag(key);
    }

    public <T extends IMaterialFlag> void setFlag(FlagKey<T> key, IMaterialFlag flag) {
        if (!RutileAPI.materialManager.canModifyMaterials()) {
            throw new IllegalStateException("Cannot add flags to a Material when registry is frozen!");
        }
        if (flag instanceof IFlagRegistry reg && !Objects.equals(reg.getExistingNamespace(), "")) {
            flags.noRegister(key);
        }
        flags.setFlag(key, flag);
        flags.verify();
    }

    public <T extends IMaterialFlag> void setNameAlternative(FlagKey<T> key, String alternativeName) {
        if (!RutileAPI.materialManager.canModifyMaterials()) {
            throw new IllegalStateException("Cannot add name alternatives to a Material when registry is frozen!");
        }
        materialInfo.nameAlternatives.put(key, alternativeName);
    }

    public <T extends IMaterialFlag> void setExistingId(FlagKey<T> key, String existingId) {
        if (!RutileAPI.materialManager.canModifyMaterials()) {
            throw new IllegalStateException("Cannot add existing ids to a Material when registry is frozen!");
        }
        materialInfo.existingIds.put(key, ResourceLocation.tryParse(existingId));
    }

    public void setMeltingPoint(double temperature) {
        if (!RutileAPI.materialManager.canModifyMaterials()) {
            throw new IllegalStateException("Cannot modify a Material's melting point when registry is frozen!");
        }
        materialInfo.meltingPoint(temperature);
    }

    public void setColor(int color) {
        if (!RutileAPI.materialManager.canModifyMaterials()) {
            throw new IllegalStateException("Cannot modify a Material's color when registry is frozen!");
        }
        materialInfo.withColour(color);
    }

    public @NotNull MaterialFlags getFlags() {
        return flags;
    }

    public void verifyMaterial() {
        flags.verify();
    }

    @Override
    public int compareTo(@NotNull Material material) {
        return toString().compareTo(material.toString());
    }

    public String getUnlocalizedName() {
        return materialInfo.resourceLocation.toLanguageKey("material");
    }

    @Override
    public String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("material", getId());
        }

        return this.descriptionId;
    }

    public static class Builder {
        private MaterialInfo materialInfo;
        private MaterialFlags flags;
        private final List<ElementData> composition = new ArrayList<>();

        public Builder(ResourceLocation resourceLocation) {
            String name = resourceLocation.getPath();
            if (name.charAt(name.length() - 1) == '_')
                throw new IllegalArgumentException("Material name cannot end with a '_'!");
            materialInfo = new MaterialInfo(resourceLocation);
            flags = new MaterialFlags();
        }

        public Builder colour(int rgb) {
            materialInfo.withColour(rgb);
            return this;
        }

        public Builder meltingPoint(double meltingPoint) {
            materialInfo.meltingPoint(meltingPoint);
            return this;
        }

        @SafeVarargs
        public final Builder noRegister(FlagKey<? extends IMaterialFlag>... matFlags) {
            flags.noRegister(matFlags);
            return this;
        }

        public Builder existingIds(Object... components) {
            Preconditions.checkArgument(
                    components.length % 2 == 0,
                    "Material Existing Ids list malformed!");

            for (int i = 0; i < components.length; i += 2) {
                if (components[i] == null || components[i + 1] == null) {
                    throw new IllegalArgumentException(
                            "Existing Id in Existing Ids List is null for Material " + this.materialInfo.resourceLocation);
                }
                materialInfo.withExistingId((FlagKey<?>) components[i], (String) components[i + 1]);
                flags.noRegister((FlagKey<?>) components[i]);
            }
            return this;
        }

        public Builder nameAlternatives(Object... components) {
            Preconditions.checkArgument(
                    components.length % 2 == 0,
                    "Material Name Alternatives list malformed!");

            for (int i = 0; i < components.length; i += 2) {
                if (components[i] == null || components[i + 1] == null) {
                    throw new IllegalArgumentException(
                            "Name Alternative in Name Alternatives List is null for Material " + this.materialInfo.resourceLocation);
                }
                materialInfo.nameAlternatives().put((FlagKey<?>) components[i], (String) components[i + 1]);
            }
            return this;
        }

        public Builder element(Element entry) {
            ElementData elementData = new ElementData(entry.getId(), 1);
            materialInfo.composition().add(new SubComposition(List.of(elementData), 1));
            return this;
        }

        public Builder composition(Object... components) {
            Preconditions.checkArgument(
                    components.length % 2 == 0,
                    "Material Composition list malformed!");
            List<ElementData> elementDataList = new ArrayList<>();
            for (int i = 0; i < components.length; i += 2) {
                if (components[i] == null) {
                    throw new IllegalArgumentException(
                            "ElementData in Compositions List is null for Material " + this.materialInfo.resourceLocation);
                }
                ElementData elementData = new ElementData(components[i] instanceof Element entry ? entry.getId() : ((Element) components[i]).getId(), ((Number) components[i + 1]).intValue());
                elementDataList.add(elementData);
            }
            ElementData.createFromList(elementDataList).forEach(materialInfo.composition()::add);
            return this;
        }

        public Builder alloyComposition(Object... components) {
            Preconditions.checkArgument(
                    components.length % 2 == 0,
                    "Material Composition list malformed!");
            for (int i = 0; i < components.length; i += 2) {
                if (components[i] == null) {
                    throw new IllegalArgumentException(
                            "ElementData in Compositions List is null for Material " + this.materialInfo.resourceLocation);
                }
                Material material = ((Material) components[i]);
                int amount = ((Number) components[i + 1]).intValue();
                List<SubComposition> materialComp = material.getComposition();
                List<ElementData> elementDataList = new ArrayList<>();
                for (SubComposition subComposition : materialComp) {
                    elementDataList.addAll(subComposition.getElements());
                }
                materialInfo.composition().add(new SubComposition(elementDataList, amount));
            }
            return this;
        }

        public Builder addFlags(IMaterialFlag... flags) {
            for (var flag : flags) {
                if (flag instanceof IFlagRegistry reg && !Objects.equals(reg.getExistingNamespace(), "")) {
                    this.flags.noRegister(flag.getKey());
                }
                this.flags.setFlag(flag.getKey(), flag);
            }
            return this;
        }

        public Material buildAndRegister() {
            if (materialInfo.colour() == 0) materialInfo.withColour(0xFFFFFF);
            var mat = new Material(materialInfo, flags);
            mat.registerMaterial();
            return mat;
        }

        public Material register() {
            return buildAndRegister();
        }
    }

    @Accessors(chain = true)
    public static class MaterialInfo {
        public final ResourceLocation resourceLocation;
        @Getter
        public Map<FlagKey<?>, String> nameAlternatives = new HashMap<>();
        @Getter
        public Map<FlagKey<?>, ResourceLocation> existingIds = new HashMap<>();
        @Getter
        public List<SubComposition> composition = new ArrayList<>();
        @Getter
        private int colour;
        @Getter
        private double meltingPoint;

        private MaterialInfo(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public MaterialInfo withNameAlternative(FlagKey<?> flag, String alternative) {
            nameAlternatives.put(flag, alternative);
            return this;
        }

        public MaterialInfo withExistingId(FlagKey<?> flag, ResourceLocation id) {
            existingIds.put(flag, id);
            return this;
        }
        public MaterialInfo withExistingId(FlagKey<?> flag, String id) {
            return withExistingId(flag, new ResourceLocation(id));
        }

        public MaterialInfo withColour(int rgb) {
            colour = rgb;
            return this;
        }

        public MaterialInfo meltingPoint(double mp) {
            this.meltingPoint = mp;
            return this;
        }


        private void verifyInfo(MaterialFlags flags) {
            // no-op
        }
    }
}
