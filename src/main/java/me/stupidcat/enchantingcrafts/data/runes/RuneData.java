package me.stupidcat.enchantingcrafts.data.runes;

import com.google.gson.JsonObject;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class RuneData {
    public Identifier id;
    public Identifier enchantmentId;
    public Enchantment enchantment;
    public List<RuneDataRecipe> recipes = new ArrayList<>();

    public RuneData(Identifier id) {
        this.id = id;
    }

    public void parseJson(JsonObject json) {
        enchantmentId = Identifier.tryParse(json.get("enchantment").getAsString());
        enchantment = Registries.ENCHANTMENT.get(enchantmentId);

        var rawRecipes = json.getAsJsonArray("recipes");
        for (var rawRecipe : rawRecipes.asList()) {
            recipes.add(RuneDataRecipe.Deserialize(rawRecipe, this));
        }
    }

    public boolean match(List<ItemStack> items, int level) {
        if (level > recipes.size()) return false;

        return recipes.get(level - 1).match(items);
    }
}
