package me.stupidcat.enchantingcrafts.mixin;

import me.stupidcat.enchantingcrafts.CraftsItems;
import me.stupidcat.enchantingcrafts.data.runes.crafting.methods.LightningCraftingMethod;
import me.stupidcat.enchantingcrafts.data.runes.crafting.methods.SetOnFireCraftingMethod;
import me.stupidcat.enchantingcrafts.data.runes.crafting.methods.SubmergeCraftingMethod;
import me.stupidcat.enchantingcrafts.data.runes.crafting.methods.SubmergeLavaCraftingMethod;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    private int itemAge;

    @Inject(method = "tick()V", at = @At("HEAD"))
    public void injectTick(CallbackInfo info) {
        var stack = getStack();

        if (stack.isOf(CraftsItems.ATTUNED_RUNE)) {
            this.itemAge = 0;
            if (this.isTouchingWater()) {
                SubmergeCraftingMethod.check(stack, (ItemEntity) (Object) this);
            }

            if (this.isInLava()) {
                SubmergeLavaCraftingMethod.check(stack, (ItemEntity) (Object) this);
            }

            SetOnFireCraftingMethod.check(stack, (ItemEntity) (Object) this);
        }
    }
    @Inject(method = "applyWaterBuoyancy()V", at = @At("HEAD"), cancellable = true)
    private void applyWaterBuoyancy(CallbackInfo info) {
        var stack = getStack();

        if (stack.isOf(CraftsItems.ATTUNED_RUNE)) {
            info.cancel();
        }
    }

    @Shadow
    public abstract ItemStack getStack();

    @Shadow
    protected abstract void initDataTracker();

    @Shadow
    public abstract void readCustomDataFromNbt(NbtCompound nbt);

    @Shadow
    public abstract void writeCustomDataToNbt(NbtCompound nbt);
}
