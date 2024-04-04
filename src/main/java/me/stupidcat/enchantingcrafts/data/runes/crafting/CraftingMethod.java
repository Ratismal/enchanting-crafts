package me.stupidcat.enchantingcrafts.data.runes.crafting;

import me.stupidcat.enchantingcrafts.CraftsItems;
import me.stupidcat.enchantingcrafts.data.runes.RuneDataOtherRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;

public class CraftingMethod {
    public static final String CHECK_KEY = "checks";
    public CraftingMethod() {

    }

    private static String getKey(RuneDataOtherRecipe recipe) {
        return recipe.runeData.enchantmentId.toString();
    }

    public static int checkNbt(RuneDataOtherRecipe recipe, ItemStack stack) {
        var checkNbt = stack.getSubNbt(CHECK_KEY);
        if (checkNbt == null) return 0;

        var key = getKey(recipe);
        if (checkNbt.contains(key)) {
            return checkNbt.getInt(key);
        }

        return 0;
    }

    public static void saveNbt(RuneDataOtherRecipe recipe, ItemStack stack, int value) {
        var checkNbt = stack.getOrCreateSubNbt(CHECK_KEY);

        checkNbt.putInt(getKey(recipe), value);
    }

    public static void removeNbt(RuneDataOtherRecipe recipe, ItemStack stack) {
        var checkNbt = stack.getSubNbt(CHECK_KEY);
        if (checkNbt == null) return;

        checkNbt.remove(getKey(recipe));
        if (checkNbt.isEmpty()) {
            stack.removeSubNbt(CHECK_KEY);
        }
    }

    public static List<ItemStack> findRunes(PlayerEntity player) {
        List<ItemStack> runes = new ArrayList<>();
        if (player.getStackInHand(Hand.MAIN_HAND).isOf(CraftsItems.ATTUNED_RUNE)) {
            runes.add(player.getStackInHand(Hand.MAIN_HAND));
        }
        if (player.getStackInHand(Hand.OFF_HAND).isOf(CraftsItems.ATTUNED_RUNE)) {
            runes.add(player.getStackInHand(Hand.OFF_HAND));
        }

        return runes;
    }
}
