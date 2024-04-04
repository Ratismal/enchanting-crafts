package me.stupidcat.enchantingcrafts.data.runes.crafting.methods;

import me.stupidcat.enchantingcrafts.CraftsCraftingMethods;
import me.stupidcat.enchantingcrafts.CraftsItems;
import me.stupidcat.enchantingcrafts.EnchantingCrafts;
import me.stupidcat.enchantingcrafts.data.runes.RuneDataEntries;
import me.stupidcat.enchantingcrafts.data.runes.RuneDataOtherRecipe;
import me.stupidcat.enchantingcrafts.data.runes.crafting.CraftingMethod;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ChargeTakeDamageCraftingMethod extends CraftingMethod {
    public static boolean validate(RuneDataOtherRecipe recipe, ItemStack stack, PlayerEntity player, DamageSource source, float amount) {
        var json = recipe.json.getAsJsonObject();
        var passes = false;

        var totalNeeded = 1;
        if (json.has("count")) {
            totalNeeded = json.get("count").getAsInt();
        } else {
            EnchantingCrafts.LOGGER.warn("Total needed for charge take damage not specified: {}", json);
        }

        if (json.has("tag")) {
            // Hacky solution, hope this works!
            var tag = TagKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(json.get("tag").getAsString()));
            if (source.isIn(tag)) {
                passes = true;
            }
        } else { // Assume anything
            passes = true;
        }

        if (passes) {
            var count = checkNbt(recipe, stack);
            int flooredAmount = (int)Math.floor(amount);
            if (flooredAmount == 0) return false;
            count += flooredAmount;

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

    public static void check(PlayerEntity player, DamageSource source, float amount) {
        var runes = findRunes(player);

        for (var rune : runes) {
            var recipes = RuneDataEntries.findRecipes(CraftsCraftingMethods.CHARGE_TAKE_DAMAGE, rune);
            for (var recipe : recipes) {
                if (recipe != null && validate(recipe, rune, player, source, amount)) {
                    recipe.apply(rune);
                }
            }
        }
    }
}
