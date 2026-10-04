package dev.metallurgists.rutile.registry;

import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.RegistryHelper;
import dev.metallurgists.rutile.api.composition.element.DeferredElements;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.composition.element.ElementLike;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.codehaus.plexus.util.Os;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

public class RutileElements {

    public static final ElementLike NULL = create(Element.NULL),
            H  =  create("hydrogen", "H", 0xff9175dc, 1.008),
            He =  create("helium", "He", 0xfffcc6f7, 4.0026),
            Li =  create("lithium", "Li", 0xff989890, 7.0),
            Be =  create("beryllium", "Be", 0xff838489, 9.012183),
            B  =  create("boron", "B", 0xff6d7079, 10.81),
            C  =  create("carbon", "C", 0xff626061, 12.011),
            N  =  create("nitrogen", "N", 0xffdfd9d9, 14.007),
            O  =  create("oxygen", "O", 0xffc7e4f6, 15.999),
            F  =  create("fluorine", "F", 0xffd0d97e, 18.99840316),
            Ne =  create("neon", "Ne", 0xffdb608f, 20.180),
            Na =  create("sodium", "Na", 0xffc9bfbe, 22.9897693),
            Mg =  create("magnesium", "Mg", 0xffb7b7b7, 24.305),
            Al =  create("aluminum", "Al", 0xffcccfd4, 26.981538),
            Si =  create("silicon", "Si", 0xff81848d, 28.085),
            P  =  create("phosphorus", "P", 0xffa16567, 30.973762),
            S  =  create("sulfur", "S", 0xffdae096, 32.07),
            Cl =  create("chlorine", "Cl", 0xffc1bb1f, 35.45),
            Ar =  create("argon", "Ar", 0xffb93de9, 39.9),
            K  =  create("potassium", "K", 0xff9aa3a2, 39.0983),
            Ca =  create("calcium", "Ca", 0xffb4ad9d, 40.08),
            Sc =  create("scandium", "Sc", 0xffa8a095, 44.95591),
            Ti =  create("titanium", "Ti", 0xffacada5, 47.867),
            V  =  create("vanadium", "V", 0xff99a1a4, 50.9415),
            Cr =  create("chromium", "Cr", 0xffb4b7c0, 51.996),
            Mn =  create("manganese", "Mn", 0xff746f6c, 54.93804),
            Fe =  create("iron", "Fe", 0xff949496, 55.84),
            Co =  create("cobalt", "Co", 0xffb0afb5, 58.93319),
            Ni =  create("nickel", "Ni", 0xffa3a29e, 58.693),
            Cu =  create("copper", "Cu", 0xffdcb491, 63.55),
            Zn =  create("zinc", "Zn", 0xffb5bdc0, 65.4),
            Ga =  create("gallium", "Ga", 0xffb5c1cd, 69.723),
            Ge =  create("germanium", "Ge", 0xff7d8379, 72.63),
            As =  create("arsenic", "As", 0xff92948f, 74.92159),
            Se =  create("selenium", "Se", 0xff5f676a, 78.97),
            Br =  create("bromine", "Br", 0xffd39131, 79.9),
            Kr =  create("krypton", "Kr", 0xffc6b4e8, 83.8),
            Rb =  create("rubidium", "Rb", 0xff9b9b93, 85.468),
            Sr =  create("strontium", "Sr", 0xff868782, 87.62),
            Y  =  create("yttrium", "Y", 0xffa8a095, 88.90584),
            Zr =  create("zirconium", "Zr", 0xffafaaa7, 91.22),
            Nb =  create("niobium", "Nb", 0xff91908c, 92.90637),
            Mo =  create("molybdenum", "Mo", 0xff878791, 95.95),
            Tc =  create("technetium", "Tc", 0xff796f66, 96.90636),
            Ru =  create("ruthenium", "Ru", 0xffa2a2a4, 101.1),
            Rh =  create("rhodium", "Rh", 0xffc7c2bf, 102.9055),
            Pd =  create("palladium", "Pd", 0xffadacaa, 106.42),
            Ag =  create("silver", "Ag", 0xffdddfda, 107.868),
            Cd =  create("cadmium", "Cd", 0xffb4b4b4, 112.41),
            In =  create("indium", "In", 0xffd5d0cd, 114.818),
            Sn =  create("tin", "Sn", 0xffc2c2c2, 118.71),
            Sb =  create("antimony", "Sb", 0xffa7afb2, 121.76),
            Te =  create("tellurium", "Te", 0xff827d7a, 127.6),
            I  =  create("iodine", "I", 0xff983087, 126.9045),
            Xe =  create("xenon", "Xe", 0xff7299f6, 131.29),
            Cs =  create("cesium", "Cs", 0xffb2aa7c, 132.905452),
            Ba =  create("barium", "Ba", 0xff676964, 137.33),
            La =  create("lanthanum", "La", 0xff8c8d92, 138.9055),
            Ce =  create("cerium", "Ce", 0xff7b7c74, 140.116),
            Pr =  create("praseodymium", "Pr", 0xff99989e, 140.90766),
            Nd =  create("neodymium", "Nd", 0xff727473, 144.24),
            Pm =  create("promethium", "Pm", 0xff32323c, 144.91276),
            Sm =  create("samarium", "Sm", 0xff515257, 150.4),
            Eu =  create("europium", "Eu", 0xff838b8e, 151.964),
            Gd =  create("gadolinium", "Gd", 0xff959589, 157.25),
            Tb =  create("terbium", "Tb", 0xffa4a3a1, 158.92535),
            Dy =  create("dysprosium", "Dy", 0xff8e8986, 162.5),
            Ho =  create("holmium", "Ho", 0xff9a9a92, 164.93033),
            Er =  create("erbium", "Er", 0xff9e9f97, 167.26),
            Tm =  create("thulium", "Tm", 0xff8e8c8d, 168.93422),
            Yb =  create("ytterbium", "Yb", 0xff979799, 173.05),
            Lu =  create("lutetium", "Lu", 0xffa6a6a4, 174.9667),
            Hf =  create("hafnium", "Hf", 0xffa19c99, 178.49),
            Ta =  create("tantalum", "Ta", 0xff8d9695, 180.9479),
            W  =  create("tungsten", "W", 0xff797876, 183.84),
            Re =  create("rhenium", "Re", 0xffa19fac, 186.207),
            Os =  create("osmium", "Os", 0xff95a6ad, 190.2),
            Ir =  create("iridium", "Ir", 0xffaba1a0, 192.22),
            Pt =  create("platinum", "Pt", 0xffc5c4c0, 195.08),
            Au =  create("gold", "Au", 0xffd1c186, 196.96657),
            Hg =  create("mercury", "Hg", 0xff898a8c, 200.59),
            Tl =  create("thallium", "Tl", 0xff7d7a81, 204.383),
            Pb =  create("lead", "Pb", 0xff8f929b, 207),
            Bi =  create("bismuth", "Bi", 0xffbcb6b6, 208.9804),
            Po =  create("polonium", "Po", 0xff30333c, 208.98243),
            At =  create("astatine", "At", 0xff2a2a2a, 209.98715),
            Rn =  create("radon", "Rn", 0xff2e313a, 222.01758),
            Fr =  create("francium", "Fr", 0xff262626, 223.01973),
            Ra =  create("radium", "Ra", 0xffa79a87, 226.02541),
            Ac =  create("actinium", "Ac", 0xff2b2d39, 227.02775),
            Th =  create("thorium", "Th", 0xff7e807d, 232.038),
            Pa =  create("protactinium", "Pa", 0xff48525e, 231.03588),
            U  =  create("uranium", "U", 0xff85807d, 238.0289),
            Np =  create("neptunium", "Np", 0xff9d9892, 237.048172),
            Pu =  create("plutonium", "Pu", 0xff6f3d40, 244.0642),
            Am =  create("americium", "Am", 0xff606166, 243.06138),
            Cm =  create("curium", "Cm", 0xff949085, 247.07035),
            Bk =  create("berkelium", "Bk", 0xff787775, 247.07031),
            Cf =  create("californium", "Cf", 0xff8c8686, 251.07959),
            Es =  create("einsteinium", "Es", 0xff333439, 252.083),
            Fm =  create("fermium", "Fm", 0xff292c35, 257.09511),
            Md =  create("mendelevium", "Md", 0xff2e2d3d, 258.09843),
            No =  create("nobelium", "No", 0xff242424, 259.101),
            Lr =  create("lawrencium", "Lr", 0xff242424, 266.120),
            Rf =  create("rutherfordium", "Rf", 0xff2a2529, 267.122),
            Db =  create("dubnium", "Db", 0xff2b2f38, 268.126),
            Sg =  create("seaborgium", "Sg", 0xff262626, 269.128),
            Bh =  create("bohrium", "Bh", 0xff282629, 270.133),
            Hs =  create("hassium", "Hs", 0xff282828, 269.1336),
            Mt =  create("meitnerium", "Mt", 0xff262628, 277.154),
            Ds =  create("darmstadtium", "Ds", 0xff262626, 282.166),
            Rg =  create("roentgenium", "Rg", 0xff262427, 282.169),
            Cn =  create("copernicium", "Cn", 0xff262626, 286.179),
            Nh =  create("nihonium", "Nh", 0xff252328, 286.182),
            Fl =  create("flerovium", "Fl", 0xff2b2b2b, 290.192),
            Mc =  create("moscovium", "Mc", 0xff242426, 290.196),
            Lv =  create("livermorium", "Lv", 0xff292728, 293.205),
            Ts =  create("tennessine", "Ts", 0xff262628, 294.211),
            Og =  create("oganesson", "Og", 0xff2a2a2a, 295.216);

    public static void init() {}

    public static @Nullable Element get(ResourceLocation id) {
        Registry<Element> registry = RutileRegistries.ELEMENTS_REGISTRY;
        return registry.containsKey(id) ? registry.get(id) : null;
    }

    public static List<Element> getAll() {
        return RutileRegistries.ELEMENTS_REGISTRY.stream().toList();
    }

    public static ElementLike create(String name, String symbol, int colour, double mass) {
        return create(new Element(symbol, colour, mass, Rutile.id(name)));
    }

    public static ElementLike create(ResourceLocation id, String symbol, int colour, double mass) {
        return create(new Element(symbol, colour, mass, id));
    }

    public static ElementLike create(Element element) {
        DeferredElements HELPER = RegistryHelper.createElements(element.getModId());
        return HELPER.register(element.getName(), () -> element);
    }
}
