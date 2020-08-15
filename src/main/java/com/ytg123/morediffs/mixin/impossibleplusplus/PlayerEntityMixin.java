package com.ytg123.morediffs.mixin.impossibleplusplus;

import com.ytg123.morediffs.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow public abstract HungerManager getHungerManager();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "Lnet/minecraft/entity/player/PlayerEntity;wakeUp(ZZ)V", at = @At("RETURN"))
    public void afterWakeUp(boolean bl, boolean updateSleepingPlayers, CallbackInfo ci) {
        System.out.println("Wake up");
        if (!world.isClient() && (world.getDifficulty().equals(Utils.difficulty("IMPOSSIBLE_PLUS_PLUS")) || world.getDifficulty().equals(Utils.difficulty("NIGHTMARE")))) {
            System.out.println("Almost there");
            if (world.getTimeOfDay() < 100L) {
                System.out.println("Should do it");
                ((ServerWorld)world).setTimeOfDay(Utils.bedTime + 11000L);
                Utils.bedTimePrevention = (Utils.bedTime + 10000L + 10000L) % 24000L;
                Objects.requireNonNull(world.getServer()).getPlayerManager().broadcastChatMessage(new TranslatableText("chat.type.text", new TranslatableText("text.morediffs.fundy"), new TranslatableText("text.morediffs.hungry")), MessageType.CHAT, Util.NIL_UUID);
                getHungerManager().setFoodLevel(getHungerManager().getFoodLevel() - 11);
                ((HungerManagerAccessor)getHungerManager()).setFoodSaturationLevel(((HungerManagerAccessor)getHungerManager()).getFoodSaturationLevel() - 11.0F);
            }
        }
    }
}
