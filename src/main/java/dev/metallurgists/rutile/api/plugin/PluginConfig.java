package dev.metallurgists.rutile.api.plugin;

import com.tterrag.registrate.AbstractRegistrate;
import dev.metallurgists.rutile.RutileRegistrate;
import dev.metallurgists.rutile.api.runtime.data.recipe.handler.IRutileMaterialRecipeHandler;
import dev.metallurgists.rutile.api.runtime.data.recipe.handler.IRutileRecipeHandler;
import dev.metallurgists.rutile.api.runtime.data.recipe.handler.IRutileRecipeRemover;
import lombok.Getter;
import lombok.Setter;

public class PluginConfig {
    private String modid;
    /**
     * -- SETTER --
     *  Sets the runtime recipe handler for this plugin
     *
     *
     * -- GETTER --
     *  The runtime recipe handler for this plugin
     *
     @param runtimeRecipes the runtime recipe handler
      * @return the runtime recipe handler
     */
    @Getter
    @Setter
    private IRutileRecipeHandler runtimeRecipes;
    /**
     * -- SETTER --
     *  Sets the runtime material recipe handler for this plugin
     *
     *
     * -- GETTER --
     *  The runtime material recipe handler for this plugin
     *
     @param runtimeMaterialRecipes the runtime material recipe handler
      * @return the runtime material recipe handler
     */
    @Getter
    @Setter
    private IRutileMaterialRecipeHandler runtimeMaterialRecipes;
    /**
     * -- SETTER --
     *  Sets the runtime recipe remover for this plugin
     *
     *
     * -- GETTER --
     *  The runtime recipe remover for this plugin
     *
     @param runtimeRecipeRemover the runtime recipe remover
      * @return the runtime recipe remover
     */
    @Getter
    @Setter
    private IRutileRecipeRemover runtimeRecipeRemover;
    /**
     * -- GETTER --
     *  The registrate for this plugin
     *
     *
     * -- SETTER --
     *  Sets the registrate for this plugin
     *
     @return registrate
      * @param registrate registrate
     */
    @Setter
    @Getter
    private AbstractRegistrate<?> registrate;

    public PluginConfig() {
        this.modid = "";
        this.runtimeRecipes = new IRutileRecipeHandler.Empty();
        this.runtimeMaterialRecipes = new IRutileMaterialRecipeHandler.Empty();
        this.runtimeRecipeRemover = new IRutileRecipeRemover.Empty();
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

}
