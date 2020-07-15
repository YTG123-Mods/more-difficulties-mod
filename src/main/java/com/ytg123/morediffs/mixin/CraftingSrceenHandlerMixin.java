package com.ytg123.morediffs.mixin;

import com.ytg123.morediffs.Utils;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CraftingScreenHandler.class)
public abstract class CraftingSrceenHandlerMixin {
    @Inject(method = "updateResult(ILnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/inventory/CraftingInventory;Lnet/minecraft/inventory/CraftingResultInventory;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void updateResult(int syncId, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftingResultInventory resultInventory, CallbackInfo ci, ServerPlayerEntity serverPlayerEntity, ItemStack itemStack) {
        if (world.getDifficulty().equals(Utils.difficulty("BABY_MODE"))) {
            if (Enchantments.EFFICIENCY.isAcceptableItem(itemStack)) {
                itemStack.addEnchantment(Enchantments.EFFICIENCY, 5);
            }
            if (Enchantments.UNBREAKING.isAcceptableItem(itemStack)) {
                itemStack.addEnchantment(Enchantments.UNBREAKING, 3);
            }
            if (Enchantments.PROTECTION.isAcceptableItem(itemStack)) {
                itemStack.addEnchantment(Enchantments.PROTECTION, 4);
            }
            if (Enchantments.MENDING.isAcceptableItem(itemStack)) {
                itemStack.addEnchantment(Enchantments.MENDING, 1);
            }

            resultInventory.setStack(0, itemStack);
        }
    }
}
