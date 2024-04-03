package me.stupidcat.enchantingcrafts.data.runes.crafting.methods;

import me.stupidcat.enchantingcrafts.CraftsCraftingMethods;
import me.stupidcat.enchantingcrafts.CraftsItems;
import me.stupidcat.enchantingcrafts.EnchantingCrafts;
import me.stupidcat.enchantingcrafts.data.runes.RuneDataEntries;
import me.stupidcat.enchantingcrafts.data.runes.RuneDataOtherRecipe;
import me.stupidcat.enchantingcrafts.data.runes.crafting.CraftingMethod;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class ChargeKillCraftingMethod extends CraftingMethod {

    public static EntityGroup getEntityGroup(String group) {
        return switch (group) {
            case "arthropod" -> EntityGroup.ARTHROPOD;
            case "undead" -> EntityGroup.UNDEAD;
            case "illager" -> EntityGroup.ILLAGER;
            case "aquatic" -> EntityGroup.AQUATIC;
            default -> EntityGroup.DEFAULT;
        };
    }

    public static boolean validate(RuneDataOtherRecipe recipe, ItemStack stack, PlayerEntity player, LivingEntity entity) {
        var json = recipe.json.getAsJsonObject();
        var passes = false;

        var totalNeeded = 1;
        if (json.has("count")) {
            totalNeeded = json.get("count").getAsInt();
        } else {
            EnchantingCrafts.LOGGER.warn("Total needed for charge kill not specified: {}", json);
        }

        if (json.has("entity_group")) {
            var target = getEntityGroup(recipe.json.getAsJsonObject().get("entity_group").getAsString());

            if (entity.getGroup() == target) {
                passes = true;
            }
        } else if (json.has("entity")) {
            var id = Identifier.tryParse(json.get("entity").getAsString());

            if (EntityType.getId(entity.getType()).equals(id)) {
                passes = true;
            }
        } else { // Assume anything
            passes = true;
        }

        if (passes) {
            var count = checkNbt(recipe, stack);
            count++;

            if (count >= totalNeeded) {
                removeNbt(recipe, stack);
                player.playSound(SoundEvents.ENTITY_ALLAY_HURT, SoundCategory.BLOCKS,1f, 0.8f);
                return true;
            } else {
                saveNbt(recipe, stack, count);
                player.playSound(SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.BLOCKS, 1f, 1f);
            }
        }

        return false;
    }

    public static void check(PlayerEntity player, LivingEntity entity) {
        ItemStack rune = null;
        if (player.getStackInHand(Hand.MAIN_HAND).isOf(CraftsItems.ATTUNED_RUNE)) {
            rune = player.getStackInHand(Hand.MAIN_HAND);
        } else if (player.getStackInHand(Hand.OFF_HAND).isOf(CraftsItems.ATTUNED_RUNE)) {
            rune = player.getStackInHand(Hand.OFF_HAND);
        }

        if (rune != null) {
            var recipes = RuneDataEntries.findRecipes(CraftsCraftingMethods.CHARGE_KILL, rune);
            for (var recipe : recipes) {
                if (recipe != null && validate(recipe, rune, player, entity)) {
                    recipe.apply(rune);
                }
            }
        }
    }
}
