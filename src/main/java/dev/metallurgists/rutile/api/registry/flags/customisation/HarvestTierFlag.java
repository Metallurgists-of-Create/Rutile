package dev.metallurgists.rutile.api.registry.flags.customisation;

import com.google.gson.JsonObject;
import dev.metallurgists.rutile.api.material.flags.MaterialFlags;
import dev.metallurgists.rutile.api.material.flags.UnitFlag;

public class HarvestTierFlag extends UnitFlag<Integer> {

    public HarvestTierFlag() {
        super(2);
    }

    public HarvestTierFlag(int value) {
        super(value);
    }

    @Override
    public boolean validValue(Integer value) {
        return value > 0;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }

    @Override
    public JsonObject debugJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("harvestLevel", getValue());
        return jsonObject;
    }
}
