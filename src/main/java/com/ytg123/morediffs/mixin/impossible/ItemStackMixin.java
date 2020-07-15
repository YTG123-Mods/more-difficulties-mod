package com.ytg123.morediffs.mixin.impossible;

import com.ytg123.morediffs.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract void setDamage(int damage);

    @Inject(method = "damage(ILjava/util/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z",
            at = @At("RETURN"))
    public void damage(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (player.getServerWorld().getDifficulty().equals(Utils.difficulty("IMPOSSIBLE")) ||
                player.getServerWorld().getDifficulty().equals(Utils.difficulty("IMPOSSIBLE_PLUS_PLUS")) ||
                player.getServerWorld().getDifficulty().equals(Utils.difficulty("NIGHTMARE"))) {
            if (random.nextDouble() < 0.10D) {
                setDamage(999999999);
            }
        }
    }
}
