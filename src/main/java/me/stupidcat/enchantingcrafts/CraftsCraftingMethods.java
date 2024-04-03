package me.stupidcat.enchantingcrafts;

import me.stupidcat.enchantingcrafts.data.runes.crafting.CraftingMethod;
import me.stupidcat.enchantingcrafts.data.runes.crafting.CraftingRegistry;
import me.stupidcat.enchantingcrafts.data.runes.crafting.methods.ChargeKillCraftingMethod;
import me.stupidcat.enchantingcrafts.data.runes.crafting.methods.DummyCraftingMethod;
import me.stupidcat.enchantingcrafts.data.runes.crafting.methods.PiglinBarterCraftingMethod;

public class CraftsCraftingMethods {
    public static final CraftingMethod DUMMY = register("dummy", new DummyCraftingMethod());
    public static final CraftingMethod PIGLIN_BARTERING = register("piglin_barter", new PiglinBarterCraftingMethod());
    public static final CraftingMethod CHARGE_KILL = register("charge_kill", new ChargeKillCraftingMethod());

    public static CraftingMethod register(String id, CraftingMethod method) {
        return CraftingRegistry.register(EnchantingCrafts.Id(id), method);
    }

    public static void register() {

    }
}
