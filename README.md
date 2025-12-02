# Rutile
[![](https://jitpack.io/v/Metallurgists-of-Create/Rutile.svg)](https://jitpack.io/#Metallurgists-of-Create/Rutile) [![](https://img.shields.io/github/v/release/Metallurgists-of-Create/Rutile)](https://github.com/Metallurgists-of-Create/Rutile/releases/latest)


API for easy material registry using flags. \
Also chemical compositions.

## About

Rutile is an API that works similarly to how GregTech registers Materials but in a more standalone and expandable manner.

With it you can use plugins to create your own materials and elements, add and remove recipes on world load, and assign chemical compositions to items.

Materials can be expanded upon and modified with the use of Flags and prefixes. Prefixes can register new objects like Items and Blocks, and Flags can define the behaviour of a material like if its items can be used as furnace fuel.

## How to implement
\
Create a plugin:
```java
@RutilePlugin
public class YourRutilePlugin implements IRutilePlugin {

    @Override
    public void configure(PluginConfig config) {
        config.setModId("modid");
        config.setRegiatrate(YourMod.registrate());
    }

}
```
\
Register Materials:
```java
public class YourMaterials {

    public static void init(IRutileRegistry<Material> registry) {
        registry.register(Iron);
    }
    
    public static Material Iron = new Material.Builder(Rutile.id("iron"))
            .element("iron")
            .flag(FlagKey.INGOT)
            .build();
}

// Register Materials in your plugin
    @Override
    public void onRegisterMaterials(IRutileRegistry<Material> registry) {
        YourMaterials.init(registry);
    }
```
\
Register Elements:
```java
public class YourElements {
    public static final Element El = create("elementium", "El", 0xff4aedd9);

    public static void init(IRutileRegistry<Element> registry) {
        registry.register(El);
    }
}

// Register Elements in your Rutile Plugin

    @Override
    public void onRegisterElements(IRutileRegistry<Element> registry) {
        YourElements.init(registry);
    }
```
\
Create Custom Flags
```java
public class YourFlagKeys {
    public static FlagKey<SheetFlag> SHEET = createFlag("sheet", SheetFlag.class);

    public static void init() {}

    private static <C extends IMaterialFlag> FlagKey<C> createFlag(String name, Class<C> type) {
        return FlagKey.create(name, type);
    }
}
```
