package com.ytg123.morediffs.mixin.baby;

import com.ytg123.morediffs.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
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
    @Shadow public abstract MinecraftServer getServer();

    protected ServerWorldMixin(MutableWorldProperties mutableWorldProperties, RegistryKey<World> registryKey, RegistryKey<DimensionType> registryKey2, DimensionType dimensionType, Supplier<Profiler> profiler, boolean bl, boolean bl2, long l) {
        super(mutableWorldProperties, registryKey, registryKey2, dimensionType, profiler, bl, bl2, l);
    }

    @Inject(method = "spawnEntity(Lnet/minecraft/entity/Entity;)Z", at = @At(value = "INVOKE"))
    public void spawnEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity.getType().equals(EntityType.WITHER) && !this.isClient && this.getDifficulty().equals(Utils.difficulty("BABY_MODE"))) {
            getServer().getPlayerManager().broadcastChatMessage(new TranslatableText("chat.type.text", new TranslatableText("text.morediffs.wither"), new TranslatableText("text.morediffs.wither_message")), MessageType.CHAT, Util.NIL_UUID);
        }
    }
}
