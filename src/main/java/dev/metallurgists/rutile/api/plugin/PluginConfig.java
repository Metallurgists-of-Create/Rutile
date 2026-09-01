package dev.metallurgists.rutile.api.plugin;

import com.tterrag.registrate.AbstractRegistrate;
import dev.metallurgists.rutile.RutileRegistrate;
import lombok.Getter;
import lombok.Setter;

//TODO: Runtime recipes
public class PluginConfig {
    private String modid;

    /**
     *  The runtime recipe handler for this plugin
     */
    //@Getter
    //@Setter
    //private IRutileRecipeHandler runtimeRecipes;
    /**
     *  The runtime recipe remover for this plugin
     */
    //@Getter
    //@Setter
    //private IRutileRecipeRemover runtimeRecipeRemover;
    /**
     *  The registrate for this plugin
     */
    @Setter
    @Getter
    private AbstractRegistrate<?> registrate;

    public PluginConfig() {
        this.modid = "";
        //this.runtimeRecipes = new IRutileRecipeHandler.Empty();
        //this.runtimeRecipeRemover = new IRutileRecipeRemover.Empty();
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
