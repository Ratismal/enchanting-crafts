package me.stupidcat.enchantingcrafts.data.runes;

import com.google.gson.JsonObject;
import me.stupidcat.enchantingcrafts.CraftsItems;
import me.stupidcat.enchantingcrafts.data.runes.crafting.CraftingMethod;
import me.stupidcat.enchantingcrafts.item.AttunedRuneItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuneDataEntries {
    public static List<RuneData> runes = new ArrayList<>();
    public static Map<Enchantment, RuneData> runeMap = new HashMap<>();

    public static HashMap<Identifier, JsonObject> savedJson = new HashMap<>();
    public static Map<Integer, List<RuneDataRecipe>> recipes = new HashMap<>();

    public static Map<CraftingMethod, List<RuneDataOtherRecipe>> otherRecipes = new HashMap<>();

    public static RuneData register(RuneData runeData) {
        if (runeMap.containsKey(runeData.enchantment)) {
            for (var recipe : runeMap.get(runeData.enchantment).recipes) {
                var list = recipes.get(recipe.predicates.size());
                if (list != null) {
                    list.remove(recipe);
                }
            }
        }

        runeMap.put(runeData.enchantment, runeData);

        // Cache first recipe by number of items
        if (runeData.recipes.size() > 0) {
            var firstRecipe = runeData.recipes.get(0);
            var list = recipes.computeIfAbsent(firstRecipe.predicates.size(), k -> new ArrayList<>());
            list.add(firstRecipe);

            for (var recipe : runeData.recipes) {
                if (recipe instanceof RuneDataOtherRecipe otherRecipe) {
                    var list2 = otherRecipes.computeIfAbsent(otherRecipe.method, k -> new ArrayList<>());
                    list2.add(otherRecipe);
                }
            }
        }

        return runeData;
    }

    public static List<RuneDataOtherRecipe> findRecipes(CraftingMethod method, ItemStack stack) {
        if (!stack.isOf(CraftsItems.ATTUNED_RUNE)) return new ArrayList<>();

        var enchants = AttunedRuneItem.getEnchantments(stack);

        var matches = new ArrayList<RuneDataOtherRecipe>();

        for (var recipe : otherRecipes.computeIfAbsent(method, k -> new ArrayList<>())) {
            if (enchants.containsKey(recipe.runeData.enchantment)) {
                var existingLevel = enchants.get(recipe.runeData.enchantment);
                if (existingLevel == recipe.level - 1) {
                    matches.add(recipe);
                }
            } else if (recipe.level == 1) {
                matches.add(recipe);
            }
        }

        return matches;
    }
}
