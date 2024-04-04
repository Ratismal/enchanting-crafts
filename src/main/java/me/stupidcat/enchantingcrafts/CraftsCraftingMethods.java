package me.stupidcat.enchantingcrafts;

import me.stupidcat.enchantingcrafts.data.runes.crafting.CraftingMethod;
import me.stupidcat.enchantingcrafts.data.runes.crafting.CraftingRegistry;
import me.stupidcat.enchantingcrafts.data.runes.crafting.methods.*;

public class CraftsCraftingMethods {
    public static final CraftingMethod DUMMY = register("dummy", new DummyCraftingMethod());
    public static final CraftingMethod PIGLIN_BARTERING = register("piglin_barter", new PiglinBarterCraftingMethod());
    public static final CraftingMethod CHARGE_KILL = register("charge_kill", new ChargeKillCraftingMethod());
    public static final CraftingMethod CHARGE_TAKE_DAMAGE = register("charge_take_damage", new ChargeTakeDamageCraftingMethod());
    public static final CraftingMethod SUBMERGE = register("submerge", new SubmergeCraftingMethod());
    public static final CraftingMethod SUBMERGE_LAVA = register("submerge_lava", new SubmergeCraftingMethod());
    public static final CraftingMethod SET_ON_FIRE = register("set_on_fire", new SubmergeCraftingMethod());
    public static final CraftingMethod LIGHTNING = register("lightning", new LightningCraftingMethod());

    public static CraftingMethod register(String id, CraftingMethod method) {
        return CraftingRegistry.register(EnchantingCrafts.Id(id), method);
    }

    public static void register() {

    }
}
