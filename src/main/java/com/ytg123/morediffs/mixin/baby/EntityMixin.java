package com.ytg123.morediffs.mixin.baby;

import com.google.common.collect.ImmutableSet;
import com.ytg123.morediffs.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.MessageType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public World world;

    @Inject(method = "onSwimmingStart()V", at = @At("RETURN"))
    public void onSwim(CallbackInfo ci) {
        if (((Object) this) instanceof PlayerEntity) {
            if (!world.isClient() && world.getDifficulty().equals(Utils.difficulty("BABY_MODE")) && !((PlayerEntity)((Object)this)).isCreative()) {
                if (!(((PlayerEntity) ((Object) this)).inventory.containsAny(ImmutableSet.of(Items.OAK_BOAT,
                                                                                             Items.ACACIA_BOAT,
                                                                                             Items.BIRCH_BOAT,
                                                                                             Items.DARK_OAK_BOAT,
                                                                                             Items.JUNGLE_BOAT,
                                                                                             Items.SPRUCE_BOAT)))) {
                    ((ServerWorld) world).getServer()
                                         .getPlayerManager()
                                         .broadcastChatMessage(new TranslatableText("chat.type.text",
                                                                                    new TranslatableText(
                                                                                            "text.morediffs.god"),
                                                                                    new TranslatableText(
                                                                                            "text.morediffs.water_message")),
                                                               MessageType.CHAT,
                                                               Util.NIL_UUID);
                    ItemEntity itemEntity;
                    ItemStack stack = new ItemStack(Items.OAK_BOAT, 1);
                    itemEntity = ((PlayerEntity) ((Object) this)).dropItem(stack, false);
                    if (itemEntity != null) {
                        itemEntity.resetPickupDelay();
                        itemEntity.setOwner(((PlayerEntity) ((Object) this)).getUuid());
                    }

                }
            }
        }
    }
}
