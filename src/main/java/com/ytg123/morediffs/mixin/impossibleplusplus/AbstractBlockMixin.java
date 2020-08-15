package com.ytg123.morediffs.mixin.impossibleplusplus;

import com.ytg123.morediffs.Utils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {
    @Inject(method = "onBlockBreakStart(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At(value = "RETURN"))
    public void onBlockBreakStartEnds(BlockState state, World world, BlockPos pos, PlayerEntity player, CallbackInfo ci) {
        if (!world.isClient()) {
            if ((Registry.BLOCK.getId(state.getBlock())
                    .getPath()
                    .contains("log") || Registry.BLOCK.getId(state.getBlock())
                    .getPath()
                    .contains("stem") || Registry.BLOCK.getId(state.getBlock())
                    .getPath()
                    .contains("stone")) && player.getMainHandStack().isEmpty() && (world.getDifficulty().equals(Utils.difficulty("IMPOSSIBLE_PLUS_PLUS")) || world.getDifficulty().equals(Utils.difficulty("NIGHTMARE")))) {
                ((ServerPlayerEntity) player).networkHandler.sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.TITLE,
                        null, 0, 40, 0));
                ((ServerPlayerEntity) player).networkHandler.sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.SUBTITLE,
                        new TranslatableText(
                                "text.morediffs.tools", 0, 40, 0)));
                player.damage(DamageSource.GENERIC, 3.5f);
            }
        }
    }
}