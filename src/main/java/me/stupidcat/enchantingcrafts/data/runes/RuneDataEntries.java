package me.stupidcat.enchantingcrafts.data.runes;

import com.google.gson.JsonObject;
import net.minecraft.enchantment.Enchantment;
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
        var firstRecipe = runeData.recipes.get(0);
        var list = recipes.computeIfAbsent(firstRecipe.predicates.size(), k -> new ArrayList<>());
        list.add(firstRecipe);

        return runeData;
    }

}
