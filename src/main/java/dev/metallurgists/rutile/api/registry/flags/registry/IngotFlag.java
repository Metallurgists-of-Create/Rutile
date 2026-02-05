package dev.metallurgists.rutile.api.registry.flags.registry;

import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.material.flags.IMaterialFlag;
import dev.metallurgists.rutile.api.material.flags.MaterialFlags;

public class IngotFlag implements IMaterialFlag {

    @Override
    public void verifyFlag(MaterialFlags flags) {
        if (flags.hasFlag(FlagKey.GEM)) {
            throw new IllegalStateException(
                    "Material " + flags.getMaterial() +
                            " has both Ingot and Gem Flag, which is not allowed!");
        }
    }
}
