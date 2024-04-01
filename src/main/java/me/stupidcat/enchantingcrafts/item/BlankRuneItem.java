package me.stupidcat.enchantingcrafts.item;

import me.stupidcat.enchantingcrafts.CraftsItemGroups;
import me.stupidcat.enchantingcrafts.CraftsItems;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.EnchantingTableBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class BlankRuneItem extends Item {
    public BlankRuneItem(Settings settings) {
        super(settings);
    }

    int calculateTablePower(World world, BlockPos blockPos) {
        int i = 0;

        for (var pos : EnchantingTableBlock.POWER_PROVIDER_OFFSETS) {
            if (EnchantingTableBlock.canAccessPowerProvider(world, blockPos, pos)) {
                ++i;
            }
        }

        return i;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var itemStack = context.getStack();
        var blockPos = context.getBlockPos();
        var world = context.getWorld();
        var block = world.getBlockState(blockPos).getBlock();
        if (block instanceof EnchantingTableBlock) {
            var level = calculateTablePower(world, blockPos);
            NbtCompound tag = itemStack.getOrCreateNbt();

            if (level >= 15) {
                var newStack = CraftsItems.ATTUNED_RUNE.getDefaultStack();
                newStack.setCount(itemStack.getCount());

                var player = context.getPlayer();
                if (player != null) {
                    var inventory = context.getPlayer().getInventory();
                    player.setStackInHand(context.getHand(), newStack);

                }

            }

            return ActionResult.PASS;
        } else {
            return ActionResult.FAIL;
        }
    }
}
