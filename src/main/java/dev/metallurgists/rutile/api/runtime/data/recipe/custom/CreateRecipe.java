package dev.metallurgists.rutile.api.runtime.data.recipe.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.rutile.Rutile;
import lombok.Getter;
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

    private static class SimpleCreateRecipe extends CreateRecipe {

        @Getter
        private ResourceLocation id;
        @Getter
        private int maxInputCount;
        @Getter
        private int maxOutputCount;
        @Getter
        private int maxFluidOutputCount = 0;
        @Getter
        private int maxFluidInputCount = 0;
        private boolean heat = false;
        private boolean duration = false;

        //copy constructor
        public SimpleCreateRecipe(SimpleCreateRecipe recipe) {
            id = recipe.id;
            maxInputCount = recipe.maxInputCount;
            maxOutputCount = recipe.maxOutputCount;
            maxFluidOutputCount = recipe.maxFluidOutputCount;
            maxFluidInputCount = recipe.maxFluidInputCount;
            heat = recipe.heat;
            duration = recipe.duration;
        }

        public SimpleCreateRecipe(String id, int maxInput, int maxOutput) {
            this.id = Rutile.getResource("create:" + id);
            this.maxInputCount = maxInput;
            this.maxOutputCount = maxOutput;
        }

        public SimpleCreateRecipe(String id, int maxInput, int maxOutput, boolean requiresHeat, boolean specificDuration) {
            this(id, maxInput, maxOutput);
            heat = requiresHeat;
            duration = specificDuration;
        }

        public SimpleCreateRecipe(String id, int maxInput, int maxOutput, int fluidMaxInput, int fluidMaxOutput) {
            this(id, maxInput, maxOutput);
            maxFluidOutputCount = fluidMaxOutput;
            maxFluidInputCount = fluidMaxInput;
        }

        public SimpleCreateRecipe(String id, int maxInput, int maxOutput, boolean requiresHeat, boolean specificDuration, int fluidMaxInput, int fluidMaxOutput) {
            this(id, maxInput, maxOutput, fluidMaxInput, fluidMaxOutput);
            heat = requiresHeat;
            duration = specificDuration;
        }

        @Override
        protected boolean canRequireHeat() {
            return heat;
        }

        @Override
        protected boolean canSpecifyDuration() {
            return duration;
        }
    }

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

    public static class ItemApplication extends CreateRecipe {
        private boolean keepHeldItem;

        @Override
        protected ResourceLocation getId() {
            return Rutile.getResource("create:item_application");
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
            return Rutile.getResource("create:deploying");
        }
    }
}
