package com.ytg123.morediffs.mixin.impossible;

import com.ytg123.morediffs.CommonMain;
import com.ytg123.morediffs.Utils;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Texts;
import net.minecraft.text.TranslatableText;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Shadow protected static boolean validatePlayerMove(PlayerMoveC2SPacket packet) {
        return false;
    }

    @Shadow public abstract void sendPacket(Packet<?> packet);

    @Inject(method = "onPlayerMove(Lnet/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket;)V", at = @At("RETURN"), cancellable = true)
    public void onPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        if (validatePlayerMove(packet)) {
            ci.cancel();
        }
        if ((
                player.getServerWorld().getDifficulty().equals(Utils.difficulty("IMPOSSIBLE")) ||
                        player.getServerWorld().getDifficulty().equals(Utils.difficulty("IMPOSSIBLE_PLUS_PLUS")) ||
                        player.getServerWorld().getDifficulty().equals(Utils.difficulty("NIGHTMARE"))) && !player.isCreative() && !player.isSpectator()) {
            double d = player.getServerWorld().getRandom().nextDouble();
            if (d < 0.02d) {
                CommonMain.log(Level.INFO, "TRIP");
                d = player.getServerWorld().getRandom().nextInt(4);
                sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.TITLE,
                                              null, 0, 40, 0));
                sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.SUBTITLE,
                                                                                                    new TranslatableText(
                                                                                                            "text.morediffs.trip." +
                                                                                                                    (int) d), 0, 40, 0));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 1, 255));
                CommonMain.log(Level.INFO, "DIDIT");
                player.damage(DamageSource.OUT_OF_WORLD, 1);
                if (player.getServerWorld().getDifficulty().equals(Utils.difficulty("NIGHTMARE"))) {
                    try {
                        Method m = PlayerEntity.class.getDeclaredMethod("dropInventory");
                        m.setAccessible(true);
                        m.invoke(player);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
