package com.ytg123.morediffs;

import com.chocohead.mm.api.ClassTinkerers;

/**
 * An early riser.
 * @author YTG1234
 */
public class EarlyRiser implements Runnable {
    /**
     * The run method.
     * Registers 4 new Difficulties.
     */
    @Override public void run() {
        ClassTinkerers.enumBuilder("net.minecraft.world.Difficulty", int.class, String.class)
                .addEnum("IMPOSSIBLE", 4, "impossible")
                .addEnum("IMPOSSIBLE_PLUS_PLUS", 5, "impossible++")
                .addEnum("NIGHTMARE", 6, "nightmare")
                .addEnum("BABY_MODE", 7, "baby").build();
    }
}
