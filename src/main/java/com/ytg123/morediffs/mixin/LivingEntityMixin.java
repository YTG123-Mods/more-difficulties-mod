package com.ytg123.morediffs.mixin;

import com.ytg123.morediffs.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

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
}
