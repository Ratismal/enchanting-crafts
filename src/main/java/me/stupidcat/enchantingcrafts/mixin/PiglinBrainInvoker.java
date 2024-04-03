package me.stupidcat.enchantingcrafts.mixin;

import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(PiglinBrain.class)
public interface PiglinBrainInvoker {
    @Invoker("doBarter")
    public static void invokeDoBarter(PiglinEntity piglin, List<ItemStack> items) {
        throw new AssertionError();
    }
}
