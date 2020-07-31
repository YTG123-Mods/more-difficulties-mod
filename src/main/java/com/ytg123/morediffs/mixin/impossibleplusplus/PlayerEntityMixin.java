package com.ytg123.morediffs.mixin.impossibleplusplus;

import com.ytg123.morediffs.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "wakeUp()V", at = @At("RETURN"))
    public void afterWakeUp(CallbackInfo ci) {
        if (!world.isClient() && (world.getDifficulty().equals(Utils.difficulty("IMPOSSIBLE_PLUS_PLUS")) || world.getDifficulty().equals(Utils.difficulty("NIGHTMARE")))) {

        }
    }
}
