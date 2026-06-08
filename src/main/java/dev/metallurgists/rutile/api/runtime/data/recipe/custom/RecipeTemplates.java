package dev.metallurgists.rutile.api.runtime.data.recipe.custom;

import static dev.metallurgists.rutile.api.runtime.data.recipe.custom.CreateRecipe.*;

public class RecipeTemplates {

    public static CreateRecipe CRUSHING = new SimpleCreateRecipe("crushing", 1, 7, false, true);

    public static CreateRecipe CUTTING = new SimpleCreateRecipe("cutting", 1, 4, false, true);

    public static CreateRecipe MILLING = new SimpleCreateRecipe("milling", 1, 4, false, true);

    public static CreateRecipe MIXING = new SimpleCreateRecipe("mixing", 64, 4, true, true, 2, 2);

    public static CreateRecipe COMPACTING = new SimpleCreateRecipe("compacting", 64, 4, true, true, 2, 2);

    public static CreateRecipe PRESSING = new SimpleCreateRecipe("pressing", 1, 2);

    public static CreateRecipe SANDPAPER = new SimpleCreateRecipe("sandpaper_polishing", 1, 1);

    public static CreateRecipe SPLASHING = new SimpleCreateRecipe("splashing", 1, 12);

    public static CreateRecipe HAUNTING = new SimpleCreateRecipe("haunting", 1, 12);

    public static CreateRecipe FILLING = new SimpleCreateRecipe("filling", 1, 1, 1, 0);

    public static CreateRecipe EMPTYING = new SimpleCreateRecipe("emptying", 1, 1, 0, 1);

    public static CreateRecipe ITEM_APPLICATION = new ItemApplication();

    public static CreateRecipe DEPLOYING = new Deploying();
}
