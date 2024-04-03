package me.stupidcat.enchantingcrafts.data.runes;

import com.google.gson.JsonElement;
import me.stupidcat.enchantingcrafts.CraftsItems;
import me.stupidcat.enchantingcrafts.data.runes.RuneData;
import me.stupidcat.enchantingcrafts.data.runes.RuneDataRecipe;
import me.stupidcat.enchantingcrafts.data.runes.crafting.CraftingMethod;
import me.stupidcat.enchantingcrafts.data.runes.crafting.CraftingRegistry;
import me.stupidcat.enchantingcrafts.item.AttunedRuneItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class RuneDataOtherRecipe extends RuneDataRecipe {
    CraftingMethod method;


    public RuneDataOtherRecipe(Identifier methodId, JsonElement json, RuneData runeData, int level) {
        super(json, runeData, level);
        method = CraftingRegistry.getMethod(methodId);
    }

    public ItemStack apply(ItemStack item) {
        if (!item.isOf(CraftsItems.ATTUNED_RUNE)) {
            return ItemStack.EMPTY;
        }

        var enchants = AttunedRuneItem.getEnchantments(item);
        enchants.put(runeData.enchantment, level);
        AttunedRuneItem.apply(item, enchants);

        return item;
    }
}
