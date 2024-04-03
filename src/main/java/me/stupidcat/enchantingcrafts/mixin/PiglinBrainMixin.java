package me.stupidcat.enchantingcrafts.mixin;

import me.stupidcat.enchantingcrafts.CraftsCraftingMethods;
import me.stupidcat.enchantingcrafts.CraftsItems;
import me.stupidcat.enchantingcrafts.data.runes.RuneDataEntries;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {

    @Inject(at = @At("HEAD"), method = "acceptsForBarter(Lnet/minecraft/item/ItemStack;)Z", cancellable = true)
    private static void acceptsForBarter(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isOf(CraftsItems.ATTUNED_RUNE)) {
            var recipes = RuneDataEntries.findRecipes(CraftsCraftingMethods.PIGLIN_BARTERING, stack);

            if (recipes.size() > 0) cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("HEAD"), method = "isGoldenItem(Lnet/minecraft/item/ItemStack;)Z", cancellable = true)
    private static void isGoldenItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isOf(CraftsItems.ATTUNED_RUNE)) {
            var recipes = RuneDataEntries.findRecipes(CraftsCraftingMethods.PIGLIN_BARTERING, stack);

            if (recipes.size() > 0) cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("HEAD"), method = "consumeOffHandItem(Lnet/minecraft/entity/mob/PiglinEntity;Z)V", cancellable = true)
    private static void consumeOffHandItem(PiglinEntity piglin, boolean barter, CallbackInfo info) {
        ItemStack itemStack = piglin.getStackInHand(Hand.OFF_HAND);
        if (itemStack.isOf(CraftsItems.ATTUNED_RUNE) && barter) {
            piglin.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);

            var recipes = RuneDataEntries.findRecipes(CraftsCraftingMethods.PIGLIN_BARTERING, itemStack);
            for (var recipe : recipes) {
                itemStack = recipe.apply(itemStack);
            }

            var list = new ArrayList<ItemStack>();
            list.add(itemStack);
            PiglinBrainInvoker.invokeDoBarter(piglin, list);
            info.cancel();
        }
    }
}
