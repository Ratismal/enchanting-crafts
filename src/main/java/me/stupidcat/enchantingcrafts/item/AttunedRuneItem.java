package me.stupidcat.enchantingcrafts.item;

import me.stupidcat.enchantingcrafts.CraftsItems;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttunedRuneItem extends Item {
    public AttunedRuneItem(Settings settings) {
        super(settings);
    }

    public static ArrayList<EnchantmentLevelEntry> getSortedEnchantmentList() {
        var allEnchantments = Registries.ENCHANTMENT.getIds();

        var sortedEnchantList = new ArrayList<EnchantmentLevelEntry>();

        for (var enchantId : allEnchantments) {
            var enchant = Registries.ENCHANTMENT.get(enchantId);
            if (enchant == null) continue;

            sortedEnchantList.add(new EnchantmentLevelEntry(enchant, enchant.getMaxLevel()));
        }

        sortedEnchantList.sort((a, b) -> {
            var count = 0;
            if (a.enchantment.isTreasure()) {
                count += 10000;
            }
            if (b.enchantment.isTreasure()) {
                count -= 10000;
            }
            if (a.enchantment.isCursed()) {
                count += 1000;
            }
            if (b.enchantment.isCursed()){
                count -= 1000;
            }

            count += a.enchantment.getTranslationKey().compareTo(b.enchantment.getTranslationKey());

            return count;
        });

        return sortedEnchantList;
    }

    public static ItemStack getMega() {
        var stack = CraftsItems.ATTUNED_RUNE.getDefaultStack();

        var sortedEnchantList = getSortedEnchantmentList();

        stack.setCustomName(Text.translatable("item.enchantingcrafts.mega_rune")
                .setStyle(Style.EMPTY
                        .withItalic(false)
                        .withColor(Formatting.RESET)
                        .withColor(Formatting.DARK_AQUA)));

        var tag = stack.getOrCreateNbt();
        var enchantments = new NbtList();

        for (var enchant : sortedEnchantList) {

            var node = new NbtCompound();

            node.putString("enchantment", Registries.ENCHANTMENT.getId(enchant.enchantment).toString());
            node.putInt("level", enchant.level);

            enchantments.add(node);
        }

        tag.put("enchants", enchantments);

        return stack;
    }

    public static ItemStack Of(Identifier enchantment, int level) {
        var stack = CraftsItems.ATTUNED_RUNE.getDefaultStack();

        var tag = stack.getOrCreateNbt();

        var enchantments = new NbtList();

        var enchant = new NbtCompound();

        enchant.putString("enchantment", enchantment.toString());
        enchant.putInt("level", level);

        enchantments.add(enchant);

        tag.put("enchants", enchantments);
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        var enchants = getEnchantments(stack);

        for (var entry : enchants.entrySet()) {
            var enchant = entry.getKey();

            var formatting = Formatting.DARK_PURPLE;
            if (enchant.isCursed()) {
                formatting = Formatting.RED;
            } else if (enchant.isTreasure()) {
                formatting = Formatting.GOLD;
            }

            tooltip.add(enchant.getName(entry.getValue()).copy().setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(formatting))));
        }
    }

    public static void apply(ItemStack stack, Map<Enchantment, Integer> map) {
        var tag = stack.getOrCreateNbt();

        var enchantments = new NbtList();

        for (var entry : map.entrySet()) {
            var enchant = new NbtCompound();
            var id = EnchantmentHelper.getEnchantmentId(entry.getKey());
            enchant.putString("enchantment", id.toString());
            enchant.putInt("level", entry.getValue());

            enchantments.add(enchant);
        }

        tag.put("enchants", enchantments);
    }

    public static Map<Enchantment, Integer> getEnchantments(ItemStack stack) {
        var tag = stack.getNbt();
        var map = new HashMap<Enchantment, Integer>();

        if (tag == null) {
            return map;
        }

        var enchants = tag.get("enchants");
        if (enchants instanceof NbtList enchantList) {
            for (var i = 0; i < enchantList.size(); i++) {
                var compound = enchantList.getCompound(i);

                var enchantId = Identifier.tryParse(compound.getString("enchantment"));
                var level = compound.getInt("level");

                var enchant = Registries.ENCHANTMENT.get(enchantId);
                if (enchant == null) {
                    enchant = Enchantments.VANISHING_CURSE;
                    level = 1;
                }

                map.put(enchant, level);
            }
        }

        return map;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        var tag = stack.getNbt();

        return tag != null && tag.contains("enchants");
    }
}
