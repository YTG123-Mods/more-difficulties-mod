package com.ytg123.morediffs;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonMain implements ModInitializer, ServerLifecycleEvents.ServerStarted {

    public static final String MOD_ID = "morediffs";
    public static final String MOD_NAME = "More Difficulties Mod";
    public static Logger LOGGER = LogManager.getLogger();
    public static MinecraftServer server;
    public static boolean serverStarted = false;

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");

        ServerLifecycleEvents.SERVER_STARTED.register(this);

        //TODO: Initializer
    }

    @Override public void onServerStarted(MinecraftServer minecraftServer) {
        CommonMain.serverStarted = true;
        CommonMain.server = minecraftServer;
    }
}
