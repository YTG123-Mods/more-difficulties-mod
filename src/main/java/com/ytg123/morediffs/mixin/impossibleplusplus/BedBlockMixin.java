package com.ytg123.morediffs.mixin.impossibleplusplus;

import com.ytg123.morediffs.Utils;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(BedBlock.class)
public abstract class BedBlockMixin extends HorizontalFacingBlock {
    protected BedBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onPlaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V", at = @At(value = "HEAD"), cancellable = true)
    public void beforePlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack, CallbackInfo ci) {
        if (placer instanceof PlayerEntity && !world.isClient()) {
            if (!world.getDimension().isBedWorking() && !((PlayerEntity)placer).isCreative() && !((PlayerEntity)placer).isSpectator() && (world.getDifficulty().equals(Utils.difficulty("IMPOSSIBLE_PLUS_PLUS")) || world.getDifficulty().equals(Utils.difficulty("NIGHTMARE")))) {
                Objects.requireNonNull(world.getServer()).getPlayerManager().broadcastChatMessage(new TranslatableText("chat.type.text", new TranslatableText("text.morediffs.fundy"), new TranslatableText("text.morediffs.bed")), MessageType.CHAT, Util.NIL_UUID);
                ci.cancel();
            }
        }
    }

    @Inject(method = "onUse(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
    public void beforeSleep(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (!player.isCreative() && !player.isSpectator() && !world.isClient()) {
            System.out.println("Before sleep");
            System.out.println(Utils.bedTimePrevention);
            long currentTime = world.getTimeOfDay();
            if (currentTime > Utils.bedTimePrevention) {
                Utils.bedTime = world.getTime();
                Utils.bedTimePrevention = -1L;
            } else {
                System.out.println("Preventing Sleep");
                Objects.requireNonNull(world.getServer()).getPlayerManager().broadcastChatMessage(new TranslatableText("chat.type.text", new TranslatableText("text.morediffs.fundy"), new TranslatableText("text.morediffs.nottired")), MessageType.CHAT, Util.NIL_UUID);
                cir.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }
}
