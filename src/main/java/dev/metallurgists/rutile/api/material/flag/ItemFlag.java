package dev.metallurgists.rutile.api.material.flag;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.types.IItemRegistry;
import dev.metallurgists.rutile.api.material.registry.item.IMaterialItem;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public abstract class ItemFlag implements IMaterialFlag, IItemRegistry {

    private final String idPattern;
    private String existingNamespace;
    @Setter
    public List<String> tagPatterns = List.of();

    public ItemFlag(String idPattern) {
        this.idPattern = idPattern;
    }

    public ItemFlag(String idPattern, String existingNamespace) {
        this.idPattern = idPattern;
        this.existingNamespace = existingNamespace;
    }


    public abstract ItemEntry<? extends IMaterialItem> registerItem(@NotNull Material material, IItemRegistry flag, @NotNull RutileRegistrate registrate);
}
