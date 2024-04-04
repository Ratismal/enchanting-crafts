package me.stupidcat.enchantingcrafts.data.runes.crafting.methods;

import me.stupidcat.enchantingcrafts.CraftsCraftingMethods;
import me.stupidcat.enchantingcrafts.EnchantingCrafts;
import me.stupidcat.enchantingcrafts.data.runes.RuneDataEntries;
import me.stupidcat.enchantingcrafts.data.runes.RuneDataOtherRecipe;
import me.stupidcat.enchantingcrafts.data.runes.crafting.CraftingMethod;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class SubmergeCraftingMethod extends CraftingMethod {
    public static boolean validate(RuneDataOtherRecipe recipe, ItemStack stack, ItemEntity entity) {
        var json = recipe.json.getAsJsonObject();
        var passes = false;

        var totalNeeded = 1;
        if (json.has("time")) {
            totalNeeded = json.get("time").getAsInt();
        }

        if (json.has("depth")) {
            var requiredDepth = json.get("depth").getAsInt();
            var basePos = entity.getBlockPos();

            passes = true;
            for (var i = 0; i < requiredDepth; i++) {
                var pos = basePos.offset(Direction.UP, i);

                if (!entity.getWorld().getBlockState(pos).isOf(Blocks.WATER)) {
                    passes = false;
                    break;
                }
            }
        } else { // Assume anything
            passes = true;
        }

        if (passes) {
            var count = checkNbt(recipe, stack);
            count++;

            if (count >= totalNeeded) {
                removeNbt(recipe, stack);
                entity.playSound(SoundEvents.ENTITY_ALLAY_HURT, 1f, 0.8f);
                return true;
            } else {
                saveNbt(recipe, stack, count);
            }
        }

        return false;
    }

    public static void check(ItemStack rune, ItemEntity itemEntity) {
        var recipes = RuneDataEntries.findRecipes(CraftsCraftingMethods.SUBMERGE, rune);
        for (var recipe : recipes) {
            if (recipe != null && validate(recipe, rune, itemEntity)) {
                recipe.apply(rune);
            }
        }
    }
}
