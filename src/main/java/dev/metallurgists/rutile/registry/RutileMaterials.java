package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.registry.flags.BurnableFlag;
import dev.metallurgists.rutile.api.registry.flags.HarvestTierFlag;

public class RutileMaterials {

    public static Material IronChloride = new Material.Builder(Rutile.id("iron_chloride"))
            .composition("1 iron", "1 chlorine")
            .colour(0xff48180E)
            .fluid()
            .build();

    public static Material TinTetrachloride = new Material.Builder(Rutile.id("tin_tetrachloride"))
            .composition("1 tin", "4 chlorine")
            .colour(0xffC2D4D4)
            .fluid()
            .build();

    public static Material SiliconTetrachloride = new Material.Builder(Rutile.id("silicon_tetrachloride"))
            .composition("1 silicon", "4 chlorine")
            .colour(0xff42495E)
            .fluid()
            .build();

    public static Material TitaniumTetrachloride = new Material.Builder(Rutile.id("titanium_tetrachloride"))
            .composition("1 titanium", "4 chlorine")
            .colour(0xffA76F6F)
            .fluid()
            .build();

    public static Material CrudeTitaniumTetrachloride = new Material.Builder(Rutile.id("crude_titanium_tetrachloride"))
            .components(IronChloride, 6, TitaniumTetrachloride, 24, TinTetrachloride, 9, SiliconTetrachloride, 3)
            .colour(0xff876253)
            .fluid()
            .build();

    public static Material Null = new Material.Builder(Rutile.id("null"))
            .element("null")
            .colour(0xffbf4cd2)
            .flag(FlagKey.INGOT)
            .fluid()
            .flag(FlagKey.HARVEST_TIER)
            .flag(FlagKey.BURNABLE, new BurnableFlag(60))
            .build();

    public static Material Iron = new Material.Builder(Rutile.id("iron"))
            .element("iron")
            .colour(0xff949496)
            .flag(FlagKey.INGOT)
            .flag(FlagKey.HARVEST_TIER, new HarvestTierFlag(1))
            .build();

    public static Material Copper = new Material.Builder(Rutile.id("copper"))
            .element("copper")
            .colour(0xffdcb491)
            .flag(FlagKey.INGOT)
            .flag(FlagKey.HARVEST_TIER, new HarvestTierFlag(1))
            .build();

    public static Material Gold = new Material.Builder(Rutile.id("gold"))
            .element("gold")
            .colour(0xffd1c186)
            .flag(FlagKey.INGOT)
            .flag(FlagKey.HARVEST_TIER, new HarvestTierFlag(2))
            .build();

    public static Material Diamond = new Material.Builder(Rutile.id("diamond"))
            .element("carbon")
            .colour(0xffa1fbe8)
            .flag(FlagKey.GEM)
            .flag(FlagKey.HARVEST_TIER, new HarvestTierFlag(2))
            .build();

    public static Material Emerald = new Material.Builder(Rutile.id("emerald"))
            .composition("3 beryllium", "2 aluminum", "6 silicon", "18 oxygen")
            .colour(0xff41f384)
            .flag(FlagKey.GEM)
            .flag(FlagKey.HARVEST_TIER, new HarvestTierFlag(2))
            .build();

    public static Material Quartz = new Material.Builder(Rutile.id("quartz"))
            .composition("1 silicon", "2 oxygen")
            .colour(0xffd4caba)
            .flag(FlagKey.GEM)
            .flag(FlagKey.HARVEST_TIER, new HarvestTierFlag(0))
            .build();

    public static Material Amethyst = new Material.Builder(Rutile.id("amethyst"))
            .composition("1 silicon", "2 oxygen", "1 iron")
            .colour(0xffcfa0f3)
            .flag(FlagKey.GEM)
            .flag(FlagKey.HARVEST_TIER, new HarvestTierFlag(0))
            .build();
}
