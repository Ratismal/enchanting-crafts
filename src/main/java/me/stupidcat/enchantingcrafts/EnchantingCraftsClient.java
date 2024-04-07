package me.stupidcat.enchantingcrafts;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.stupidcat.enchantingcrafts.client.renderer.AttunedRuneItemRenderer;
import me.stupidcat.enchantingcrafts.data.runes.RuneData;
import me.stupidcat.enchantingcrafts.data.runes.RuneDataEntries;
import me.stupidcat.enchantingcrafts.networking.SyncRunesS2CPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.network.ClientPlayerEntity;

import java.io.InputStreamReader;

public class EnchantingCraftsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        BuiltinItemRendererRegistry.INSTANCE.register(CraftsItems.ATTUNED_RUNE, new AttunedRuneItemRenderer());

        ClientPlayConnectionEvents.INIT.register(((clientPlayNetworkHandler, minecraftClient) -> {
            ClientPlayNetworking.registerReceiver(SyncRunesS2CPacket.TYPE, EnchantingCraftsClient::receiveRunes);
        }));
    }

    public static void receiveRunes(SyncRunesS2CPacket packet, ClientPlayerEntity player, PacketSender responseSender) {
        for (var entry : packet.runes().entrySet()) {
            var obj = JsonParser.parseString(entry.getValue()).getAsJsonObject();
            var data = new RuneData(entry.getKey());

            data.parseJson(obj);

            RuneDataEntries.register(data);
        }
    }
}