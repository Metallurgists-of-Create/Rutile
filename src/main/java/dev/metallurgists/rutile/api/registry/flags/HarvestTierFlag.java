package dev.metallurgists.rutile.api.registry.flags;

import com.google.gson.JsonObject;
import dev.metallurgists.rutile.api.material.flags.IMaterialFlag;
import dev.metallurgists.rutile.api.material.flags.MaterialFlags;
import lombok.Getter;

public class HarvestTierFlag implements IMaterialFlag {
    @Getter
    private int harvestLevel;

    public HarvestTierFlag() {
        this.harvestLevel = 2;
    }

    public HarvestTierFlag(int harvestLevel) {
        this.harvestLevel = harvestLevel;
    }

    public void setHarvestLevel(int harvestLevel) {
        if (harvestLevel <= 0) throw new IllegalArgumentException("Harvest Level must be greater than zero!");
        this.harvestLevel = harvestLevel;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }

    @Override
    public JsonObject debugJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("harvestLevel", harvestLevel);
        return jsonObject;
    }
}
