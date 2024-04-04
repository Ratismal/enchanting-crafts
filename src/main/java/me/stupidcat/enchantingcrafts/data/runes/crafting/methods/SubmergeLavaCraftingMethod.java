package me.stupidcat.enchantingcrafts.data.runes.crafting.methods;

import me.stupidcat.enchantingcrafts.CraftsCraftingMethods;
import me.stupidcat.enchantingcrafts.data.runes.RuneDataEntries;
import me.stupidcat.enchantingcrafts.data.runes.RuneDataOtherRecipe;
import me.stupidcat.enchantingcrafts.data.runes.crafting.CraftingMethod;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Direction;

public class SubmergeLavaCraftingMethod extends CraftingMethod {
    public static void check(ItemStack rune, ItemEntity entity) {
        var recipes = RuneDataEntries.findRecipes(CraftsCraftingMethods.SUBMERGE_LAVA, rune);
        for (var recipe : recipes) {
            if (recipe != null) {
                entity.playSound(SoundEvents.ENTITY_ALLAY_HURT, 1f, 0.8f);
                recipe.apply(rune);
            }
        }
    }
}
