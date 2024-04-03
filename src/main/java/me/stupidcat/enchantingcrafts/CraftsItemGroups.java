package me.stupidcat.enchantingcrafts;

import me.stupidcat.enchantingcrafts.data.runes.RuneDataEntries;
import me.stupidcat.enchantingcrafts.item.AttunedRuneItem;
import me.stupidcat.enchantingcrafts.item.BlankRuneItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

import java.util.Objects;

public class CraftsItemGroups {
    public static final ItemGroup CRAFTS_ITEMS = register("enchanting_crafts_items", FabricItemGroup.builder()
            .icon(() -> new ItemStack(CraftsItems.BLANK_RUNE))
            .displayName(Text.translatable("itemGroup.enchantingcrafts.enchanting_crafts_items"))
            .entries((context, entries) -> {
                entries.add(CraftsItems.BLANK_RUNE);
                entries.add(CraftsItems.ATTUNED_RUNE);

                entries.add(AttunedRuneItem.getMega());

                for (var entry : AttunedRuneItem.getSortedEnchantmentList()) {
                    var data = RuneDataEntries.runeMap.get(entry.enchantment);
                    for (var i = 1; i <= Math.max(entry.enchantment.getMaxLevel(), data.recipes.size()); i++) {
                        entries.add(AttunedRuneItem.Of(Objects.requireNonNull(EnchantmentHelper.getEnchantmentId(entry.enchantment)), i));
                    }
                }
            })
            .build()
    );

    public static ItemGroup register(String id, ItemGroup group) {
        return Registry.register(Registries.ITEM_GROUP, RegistryKey.of(RegistryKeys.ITEM_GROUP, EnchantingCrafts.Id(id)), group);
    }

    public static void register() {
        // Intentionally left blank
    }
}
