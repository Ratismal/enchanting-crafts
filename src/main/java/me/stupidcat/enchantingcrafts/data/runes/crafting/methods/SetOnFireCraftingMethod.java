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

public class SetOnFireCraftingMethod extends CraftingMethod {
    public static void check(ItemStack rune, ItemEntity entity) {
        var recipes = RuneDataEntries.findRecipes(CraftsCraftingMethods.SET_ON_FIRE, rune);
        int onFire = -1;
        for (var recipe : recipes) {
            if (recipe != null) {
                if (onFire == -1) {
                    onFire = entity.getWorld().getBlockState(entity.getBlockPos()).isOf(Blocks.FIRE) ? 1 : 0;
                }
                if (onFire == 1) {
                    entity.playSound(SoundEvents.ENTITY_ALLAY_HURT, 1f, 0.8f);
                    recipe.apply(rune);
                } else {
                    break;
                }
            }
        }
    }
}
