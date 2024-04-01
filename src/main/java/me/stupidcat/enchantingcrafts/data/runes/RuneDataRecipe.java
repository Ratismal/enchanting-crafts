package me.stupidcat.enchantingcrafts.data.runes;

import com.google.gson.JsonElement;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.*;

public class RuneDataRecipe {

    public List<Ingredient> predicates = new ArrayList<>();
    public RuneData runeData;

    public boolean match(List<ItemStack> items) {
        if (predicates.size() != items.size()) return false;

        var list = new ArrayList<>(items);
        for (var predicate : predicates) {
            ItemStack match = null;
            for (var item : list) {
                if (predicate.test(item)) {
                    match = item;
                    break;
                }
            }

            if (match == null) {
                return false;
            } else {
                list.remove(match);
            }
        }

        return true;
    }

    public static RuneDataRecipe Deserialize(JsonElement json, RuneData runeData) {
        var recipe = new RuneDataRecipe();

        var itemArray = json.getAsJsonObject().get("items");
        recipe.runeData = runeData;

        for (var item : itemArray.getAsJsonArray().asList()) {
            recipe.predicates.add(Ingredient.fromJson(item, false));
        }

        return recipe;
    }
}
