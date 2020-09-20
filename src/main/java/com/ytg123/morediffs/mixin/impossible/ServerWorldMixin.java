package com.ytg123.morediffs.mixin.impossible;

import com.ytg123.morediffs.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    protected ServerWorldMixin(MutableWorldProperties mutableWorldProperties, RegistryKey<World> registryKey, DimensionType dimensionType, Supplier<Profiler> profiler, boolean bl, boolean bl2, long l) {
        super(mutableWorldProperties, registryKey, dimensionType, profiler, bl, bl2, l);
    }

    @Shadow
    protected abstract boolean addEntity(Entity entity);

    @Inject(method = "spawnEntity(Lnet/minecraft/entity/Entity;)Z",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/server/world/ServerWorld;addEntity(Lnet/minecraft/entity/Entity;)Z"),
            cancellable = true)
    public void spawnEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (getDifficulty().equals(Utils.difficulty("IMPOSSIBLE")) || getDifficulty().equals(Utils.difficulty("IMPOSSIBLE_PLUS_PLUS")) || getDifficulty().equals(Utils.difficulty("NIGHTMARE"))) {
            if (entity.getType().equals(EntityType.ZOMBIE) || entity.getType().equals(EntityType.SKELETON)) {
                HostileEntity hostileEntity = (HostileEntity) entity;
                if (entity.getType().equals(EntityType.ZOMBIE)) {
                    hostileEntity.setBaby(true);
                }
                hostileEntity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.OAK_BUTTON, 1));
                hostileEntity.setEquipmentDropChance(EquipmentSlot.HEAD, 0);
                hostileEntity.setEquipmentDropChance(EquipmentSlot.CHEST, 0);
                hostileEntity.setEquipmentDropChance(EquipmentSlot.LEGS, 0);
                hostileEntity.setEquipmentDropChance(EquipmentSlot.FEET, 0);
            } else if (entity.getType().equals(EntityType.SPIDER)) {
                ((SpiderEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 19999980, 5, false, false, false));
                ((SpiderEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 19999980, 2, false, false, false));
            } else if (entity.getType().equals(EntityType.CREEPER)) {
                CompoundTag tag = new CompoundTag();
                tag.put("powered", ByteTag.of(true));
                tag.put("ExplosionRadius", ByteTag.of((byte) 3));
                ((CreeperEntity) entity).readCustomDataFromTag(tag);
            }
            cir.setReturnValue(addEntity(entity));
            cir.cancel();
        }
    }
}
