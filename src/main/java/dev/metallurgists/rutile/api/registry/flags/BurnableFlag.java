package dev.metallurgists.rutile.api.registry.flags;

import com.google.gson.JsonObject;
import dev.metallurgists.rutile.api.material.flags.IMaterialFlag;
import dev.metallurgists.rutile.api.material.flags.MaterialFlags;
import lombok.Getter;

public class BurnableFlag implements IMaterialFlag {
    @Getter
    private int burnTime;

    public BurnableFlag() {
        this.burnTime = 0;
    }

    public BurnableFlag(int burnTime) {
        this.burnTime = burnTime;
    }

    public void setBurnTime(int burnTime) {
        if (burnTime < 0) throw new IllegalArgumentException("Burn Time cannot be negative!");
        this.burnTime = burnTime;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }

    @Override
    public JsonObject debugJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("burnTime", burnTime);
        return jsonObject;
    }
}
