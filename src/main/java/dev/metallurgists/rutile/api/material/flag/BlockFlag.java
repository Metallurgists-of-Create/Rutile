package dev.metallurgists.rutile.api.material.flag;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.types.IBlockRegistry;
import dev.metallurgists.rutile.api.material.flag.types.IConditionalComposition;
import dev.metallurgists.rutile.api.material.flag.types.IHaveTags;
import dev.metallurgists.rutile.api.material.flag.types.IItemRegistry;
import dev.metallurgists.rutile.api.material.registry.block.IMaterialBlock;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public abstract class BlockFlag implements IMaterialFlag, IBlockRegistry, IConditionalComposition, IHaveTags {

    private final String idPattern;
    @Getter
    private String existingNamespace = "";

    public BlockFlag(String idPattern) {
        this.idPattern = idPattern;
    }

    public BlockFlag(String idPattern, String existingNamespace) {
        this.idPattern = idPattern;
        this.existingNamespace = existingNamespace;
    }

    public abstract BlockEntry<? extends IMaterialBlock> registerBlock(@NotNull Material material, IBlockRegistry flag, @NotNull AbstractRegistrate<?> registrate);

    public abstract boolean shouldHaveComposition();

    public abstract FlagKey<? extends IBlockRegistry> getKey();


    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
