package me.stupidcat.enchantingcrafts;

import me.stupidcat.enchantingcrafts.item.AttunedRuneItem;
import me.stupidcat.enchantingcrafts.item.BlankRuneItem;
import me.stupidcat.enchantingcrafts.item.ChargedRuneItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CraftsItems {

    public static final Item BLANK_RUNE = register("blank_rune", new BlankRuneItem(new FabricItemSettings().maxCount(16)));
    // public static final Item CHARGED_RUNE = register("charged_rune", new ChargedRuneItem(new FabricItemSettings().maxCount(16)));
    public static final Item ATTUNED_RUNE = register("attuned_rune", new AttunedRuneItem(new FabricItemSettings().maxCount(16).fireproof()));

    public static Item register(String id, Item item) {
        return Registry.register(Registries.ITEM, EnchantingCrafts.Id(id), item);
    }

    public static void register() {

    }
}
