package dev.metallurgists.rutile.api.material;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RutileMaterial {

    private static final Map<String, MaterialData> materials = new HashMap<>();

    public static MaterialData getOrCreate(String name) {
        return materials.computeIfAbsent(name, (id) -> new MaterialData());
    }

    public static void modifyData(String name, Function<MaterialData, MaterialData> consumer) {
        MaterialData data = getOrCreate(name);
        materials.put(name, consumer.apply(data));
    }
}
