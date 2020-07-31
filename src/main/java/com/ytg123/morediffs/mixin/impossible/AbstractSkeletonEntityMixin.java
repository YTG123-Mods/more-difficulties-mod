package com.ytg123.morediffs.mixin.impossible;

import com.ytg123.morediffs.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin extends HostileEntity {
    protected AbstractSkeletonEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "attack(Lnet/minecraft/entity/LivingEntity;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void attack(LivingEntity target, float pullProgress, CallbackInfo ci, ItemStack itemStack, PersistentProjectileEntity persistentProjectileEntity) {
        if (world.getDifficulty().equals(Utils.difficulty("IMPOSSIBLE")) || world.getDifficulty().equals(Utils.difficulty("IMPOSSIBLE_PLUS_PLUS")) || world.getDifficulty().equals(Utils.difficulty("NIGHTMARE")) && !world.isClient) {
            persistentProjectileEntity.setFireTicks(19999980);
            Vec3d velocity = persistentProjectileEntity.getVelocity();
            velocity.multiply(2);
            Vec3d newV = new Vec3d(velocity.getX(), velocity.getY() - 0.15D, velocity.getZ());
            persistentProjectileEntity.setVelocity(newV);
        }
    }
}
