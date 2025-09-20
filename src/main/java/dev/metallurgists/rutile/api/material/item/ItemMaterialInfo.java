package dev.metallurgists.rutile.api.material.item;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialStack;
import it.unimi.dsi.fastutil.objects.Reference2LongMap;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

public class ItemMaterialInfo {

    private final List<MaterialStack> sortedMaterials = new ArrayList<>();
    private int sortedHash = 0;
    private String toStringValue;

    public ItemMaterialInfo(MaterialStack... materialStacks) {
        this(Arrays.asList(materialStacks));
    }

    public ItemMaterialInfo(List<MaterialStack> materialStacks) {
        var materials = new Reference2LongOpenHashMap<Material>();
        for (var stack : materialStacks) {
            materials.addTo(stack.material(), stack.amount());
        }
        setSortedMaterials(materials);
    }

    public ItemMaterialInfo(Reference2LongMap<Material> materialList) {
        setSortedMaterials(materialList);
    }

    public MaterialStack getMaterial() {
        return sortedMaterials.isEmpty() ? MaterialStack.EMPTY : sortedMaterials.get(0);
    }

    @UnmodifiableView
    public List<MaterialStack> getMaterials() {
        return Collections.unmodifiableList(sortedMaterials);
    }

    public void addMaterialStacks(List<MaterialStack> stacks) {
        var materials = new Reference2LongOpenHashMap<Material>();
        sortedMaterials.forEach(stack -> materials.addTo(stack.material(), stack.amount()));
        stacks.forEach(stack -> materials.addTo(stack.material(), stack.amount()));
        setSortedMaterials(materials);
    }

    private void setSortedMaterials(Reference2LongMap<Material> matStacks) {
        sortedMaterials.clear();

        for (var entry : matStacks.reference2LongEntrySet()) {
            sortedMaterials.add(new MaterialStack(entry.getKey(), entry.getLongValue()));
        }
        sortedMaterials.sort(Comparator.comparingLong(MaterialStack::amount));

        sortedHash = sortedMaterials.hashCode();

        StringBuilder ret = new StringBuilder("[ ");
        for (var matStack : sortedMaterials) {
            ret.append(matStack.amount() / (float) 3628800).append("x ")
                    .append(matStack.material().getId()).append(" ");
        }
        ret.append("]");
        toStringValue = ret.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemMaterialInfo that = (ItemMaterialInfo) o;
        return this.hashCode() == o.hashCode() && sortedMaterials.equals(that.sortedMaterials);
    }

    @Override
    public int hashCode() {
        return sortedHash;
    }

    @Override
    public String toString() {
        return toStringValue;
    }
}
