package me.stupidcat.enchantingcrafts.mixin;

import me.stupidcat.enchantingcrafts.EnchantingCrafts;
import me.stupidcat.enchantingcrafts.data.runes.crafting.methods.ChargeKillCraftingMethod;
import me.stupidcat.enchantingcrafts.data.runes.crafting.methods.ChargeTakeDamageCraftingMethod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(at = @At("HEAD"), method = "onKilledOther(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)Z")
    private void init(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
        ChargeKillCraftingMethod.check((PlayerEntity)(Object)this, other);
    }

    @Redirect(at = @At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerEntity;setHealth(F)V"), method = "applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V")
    public void injectApplyDamage(PlayerEntity player, float health, DamageSource source) {
        var amount = player.getHealth() - health;
        EnchantingCrafts.LOGGER.info("Took {} damage", amount);
        ChargeTakeDamageCraftingMethod.check(player, source, amount);

        player.setHealth(health);
    }
}
