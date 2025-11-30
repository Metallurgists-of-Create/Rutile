package dev.metallurgists.rutile.api.plugin;

import com.tterrag.registrate.AbstractRegistrate;
import dev.metallurgists.rutile.api.dynamic_pack.data.recipe.handler.*;
import dev.metallurgists.rutile.api.registrate.RutileRegistrate;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class PluginConfig {
    private String modid;
    private IRutileRecipeHandler runtimeRecipes;
    private IRutileMaterialRecipeHandler runtimeMaterialRecipes;
    private IRutileRecipeRemover runtimeRecipeRemover;
    private AbstractRegistrate<?> registrate;

    public PluginConfig() {
        this.modid = "";
        this.runtimeRecipes = new EmptyRecipeHandler();
        this.runtimeMaterialRecipes = new EmptyMaterialRecipeHandler();
        this.runtimeRecipeRemover = new EmptyRecipeRemover();
        this.registrate = RutileRegistrate.create(modid);
    }

    /**
     * The modid for this plugin
     * @return the modid
     */
    public String getModId() {
        return this.modid;
    }

    /**
     * Sets the mod id for this plugin
     * @param modid the modid
     */
    public void setModId(String modid) {
        this.modid = modid;
    }

    /**
     * The runtime recipe handler for this plugin
     * @return the runtime recipe handler
     */
    public IRutileRecipeHandler getRuntimeRecipes() {
        return this.runtimeRecipes;
    }

    /**
     * Sets the runtime recipe handler for this plugin
     * @param runtimeRecipes the runtime recipe handler
     */
    public void setRuntimeRecipes(IRutileRecipeHandler runtimeRecipes) {
        this.runtimeRecipes = runtimeRecipes;
    }

    /**
     * The runtime material recipe handler for this plugin
     * @return the runtime material recipe handler
     */
    public IRutileMaterialRecipeHandler getRuntimeMaterialRecipes() {
        return this.runtimeMaterialRecipes;
    }

    /**
     * Sets the runtime material recipe handler for this plugin
     * @param runtimeMaterialRecipes the runtime material recipe handler
     */
    public void setRuntimeMaterialRecipes(IRutileMaterialRecipeHandler runtimeMaterialRecipes) {
        this.runtimeMaterialRecipes = runtimeMaterialRecipes;
    }

    /**
     * The runtime recipe remover for this plugin
     * @return the runtime recipe remover
     */
    public IRutileRecipeRemover getRuntimeRecipeRemover() {
        return this.runtimeRecipeRemover;
    }

    /**
     * Sets the runtime recipe remover for this plugin
     * @param runtimeRecipeRemover the runtime recipe remover
     */
    public void setRuntimeRecipeRemover(IRutileRecipeRemover runtimeRecipeRemover) {
        this.runtimeRecipeRemover = runtimeRecipeRemover;
    }

    /**
     * The registrate for this plugin
     * @return registrate
     */
    public AbstractRegistrate<?> getRegistrate() {
        return this.registrate;
    }

    /**
     * Sets the registrate for this plugin
     * @param registrate registrate
     */
    public void setRegistrate(AbstractRegistrate<?> registrate) {
        this.registrate = registrate;
    }
}
