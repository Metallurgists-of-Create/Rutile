package dev.metallurgists.rutile.api.material.component;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialLike;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class ModifyMaterialComponentsEvent extends Event implements IModBusEvent {
    public void modify(MaterialLike material, Consumer<MaterialComponentPatch.Builder> patch) {
        MaterialComponentPatch.Builder builder = MaterialComponentPatch.builder();
        patch.accept(builder);
        MaterialComponentPatch compPatch = builder.build();
        if (!compPatch.isEmpty()) {
            material.asMaterial().modifyDefaultComponentsFrom(builder.build());
        }

    }

    public void modifyMatching(Predicate<? super Material> predicate, Consumer<MaterialComponentPatch.Builder> patch) {
        this.getAllMaterials().filter(predicate).forEach((material) -> this.modify(material, patch));
    }

    public Stream<Material> getAllMaterials() {
        return RutileAPI.getMaterialRegistry().getAll().stream();
    }
}
