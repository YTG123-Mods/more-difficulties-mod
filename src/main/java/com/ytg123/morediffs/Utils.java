package com.ytg123.morediffs;

import com.chocohead.mm.api.ClassTinkerers;
import net.minecraft.world.Difficulty;

public abstract class Utils {
    public static Difficulty difficulty(String diff) {
        return ClassTinkerers.getEnum(Difficulty.class, diff);
    }

    public static long bedTimePrevention = -1L;
    public static long bedTime = 0L;
}
