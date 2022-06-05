package com.composite.common;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public interface TimeUtils {
    static String format(Duration duration) {
        long totalSeconds = duration.get(ChronoUnit.SECONDS);
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }
}
