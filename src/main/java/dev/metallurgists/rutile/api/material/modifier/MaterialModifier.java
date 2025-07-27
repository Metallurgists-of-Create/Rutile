package dev.metallurgists.rutile.api.material.modifier;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.IMaterialFlag;

import java.util.*;

public class MaterialModifier {
    private final List<Material> materials;
    private final Map<FlagKey<? extends IMaterialFlag>, IMaterialFlag> flagMap;


    private MaterialModifier(List<Material> materials) {
        this.materials = materials;
        this.flagMap = new HashMap<>();
    }

    public static MaterialModifier create(Material... materials) {
        return new MaterialModifier(Arrays.stream(materials).toList());
    }

    public static MaterialModifier create(MaterialGroup... materialGroups) {
        List<Material> materialList = new ArrayList<>();
        Arrays.stream(materialGroups).forEach(group -> materialList.addAll(group.getMaterials()));
        return new MaterialModifier(materialList);
    }

    static class Builder {

    }
}
