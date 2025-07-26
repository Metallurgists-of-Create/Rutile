package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;
import dev.metallurgists.rutile.api.registrate.element.ElementEntry;

public class RutileElements {

    public static ElementEntry<Element>
            NULL,
            HYDROGEN,
            HELIUM,
            LITHIUM,
            BERYLLIUM,
            BORON,
            CARBON,
            NITROGEN,
            OXYGEN,
            FLUORINE,
            NEON,
            SODIUM,
            MAGNESIUM,
            ALUMINUM,
            SILICON,
            PHOSPHORUS,
            SULFUR,
            CHLORINE,
            ARGON,
            POTASSIUM,
            CALCIUM,
            SCANDIUM,
            TITANIUM,
            VANADIUM,
            CHROMIUM,
            MANGANESE,
            IRON,
            COBALT,
            NICKEL,
            COPPER,
            ZINC,
            GALLIUM,
            GERMANIUM,
            ARSENIC,
            SELENIUM,
            BROMINE,
            KRYPTON,
            RUBIDIUM,
            STRONTIUM,
            YTTRIUM,
            ZIRCONIUM,
            NIOBIUM,
            MOLYBDENUM,
            TECHNETIUM,
            RUTHENIUM,
            RHODIUM,
            PALLADIUM,
            SILVER,
            CADMIUM,
            INDIUM,
            TIN,
            ANTIMONY,
            TELLURIUM,
            IODINE,
            XENON,
            CESIUM,
            BARIUM,
            LANTHANUM,
            CERIUM,
            PRASEODYMIUM,
            NEODYMIUM,
            PROMETHIUM,
            SAMARIUM,
            EUROPIUM,
            GADOLINIUM,
            TERBIUM,
            DYSPROSIUM,
            HOLMIUM,
            ERBIUM,
            THULIUM,
            YTTERBIUM,
            LUTETIUM,
            HAFNIUM,
            TANTALUM,
            TUNGSTEN,
            RHENIUM,
            OSMIUM,
            IRIDIUM,
            PLATINUM,
            GOLD,
            MERCURY,
            THALLIUM,
            LEAD,
            BISMUTH,
            POLONIUM,
            ASTATINE,
            RADON,
            FRANCIUM,
            RADAIUM,
            ACTINIUM,
            THORIUM,
            PROTACTINIUM,
            URANIUM,
            NEPTUNIUM,
            PLUTONIUM,
            AMERICIUM,
            CURIUM,
            BERKELIUM,
            CALIFORNIUM,
            EINSTEINIUM,
            FERMIUM,
            MENDELEVIUM,
            NOBELIUM,
            LAWRENCIUM,
            RUTHERFORDIUM,
            DUBNIUM,
            SEABORGIUM,
            BOHRIUM,
            HASSIUM,
            MEITNERIUM,
            DARMSTADTIUM,
            ROENTGENIUM,
            COPERNICIUM,
            NIHONIUM,
            FLEROVIUM,
            MOSCOVIUM,
            LIVERMORIUM,
            TENNESSINE,
            OGANESSON;

    

