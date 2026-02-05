package dev.metallurgists.rutile.api.runtime.data.recipe.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.Rutile;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;

public abstract class CreateRecipe extends CustomRecipe<RecipeInput> {

    protected NonNullList<Ingredient> ingredients;
    protected NonNullList<ProcessingOutput> results;
    protected NonNullList<SizedFluidIngredient> fluidIngredients;
    protected NonNullList<FluidStack> fluidResults;
    protected int processingDuration;
    protected HeatCondition requiredHeat;

    protected abstract ResourceLocation getId();

    protected abstract int getMaxInputCount();

    protected abstract int getMaxOutputCount();

    protected boolean canRequireHeat() {
        return false;
    }

    protected boolean canSpecifyDuration() {
        return false;
    }

    protected int getMaxFluidInputCount() {
        return 0;
    }

    protected int getMaxFluidOutputCount() {
        return 0;
    }

    public CreateRecipe input(Ingredient input) {
        if (this.ingredients.size() >= getMaxInputCount()) {
            return this;
        }
        this.ingredients.add(input);
        return this;
    }

    public CreateRecipe input(ItemLike input) {
        return input(Ingredient.of(input));
    }

    public CreateRecipe input(ItemStack input) {
        return input(Ingredient.of(input));
    }

    public CreateRecipe input(SizedFluidIngredient input) {
        if (this.fluidIngredients.size() >= getMaxFluidInputCount()) {
            return this;
        }
        this.fluidIngredients.add(input);
        return this;
    }

    public CreateRecipe input(FluidIngredient input, int amount) {
        return this.input(new SizedFluidIngredient(input, amount));
    }

    public CreateRecipe input(FluidStack input, int amount) {
        return this.input(FluidIngredient.of(input), amount);
    }

    public CreateRecipe result(float chance, ItemLike output, DataComponentPatch patch, int count) {
        if (this.results.size() >= getMaxOutputCount()) {
            return this;
        }
        this.results.add(new ProcessingOutput(output.asItem(), count, patch, chance));
        return this;
    }

    public CreateRecipe result(ItemLike output, DataComponentPatch patch, int count) {
        return this.result(1, output, patch, count);
    }

    public CreateRecipe result(ItemLike output, DataComponentPatch patch) {
        return this.result(output, patch, 1);
    }

    public CreateRecipe result(float chance, ItemStack output, int count) {
        return this.result(chance, output.getItem(), output.getComponentsPatch(), count);
    }

    public CreateRecipe result(ItemStack output, int count) {
        return this.result(1, output, count);
    }

    public CreateRecipe result(ItemStack output) {
        return this.result(output, 1);
    }

    public CreateRecipe result(float chance, ItemLike output, int count) {
        return this.result(chance, output, DataComponentPatch.EMPTY, count);
    }

    public CreateRecipe result(ItemLike output, int count) {
        return this.result(1, output, count);
    }

    public CreateRecipe result(ItemLike output) {
        return this.result(output, 1);
    }

    public CreateRecipe result(FluidStack output) {
        if (this.fluidResults.size() >= getMaxFluidOutputCount()) {
            return this;
        }
        this.fluidResults.add(output);
        return this;
    }

    public CreateRecipe result(Fluid output, int amount) {
        return result(new FluidStack(output, amount));
    }

    public CreateRecipe heated() {
        if (canRequireHeat()) {
            this.requiredHeat = HeatCondition.HEATED;
        }
        return this;
    }

    public CreateRecipe superheated() {
        if (canRequireHeat()) {
            this.requiredHeat = HeatCondition.SUPERHEATED;
        }
        return this;
    }

    public CreateRecipe duration(int duration) {
        if (canSpecifyDuration()) {
            this.processingDuration = duration;
        }
        return this;
    }


    public JsonObject serializeExtra(JsonObject jsonObject) {
        return jsonObject;
    }

