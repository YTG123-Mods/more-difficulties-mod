package com.ytg123.morediffs.mixin.baby;

import com.mojang.authlib.GameProfile;
import com.ytg123.morediffs.ModPacketThings;
import com.ytg123.morediffs.Utils;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "tick()V", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (world.isClient() && MinecraftClient.getInstance().crosshairTarget.getType().equals(HitResult.Type.BLOCK) && world.getDifficulty().equals(
                Utils.difficulty("BABY_MODE"))) {
            if (BlockTags.LOGS.values()
                              .contains(world.getBlockState(new BlockPos(MinecraftClient.getInstance().crosshairTarget.getPos()))
                                             .getBlock())) {
//                Block block = world.getBlockState(new BlockPos(MinecraftClient.getInstance().crosshairTarget.getPos()))
//                                   .getBlock();
                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                passedData.writeBlockPos(new BlockPos(MinecraftClient.getInstance().crosshairTarget.getPos()));
                ClientSidePacketRegistry.INSTANCE.sendToServer(ModPacketThings.DESTROY_BLOCK_PACKET_ID, passedData);
            }
        }
    }
}