    public static void register(RutileRegistrate registrate) {
        NULL = createElement("null", "?", 0xff171a2d, registrate);
        HYDROGEN = createElement("hydrogen", "H", 0xff9175dc, registrate);
        HELIUM = createElement("helium", "He", 0xfffcc6f7, registrate);
        LITHIUM = createElement("lithium", "Li", 0xff989890, registrate);
        BERYLLIUM = createElement("beryllium", "Be", 0xff838489, registrate);
        BORON = createElement("boron", "B", 0xff6d7079, registrate);
        CARBON = createElement("carbon", "C", 0xff626061, registrate);
        NITROGEN = createElement("nitrogen", "N", 0xffdfd9d9, registrate);
        OXYGEN = createElement("oxygen", "O", 0xffc7e4f6, registrate);
        FLUORINE = createElement("fluorine", "F", 0xffd0d97e, registrate);
        NEON = createElement("neon", "Ne", 0xffdb608f, registrate);
        SODIUM = createElement("sodium", "Na", 0xffc9bfbe, registrate);
        MAGNESIUM = createElement("magnesium", "Mg", 0xffb7b7b7, registrate);
        ALUMINUM = createElement("aluminum", "Al", 0xffcccfd4, registrate);
        SILICON = createElement("silicon", "Si", 0xff81848d, registrate);
        PHOSPHORUS = createElement("phosphorus", "P", 0xffa16567, registrate);
        SULFUR = createElement("sulfur", "S", 0xffdae096, registrate);
        CHLORINE = createElement("chlorine", "Cl", 0xffc1bb1f, registrate);
        ARGON = createElement("argon", "Ar", 0xffb93de9, registrate);
        POTASSIUM = createElement("potassium", "K", 0xff9aa3a2, registrate);
        CALCIUM = createElement("calcium", "Ca", 0xffb4ad9d, registrate);
        SCANDIUM = createElement("scandium", "Sc", 0xffa8a095, registrate);
        TITANIUM = createElement("titanium", "Ti", 0xffacada5, registrate);
        VANADIUM = createElement("vanadium", "V", 0xff99a1a4, registrate);
        CHROMIUM = createElement("chromium", "Cr", 0xffb4b7c0, registrate);
        MANGANESE = createElement("manganese", "Mn", 0xff746f6c, registrate);
        IRON = createElement("iron", "Fe", 0xff949496, registrate);
        COBALT = createElement("cobalt", "Co", 0xffb0afb5, registrate);
        NICKEL = createElement("nickel", "Ni", 0xffa3a29e, registrate);
        COPPER = createElement("copper", "Cu", 0xffdcb491, registrate);
        ZINC = createElement("zinc", "Zn", 0xffb5bdc0, registrate);
        GALLIUM = createElement("gallium", "Ga", 0xffb5c1cd, registrate);
        GERMANIUM = createElement("germanium", "Ge", 0xff7d8379, registrate);
        ARSENIC = createElement("arsenic", "As", 0xff92948f, registrate);
        SELENIUM = createElement("selenium", "Se", 0xff5f676a, registrate);
        BROMINE = createElement("bromine", "Br", 0xffd39131, registrate);
        KRYPTON = createElement("krypton", "Kr", 0xffc6b4e8, registrate);
        RUBIDIUM = createElement("rubidium", "Rb", 0xff9b9b93, registrate);
        STRONTIUM = createElement("strontium", "Sr", 0xff868782, registrate);
        YTTRIUM = createElement("yttrium", "Y", 0xffa8a095, registrate);
        ZIRCONIUM = createElement("zirconium", "Zr", 0xffafaaa7, registrate);
        NIOBIUM = createElement("niobium", "Nb", 0xff91908c, registrate);
        MOLYBDENUM = createElement("molybdenum", "Mo", 0xff878791, registrate);
        TECHNETIUM = createElement("technetium", "Tc", 0xff796f66, registrate);
        RUTHENIUM = createElement("ruthenium", "Ru", 0xffa2a2a4, registrate);
        RHODIUM = createElement("rhodium", "Rh", 0xffc7c2bf, registrate);
        PALLADIUM = createElement("palladium", "Pd", 0xffadacaa, registrate);
        SILVER = createElement("silver", "Ag", 0xffdddfda, registrate);
        CADMIUM = createElement("cadmium", "Cd", 0xffb4b4b4, registrate);
        INDIUM = createElement("indium", "In", 0xffd5d0cd, registrate);
        TIN = createElement("tin", "Sn", 0xffc2c2c2, registrate);
        ANTIMONY = createElement("antimony", "Sb", 0xffa7afb2, registrate);
        TELLURIUM = createElement("tellurium", "Te", 0xff827d7a, registrate);
        IODINE = createElement("iodine", "I", 0xff983087, registrate);
        XENON = createElement("xenon", "Xe", 0xff7299f6, registrate);
        CESIUM = createElement("cesium", "Cs", 0xffb2aa7c, registrate);
        BARIUM = createElement("barium", "Ba", 0xff676964, registrate);
        LANTHANUM = createElement("lanthanum", "La", 0xff8c8d92, registrate);
        CERIUM = createElement("cerium", "Ce", 0xff7b7c74, registrate);
        PRASEODYMIUM = createElement("praseodymium", "Pr", 0xff99989e, registrate);
        NEODYMIUM = createElement("neodymium", "Nd", 0xff727473, registrate);
        PROMETHIUM = createElement("promethium", "Pm", 0xff32323c, registrate);
        SAMARIUM = createElement("samarium", "Sm", 0xff515257, registrate);
        EUROPIUM = createElement("europium", "Eu", 0xff838b8e, registrate);
        GADOLINIUM = createElement("gadolinium", "Gd", 0xff959589, registrate);
        TERBIUM = createElement("terbium", "Tb", 0xffa4a3a1, registrate);
        DYSPROSIUM = createElement("dysprosium", "Dy", 0xff8e8986, registrate);
        HOLMIUM = createElement("holmium", "Ho", 0xff9a9a92, registrate);
        ERBIUM = createElement("erbium", "Er", 0xff9e9f97, registrate);
        THULIUM = createElement("thulium", "Tm", 0xff8e8c8d, registrate);
        YTTERBIUM = createElement("ytterbium", "Yb", 0xff979799, registrate);
        LUTETIUM = createElement("lutetium", "Lu", 0xffa6a6a4, registrate);
        HAFNIUM = createElement("hafnium", "Hf", 0xffa19c99, registrate);
        TANTALUM = createElement("tantalum", "Ta", 0xff8d9695, registrate);
        TUNGSTEN = createElement("tungsten", "W", 0xff797876, registrate);
        RHENIUM = createElement("rhenium", "Re", 0xffa19fac, registrate);
        OSMIUM = createElement("osmium", "Os", 0xff95a6ad, registrate);
        IRIDIUM = createElement("iridium", "Ir", 0xffaba1a0, registrate);
        PLATINUM = createElement("platinum", "Pt", 0xffc5c4c0, registrate);
        GOLD = createElement("gold", "Au", 0xffd1c186, registrate);
        MERCURY = createElement("mercury", "Hg", 0xff898a8c, registrate);
        THALLIUM = createElement("thallium", "Tl", 0xff7d7a81, registrate);
        LEAD = createElement("lead", "Pb", 0xff8f929b, registrate);
        BISMUTH = createElement("bismuth", "Bi", 0xffbcb6b6, registrate);
        POLONIUM = createElement("polonium", "Po", 0xff30333c, registrate);
        ASTATINE = createElement("astatine", "At", 0xff2a2a2a, registrate);
        RADON = createElement("radon", "Rn", 0xff2e313a, registrate);
        FRANCIUM = createElement("francium", "Fr", 0xff262626, registrate);
        RADAIUM = createElement("radium", "Ra", 0xffa79a87, registrate);
        ACTINIUM = createElement("actinium", "Ac", 0xff2b2d39, registrate);
        THORIUM = createElement("thorium", "Th", 0xff7e807d, registrate);
        PROTACTINIUM = createElement("protactinium", "Pa", 0xff48525e, registrate);
        URANIUM = createElement("uranium", "U", 0xff85807d, registrate);
        NEPTUNIUM = createElement("neptunium", "Np", 0xff9d9892, registrate);
        PLUTONIUM = createElement("plutonium", "Pu", 0xff6f3d40, registrate);
        AMERICIUM = createElement("americium", "Am", 0xff606166, registrate);
        CURIUM = createElement("curium", "Cm", 0xff949085, registrate);
        BERKELIUM = createElement("berkelium", "Bk", 0xff787775, registrate);
        CALIFORNIUM = createElement("californium", "Cf", 0xff8c8686, registrate);
        EINSTEINIUM = createElement("einsteinium", "Es", 0xff333439, registrate);
        FERMIUM = createElement("fermium", "Fm", 0xff292c35, registrate);
        MENDELEVIUM = createElement("mendelevium", "Md", 0xff2e2d3d, registrate);
        NOBELIUM = createElement("nobelium", "No", 0xff242424, registrate);
        LAWRENCIUM = createElement("lawrencium", "Lr", 0xff242424, registrate);
        RUTHERFORDIUM = createElement("rutherfordium", "Rf", 0xff2a2529, registrate);
        DUBNIUM = createElement("dubnium", "Db", 0xff2b2f38, registrate);
        SEABORGIUM = createElement("seaborgium", "Sg", 0xff262626, registrate);
        BOHRIUM = createElement("bohrum", "Bh", 0xff282629, registrate);
        HASSIUM = createElement("hassium", "Hs", 0xff282828, registrate);
        MEITNERIUM = createElement("meitnerium", "Mt", 0xff262628, registrate);
        DARMSTADTIUM = createElement("darmstadtium", "Ds", 0xff262626, registrate);
        ROENTGENIUM = createElement("roentgenium", "Rg", 0xff262427, registrate);
        COPERNICIUM = createElement("copernicium", "Cn", 0xff262626, registrate);
        NIHONIUM = createElement("nihonium", "Nh", 0xff252328, registrate);
        FLEROVIUM = createElement("flerovium", "Fl", 0xff2b2b2b, registrate);
        MOSCOVIUM = createElement("moscovium", "Mc", 0xff242426, registrate);
        LIVERMORIUM = createElement("livermorium", "Lv", 0xff292728, registrate);
        TENNESSINE = createElement("tennessine", "Ts", 0xff262628, registrate);
        OGANESSON = createElement("oganesson", "Og", 0xff2a2a2a, registrate);
    }

    private static ElementEntry<Element> createElement(String name, String symbol, int colour, RutileRegistrate registrate) {
        return registrate.element(name, symbol, Element::new).properties(p -> p.color(colour)).register();
    }

}