    @Override
    public JsonElement serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("type", getId().toString());
        JsonArray ingredients = new JsonArray();
        for (Ingredient ingredient : this.ingredients) {
            Ingredient.CODEC.encodeStart(JsonOps.INSTANCE, ingredient).result().ifPresent(ingredients::add);
        }
        for (SizedFluidIngredient ingredient : this.fluidIngredients) {
            SizedFluidIngredient.NESTED_CODEC.encodeStart(JsonOps.INSTANCE, ingredient).result().ifPresent(ingredients::add);
        }
        json.add("ingredients", ingredients);
        JsonArray results = new JsonArray();
        for (ProcessingOutput result : this.results) {
            ProcessingOutput.CODEC.encodeStart(JsonOps.INSTANCE, result).result().ifPresent(results::add);
        }
        for (FluidStack result : this.fluidResults) {
            FluidStack.CODEC.encodeStart(JsonOps.INSTANCE, result).result().ifPresent(results::add);
        }
        json.add("results", results);
        if (processingDuration != 0) json.addProperty("processing_time", processingDuration);
        if (requiredHeat != HeatCondition.NONE) json.addProperty("heat_requirement", requiredHeat.getSerializedName());
        return serializeExtra(json);
    }

    protected enum HeatCondition implements StringRepresentable {
        NONE, HEATED, SUPERHEATED;

        @Override
        public @NotNull String getSerializedName() {
            return Lang.asId(name());
        }
    }

    protected record ProcessingOutput(Item item, int count, DataComponentPatch patch, float chance) {

        public static final Codec<ProcessingOutput> CODEC = RecordCodecBuilder.create(i -> i.group(
                BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(s -> s.item),
                ExtraCodecs.intRange(1, 99).optionalFieldOf("count", 1).forGetter(s -> s.count),
                DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(s -> s.patch),
                ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("chance", 1F).forGetter(s -> s.chance)
        ).apply(i, ProcessingOutput::new));

    }

    public static class Crushing extends CreateRecipe {

        @Override
        protected ResourceLocation getId() {
            return Rutile.id("create:crushing");
        }

        @Override
        protected int getMaxInputCount() {
            return 1;
        }

        @Override
        protected int getMaxOutputCount() {
            return 7;
        }

        @Override
        protected boolean canSpecifyDuration() {
            return true;
        }
    }

    public static class Cutting extends CreateRecipe {
        @Override
        protected ResourceLocation getId() {
            return Rutile.id("create:cutting");
        }

        @Override
        protected int getMaxInputCount() {
            return 1;
        }

        @Override
        protected int getMaxOutputCount() {
            return 4;
        }

        @Override
        protected boolean canSpecifyDuration() {
            return true;
        }
    }

    public static class Milling extends Crushing {
        @Override
        protected ResourceLocation getId() {
            return Rutile.id("create:milling");
        }

        @Override
        protected int getMaxOutputCount() {
            return 4;
        }
    }

    public abstract static class Basin extends CreateRecipe {
        @Override
        protected int getMaxInputCount() {
            return 64;
        }
        @Override
        protected int getMaxOutputCount() {
            return 4;
        }

        @Override
        protected int getMaxFluidInputCount() {
            return 2;
        }

        @Override
        protected int getMaxFluidOutputCount() {
            return 2;
        }

        @Override
        protected boolean canRequireHeat() {
            return true;
        }

        @Override
        protected boolean canSpecifyDuration() {
            return true;
        }
    }

    public static class Mixing extends Basin {
        @Override
        protected ResourceLocation getId() {
            return Rutile.id("create:mixing");
        }
    }

    public static class Compacting extends Basin {
        @Override
        protected ResourceLocation getId() {
            return Rutile.id("create:compacting");
        }
    }

    public static class Pressing extends CreateRecipe {
        @Override
        protected ResourceLocation getId() {
            return Rutile.id("create:pressing");
        }

        @Override
        protected int getMaxInputCount() {
            return 1;
        }

        @Override
        protected int getMaxOutputCount() {
            return 2;
        }
    }

    public static class Sandpaper extends CreateRecipe {
        @Override
        protected ResourceLocation getId() {
            return Rutile.id("create:sandpaper_polishing");
        }

        @Override
        protected int getMaxInputCount() {
            return 1;
        }

        @Override
        protected int getMaxOutputCount() {
            return 1;
        }
    }

    public abstract static class Fan extends CreateRecipe {
        @Override
        protected int getMaxInputCount() {
            return 1;
        }

        @Override
        protected int getMaxOutputCount() {
            return 12;
        }
    }

    public static class Splashing extends Fan {
        @Override
        protected ResourceLocation getId() {
            return Rutile.id("create:splashing");
        }
    }

    public static class Haunting extends Fan {
        @Override
        protected ResourceLocation getId() {
            return Rutile.id("create:haunting");
        }
    }

    public static class ItemApplication extends CreateRecipe {
        private boolean keepHeldItem;

        @Override
        protected ResourceLocation getId() {
            return Rutile.id("create:item_application");
        }

        @Override
        protected int getMaxInputCount() {
            return 2;
        }

        @Override
        protected int getMaxOutputCount() {
            return 4;
        }

        public ItemApplication keepHeldItem(boolean keepHeldItem) {
            this.keepHeldItem = keepHeldItem;
            return this;
        }

        @Override
        public JsonObject serializeExtra(JsonObject jsonObject) {
            jsonObject.addProperty("keep_held_item", keepHeldItem);
            return jsonObject;
        }
    }

    public static class Deploying extends ItemApplication {
        @Override
        protected ResourceLocation getId() {
            return Rutile.id("create:deploying");
        }
    }

    public static class Filling extends CreateRecipe {
        @Override
        protected ResourceLocation getId() {
            return Rutile.id("create:filling");
        }

        @Override
        protected int getMaxInputCount() {
            return 1;
        }
        @Override
        protected int getMaxOutputCount() {
            return 1;
        }
        @Override
        protected int getMaxFluidInputCount() {
            return 1;
        }
    }

    public static class Emptying extends CreateRecipe {
        @Override
        protected ResourceLocation getId() {
            return Rutile.id("create:emptying");
        }

        @Override
        protected int getMaxInputCount() {
            return 1;
        }
        @Override
        protected int getMaxOutputCount() {
            return 1;
        }
        @Override
        protected int getMaxFluidOutputCount() {
            return 1;
        }
    }
}
