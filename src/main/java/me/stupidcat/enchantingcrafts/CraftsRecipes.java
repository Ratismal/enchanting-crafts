package me.stupidcat.enchantingcrafts;

import me.stupidcat.enchantingcrafts.recipes.RuneRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CraftsRecipes {
    public static final SpecialRecipeSerializer<RuneRecipe> RUNE_SERIALIZER = new SpecialRecipeSerializer<>(RuneRecipe::new);

    public static void register() {
        Registry.register(Registries.RECIPE_SERIALIZER, EnchantingCrafts.Id("attuned_rune"), RUNE_SERIALIZER);
    }
}
