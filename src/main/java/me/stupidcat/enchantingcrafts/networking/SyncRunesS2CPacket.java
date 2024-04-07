package me.stupidcat.enchantingcrafts.networking;

import me.stupidcat.enchantingcrafts.EnchantingCrafts;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.Map;

public record SyncRunesS2CPacket(Map<Identifier, String> runes) implements FabricPacket {
    public static final PacketType<SyncRunesS2CPacket> TYPE = PacketType.create(
            EnchantingCrafts.Id("s2c/sync_origin_registry"), SyncRunesS2CPacket::read
    );

    private static SyncRunesS2CPacket read(PacketByteBuf buffer) {
        return new SyncRunesS2CPacket(buffer.readMap(PacketByteBuf::readIdentifier, PacketByteBuf::readString));
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeMap(runes, PacketByteBuf::writeIdentifier, PacketByteBuf::writeString);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
