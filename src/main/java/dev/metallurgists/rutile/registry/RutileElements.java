package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.plugin.IRutilePlugin;
import dev.metallurgists.rutile.api.plugin.RutilePluginFinder;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import net.minecraftforge.fml.ModLoader;

public class RutileElements {

    static {
        RutileRegistries.ELEMENTS.unfreeze();
    }

    public static final Element NULL             =  createAndRegister("null", "?", 0xff171a2d);
    public static final Element HYDROGEN         =  createAndRegister("hydrogen", "H", 0xff9175dc);
    public static final Element HELIUM           =  createAndRegister("helium", "He", 0xfffcc6f7);
    public static final Element LITHIUM          =  createAndRegister("lithium", "Li", 0xff989890);
    public static final Element BERYLLIUM        =  createAndRegister("beryllium", "Be", 0xff838489);
    public static final Element BORON            =  createAndRegister("boron", "B", 0xff6d7079);
    public static final Element CARBON           =  createAndRegister("carbon", "C", 0xff626061);
    public static final Element NITROGEN         =  createAndRegister("nitrogen", "N", 0xffdfd9d9);
    public static final Element OXYGEN           =  createAndRegister("oxygen", "O", 0xffc7e4f6);
    public static final Element FLUORINE         =  createAndRegister("fluorine", "F", 0xffd0d97e);
    public static final Element NEON             =  createAndRegister("neon", "Ne", 0xffdb608f);
    public static final Element SODIUM           =  createAndRegister("sodium", "Na", 0xffc9bfbe);
    public static final Element MAGNESIUM        =  createAndRegister("magnesium", "Mg", 0xffb7b7b7);
    public static final Element ALUMINUM         =  createAndRegister("aluminum", "Al", 0xffcccfd4);
    public static final Element SILICON          =  createAndRegister("silicon", "Si", 0xff81848d);
    public static final Element PHOSPHORUS       =  createAndRegister("phosphorus", "P", 0xffa16567);
    public static final Element SULFUR           =  createAndRegister("sulfur", "S", 0xffdae096);
    public static final Element CHLORINE         =  createAndRegister("chlorine", "Cl", 0xffc1bb1f);
    public static final Element ARGON            =  createAndRegister("argon", "Ar", 0xffb93de9);
    public static final Element POTASSIUM        =  createAndRegister("potassium", "K", 0xff9aa3a2);
    public static final Element CALCIUM          =  createAndRegister("calcium", "Ca", 0xffb4ad9d);
    public static final Element SCANDIUM         =  createAndRegister("scandium", "Sc", 0xffa8a095);
    public static final Element TITANIUM         =  createAndRegister("titanium", "Ti", 0xffacada5);
    public static final Element VANADIUM         =  createAndRegister("vanadium", "V", 0xff99a1a4);
    public static final Element CHROMIUM         =  createAndRegister("chromium", "Cr", 0xffb4b7c0);
    public static final Element MANGANESE        =  createAndRegister("manganese", "Mn", 0xff746f6c);
    public static final Element IRON             =  createAndRegister("iron", "Fe", 0xff949496);
    public static final Element COBALT           =  createAndRegister("cobalt", "Co", 0xffb0afb5);
    public static final Element NICKEL           =  createAndRegister("nickel", "Ni", 0xffa3a29e);
    public static final Element COPPER           =  createAndRegister("copper", "Cu", 0xffdcb491);
    public static final Element ZINC             =  createAndRegister("zinc", "Zn", 0xffb5bdc0);
    public static final Element GALLIUM          =  createAndRegister("gallium", "Ga", 0xffb5c1cd);
    public static final Element GERMANIUM        =  createAndRegister("germanium", "Ge", 0xff7d8379);
    public static final Element ARSENIC          =  createAndRegister("arsenic", "As", 0xff92948f);
    public static final Element SELENIUM         =  createAndRegister("selenium", "Se", 0xff5f676a);
    public static final Element BROMINE          =  createAndRegister("bromine", "Br", 0xffd39131);
    public static final Element KRYPTON          =  createAndRegister("krypton", "Kr", 0xffc6b4e8);
    public static final Element RUBIDIUM         =  createAndRegister("rubidium", "Rb", 0xff9b9b93);
    public static final Element STRONTIUM        =  createAndRegister("strontium", "Sr", 0xff868782);
    public static final Element YTTRIUM          =  createAndRegister("yttrium", "Y", 0xffa8a095);
    public static final Element ZIRCONIUM        =  createAndRegister("zirconium", "Zr", 0xffafaaa7);
    public static final Element NIOBIUM          =  createAndRegister("niobium", "Nb", 0xff91908c);
    public static final Element MOLYBDENUM       =  createAndRegister("molybdenum", "Mo", 0xff878791);
    public static final Element TECHNETIUM       =  createAndRegister("technetium", "Tc", 0xff796f66);
    public static final Element RUTHENIUM        =  createAndRegister("ruthenium", "Ru", 0xffa2a2a4);
    public static final Element RHODIUM          =  createAndRegister("rhodium", "Rh", 0xffc7c2bf);
    public static final Element PALLADIUM        =  createAndRegister("palladium", "Pd", 0xffadacaa);
    public static final Element SILVER           =  createAndRegister("silver", "Ag", 0xffdddfda);
    public static final Element CADMIUM          =  createAndRegister("cadmium", "Cd", 0xffb4b4b4);
    public static final Element INDIUM           =  createAndRegister("indium", "In", 0xffd5d0cd);
    public static final Element TIN              =  createAndRegister("tin", "Sn", 0xffc2c2c2);
    public static final Element ANTIMONY         =  createAndRegister("antimony", "Sb", 0xffa7afb2);
    public static final Element TELLURIUM        =  createAndRegister("tellurium", "Te", 0xff827d7a);
    public static final Element IODINE           =  createAndRegister("iodine", "I", 0xff983087);
    public static final Element XENON            =  createAndRegister("xenon", "Xe", 0xff7299f6);
    public static final Element CESIUM           =  createAndRegister("cesium", "Cs", 0xffb2aa7c);
    public static final Element BARIUM           =  createAndRegister("barium", "Ba", 0xff676964);
    public static final Element LANTHANUM        =  createAndRegister("lanthanum", "La", 0xff8c8d92);
    public static final Element CERIUM           =  createAndRegister("cerium", "Ce", 0xff7b7c74);
    public static final Element PRASEODYMIUM     =  createAndRegister("praseodymium", "Pr", 0xff99989e);
    public static final Element NEODYMIUM        =  createAndRegister("neodymium", "Nd", 0xff727473);
    public static final Element PROMETHIUM       =  createAndRegister("promethium", "Pm", 0xff32323c);
    public static final Element SAMARIUM         =  createAndRegister("samarium", "Sm", 0xff515257);
    public static final Element EUROPIUM         =  createAndRegister("europium", "Eu", 0xff838b8e);
    public static final Element GADOLINIUM       =  createAndRegister("gadolinium", "Gd", 0xff959589);
    public static final Element TERBIUM          =  createAndRegister("terbium", "Tb", 0xffa4a3a1);
    public static final Element DYSPROSIUM       =  createAndRegister("dysprosium", "Dy", 0xff8e8986);
    public static final Element HOLMIUM          =  createAndRegister("holmium", "Ho", 0xff9a9a92);
    public static final Element ERBIUM           =  createAndRegister("erbium", "Er", 0xff9e9f97);
    public static final Element THULIUM          =  createAndRegister("thulium", "Tm", 0xff8e8c8d);
    public static final Element YTTERBIUM        =  createAndRegister("ytterbium", "Yb", 0xff979799);
    public static final Element LUTETIUM         =  createAndRegister("lutetium", "Lu", 0xffa6a6a4);
    public static final Element HAFNIUM          =  createAndRegister("hafnium", "Hf", 0xffa19c99);
    public static final Element TANTALUM         =  createAndRegister("tantalum", "Ta", 0xff8d9695);
    public static final Element TUNGSTEN         =  createAndRegister("tungsten", "W", 0xff797876);
    public static final Element RHENIUM          =  createAndRegister("rhenium", "Re", 0xffa19fac);
    public static final Element OSMIUM           =  createAndRegister("osmium", "Os", 0xff95a6ad);
    public static final Element IRIDIUM          =  createAndRegister("iridium", "Ir", 0xffaba1a0);
    public static final Element PLATINUM         =  createAndRegister("platinum", "Pt", 0xffc5c4c0);
    public static final Element GOLD             =  createAndRegister("gold", "Au", 0xffd1c186);
    public static final Element MERCURY          =  createAndRegister("mercury", "Hg", 0xff898a8c);
    public static final Element THALLIUM         =  createAndRegister("thallium", "Tl", 0xff7d7a81);
    public static final Element LEAD             =  createAndRegister("lead", "Pb", 0xff8f929b);
    public static final Element BISMUTH          =  createAndRegister("bismuth", "Bi", 0xffbcb6b6);
    public static final Element POLONIUM         =  createAndRegister("polonium", "Po", 0xff30333c);
    public static final Element ASTATINE         =  createAndRegister("astatine", "At", 0xff2a2a2a);
    public static final Element RADON            =  createAndRegister("radon", "Rn", 0xff2e313a);
    public static final Element FRANCIUM         =  createAndRegister("francium", "Fr", 0xff262626);
    public static final Element RADAIUM          =  createAndRegister("radium", "Ra", 0xffa79a87);
    public static final Element ACTINIUM         =  createAndRegister("actinium", "Ac", 0xff2b2d39);
    public static final Element THORIUM          =  createAndRegister("thorium", "Th", 0xff7e807d);
    public static final Element PROTACTINIUM     =  createAndRegister("protactinium", "Pa", 0xff48525e);
    public static final Element URANIUM          =  createAndRegister("uranium", "U", 0xff85807d);
    public static final Element NEPTUNIUM        =  createAndRegister("neptunium", "Np", 0xff9d9892);
    public static final Element PLUTONIUM        =  createAndRegister("plutonium", "Pu", 0xff6f3d40);
    public static final Element AMERICIUM        =  createAndRegister("americium", "Am", 0xff606166);
    public static final Element CURIUM           =  createAndRegister("curium", "Cm", 0xff949085);
    public static final Element BERKELIUM        =  createAndRegister("berkelium", "Bk", 0xff787775);
    public static final Element CALIFORNIUM      =  createAndRegister("californium", "Cf", 0xff8c8686);
    public static final Element EINSTEINIUM      =  createAndRegister("einsteinium", "Es", 0xff333439);
    public static final Element FERMIUM          =  createAndRegister("fermium", "Fm", 0xff292c35);
    public static final Element MENDELEVIUM      =  createAndRegister("mendelevium", "Md", 0xff2e2d3d);
    public static final Element NOBELIUM         =  createAndRegister("nobelium", "No", 0xff242424);
    public static final Element LAWRENCIUM       =  createAndRegister("lawrencium", "Lr", 0xff242424);
    public static final Element RUTHERFORDIUM    =  createAndRegister("rutherfordium", "Rf", 0xff2a2529);
    public static final Element DUBNIUM          =  createAndRegister("dubnium", "Db", 0xff2b2f38);
    public static final Element SEABORGIUM       =  createAndRegister("seaborgium", "Sg", 0xff262626);
    public static final Element BOHRIUM          =  createAndRegister("bohrum", "Bh", 0xff282629);
    public static final Element HASSIUM          =  createAndRegister("hassium", "Hs", 0xff282828);
    public static final Element MEITNERIUM       =  createAndRegister("meitnerium", "Mt", 0xff262628);
    public static final Element DARMSTADTIUM     =  createAndRegister("darmstadtium", "Ds", 0xff262626);
    public static final Element ROENTGENIUM      =  createAndRegister("roentgenium", "Rg", 0xff262427);
    public static final Element COPERNICIUM      =  createAndRegister("copernicium", "Cn", 0xff262626);
    public static final Element NIHONIUM         =  createAndRegister("nihonium", "Nh", 0xff252328);
    public static final Element FLEROVIUM        =  createAndRegister("flerovium", "Fl", 0xff2b2b2b);
    public static final Element MOSCOVIUM        =  createAndRegister("moscovium", "Mc", 0xff242426);
    public static final Element LIVERMORIUM      =  createAndRegister("livermorium", "Lv", 0xff292728);
    public static final Element TENNESSINE       =  createAndRegister("tennessine", "Ts", 0xff262628);
    public static final Element OGANESSON        =  createAndRegister("oganesson", "Og", 0xff2a2a2a);

    public static Element createAndRegister(String name, String symbol, int colour) {
        Element element = new Element(new Element.Properties(Rutile.id(name)).color(colour).symbol(symbol));
        RutileRegistries.ELEMENTS.register(Rutile.id(name), element);
        return element;
    }

    public static void init() {
        RutilePluginFinder.getModPlugins().forEach(IRutilePlugin::registerElements);
        ModLoader.get().postEvent(new RutileAPI.RegisterEvent<>(RutileRegistries.ELEMENTS, Element.class));
        RutileRegistries.ELEMENTS.freeze();
    }

}
