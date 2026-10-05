# Rutile Changelog
![GitHub Release](https://img.shields.io/github/v/release/Metallurgists-of-Create/Rutile)

### Changes
- ElementStacks and Compositions can now have Mass.
- Added support for KubeJS.
- Added some compat compositions for common metals & sulfur.
- Compositions can now be nested.
- Added `getColor`, `groupStyle`, `getDisplay`, `getContainedElements` & `getContainedAmount` to SubComposition.
- SubComposition now uses a recursive codec.
- Elements list in SubComposition is now optional.
- Added `nested` builder methods to Composition & SubComposition.
- CompositionHandler#createTooltip now uses `getDisplay` from the SubCompositions.
- ElementStack mass is now multiplied by the amount in the stack.
- The displayed ElementStack in JEI is now updated to have the correct count.
