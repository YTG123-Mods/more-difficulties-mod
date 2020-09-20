package com.ytg123.morediffs;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public abstract class ModPacketThings {
    public static final Identifier DESTROY_BLOCK_PACKET_ID = new Identifier("morediffs", "break");

    public static void registerAllPackets() {
        ServerSidePacketRegistry.INSTANCE.register(DESTROY_BLOCK_PACKET_ID, (packetContext, attachedData) -> {
            // Get the BlockPos we put earlier in the IO thread
            BlockPos pos = attachedData.readBlockPos();
            packetContext.getTaskQueue().execute(() -> {
                // Execute on the main thread
                if (
                        !packetContext.getPlayer().isCreative() &&
                                !packetContext.getPlayer().isSpectator() && packetContext.getPlayer().world.canSetBlock(pos))
                    packetContext.getPlayer().world.breakBlock(pos, true, packetContext.getPlayer());
            });
        });
    }
}
