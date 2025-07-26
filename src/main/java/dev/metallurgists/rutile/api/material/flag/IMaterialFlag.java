package dev.metallurgists.rutile.api.material.flag;

import dev.metallurgists.rutile.api.material.base.MaterialFlags;

public interface IMaterialFlag {
    FlagKey<?> getKey();

    void verifyFlag(MaterialFlags flags);
}
