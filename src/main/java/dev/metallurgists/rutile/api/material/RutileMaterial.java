package dev.metallurgists.rutile.api.material;

import java.util.HashMap;
import java.util.Map;

public class RutileMaterial {
    public static final RutileMaterial INSTANCE = new RutileMaterial();

    private final Map<String, MaterialData> materials = new HashMap<>();

    public MaterialData getOrCreate(String name) {
        return materials.computeIfAbsent(name, MaterialData::new);
    }
}
