package com.ytg123.morediffs;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The main mod class.
 * @author YTG1234
 */
public class CommonMain implements ModInitializer, ServerLifecycleEvents.ServerStarted {

    /**
     * This Mod's mod ID
     */
    public static final String MOD_ID = "morediffs";
    /**
     * This Mod's name
     */
    public static final String MOD_NAME = "More Difficulties Mod";
    /**
     * The logger that this mod uses
     */
    public static Logger LOGGER = LogManager.getLogger();
    /**
     * The current server - May be <code>null</code>.
     */
    public static MinecraftServer server;
    /**
     * Has the server fully started?
     */
    public static boolean serverStarted = false;

    /**
     * A method to log the specified information to the console.
     * @param level The log level. {@see Level}
     * @param message The message to log.
     */
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
    }

    /**
     * The Server started event callback.
     * @param minecraftServer The server that started
     */
    @Override public void onServerStarted(MinecraftServer minecraftServer) {
        CommonMain.serverStarted = true;
        CommonMain.server = minecraftServer;
    }
}
