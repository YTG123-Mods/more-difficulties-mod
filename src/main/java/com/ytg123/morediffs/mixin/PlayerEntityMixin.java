package com.ytg123.morediffs.mixin;

import com.ytg123.morediffs.Utils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.NumberRange;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * A mixin for {@code PlayerEntity}.
 *
 * @author YTG1234
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    /**
     * A useless constructor the should not be used anywhere.
     *
     * @param entityType {@see LivingEntity#LivingEntity}
     * @param world      {@see LivingEntity#LivingEntity}
     */
    private PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract void setFireTicks(int ticks);

    /**
     * A method that Injects into PlayerEntity$damage. {@see Inject}
     *
     * @param source {@see PlayerEntity$damage}
     * @param amount {@see PlayerEntity$damage}
     * @param cir    {@see Inject}
     */
    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", cancellable = true, at = @At("RETURN"))
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (world.getDifficulty().equals(Utils.difficulty("BABY_MODE"))) {
            if (source.equals(DamageSource.LAVA)) {
                amount = 0.0F;
                if (world.getBlockState(getBlockPos()).isOf(Blocks.LAVA)) {
                    if (world.getBlockState(getBlockPos()).getFluidState().getLevel() == 0) {
                        world.setBlockState(getBlockPos(), Blocks.OBSIDIAN.getDefaultState());
                    } else {
                        world.setBlockState(getBlockPos(), Blocks.COBBLESTONE.getDefaultState());
                    }
                }
            } else if (source.equals(DamageSource.IN_FIRE)) {
                amount = 0.0F;
                if (world.getBlockState(getBlockPos()).isOf(Blocks.FIRE)) {
                    world.setBlockState(getBlockPos(), Blocks.AIR.getDefaultState());
                }
            } else if (source.equals(DamageSource.ON_FIRE)) {
                amount = 0.0F;
                this.setFireTicks(0);
            } else if (source.equals(DamageSource.FALL)) {
                amount /= 20.0F;
            } else if (source.equals(DamageSource.CRAMMING) || source.equals(DamageSource.HOT_FLOOR) || source.equals(DamageSource.STARVE)) {
                amount = 0.0F;
                cir.setReturnValue(false);
            }
        }

        if (source.isScaledWithDifficulty()) {
            if (this.world.getDifficulty().equals(Utils.difficulty("IMPOSSIBLE")) || this.world.getDifficulty().equals(Utils.difficulty("IMPOSSIBLE_PLUS_PLUS")) || this.world.getDifficulty().equals(Utils.difficulty("NIGHTMARE"))) {
                cir.setReturnValue(amount != 0.0f && super.damage(source, amount * 3.0F / 2.0F));
            } else if (this.world.getDifficulty().equals(Utils.difficulty("BABY_MODE"))) {
                amount /= 20.0F;
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "tick()V", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (!world.isClient && world.getDifficulty().equals(Utils.difficulty("BABY_MODE"))) {
            List<Entity> entities = ((ServerWorld) world).getEntities(null, (entity) -> {
                class FloatRange extends NumberRange<Float> {
                    private final Double squaredMin;
                    private final Double squaredMax;

                    public FloatRange(Float min, Float max) {
                        super(min, max);
                        this.squaredMin = square(min);
                        this.squaredMax = square(max);
                    }

                    public Double square(Float value) {
                        return value == null ? null : value.doubleValue() * value.doubleValue();
                    }

                    public boolean testSqrt(double value) {
                        if (this.squaredMin != null && this.squaredMin > value) {
                            return false;
                        } else {
                            return this.squaredMax == null || this.squaredMax >= value;
                        }
                    }
                }
                return new FloatRange(0.0F, 3.0F).testSqrt(entity.squaredDistanceTo(new Vec3d(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ())));
            });
            for (Entity entity : entities) {
                if (entity instanceof HostileEntity) {
                    ((HostileEntity) entity).damage(DamageSource.mob(this), Float.MAX_VALUE);
                }
            }
        }
    }
}
