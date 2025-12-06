package dev.metallurgists.rutile.api.registry.flags;

import com.google.gson.JsonObject;
import dev.metallurgists.rutile.api.material.flags.MaterialFlags;
import dev.metallurgists.rutile.api.material.flags.UnitFlag;

public class BurnableFlag extends UnitFlag<Integer> {

    public BurnableFlag() {
        super(0);
    }

    public BurnableFlag(int value) {
        super(value);
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }

    @Override
    public boolean validValue(Integer value) {
        return value >= 0;
    }

    @Override
    public JsonObject debugJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("burnTime", getValue());
        return jsonObject;
    }
}
