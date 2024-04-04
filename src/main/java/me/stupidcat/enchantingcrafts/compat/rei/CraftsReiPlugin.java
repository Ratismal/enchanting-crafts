package me.stupidcat.enchantingcrafts.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.stupidcat.enchantingcrafts.CraftsItems;
import me.stupidcat.enchantingcrafts.EnchantingCrafts;
import me.stupidcat.enchantingcrafts.compat.rei.display.CraftingDisplay;
import me.stupidcat.enchantingcrafts.data.runes.RuneDataEntries;
import me.stupidcat.enchantingcrafts.data.runes.RuneDataOtherRecipe;
import me.stupidcat.enchantingcrafts.item.AttunedRuneItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CraftsReiPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<CraftingDisplay> CRAFTING_DISPLAY = CategoryIdentifier.of(EnchantingCrafts.MOD_ID, "my_display");

    @Override
    public void registerItemComparators(ItemComparatorRegistry registry) {
        registry.registerNbt(CraftsItems.ATTUNED_RUNE);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        for (var entry : RuneDataEntries.runeMap.entrySet()) {
            var runeData = entry.getValue();
            var recipes = runeData.recipes;

            for (var recipe : recipes) {
                var level = recipe.level;
                var inputRune = CraftsItems.ATTUNED_RUNE.getDefaultStack();
                var inputEnchants = new HashMap<Enchantment, Integer>();
                var outputRune = CraftsItems.ATTUNED_RUNE.getDefaultStack();
                var outputEnchants = new HashMap<Enchantment, Integer>();

                if (level > 1) {
                    inputEnchants.put(recipe.runeData.enchantment, level - 1);
                    AttunedRuneItem.apply(inputRune, inputEnchants);
                }
                outputEnchants.put(recipe.runeData.enchantment, level);

                AttunedRuneItem.apply(outputRune, outputEnchants);
                var id = EnchantingCrafts.Id("enchant/" + runeData.enchantmentId.getNamespace() + "/" + runeData.enchantmentId.getPath() + "/" + "level" + level);

                if (recipe instanceof RuneDataOtherRecipe otherRecipe) {
                    // TODO: Add other recipe types
                } else {
                    var ingredients = new ArrayList<Ingredient>(recipe.predicates);
                    if (ingredients.size() >= 4) {
                        ingredients.add(4, Ingredient.ofStacks(inputRune));
                    } else {
                        ingredients.add(Ingredient.ofStacks(inputRune));
                    }
                    var defaultedList = DefaultedList.copyOf(Ingredient.empty(), ingredients.toArray(new Ingredient[0]));

                    var rec = new ShapelessRecipe(id, "rune", CraftingRecipeCategory.MISC, outputRune, defaultedList);
                    registry.add(rec);
                }
            }
        }
    }
}
