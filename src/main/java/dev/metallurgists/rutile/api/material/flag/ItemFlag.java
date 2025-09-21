package dev.metallurgists.rutile.api.material.flag;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.types.IFluidRegistry;
import dev.metallurgists.rutile.api.material.flag.types.IHaveTags;
import dev.metallurgists.rutile.api.material.flag.types.IItemRegistry;
import dev.metallurgists.rutile.api.material.registry.item.IMaterialItem;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public abstract class ItemFlag implements IMaterialFlag, IItemRegistry, IHaveTags {

    private final String idPattern;
    private String existingNamespace;

    public ItemFlag(String idPattern) {
        this.idPattern = idPattern;
    }

    public ItemFlag(String idPattern, String existingNamespace) {
        this.idPattern = idPattern;
        this.existingNamespace = existingNamespace;
    }

    public abstract FlagKey<? extends IItemRegistry> getKey();

    public abstract ItemEntry<? extends IMaterialItem> registerItem(@NotNull Material material, IItemRegistry flag, @NotNull AbstractRegistrate<?> registrate);
}
