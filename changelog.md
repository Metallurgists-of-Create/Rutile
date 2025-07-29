# Rutile Changelog
![GitHub Release](https://img.shields.io/github/v/release/Metallurgists-of-Create/Rutile)

## v1.0.3
### Changes
- Material Modification can now modify the following:
    - Name Alternatives for flags e.g: `"golden"` could be set for ingots for `"golden_ingot"`.
    - Existing IDs for flags e.g: `"redstone"` could be set for dust to find `"minecraft:redstone"` instead of `"minecraft:redstone_dust"`.
    - Melting Point of the material.
    - Color of the material.
- Material Registry is now handled within the Rutile plugin. You can still register them with the `MaterialEvent` if you want.
- Material Modification is now handled within the Rutile plugin. Like material registry, the `PostMaterialEvent` still works.

## v1.0.2
### Changes
- Added `generatedItemModel` method for registering a simple generated item model to `ModelHelpers`.
- Added abstract `FluidFlag` for fluid registry.

## v1.0.1
### Changes
- Plugins now use an `AbstractRegistrate` instance instead of `RutileRegistrate`.