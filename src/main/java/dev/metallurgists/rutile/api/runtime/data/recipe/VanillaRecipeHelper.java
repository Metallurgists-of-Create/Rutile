package dev.metallurgists.rutile.api.runtime.data.recipe;

import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import dev.metallurgists.rutile.Rutile;
import dev.metallurgists.rutile.api.material.ItemMaterialData;
import dev.metallurgists.rutile.api.material.Material;
import dev.metallurgists.rutile.api.material.MaterialHelper;
import dev.metallurgists.rutile.api.material.data.MaterialEntry;
import dev.metallurgists.rutile.api.material.flags.FlagKey;
import dev.metallurgists.rutile.api.material.module.dynamic.TagsModule;
import dev.metallurgists.rutile.api.material.module.registry.RegistryModule;
import dev.metallurgists.rutile.api.material.stack.ItemMaterialInfo;
import dev.metallurgists.rutile.api.material.stack.MaterialStack;
import dev.metallurgists.rutile.api.runtime.data.recipe.builder.ShapedRecipeBuilder;
import dev.metallurgists.rutile.api.runtime.data.recipe.builder.ShapelessRecipeBuilder;
import dev.metallurgists.rutile.api.runtime.data.recipe.builder.SimpleCookingRecipeBuilder;
import dev.metallurgists.rutile.api.tag.TagPrefix;
import dev.metallurgists.rutile.registry.RutileModules;
import it.unimi.dsi.fastutil.chars.Char2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VanillaRecipeHelper {
    public static void addSmeltingRecipe(RecipeOutput provider, @NotNull String regName, TagKey<Item> input,
                                         ItemStack output) {
        addSmeltingRecipe(provider, Rutile.id(regName), input, output);
    }

    public static void addSmeltingRecipe(RecipeOutput provider, @NotNull ResourceLocation regName,
                                         TagKey<Item> input, ItemStack output) {
        addSmeltingRecipe(provider, regName, input, output, 0.0f);
    }

    public static void addSmeltingRecipe(RecipeOutput provider, @NotNull String regName, TagKey<Item> input,
                                         ItemStack output, float experience) {
        addSmeltingRecipe(provider, Rutile.id(regName), input, output, experience);
    }

    public static void addSmeltingRecipe(RecipeOutput provider, @NotNull String regName, Ingredient input,
                                         ItemStack output, float experience) {
        addSmeltingRecipe(provider, Rutile.id(regName), input, output, experience);
    }

    public static void addSmeltingRecipe(RecipeOutput provider, @NotNull ResourceLocation regName,
                                         Ingredient input, ItemStack output, float experience) {
        SimpleCookingRecipeBuilder.smelting(regName).input(input).output(output).cookingTime(200).experience(experience)
                .save(provider);
    }

    public static void addSmeltingRecipe(RecipeOutput provider, @NotNull ResourceLocation regName,
                                         TagKey<Item> input, ItemStack output, float experience) {
        SimpleCookingRecipeBuilder.smelting(regName).input(input).output(output).cookingTime(200).experience(experience)
                .save(provider);
    }

    public static void addSmeltingRecipe(RecipeOutput provider, @NotNull String regName, ItemStack input,
                                         ItemStack output) {
        addSmeltingRecipe(provider, Rutile.id(regName), input, output, 0.0f);
    }

    public static void addSmeltingRecipe(RecipeOutput provider, @NotNull String regName, Item input,
                                         Item output) {
        addSmeltingRecipe(provider, Rutile.id(regName), input.getDefaultInstance(), output.getDefaultInstance(), 0.0f);
    }

    public static void addSmeltingRecipe(RecipeOutput provider, @NotNull String regName, Item input,
                                         Item output, float experience) {
        addSmeltingRecipe(provider, Rutile.id(regName), input.getDefaultInstance(), output.getDefaultInstance(),
                experience);
    }

    public static void addSmeltingRecipe(RecipeOutput provider, @NotNull String regName, ItemStack input,
                                         ItemStack output, float experience) {
        addSmeltingRecipe(provider, Rutile.id(regName), input, output, experience);
    }

    public static void addSmeltingRecipe(RecipeOutput provider, @NotNull ResourceLocation regName,
                                         ItemStack input, ItemStack output, float experience) {
        SimpleCookingRecipeBuilder.smelting(regName).input(input).output(output).cookingTime(200).experience(experience)
                .save(provider);
    }

    public static void addBlastingRecipe(RecipeOutput provider, @NotNull String regName, TagKey<Item> input,
                                         ItemStack output) {
        addBlastingRecipe(provider, Rutile.id(regName), input, output);
    }

    public static void addBlastingRecipe(RecipeOutput provider, @NotNull ResourceLocation regName,
                                         TagKey<Item> input, ItemStack output) {
        addBlastingRecipe(provider, regName, input, output, 0.0f);
    }

    public static void addBlastingRecipe(RecipeOutput provider, @NotNull String regName, TagKey<Item> input,
                                         ItemStack output, float experience) {
        addBlastingRecipe(provider, Rutile.id(regName), input, output, experience);
    }

    public static void addBlastingRecipe(RecipeOutput provider, @NotNull String regName, Ingredient input,
                                         ItemStack output, float experience) {
        addBlastingRecipe(provider, Rutile.id(regName), input, output, experience);
    }

    public static void addBlastingRecipe(RecipeOutput provider, @NotNull ResourceLocation regName,
                                         Ingredient input, ItemStack output, float experience) {
        SimpleCookingRecipeBuilder.smelting(regName).input(input).output(output).cookingTime(200).experience(experience)
                .save(provider);
    }

    public static void addBlastingRecipe(RecipeOutput provider, @NotNull ResourceLocation regName,
                                         TagKey<Item> input, ItemStack output, float experience) {
        SimpleCookingRecipeBuilder.smelting(regName).input(input).output(output).cookingTime(200).experience(experience)
                .save(provider);
    }

    public static void addBlastingRecipe(RecipeOutput provider, @NotNull String regName, ItemStack input,
                                         ItemStack output) {
        addBlastingRecipe(provider, Rutile.id(regName), input, output, 0.0f);
    }

    public static void addBlastingRecipe(RecipeOutput provider, @NotNull String regName, Item input,
                                         Item output) {
        addBlastingRecipe(provider, Rutile.id(regName), input.getDefaultInstance(), output.getDefaultInstance(), 0.0f);
    }

    public static void addBlastingRecipe(RecipeOutput provider, @NotNull String regName, Item input,
                                         Item output, float experience) {
        addBlastingRecipe(provider, Rutile.id(regName), input.getDefaultInstance(), output.getDefaultInstance(),
                experience);
    }

    public static void addBlastingRecipe(RecipeOutput provider, @NotNull String regName, ItemStack input,
                                         ItemStack output, float experience) {
        addBlastingRecipe(provider, Rutile.id(regName), input, output, experience);
    }

    public static void addBlastingRecipe(RecipeOutput provider, @NotNull ResourceLocation regName,
                                         ItemStack input, ItemStack output, float experience) {
        SimpleCookingRecipeBuilder.blasting(regName).input(input).output(output).cookingTime(200).experience(experience)
                .save(provider);
    }

    public static void addSmokingRecipe(RecipeOutput provider, @NotNull String regName, TagKey<Item> input,
                                        ItemStack output) {
        addSmokingRecipe(provider, Rutile.id(regName), input, output);
    }

    public static void addSmokingRecipe(RecipeOutput provider, @NotNull ResourceLocation regName,
                                        TagKey<Item> input, ItemStack output) {
        addSmokingRecipe(provider, regName, input, output, 0.0f);
    }

    public static void addSmokingRecipe(RecipeOutput provider, @NotNull String regName, TagKey<Item> input,
                                        ItemStack output, float experience) {
        addSmokingRecipe(provider, Rutile.id(regName), input, output, experience);
    }

    public static void addSmokingRecipe(RecipeOutput provider, @NotNull String regName, Ingredient input,
                                        ItemStack output, float experience) {
        addSmokingRecipe(provider, Rutile.id(regName), input, output, experience);
    }

    public static void addSmokingRecipe(RecipeOutput provider, @NotNull ResourceLocation regName,
                                        Ingredient input, ItemStack output, float experience) {
        SimpleCookingRecipeBuilder.smelting(regName).input(input).output(output).cookingTime(200).experience(experience)
                .save(provider);
    }

    public static void addSmokingRecipe(RecipeOutput provider, @NotNull ResourceLocation regName,
                                        TagKey<Item> input, ItemStack output, float experience) {
        SimpleCookingRecipeBuilder.smelting(regName).input(input).output(output).cookingTime(200).experience(experience)
                .save(provider);
    }

    public static void addSmokingRecipe(RecipeOutput provider, @NotNull String regName, ItemStack input,
                                        ItemStack output) {
        addSmokingRecipe(provider, Rutile.id(regName), input, output, 0.0f);
    }

    public static void addSmokingRecipe(RecipeOutput provider, @NotNull String regName, Item input,
                                        Item output) {
        addSmokingRecipe(provider, Rutile.id(regName), input.getDefaultInstance(), output.getDefaultInstance(), 0.0f);
    }

    public static void addSmokingRecipe(RecipeOutput provider, @NotNull String regName, Item input,
                                        Item output, float experience) {
        addSmokingRecipe(provider, Rutile.id(regName), input.getDefaultInstance(), output.getDefaultInstance(),
                experience);
    }

    public static void addSmokingRecipe(RecipeOutput provider, @NotNull String regName, ItemStack input,
                                        ItemStack output, float experience) {
        addSmokingRecipe(provider, Rutile.id(regName), input, output, experience);
    }

    public static void addSmokingRecipe(RecipeOutput provider, @NotNull ResourceLocation regName,
                                        ItemStack input, ItemStack output, float experience) {
        SimpleCookingRecipeBuilder.smoking(regName).input(input).output(output).cookingTime(200).experience(experience)
                .save(provider);
    }

    public static void addCampfireRecipe(RecipeOutput provider, @NotNull String regName, ItemStack input,
                                         ItemStack output, float experience) {
        addCampfireRecipe(provider, Rutile.id(regName), input, output, experience);
    }

    public static void addCampfireRecipe(RecipeOutput provider, @NotNull String regName, ItemStack input,
                                         ItemStack output) {
        addCampfireRecipe(provider, Rutile.id(regName), input, output, 0);
    }

    public static void addCampfireRecipe(RecipeOutput provider, @NotNull ResourceLocation regName,
                                         ItemStack input, ItemStack output, float experience) {
        SimpleCookingRecipeBuilder.campfireCooking(regName).input(input).output(output).cookingTime(100)
                .experience(experience)
                .save(provider);
    }

    public static void addCampfireRecipe(RecipeOutput provider, @NotNull String regName, TagKey<Item> input,
                                         ItemStack output, float experience) {
        addCampfireRecipe(provider, Rutile.id(regName), input, output, experience);
    }

    public static void addCampfireRecipe(RecipeOutput provider, @NotNull String regName, TagKey<Item> input,
                                         ItemStack output) {
        addCampfireRecipe(provider, Rutile.id(regName), input, output, 0);
    }

    public static void addCampfireRecipe(RecipeOutput provider, @NotNull ResourceLocation regName,
                                         TagKey<Item> input, ItemStack output, float experience) {
        SimpleCookingRecipeBuilder.campfireCooking(regName).input(input).output(output).cookingTime(100)
                .experience(experience)
                .save(provider);
    }

    /**
     * @see #addShapedRecipe(RecipeOutput, boolean, ResourceLocation, ItemStack, Object...)
     */
    public static void addShapedRecipe(RecipeOutput provider, @NotNull String regName, @NotNull ItemStack result, @NotNull Object... recipe) {
        addShapedRecipe(provider, Rutile.id(regName), result, recipe);
    }

    /**
     * @see #addShapedRecipe(RecipeOutput, boolean, ResourceLocation, ItemStack, Object...)
     */
    public static void addShapedRecipe(RecipeOutput provider, @NotNull ResourceLocation regName, @NotNull ItemStack result, @NotNull Object... recipe) {
        addShapedRecipe(provider, false, regName, result, recipe);
    }

    /**
     * Adds Shaped Crafting Recipes.
     * <p/>
     * For Enums - {@link Enum#name()} is called.
     * <p/>
     * For {@link MaterialEntry} - {@link MaterialEntry#toString()} is called.
     *
     * @param setMaterialInfoData whether to add material decomposition information to the recipe output
     * @param regName             the registry name for the recipe
     * @param result              the output for the recipe
     * @param recipe              the contents of the recipe
     */
    public static void addShapedRecipe(RecipeOutput provider, boolean setMaterialInfoData, @NotNull ResourceLocation regName, @NotNull ItemStack result, @NotNull Object... recipe) {
        var builder = new ShapedRecipeBuilder(regName).output(result);
        for (int i = 0; i < recipe.length; i++) {
            var o = recipe[i];
            if (o instanceof String pattern) {
                builder.pattern(pattern);
            }
            if (o instanceof String[] pattern) {
                for (String s : pattern) {
                    builder.pattern(s);
                }
            }
            if (o instanceof Character sign) {
                var content = recipe[i + 1];
                i++;
                switch (content) {
                    case Ingredient ingredient -> builder.define(sign, ingredient);
                    case ICustomIngredient ingredient -> builder.define(sign, ingredient.toVanilla());
                    case ItemStack itemStack -> builder.define(sign, itemStack);
                    case TagKey<?> key when key.isFor(Registries.ITEM) -> builder.define(sign, (TagKey<Item>) key);
                    case MaterialEntry(Holder<RegistryModule.Key> keyHolder, Material material) -> {
                        TagKey<Item> tag = MaterialHelper.getTag(keyHolder, material);
                        if (tag != null) {
                            builder.define(sign, tag);
                        } else builder.define(sign, MaterialHelper.get(keyHolder, material));
                    }
                    case Pair<Material, Holder<RegistryModule.Key>> keyedMaterial -> {
                        TagsModule module = keyedMaterial.getFirst().getModule(RutileModules.TAGS).orElse(null);
                        if (module != null) {
                            builder.define(sign, module.getItemParentTags(keyedMaterial.getSecond()).getFirst());
                        }
                    }
                    case ItemLike itemLike -> builder.define(sign, itemLike);

                    default -> {}
                }
            }
        }
        builder.save(provider);

        if (setMaterialInfoData) {
            ItemMaterialData.registerMaterialInfo(result.getItem(), getRecyclingIngredients(result.getCount(), recipe));
        }
    }

    /**
     * @see #addShapedRecipe(RecipeOutput, boolean, ResourceLocation, ItemStack, Object...)
     */
    public static void addShapedRecipe(RecipeOutput provider, boolean setMaterialInfoData,
                                       @NotNull String regName, @NotNull ItemStack result, @NotNull Object... recipe) {
        addShapedRecipe(provider, setMaterialInfoData, Rutile.id(regName), result, recipe);
    }

    public static void addShapelessRecipe(RecipeOutput provider, @NotNull String regName,
                                          @NotNull ItemStack result, @NotNull Object... recipe) {
        addShapelessRecipe(provider, Rutile.id(regName), result, recipe);
    }

    /**
     * Adds a shapeless recipe which clears the components of the outputs
     *
     * @see VanillaRecipeHelper#addShapelessRecipe(RecipeOutput, String, ItemStack, Object...)
     */
    public static void addShapelessNBTClearingRecipe(RecipeOutput provider, @NotNull String regName, @NotNull ItemStack result, @NotNull Object... recipe) {
        addShapelessRecipe(provider, regName, result, recipe);
    }

    public static void addShapelessRecipe(RecipeOutput provider, @NotNull ResourceLocation regName,
                                          @NotNull ItemStack result, @NotNull Object... recipe) {
        var builder = new ShapelessRecipeBuilder(regName).output(result);
        for (Object content : recipe) {
            if (content instanceof Ingredient ingredient) {
                builder.requires(ingredient);
            } else if (content instanceof ItemStack itemStack) {
                builder.requires(itemStack);
            } else if (content instanceof TagKey<?> key) {
                builder.requires((TagKey<Item>) key);
            } else if (content instanceof ItemLike itemLike) {
                builder.requires(itemLike);
            } else if (content instanceof MaterialEntry entry) {
                TagKey<Item> tag = MaterialHelper.getTag(entry.key(), entry.material());
                if (tag != null) {
                    builder.requires(tag);
                } else builder.requires(MaterialHelper.get(entry.key(), entry.material()));
            } else if (content instanceof ItemProviderEntry<?, ?> entry) {
                builder.requires(entry.asStack());
            }
        }
        builder.save(provider);
    }

    public static boolean isMaterialWood(@NotNull Material material) {
        return !material.isNull() && material.hasFlag(FlagKey.WOOD);
    }

    public static ItemMaterialInfo getRecyclingIngredients(int outputCount, @NotNull Object... recipe) {
        Char2IntOpenHashMap inputCountMap = new Char2IntOpenHashMap();
        Reference2LongOpenHashMap<Material> materialStacksExploded = new Reference2LongOpenHashMap<>();

        int itr = 0;
        while (recipe[itr] instanceof String) {
            itr++;
        }

        char lastChar = ' ';
        for (int i = itr; i < recipe.length; i++) {
            Object ingredient = recipe[i];

            // Track the current working ingredient symbol
            if (ingredient instanceof Character) {
                lastChar = (char) ingredient;
                continue;
            }

            // Should never happen if recipe is formatted correctly
            // In the case that it isn't, this error should be handled
            // by an earlier method call parsing the recipe.
            if (lastChar == ' ') return null;

            ItemLike itemLike;
            switch (ingredient) {
                case Ingredient ingr -> {
                    if (ingr.hasNoItems()) continue;
                    ItemStack stack = ingr.getItems()[0];
                    if (stack.isEmpty()) continue;
                    itemLike = stack.getItem();
                }
                case ICustomIngredient custom -> {
                    Ingredient ingr = custom.toVanilla();
                    if (ingr.hasNoItems()) continue;
                    ItemStack stack = ingr.getItems()[0];
                    if (stack.isEmpty()) continue;
                    itemLike = stack.getItem();
                }
                case ItemStack itemStack -> itemLike = itemStack.getItem();
                case TagKey<?> key -> {
                    continue; // todo can this be improved?
                }
                case ItemLike like -> itemLike = like;
                case MaterialEntry(Holder<RegistryModule.Key> keyHolder, Material material) -> {
                    ItemStack stack = MaterialHelper.get(keyHolder, material);
                    if (stack == ItemStack.EMPTY) continue;
                    itemLike = stack.getItem();
                }
                default -> {
                    continue; // throw out bad entries
                }
            }

            // First try to get ItemMaterialInfo
            ItemMaterialInfo info = ItemMaterialData.getMaterialInfo(itemLike);
            if (info != null) {
                for (MaterialStack ms : info.getMaterials()) {
                    if (!(ms.getMaterial().isNull())) {
                        addMaterialStack(materialStacksExploded, inputCountMap.get(lastChar), outputCount, ms);
                    }
                }
                continue;
            }

            // Then try to get a single Material (UnificationEntry needs this, for example)
            MaterialStack materialStack = MaterialHelper.getMaterialStack(itemLike);
            if (!materialStack.isEmpty() && !(materialStack.getMaterial().isNull())) {
                addMaterialStack(materialStacksExploded, inputCountMap.get(lastChar), outputCount, materialStack);
            }

            // Gather any secondary materials if this item has the module
            Holder<RegistryModule.Key> key = MaterialHelper.getKey(itemLike);
            if (!materialStack.isEmpty() && !(materialStack.getMaterial().isNull())) {
                var stacks = materialStack.getMaterial().getModule(RutileModules.SECONDARY_MATERIALS).map(module -> module.getSecondaryMaterials(key)).orElse(List.of());
                for (MaterialStack ms : stacks) {
                    addMaterialStack(materialStacksExploded, inputCountMap.get(lastChar), outputCount, ms);
                }
            }
        }

        return new ItemMaterialInfo(materialStacksExploded);
    }

    private static void addMaterialStack(@NotNull Reference2LongOpenHashMap<Material> materialStacksExploded, int inputCount, int outputCount, @NotNull MaterialStack ms) {
        materialStacksExploded.addTo(ms.getMaterial(), (ms.getAmount() * inputCount / outputCount));
    }
}
