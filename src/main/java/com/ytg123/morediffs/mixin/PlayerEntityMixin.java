package com.ytg123.morediffs.mixin;

import com.chocohead.mm.api.ClassTinkerers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", cancellable = true, at=@At("RETURN"))
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.world.getDifficulty().equals(ClassTinkerers.getEnum(Difficulty.class, "IMPOSSIBLE"))) {
            if (source.isScaledWithDifficulty())
                cir.setReturnValue(amount == 0.0f ? false : super.damage(source, amount * 10000));
        }
    }
}
