package com.ytg123.morediffs;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class CommonMain implements ModInitializer, ServerLifecycleEvents.ServerStarted {

    public static final String MOD_ID = "morediffs";

    public static final String MOD_NAME = "More Difficulties Mod";

    public static Logger LOGGER = LogManager.getLogger();

    @Nullable
    public static MinecraftServer SERVER;

    public static boolean SERVER_STARTED = false;

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(this);
        ModPacketThings.registerAllPackets();
    }

    @Override public void onServerStarted(MinecraftServer minecraftServer) {
        CommonMain.SERVER_STARTED = true;
        CommonMain.SERVER = minecraftServer;
    }
}
