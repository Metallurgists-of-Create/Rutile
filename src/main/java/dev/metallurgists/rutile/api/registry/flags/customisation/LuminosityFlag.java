package dev.metallurgists.rutile.api.registry.flags.customisation;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import dev.metallurgists.rutile.api.fluid.storage.FluidStorageKey;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.material.flags.IMaterialFlag;
import dev.metallurgists.rutile.api.material.flags.MaterialFlags;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.Getter;

public class LuminosityFlag implements IMaterialFlag {
    @Getter
    private int globalLuminosity;

    private Object2IntOpenHashMap<FluidStorageKey> luminosityMap = new Object2IntOpenHashMap<>();

    public LuminosityFlag(int luminosity) {
        Preconditions.checkArgument(luminosity >= 0 && luminosity < 16, "luminosity must be >= 0 and < 16");
        this.globalLuminosity = luminosity;
    }

    public LuminosityFlag() {
        this.globalLuminosity = -1;
    }

    public LuminosityFlag forKey(FluidStorageKey key, int luminosity) {
        Preconditions.checkArgument(luminosity >= 0 && luminosity < 16, "luminosity must be >= 0 and < 16");
        luminosityMap.put(key, luminosity);
        return this;
    }

    public int getLuminosity(FluidStorageKey key) {
        if (luminosityMap.containsKey(key)) {
            return luminosityMap.getInt(key);
        }
        return globalLuminosity;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(FlagKey.FLUID);
    }

    @Override
    public JsonObject debugJson() {
        JsonObject jsonObject = new JsonObject();
        JsonObject luminosityObject = new JsonObject();
        luminosityObject.addProperty("global", globalLuminosity);
        JsonObject flagsObject = new JsonObject();
        for (FluidStorageKey flagKey : luminosityMap.keySet()) {
            flagsObject.addProperty(flagKey.toString(), luminosityMap.getInt(flagKey));
        }
        luminosityObject.add("flags", flagsObject);
        jsonObject.add("luminosity", luminosityObject);
        return jsonObject;
    }
}
