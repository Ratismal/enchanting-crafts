package me.stupidcat.enchantingcrafts.recipes;

import me.stupidcat.enchantingcrafts.CraftsItems;
import me.stupidcat.enchantingcrafts.CraftsRecipes;
import me.stupidcat.enchantingcrafts.data.runes.RuneDataEntries;
import me.stupidcat.enchantingcrafts.item.AttunedRuneItem;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RuneRecipe extends SpecialCraftingRecipe {
    public RuneRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }


    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        var items = inventory.getInputStacks();
        ItemStack rune = null;
        for (var item : items) {
            if (item.isOf(CraftsItems.ATTUNED_RUNE)) {
                if (rune == null) {
                    rune = item;
                } else {
                    // Multiple runes, exit
                    return false;
                }
            }
        }

        return rune != null;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        var items = inventory.getInputStacks();
        ItemStack rune = null;
        for (var item : items) {
            if (item.isOf(CraftsItems.ATTUNED_RUNE)) {
                if (rune == null) {
                    rune = item;
                } else {
                    // Multiple runes, exit
                    return ItemStack.EMPTY;
                }
            }
        }

        if (rune == null) {
            return ItemStack.EMPTY;
        }

        List<ItemStack> newItems = new ArrayList<>();
        for (var item : items) {
            if (!item.equals(rune) && !item.isOf(Items.AIR)) newItems.add(item);
        }

        var enchantments = AttunedRuneItem.getEnchantments(rune);

        var applyEnchantments = false;

        // Process upgrades
        for (var entry : enchantments.entrySet()) {
            var enchantment = entry.getKey();
            var level = entry.getValue();
            if (RuneDataEntries.runeMap.containsKey(enchantment)) {
                var data = RuneDataEntries.runeMap.get(enchantment);
                if (data != null) {
                    var match = data.match(newItems, level + 1);
                    if (match) {
                        applyEnchantments = true;
                        enchantments.put(enchantment, level + 1);
                    }
                }
            }
        }

        if (!applyEnchantments) {
            // Process new enchantments
            var itemCount = newItems.size();
            var recipes = RuneDataEntries.recipes.get(itemCount);
            if (recipes != null) {
                for (var entry : RuneDataEntries.recipes.get(itemCount)) {
                    var data = entry.runeData;
                    var enchant = data.enchantment;
                    if (enchantments.containsKey(enchant)) continue;

                    var match = entry.match(newItems);
                    if (match) {
                        applyEnchantments = true;
                        enchantments.put(enchant, 1);
                    }
                }
            }
        }

        if (applyEnchantments) {
            var resultRune = rune.copy();
            AttunedRuneItem.apply(resultRune, enchantments);
            resultRune.setCount(1);

            return resultRune;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 9;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CraftsRecipes.RUNE_SERIALIZER;
    }
}
