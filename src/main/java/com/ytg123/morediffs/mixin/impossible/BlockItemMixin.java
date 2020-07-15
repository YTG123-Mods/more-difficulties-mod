package com.ytg123.morediffs.mixin.impossible;

import com.ytg123.morediffs.Utils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {
    public BlockItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
            at = @At("RETURN"))
    public void place(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (!context.getWorld().isClient &&
                (context.getWorld().getDifficulty().equals(Utils.difficulty("IMPOSSIBLE")) ||
                        context.getWorld().getDifficulty().equals(Utils.difficulty("IMPOSSIBLE_PLUS_PLUS")) ||
                        context.getWorld().getDifficulty().equals(Utils.difficulty("NIGHTMARE"))) &&
                (context.getWorld().getBlockState(context.getBlockPos()).isOf(Blocks.TORCH) ||
                        context.getWorld().getBlockState(context.getBlockPos()).isOf(Blocks.WALL_TORCH))) {
            if (cir.getReturnValue().equals(ActionResult.success(context.getWorld().isClient))) {
                class Runny implements Runnable {
                    @Override public void run() {
                        if (context.getWorld().getBlockState(context.getBlockPos()).isOf(Blocks.TORCH) ||
                                context.getWorld().getBlockState(context.getBlockPos()).isOf(Blocks.WALL_TORCH)) {
                            context.getWorld().setBlockState(context.getBlockPos(), Blocks.AIR.getDefaultState());
                            ItemEntity itemEntity = new ItemEntity(context.getWorld(), context.getBlockPos().getX(), context.getBlockPos().getY(), context.getBlockPos().getZ(), new ItemStack(
                                    Items.STICK, 1));
                            context.getWorld().spawnEntity(itemEntity);
                        }
                    }
                }
                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                scheduler.schedule(new Runny(), 20, TimeUnit.SECONDS);
            }
        }
    }
}
