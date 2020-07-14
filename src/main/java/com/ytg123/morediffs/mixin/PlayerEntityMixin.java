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

/**
 * A mixin for {@code PlayerEntity}.
 * @author YTG1234
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    /**
     * A useless constructor the should not be used anywhere.
     * @param entityType {@see LivingEntity#LivingEntity}
     * @param world {@see LivingEntity#LivingEntity}
     */
    private PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * A method that gets a difficulty from the difficulty enum.
     * @param diff The difficulty String to get.
     * @return A {@code Difficulty} object representing the difficulty.
     */
    private Difficulty difficulty(String diff) {
        return ClassTinkerers.getEnum(Difficulty.class, diff);
    }

    /**
     * A method that Injects into PlayerEntity$damage. {@see Inject}
     * @param source {@see PlayerEntity$damage}
     * @param amount {@see PlayerEntity$damage}
     * @param cir {@see Inject}
     */
    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", cancellable = true, at = @At("RETURN"))
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.isScaledWithDifficulty()) {
            if (this.world.getDifficulty().equals(difficulty("IMPOSSIBLE")) || this.world.getDifficulty().equals(difficulty("IMPOSSIBLE_PLUS_PLUS")) || this.world.getDifficulty().equals(difficulty("NIGHTMARE"))) {
                cir.setReturnValue(amount == 0.0f ? false : super.damage(source, amount * 3.0F / 2.0F));
            } else if (this.world.getDifficulty().equals(difficulty("BABY_MODE"))) {
                cir.setReturnValue(false);
            }
        }
    }
}
