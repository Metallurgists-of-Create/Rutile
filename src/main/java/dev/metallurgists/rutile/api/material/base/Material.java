package dev.metallurgists.rutile.api.material.base;

import dev.metallurgists.rutile.api.IHasDescriptionId;
import dev.metallurgists.rutile.api.composition.ElementData;
import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;
import dev.metallurgists.rutile.api.material.flag.types.IFlagRegistry;
import dev.metallurgists.rutile.registry.RutileElements;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Accessors(chain = true, fluent = true)
public class Material implements Comparable<Material>, IHasDescriptionId, MaterialLike {
    private String descriptionId;

    @NotNull
    @Getter
    private final MaterialInfo materialInfo;

    @NotNull
    private final MaterialFlags flags;

    @NotNull
    private final ResourceLocation resourceLocation;

    public Material(@NotNull MaterialInfo materialInfo, @NotNull MaterialFlags flags, @NotNull ResourceLocation resourceLocation) {
        this.materialInfo = materialInfo;
        this.flags = flags;
        this.resourceLocation = resourceLocation;
        this.flags.setMaterial(this);
        verifyMaterial();
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
            return List.of(new SubComposition(List.of(new ElementData(RutileElements.NULL, 1)), 1));
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
        if (flag instanceof IFlagRegistry reg && !Objects.equals(reg.getExistingNamespace(), "")) {
            flags.noRegister(key);
        }
        flags.setFlag(key, flag);
        flags.verify();
    }

    public <T extends IMaterialFlag> void setNameAlternative(FlagKey<T> key, String alternativeName) {
        materialInfo.nameAlternatives.put(key, alternativeName);
    }

    public <T extends IMaterialFlag> void setExistingId(FlagKey<T> key, String existingId) {
        materialInfo.existingIds.put(key, ResourceLocation.tryParse(existingId));
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
        flags.verify();
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

    @Accessors(chain = true)
    public static class MaterialInfo {
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

        public MaterialInfo() {

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
            return withExistingId(flag, ResourceLocation.parse(id));
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
