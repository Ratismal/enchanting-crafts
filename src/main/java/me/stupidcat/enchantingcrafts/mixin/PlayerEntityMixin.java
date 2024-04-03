package me.stupidcat.enchantingcrafts.mixin;

import me.stupidcat.enchantingcrafts.data.runes.crafting.methods.ChargeKillCraftingMethod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "onKilledOther(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)Z")
    private void init(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
        ChargeKillCraftingMethod.check((PlayerEntity)(Object)this, other);
        // This code is injected into the start of MinecraftServer.loadWorld()V
    }
}
