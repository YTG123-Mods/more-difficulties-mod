package com.ytg123.morediffs.mixin;

import com.ytg123.morediffs.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Shadow
    public ServerWorld world;

    @Inject(method = "tryBreakBlock",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir, BlockState state, BlockEntity entity, Block block) {
        if ((state.isOf(Blocks.STONE) || state.isOf(Blocks.ANDESITE) || state.isOf(Blocks.DIORITE) || state.isOf(Blocks.GRANITE)) && world.getDifficulty().equals(Utils.difficulty("BABY_MODE"))) {
            Random r = new Random();
            double num = r.nextDouble();
            if (num < 0.25D) {
                num *= 4.0D;
                if (num < 0.3D) {
                    dropItem(new ItemStack(Items.COAL, 1 + r.nextInt(2)), player);
                } else if (num < 0.5D) {
                    dropItem(new ItemStack(Items.IRON_ORE), player);
                } else if (num < 0.65D) {
                    dropItem(new ItemStack(Items.LAPIS_LAZULI, 2 + r.nextInt(9)), player);
                } else if (num < 0.8D) {
                    dropItem(new ItemStack(Items.REDSTONE, 2 + r.nextInt(5)), player);
                } else if (num < 0.9D) {
                    dropItem(new ItemStack(Items.EMERALD), player);
                } else if (num < 1.0D) {
                    dropItem(new ItemStack(Items.DIAMOND), player);
                }


            }
        }
    }

    private void dropItem(ItemStack stack, ServerPlayerEntity player) {
        ItemEntity itemEntity;
        itemEntity = player.dropItem(stack, false);
        if (itemEntity != null) {
            itemEntity.resetPickupDelay();
            itemEntity.setOwner(player.getUuid());
        }
    }
}
