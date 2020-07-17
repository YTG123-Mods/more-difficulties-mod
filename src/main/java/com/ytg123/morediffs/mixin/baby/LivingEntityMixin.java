package com.ytg123.morediffs.mixin.baby;

import com.ytg123.morediffs.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow @Final private Map<StatusEffect, StatusEffectInstance> activeStatusEffects;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow protected abstract void onStatusEffectApplied(StatusEffectInstance effect);

    @Shadow protected abstract void onStatusEffectUpgraded(StatusEffectInstance effect, boolean reapplyEffect);

    @Inject(method = "drop(Lnet/minecraft/entity/damage/DamageSource;)V", at = @At("HEAD"), cancellable = true)
    public void drop(DamageSource source, CallbackInfo ci) {
        if (world.getDifficulty().equals(Utils.difficulty("BABY_MODE"))) {
            if ((Object) this instanceof EndermanEntity) {
                dropStack(new ItemStack(Items.ENDER_PEARL, 2));
                ci.cancel();
            } else if ((Object) this instanceof WitherSkeletonEntity) {
                dropStack(new ItemStack(Items.WITHER_SKELETON_SKULL, 1));
                ci.cancel();
            } else if ((Object) this instanceof BlazeEntity) {
                dropStack(new ItemStack(Items.BLAZE_ROD, 12));
                ci.cancel();
            }
        }
    }

    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/entity/LivingEntity;onStatusEffectApplied(Lnet/minecraft/entity/effect/StatusEffectInstance;)V"),
            cancellable = true)
    public void onStatusEffectAdded(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if (world.getDifficulty().equals(Utils.difficulty("BABY_MODE")) && ((Object)this) instanceof PlayerEntity) {
            StatusEffectInstance
                    effectInstance =
                    new StatusEffectInstance(effect.getEffectType(), 32767, effect.getAmplifier());
            onStatusEffectApplied(effectInstance);
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/entity/LivingEntity;onStatusEffectUpgraded(Lnet/minecraft/entity/effect/StatusEffectInstance;Z)V"),
            cancellable = true)
    public void statusEffectUpgraded(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if (world.getDifficulty().equals(Utils.difficulty("BABY_MODE")) && ((Object)this) instanceof PlayerEntity) {
            StatusEffectInstance
                    statusEffectInstance =
                    (StatusEffectInstance) this.activeStatusEffects.get(effect.getEffectType());

            StatusEffectInstance
                    effectInstance =
                    new StatusEffectInstance(effect.getEffectType(), 32767, effect.getAmplifier());
            onStatusEffectApplied(effectInstance);
            this.onStatusEffectUpgraded(statusEffectInstance, true);
            cir.setReturnValue(true);
        }
    }
}
