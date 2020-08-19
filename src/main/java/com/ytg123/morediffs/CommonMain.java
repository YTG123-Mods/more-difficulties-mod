package com.ytg123.morediffs;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

/**
 * The main mod class.
 * @author YTG1234
 */
public class CommonMain implements ModInitializer, ServerLifecycleEvents.ServerStarted {

    public static final String MOD_ID = "morediffs";

    public static final String MOD_NAME = "More Difficulties Mod";

    public static Logger LOGGER = LogManager.getLogger();

    @Nullable
    public static MinecraftServer server;

    public static boolean serverStarted = false;

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

    /**
     * Initializes The mod.
     */
    @Override
    public void onInitialize() {
        // Registering a SERVER_STARTED event, may be used later.
        ServerLifecycleEvents.SERVER_STARTED.register(this);
        ModPacketThings.registerAllPackets();
    }

    @Override public void onServerStarted(MinecraftServer minecraftServer) {
        CommonMain.serverStarted = true;
        CommonMain.server = minecraftServer;
    }
}
