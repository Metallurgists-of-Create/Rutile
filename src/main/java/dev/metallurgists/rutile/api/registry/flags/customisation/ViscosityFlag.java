package dev.metallurgists.rutile.api.registry.flags.customisation;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import dev.metallurgists.rutile.api.fluid.storage.FluidStorageKey;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.material.flags.IMaterialFlag;
import dev.metallurgists.rutile.api.material.flags.MaterialFlags;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.Getter;

public class ViscosityFlag implements IMaterialFlag {

    @Getter
    private int globalViscosity;

    private Object2IntOpenHashMap<FluidStorageKey> viscosityMap = new Object2IntOpenHashMap<>();

    public ViscosityFlag(int viscosity) {
        int mcViscosity = viscosity * 10000;
        Preconditions.checkArgument(mcViscosity >= 0, "viscosity must be >= 0");
        this.globalViscosity = mcViscosity;
    }

    public ViscosityFlag() {
        this.globalViscosity = -1;
    }

    public ViscosityFlag forKey(FluidStorageKey key, int viscosity) {
        int mcViscosity = viscosity * 10000;
        Preconditions.checkArgument(mcViscosity >= 0, "viscosity must be >= 0");
        viscosityMap.put(key, mcViscosity);
        return this;
    }

    public int getViscosity(FluidStorageKey key) {
        if (viscosityMap.containsKey(key)) {
            return viscosityMap.getInt(key);
        }
        return globalViscosity;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(FlagKey.FLUID);
    }

    @Override
    public JsonObject debugJson() {
        JsonObject jsonObject = new JsonObject();
        JsonObject viscosityObject = new JsonObject();
        viscosityObject.addProperty("global", globalViscosity);
        JsonObject flagsObject = new JsonObject();
        for (FluidStorageKey flagKey : viscosityMap.keySet()) {
            flagsObject.addProperty(flagKey.toString(), viscosityMap.getInt(flagKey));
        }
        viscosityObject.add("flags", flagsObject);
        jsonObject.add("viscosity", viscosityObject);
        return jsonObject;
    }
}
