package me.stupidcat.enchantingcrafts;

import me.stupidcat.enchantingcrafts.data.runes.RuneDataResourceReloadListener;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceManager;
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

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new RuneDataResourceReloadListener());

		register();
	}

	public void register() {
		CraftsItems.register();
		CraftsItemGroups.register();
		CraftsRecipe.register();
	}

	public static Identifier Id(String path) {
		return new Identifier(MOD_ID, path);
	}

}