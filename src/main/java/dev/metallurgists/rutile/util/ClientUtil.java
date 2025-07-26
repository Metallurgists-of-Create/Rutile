package dev.metallurgists.rutile.util;

import com.google.common.base.CaseFormat;
import dev.metallurgists.rutile.Rutile;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.lang.LangNumberFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class ClientUtil {

    public static Level getWorld() {
        return Minecraft.getInstance().level;
    }

    public static Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static boolean isClientSide() {
        return FMLEnvironment.dist.isClient();
    }

    public static String fromId(String key) {
        String s = key.replaceAll("_", " ");
        s = Arrays.stream(StringUtils.splitByCharacterTypeCamelCase(s)).map(StringUtils::capitalize).collect(Collectors.joining(" "));
        s = StringUtils.normalizeSpace(s);
        return s;
    }

    public static String toLowerCaseUnder(String string) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, string);
    }

    public static String toEnglishName(Object internalName) {
        return Arrays.stream(internalName.toString().toLowerCase(Locale.ROOT).split("_"))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(" "));
    }

    public static boolean langExists(String localisationKey) {
        if (isClientSide()) {
            return I18n.exists(localisationKey);
        } else {
            return false;
        }
    }

    private static final int SMALL_DOWN_NUMBER_BASE = '\u2080';
    private static final int SMALL_UP_NUMBER_BASE = '\u2070';
    private static final int SMALL_UP_NUMBER_TWO = '\u00B2';
    private static final int SMALL_UP_NUMBER_THREE = '\u00B3';
    private static final int NUMBER_BASE = '0';

    public static String toSmallUpNumbers(String string) {
        return checkNumbers(string, SMALL_UP_NUMBER_BASE, true);
    }

    public static String toSmallDownNumbers(String string) {
        return checkNumbers(string, SMALL_DOWN_NUMBER_BASE, false);
    }

    @NotNull
    private static String checkNumbers(String string, int smallUpNumberBase, boolean isUp) {
        char[] charArray = string.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            int relativeIndex = charArray[i] - NUMBER_BASE;
            if (relativeIndex >= 0 && relativeIndex <= 9) {
                if (isUp) {
                    if (relativeIndex == 2 ) {
                        charArray[i] = SMALL_UP_NUMBER_TWO;
                        continue;
                    } else if (relativeIndex == 3) {
                        charArray[i] = SMALL_UP_NUMBER_THREE;
                        continue;
                    }
                }
                int newChar = smallUpNumberBase + relativeIndex;
                charArray[i] = (char) newChar;
            }
        }
        return new String(charArray);
    }

    public static LangBuilder lang() {
        return new LangBuilder(Rutile.ID);
    }
    public static LangBuilder number(double d) {
        return lang().text(LangNumberFormat.format(d));
    }
}
