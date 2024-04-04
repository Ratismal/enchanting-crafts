package me.stupidcat.enchantingcrafts.mixin;

import me.stupidcat.enchantingcrafts.CraftsItems;
import me.stupidcat.enchantingcrafts.data.runes.crafting.methods.LightningCraftingMethod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "onStruckByLightning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LightningEntity;)V", at = @At("HEAD"), cancellable = true)
    public void onStruckByLightning(ServerWorld world, LightningEntity lightning, CallbackInfo info) {
        if ((Entity)(Object)this instanceof ItemEntity itemEntity) {
            var stack = itemEntity.getStack();
            if (stack.isOf(CraftsItems.ATTUNED_RUNE)) {
                LightningCraftingMethod.check(stack, itemEntity);
                info.cancel();
            }
        }
    }
}
