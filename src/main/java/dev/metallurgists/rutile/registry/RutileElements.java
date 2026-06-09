package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.registry.deferred.DeferredElements;
import dev.metallurgists.rutile.api.element.Element;
import dev.metallurgists.rutile.api.element.ElementLike;
import dev.metallurgists.rutile.util.RegistryHelper;
import net.minecraft.world.flag.FeatureFlag;

public class RutileElements {

    public static final ElementLike NULL = create("null", "?", 0xffbf4cd2),
            H  =  create("hydrogen", "H", 0xff9175dc),
            He =  create("helium", "He", 0xfffcc6f7),
            Li =  create("lithium", "Li", 0xff989890),
            Be =  create("beryllium", "Be", 0xff838489),
            B  =  create("boron", "B", 0xff6d7079),
            C  =  create("carbon", "C", 0xff626061),
            N  =  create("nitrogen", "N", 0xffdfd9d9),
            O  =  create("oxygen", "O", 0xffc7e4f6),
            F  =  create("fluorine", "F", 0xffd0d97e),
            Ne =  create("neon", "Ne", 0xffdb608f),
            Na =  create("sodium", "Na", 0xffc9bfbe),
            Mg =  create("magnesium", "Mg", 0xffb7b7b7),
            Al =  create("aluminum", "Al", 0xffcccfd4),
            Si =  create("silicon", "Si", 0xff81848d),
            P  =  create("phosphorus", "P", 0xffa16567),
            S  =  create("sulfur", "S", 0xffdae096),
            Cl =  create("chlorine", "Cl", 0xffc1bb1f),
            Ar =  create("argon", "Ar", 0xffb93de9),
            K  =  create("potassium", "K", 0xff9aa3a2),
            Ca =  create("calcium", "Ca", 0xffb4ad9d),
            Sc =  create("scandium", "Sc", 0xffa8a095),
            Ti =  create("titanium", "Ti", 0xffacada5),
            V  =  create("vanadium", "V", 0xff99a1a4),
            Cr =  create("chromium", "Cr", 0xffb4b7c0),
            Mn =  create("manganese", "Mn", 0xff746f6c),
            Fe =  create("iron", "Fe", 0xff949496),
            Co =  create("cobalt", "Co", 0xffb0afb5),
            Ni =  create("nickel", "Ni", 0xffa3a29e),
            Cu =  create("copper", "Cu", 0xffdcb491),
            Zn =  create("zinc", "Zn", 0xffb5bdc0),
            Ga =  create("gallium", "Ga", 0xffb5c1cd),
            Ge =  create("germanium", "Ge", 0xff7d8379),
            As =  create("arsenic", "As", 0xff92948f),
            Se =  create("selenium", "Se", 0xff5f676a),
            Br =  create("bromine", "Br", 0xffd39131),
            Kr =  create("krypton", "Kr", 0xffc6b4e8),
            Rb =  create("rubidium", "Rb", 0xff9b9b93),
            Sr =  create("strontium", "Sr", 0xff868782),
            Y  =  create("yttrium", "Y", 0xffa8a095),
            Zr =  create("zirconium", "Zr", 0xffafaaa7),
            Nb =  create("niobium", "Nb", 0xff91908c),
            Mo =  create("molybdenum", "Mo", 0xff878791),
            Tc =  create("technetium", "Tc", 0xff796f66),
            Ru =  create("ruthenium", "Ru", 0xffa2a2a4),
            Rh =  create("rhodium", "Rh", 0xffc7c2bf),
            Pd =  create("palladium", "Pd", 0xffadacaa),
            Ag =  create("silver", "Ag", 0xffdddfda),
            Cd =  create("cadmium", "Cd", 0xffb4b4b4),
            In =  create("indium", "In", 0xffd5d0cd),
            Sn =  create("tin", "Sn", 0xffc2c2c2),
            Sb =  create("antimony", "Sb", 0xffa7afb2),
            Te =  create("tellurium", "Te", 0xff827d7a),
            I  =  create("iodine", "I", 0xff983087),
            Xe =  create("xenon", "Xe", 0xff7299f6),
            Cs =  create("cesium", "Cs", 0xffb2aa7c),
            Ba =  create("barium", "Ba", 0xff676964),
            La =  create("lanthanum", "La", 0xff8c8d92),
            Ce =  create("cerium", "Ce", 0xff7b7c74),
            Pr =  create("praseodymium", "Pr", 0xff99989e),
            Nd =  create("neodymium", "Nd", 0xff727473),
            Pm =  create("promethium", "Pm", 0xff32323c),
            Sm =  create("samarium", "Sm", 0xff515257),
            Eu =  create("europium", "Eu", 0xff838b8e),
            Gd =  create("gadolinium", "Gd", 0xff959589),
            Tb =  create("terbium", "Tb", 0xffa4a3a1),
            Dy =  create("dysprosium", "Dy", 0xff8e8986),
            Ho =  create("holmium", "Ho", 0xff9a9a92),
            Er =  create("erbium", "Er", 0xff9e9f97),
            Tm =  create("thulium", "Tm", 0xff8e8c8d),
            Yb =  create("ytterbium", "Yb", 0xff979799),
            Lu =  create("lutetium", "Lu", 0xffa6a6a4),
            Hf =  create("hafnium", "Hf", 0xffa19c99),
            Ta =  create("tantalum", "Ta", 0xff8d9695),
            W  =  create("tungsten", "W", 0xff797876),
            Re =  create("rhenium", "Re", 0xffa19fac),
            Os =  create("osmium", "Os", 0xff95a6ad),
            Ir =  create("iridium", "Ir", 0xffaba1a0),
            Pt =  create("platinum", "Pt", 0xffc5c4c0),
            Au =  create("gold", "Au", 0xffd1c186),
            Hg =  create("mercury", "Hg", 0xff898a8c),
            Tl =  create("thallium", "Tl", 0xff7d7a81),
            Pb =  create("lead", "Pb", 0xff8f929b),
            Bi =  create("bismuth", "Bi", 0xffbcb6b6),
            Po =  create("polonium", "Po", 0xff30333c),
            At =  create("astatine", "At", 0xff2a2a2a),
            Rn =  create("radon", "Rn", 0xff2e313a),
            Fr =  create("francium", "Fr", 0xff262626),
            Ra =  create("radium", "Ra", 0xffa79a87),
            Ac =  create("actinium", "Ac", 0xff2b2d39),
            Th =  create("thorium", "Th", 0xff7e807d),
            Pa =  create("protactinium", "Pa", 0xff48525e),
            U  =  create("uranium", "U", 0xff85807d),
            Np =  create("neptunium", "Np", 0xff9d9892),
            Pu =  create("plutonium", "Pu", 0xff6f3d40),
            Am =  create("americium", "Am", 0xff606166),
            Cm =  create("curium", "Cm", 0xff949085),
            Bk =  create("berkelium", "Bk", 0xff787775),
            Cf =  create("californium", "Cf", 0xff8c8686),
            Es =  create("einsteinium", "Es", 0xff333439),
            Fm =  create("fermium", "Fm", 0xff292c35),
            Md =  create("mendelevium", "Md", 0xff2e2d3d),
            No =  create("nobelium", "No", 0xff242424),
            Lr =  create("lawrencium", "Lr", 0xff242424),
            Rf =  create("rutherfordium", "Rf", 0xff2a2529),
            Db =  create("dubnium", "Db", 0xff2b2f38),
            Sg =  create("seaborgium", "Sg", 0xff262626),
            Bh =  create("bohrium", "Bh", 0xff282629),
            Hs =  create("hassium", "Hs", 0xff282828),
            Mt =  create("meitnerium", "Mt", 0xff262628),
            Ds =  create("darmstadtium", "Ds", 0xff262626),
            Rg =  create("roentgenium", "Rg", 0xff262427),
            Cn =  create("copernicium", "Cn", 0xff262626),
            Nh =  create("nihonium", "Nh", 0xff252328),
            Fl =  create("flerovium", "Fl", 0xff2b2b2b),
            Mc =  create("moscovium", "Mc", 0xff242426),
            Lv =  create("livermorium", "Lv", 0xff292728),
            Ts =  create("tennessine", "Ts", 0xff262628),
            Og =  create("oganesson", "Og", 0xff2a2a2a);

    public static void init() {}

    public static ElementLike create(String prename, String symbol, int colour) {
        String name = "element_" + prename;
        Element element = new Element(symbol, colour, Rutile.getResource(name));
        DeferredElements HELPER = RegistryHelper.createElements(element.getModId());
        return HELPER.register(element.getName(), () -> element);
    }

    public static ElementLike create(String prename, String symbol, int colour, FeatureFlag... requiredFeatures) {
        String name = "element_" + prename;
        Element element = new Element(symbol, colour, Rutile.getResource(name), requiredFeatures);
        DeferredElements HELPER = RegistryHelper.createElements(element.getModId());
        return HELPER.register(element.getName(), () -> element);
    }
}
