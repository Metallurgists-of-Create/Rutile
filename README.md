# Rutile
[![](https://jitpack.io/v/Metallurgists-of-Create/Rutile.svg)](https://jitpack.io/#Metallurgists-of-Create/Rutile)


API for easy material registry using flags. \
Also chemical compositions.

## How to implement
\
Create a plugin:
```java
@RutilePlugin
public class YourRutilePlugin implements IRutilePlugin {

    @Override
    public String getPluginNamespace() {
        return "modid";
    }

    public AbstractRegistrate<?> getRegistrate() {
        return YourMod.registrate();
    }
}
```
\
Register Materials:
```java
public class YourMaterials {

    public static void init() {}
    
    public static Material Iron = new Material.Builder(Rutile.id("iron"))
            .element(RutileElements.IRON)
            .addFlags(
                    new IngotFlag("minecraft")
            ).buildAndRegister();
}

// Register Materials in your mod container

    @SubscribeEvent
    public void registerMaterials(MaterialEvent event) {
        YourMaterials.init();
    }
```
\
Register Elements:
```java
public class YourElements {
    public static final Element ELEMENTIUM = createAndRegister("elementium", "El", 0xff4aedd9);

    public static void init() {}
}

// Register Elements in your Rutile Plugin

    @Override
    public void registerElements() {
        YourElements.init();
    }
```
\
Register Custom Flags
```java
public class YourFlagKeys {
    public static FlagKey<SheetFlag> SHEET = createFlag("sheet", SheetFlag.class);

    public static void init() {}

    private static <C extends IMaterialFlag> FlagKey<C> createFlag(String name, Class<C> type) {
        ResourceLocation location = YourMod.asResource(name);
        return RutileAPI.registerFlag(location, FlagKey.create(name, type));
    }
}

// Register Flags in your Rutile Plugin

    @Override
    public void registerFlags() {
        YourFlagKeys.init();
    }
```
