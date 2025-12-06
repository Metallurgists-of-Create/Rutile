package dev.metallurgists.rutile.api.material.flags;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public abstract class MapFlag<K, V> extends UnitFlag<Map<K, V>> {

    public MapFlag(Map<K, V> value) {
        super(value);
    }

    public MapFlag(K key, V value) {
        super(Map.of(key, value));
    }

    public void setValue(K key, V value) {
        getValue().put(key, value);
    }

    public abstract String getKeyJson(K key);

    public abstract JsonElement getValueJson(V key);

    @Override
    public JsonObject debugJson() {
        JsonObject jsonObject = new JsonObject();
        JsonObject mapObject = new JsonObject();
        for (Map.Entry<K, V> entry : getValue().entrySet()) {
            mapObject.add(getKeyJson(entry.getKey()), getValueJson(entry.getValue()));
        }
        jsonObject.add("value_map", mapObject);
        return jsonObject;
    }
}
