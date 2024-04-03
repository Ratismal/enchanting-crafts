package me.stupidcat.enchantingcrafts;

import me.stupidcat.enchantingcrafts.client.renderer.AttunedRuneItemRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;

public class EnchantingCraftsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        BuiltinItemRendererRegistry.INSTANCE.register(CraftsItems.ATTUNED_RUNE, new AttunedRuneItemRenderer());

    }
}