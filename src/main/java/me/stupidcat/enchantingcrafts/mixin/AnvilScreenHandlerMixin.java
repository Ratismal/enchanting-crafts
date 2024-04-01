package me.stupidcat.enchantingcrafts.mixin;

import me.stupidcat.enchantingcrafts.CraftsItems;
import me.stupidcat.enchantingcrafts.item.AttunedRuneItem;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
    @Shadow
    @Final
    private Property levelCost;

    @Shadow
    private String newItemName;

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    public int getCost(Enchantment enchantment, int level) {
        int cost = switch (enchantment.getRarity()) {
            case COMMON -> 1;
            case UNCOMMON -> 2;
            case RARE -> 4;
            case VERY_RARE -> 8;
        };

        cost = Math.max(1, cost / 2);
        return cost * Math.min(enchantment.getMaxLevel(), level);
    }

    @Inject(at = @At("HEAD"), method = "updateResult()V", cancellable = true)
    public void updateResult(CallbackInfo info) {
        ItemStack sourceStack = this.input.getStack(0);
        ItemStack upgradeStack = this.input.getStack(1);

        if (!sourceStack.isEmpty() && !upgradeStack.isEmpty() && upgradeStack.isOf(CraftsItems.ATTUNED_RUNE)) {
            int cost = sourceStack.getRepairCost() + upgradeStack.getRepairCost();
            int extraCost = 0;

            var resultStack = sourceStack.copy();

            Map<Enchantment, Integer> map = EnchantmentHelper.get(resultStack);
            var upgrades = AttunedRuneItem.getEnchantments(upgradeStack);
            var appliedEnchantment = false;

            for (var upgrade : upgrades.entrySet()) {
                var enchantment = upgrade.getKey();
                var level = upgrade.getValue();
                if (!enchantment.isAcceptableItem(resultStack)) continue;

                boolean hasConflict = false;
                boolean alreadyExists = false;
                for (var existingEntry : map.entrySet()) {
                    if (enchantment == existingEntry.getKey()) {
                        alreadyExists = true;

                        if (level > existingEntry.getValue()) {
                            map.put(enchantment, level);
                            extraCost += getCost(enchantment, level);
                            appliedEnchantment = true;
                        }
                    } else if (!existingEntry.getKey().canCombine(enchantment)) {
                        hasConflict = true;
                    }
                }

                // TODO: Allow conflicts based on config.
                if (!alreadyExists && !hasConflict) {
                    map.put(enchantment, level);
                    extraCost += getCost(enchantment, level);
                    appliedEnchantment = true;
                }
            }

            if (!appliedEnchantment) {
                info.cancel();
                return;
            }

            EnchantmentHelper.set(map, resultStack);

            if (this.newItemName != null && !Util.isBlank(this.newItemName)) {
                if (!this.newItemName.equals(resultStack.getName().getString())) {
                    extraCost += 1;
                    resultStack.setCustomName(Text.literal(this.newItemName));
                }
            } else if (resultStack.hasCustomName()) {
                extraCost += 1;
                resultStack.removeCustomName();
            }

            int repairCost = resultStack.getRepairCost();
            repairCost = AnvilScreenHandler.getNextCost(repairCost);
            resultStack.setRepairCost(repairCost);

            this.levelCost.set(cost + extraCost);
            this.output.setStack(0, resultStack);
            this.sendContentUpdates();
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "canTakeOutput(Lnet/minecraft/entity/player/PlayerEntity;Z)Z", cancellable = true)
    protected void canTakeOutput(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> cir) {

    }

    @Inject(at = @At("HEAD"), method = "onTakeOutput(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)V", cancellable = true)
    protected void onTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfo info) {

    }

    @Shadow
    protected abstract boolean canUse(BlockState state);

    @Shadow
    protected abstract ForgingSlotsManager getForgingSlotsManager();
}
