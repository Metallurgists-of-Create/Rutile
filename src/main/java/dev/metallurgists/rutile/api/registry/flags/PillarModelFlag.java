package dev.metallurgists.rutile.api.registry.flags;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.metallurgists.rutile.api.material.flags.MapFlag;
import dev.metallurgists.rutile.api.material.flags.MaterialFlags;
import dev.metallurgists.rutile.api.tag.TagPrefix;

import java.util.Map;

public class PillarModelFlag extends MapFlag<TagPrefix, Boolean> {

    public PillarModelFlag(TagPrefix key, Boolean value) {
        super(key, value);
    }

    public PillarModelFlag(TagPrefix pOld, TagPrefix pNew) {
        super(Map.of(pOld, false, pNew, true));
    }

    @Override
    public String getKeyJson(TagPrefix key) {
        return key.getModId() + ":" + key.getName();
    }

    @Override
    public JsonElement getValueJson(Boolean value) {
        return new JsonPrimitive(value);
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
