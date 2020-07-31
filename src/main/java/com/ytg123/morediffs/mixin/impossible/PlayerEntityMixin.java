package com.ytg123.morediffs.mixin.impossible;

import com.ytg123.morediffs.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType,
                                World world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract void dropInventory();

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @Shadow
    public abstract void setFireTicks(int ticks);

    @Inject(method = "tick()V", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        if (world.getDifficulty().equals(Utils.difficulty("IMPOSSIBLE")) || world.getDifficulty().equals(Utils.difficulty("IMPOSSIBLE_PLUS_PLUS")) || world.getDifficulty().equals(Utils.difficulty("NIGHTMARE")) && !world.isClient) {
            if (this.isInLava()) {
                this.setOnFireFromLava();
                this.fallDistance *= 0.5F;
            }
        }
    }

    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("RETURN"))
    public void damageInject(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci) {
        if (world.getDifficulty().equals(Utils.difficulty("IMPOSSIBLE")) || world.getDifficulty().equals(Utils.difficulty("IMPOSSIBLE_PLUS_PLUS")) || world.getDifficulty().equals(Utils.difficulty("NIGHTMARE")) && !world.isClient) {
            if (source.getAttacker() instanceof SilverfishEntity) {
                SilverfishEntity newSilverFish = new SilverfishEntity(EntityType.SILVERFISH, world);
                newSilverFish.setPos(source.getAttacker().getPos().getX(), source.getAttacker().getPos().getY(), source.getAttacker().getPos().getZ());
                world.spawnEntity(newSilverFish);
            } else if (source.equals(DamageSource.LAVA)) {
                setFireTicks(19999980);
            } else if (source.equals(DamageSource.DROWN)) {
                setHealth(0.1f);
            } else if (source.getAttacker() instanceof PersistentProjectileEntity) {
                damage(source, amount);
            } else if (source.equals(DamageSource.ON_FIRE)) {
                setFireTicks(19999980);
            }
        }
    }
}
