**The intention of this file is to document how Materials will work in Rutile. I will try to keep it up to date with any thoughts.**

## What Are Materials?
Rutile will add `Materials` which are single objects that can be registered, that can go off and register other non-material objects during registration. 
Their purpose is to reduce registry class bloat and allow for shared properties over objects of the same material without the need for new classes for each object.

## How Registry Works
Materials won't follow a traditional registry, due to their need to be present before registries are loaded.
They will be "modifiable" and the modifications will run before registry but after the materials have been loaded.

When creating a material you can define various values to it. There are two different ways to add a value to a material, `Variables` and `Modules`.
- `Variables` are for storing a single value of any type, these can be any object, though usually a number.
  - Variables are registered with a key, a class and a default value to fall back to if the material does not contain that variable.
  - Some uses of a variable are; setting the harvest tier or burn time of a material.
- `Modules` are much more complex. These act as settings that change how a material might behave and often contain builders or larger constructors.
  - Some uses of a module are; setting the composition for the material, or, redirecting a material's part to an existing object.
- Both `Variables` & `Modules` can be retrieved from the material through their key at any time after creation and cannot be altered after the modification stage.

To actually register objects, you would define `Parts` to a material. 
- `Parts` will be their own class that contains the resource key for the registry they target, and the method for registering the object. All of a material's parts are then registered during the registry event.
- All parts for a material are registered under the `rutile` namespace to avoid duplicate users of Rutile creating duplicate objects. 
- The id of a created part follows a format pattern. The part would store the unformatted value, `%s_ingot`, and the Material then formats that with its id. An id format must only have one space.

## Redirects?
Sometimes a material will be created with an `Ingot` part, but that ingot already exists in vanilla. This can be solved by redirecting the part to that existing object through a material module.
<br>
Redirecting a part will stop Rutile from registering that part for the material, and it will instead reference the redirected object in its place.

# WIP
**will add more later**
