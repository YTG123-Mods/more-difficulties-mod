package com.ytg123.morediffs.mixin.impossibleplusplus;

import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HungerManager.class)
public interface HungerManagerAccessor {
    @Accessor
    float getFoodSaturationLevel();

    @Accessor
    void setFoodSaturationLevel(float saturationLevel);
}
