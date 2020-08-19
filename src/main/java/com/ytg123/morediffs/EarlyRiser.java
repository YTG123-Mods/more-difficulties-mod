package com.ytg123.morediffs;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;

public class EarlyRiser implements Runnable {
    @Override public void run() {
        ClassTinkerers.enumBuilder(FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net.minecraft.class_1267"), int.class, String.class)
                .addEnum("IMPOSSIBLE", 4, "impossible")
                .addEnum("IMPOSSIBLE_PLUS_PLUS", 5, "impossible++")
                .addEnum("NIGHTMARE", 6, "nightmare")
                .addEnum("BABY_MODE", 7, "baby").build();
        System.out.println("EARLY RISER!");
    }
}
