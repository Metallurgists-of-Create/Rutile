package dev.metallurgists.rutile.api.fluid;

public class FluidConstants {

    public static final int ROOM_TEMPERATURE = 293;

    public static final int SOLID_LIQUID_TEMPERATURE = 1200;

    public static final int BASE_PLASMA_TEMPERATURE = 10000;

    public static final int LIQUID_TEMPERATURE_OFFSET = 0;

    public static final int GAS_TEMPERATURE_OFFSET = 100;

    public static final int DEFAULT_LIQUID_DENSITY = 1000;
    public static final int DEFAULT_GAS_DENSITY = -100;
    public static final int DEFAULT_PLASMA_DENSITY = -100000;
    public static final int DEFAULT_MOLTEN_DENSITY = 1500;

    public static final int DEFAULT_LIQUID_VISCOSITY = 1000;
    public static final int DEFAULT_GAS_VISCOSITY = 200;
    public static final int DEFAULT_PLASMA_VISCOSITY = 10;
    public static final int DEFAULT_MOLTEN_VISCOSITY = 2000;

    private FluidConstants() {}
}
