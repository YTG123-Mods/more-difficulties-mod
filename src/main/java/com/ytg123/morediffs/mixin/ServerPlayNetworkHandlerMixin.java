package com.ytg123.morediffs.mixin;

import com.ytg123.morediffs.Utils;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.command.arguments.IdentifierArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Shadow @Final private MinecraftServer server;

    @Inject(method = "onGameMessage(Lnet/minecraft/network/packet/c2s/play/ChatMessageC2SPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcastChatMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"))
    public void checkForMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
        if (player.getServerWorld().getDifficulty().equals(Utils.difficulty("BABY_MODE"))) {
            String msg = StringUtils.normalizeSpace(packet.getChatMessage()).toLowerCase();
            if ((msg.contains("d love to have some") || msg.contains("i wish i had") || msg.contains("d like some"))) {
                if (msg.contains("ender pearls")) {
                    ItemStack stack = new ItemStack(Items.ENDER_PEARL, 4);
                    boolean bl = player.inventory.insertStack(stack);
                    ItemEntity itemEntity;
                    if (!bl) {
                        itemEntity = player.dropItem(stack, false);
                        if (itemEntity != null) {
                            itemEntity.resetPickupDelay();
                            itemEntity.setOwner(player.getUuid());
                        }
                    }
                } else if (msg.contains("blaze rods")) {
                    ItemStack stack = new ItemStack(Items.BLAZE_ROD, 4);
                    boolean bl = player.inventory.insertStack(stack);
                    ItemEntity itemEntity;
                    if (!bl) {
                        itemEntity = player.dropItem(stack, false);
                        if (itemEntity != null) {
                            itemEntity.resetPickupDelay();
                            itemEntity.setOwner(player.getUuid());
                        }
                    }
                }
            } else if (msg.contains("no u")) {
                for (Entity entity : player.getServerWorld().getEntities(null, (Entity entity) -> entity instanceof WitherEntity)) {
                    if (entity instanceof WitherEntity) {
                        entity.kill();
                    }
                }
                for (Entity entity : player.getServerWorld().getEntities(null, (Entity entity) -> entity instanceof EnderDragonEntity)) {
                    entity.kill();
                    Advancement advancement = server.getAdvancementLoader().get(new Identifier("minecraft", "end/kill_dragon"));
                    AdvancementProgress advancementProgress = player.getAdvancementTracker().getProgress(advancement);
                    if (advancementProgress.isDone()) {
                    } else {
                        Iterator var4 = advancementProgress.getUnobtainedCriteria().iterator();

                        while(var4.hasNext()) {
                            String string = (String)var4.next();
                            player.getAdvancementTracker().grantCriterion(advancement, string);
                        }
                    }
                }

            }
        }
    }
}
