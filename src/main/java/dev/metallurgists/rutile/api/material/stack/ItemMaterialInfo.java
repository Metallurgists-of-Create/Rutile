package dev.metallurgists.rutile.api.material.stack;

import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.tag.TagPrefix;
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
            materials.addTo(stack.getMaterial(), stack.getAmount());
        }
        setSortedMaterials(materials);
    }

    public ItemMaterialInfo(Reference2LongMap<Material> materialList) {
        setSortedMaterials(materialList);
    }

    public MaterialStack getMaterial() {
        return sortedMaterials.isEmpty() ? MaterialStack.EMPTY : sortedMaterials.getFirst();
    }

    @UnmodifiableView
    public List<MaterialStack> getMaterials() {
        return Collections.unmodifiableList(sortedMaterials);
    }

    public void addMaterialStacks(List<MaterialStack> stacks) {
        var materials = new Reference2LongOpenHashMap<Material>();
        sortedMaterials.forEach(stack -> materials.addTo(stack.getMaterial(), stack.getAmount()));
        stacks.forEach(stack -> materials.addTo(stack.getMaterial(), stack.getAmount()));
        setSortedMaterials(materials);
    }

    private void setSortedMaterials(Reference2LongMap<Material> matStacks) {
        sortedMaterials.clear();

        for (var entry : matStacks.reference2LongEntrySet()) {
            sortedMaterials.add(new MaterialStack(entry.getKey().getId(), entry.getLongValue()));
        }
        sortedMaterials.sort(Comparator.comparingLong(MaterialStack::getAmount));

        sortedHash = sortedMaterials.hashCode();

        StringBuilder ret = new StringBuilder("[ ");
        for (var matStack : sortedMaterials) {
            ret.append(matStack.getAmount() / (float) TagPrefix.M).append("x ")
                    .append(matStack.getMaterial().getId()).append(" ");
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
