package com.ytg123.morediffs.mixin;

import com.ytg123.morediffs.Utils;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.network.MessageType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderDragonFight.class)
public class EnderDragonFightMixin {
    @Shadow @Final private ServerWorld world;

    @Inject(method = "createDragon()Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;", at = @At("INVOKE"))
    private void createDragon(CallbackInfoReturnable<EnderDragonEntity> cir) {
        if (world.getDifficulty().equals(Utils.difficulty("BABY_MODE")))
            world.getServer().getPlayerManager().broadcastChatMessage(new TranslatableText("chat.type.text", new TranslatableText("text.morediffs.end"), new TranslatableText("text.morediffs.end_message")), MessageType.CHAT, Util.NIL_UUID);
    }
}
