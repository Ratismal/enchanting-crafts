package me.stupidcat.enchantingcrafts.data.runes;

import com.google.gson.JsonElement;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;

import java.util.ArrayList;
import java.util.List;

public class RuneDataRecipe {
    public final JsonElement json;
    public final List<Ingredient> predicates = new ArrayList<>();
    public final RuneData runeData;
    public final int level;

    public RuneDataRecipe(JsonElement json, RuneData runeData, int level) {
        this.runeData = runeData;
        this.level = level;
        this.json = json;

        if (json.getAsJsonObject().has("items")) {
            var itemArray = json.getAsJsonObject().get("items");
            for (var item : itemArray.getAsJsonArray().asList()) {
                var ingredient = Ingredient.fromJson(item, false);
                if (ingredient.test(Items.POTION.getDefaultStack())) {
                    ingredient = Ingredient.ofStacks(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER));
                }

                this.predicates.add(ingredient);
            }
        }
    }

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

    public static RuneDataRecipe Deserialize(JsonElement json, RuneData runeData, int level) {
        return new RuneDataRecipe(json, runeData, level);
    }
}
