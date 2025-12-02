package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.RutileRegistrate;
import dev.metallurgists.rutile.api.RutileApi;
import dev.metallurgists.rutile.api.material.flags.FlagKey;

public class RutileMaterialFluids {

    public static void generateMaterialFluids() {
        Rutile.LOGGER.info("Generating material fluids...");
        int registeredFluids = 0;
        for (var material : RutileApi.getMaterialRegistry().getAll()) {
            var flag = material.getFlag(FlagKey.FLUID);

            if (flag != null) {
                RutileRegistrate registrate = RutileRegistrate.createIgnoringListenerErrors(material.getModId());
                flag.registerFluids(material, registrate);
                registeredFluids++;
            }
        }
        Rutile.LOGGER.info("Generated {} material fluids", registeredFluids);
    }
}
