package me.stupidcat.enchantingcrafts;

import me.stupidcat.enchantingcrafts.data.runes.RuneDataEntries;
import me.stupidcat.enchantingcrafts.data.runes.RuneDataResourceReloadListener;
import me.stupidcat.enchantingcrafts.networking.SyncRunesS2CPacket;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantingCrafts implements ModInitializer {
	public static final String MOD_ID = "enchantingcrafts";
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		register();

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new RuneDataResourceReloadListener());
	}

	public void register() {
		CraftsItems.register();
		CraftsItemGroups.register();
		CraftsLoot.register();
		CraftsCraftingMethods.register();

		CraftsRecipes.register();

		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((phase, listener, success) -> {
			for (var rune : RuneDataEntries.runeMap.values()) {
				for (var recipe : rune.recipes) {
					recipe.refreshRecipe();
				}
			}
		});

		ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(EnchantingCrafts.Id("enchantment_runes"), (player, joined) -> {
			ServerPlayNetworking.send(player, new SyncRunesS2CPacket(RuneDataEntries.getRawData()));
		});
	}

	public static Identifier Id(String path) {
		return new Identifier(MOD_ID, path);
	}

}