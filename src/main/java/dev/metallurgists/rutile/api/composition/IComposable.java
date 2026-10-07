package dev.metallurgists.rutile.api.composition;

import dev.metallurgists.rutile.api.composition.element.ElementStack;
import dev.metallurgists.rutile.api.data.ISerializable;
import net.createmod.catnip.utility.lang.LangBuilder;

import java.util.HashMap;

public interface IComposable extends ISerializable {
	
	int getColor();
	
	LangBuilder getDisplay();
	
	HashMap<ElementStack, Integer> getContainedElements();
}
