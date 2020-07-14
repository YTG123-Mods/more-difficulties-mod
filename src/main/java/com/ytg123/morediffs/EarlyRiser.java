package com.ytg123.morediffs;

import com.chocohead.mm.api.ClassTinkerers;

public class EarlyRiser implements Runnable {
    @Override public void run() {
        ClassTinkerers.enumBuilder("net.minecraft.world.Difficulty", int.class, String.class)
                .addEnum("IMPOSSIBLE", 4, "impossible")
                .addEnum("IMPOSSIBLE_PLUS_PLUS", 5, "impossible_plus_plus")
                .addEnum("NIGHTMARE", 6, "nightmare").build();
    }
}
