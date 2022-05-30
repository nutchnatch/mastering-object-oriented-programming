package com.domain.logic.with.streams;

import java.time.Duration;
import java.util.List;

public interface Painter {

    boolean isAvailable();
    Duration estimateTimeToPaint(double sqMeters);
    Money estimateCompensation(double sqMeters);
    String getName();

    /**
     * Converts an array of Painters in a concrete PaintersSteam
     */
    static PaintersStream stream(List<Painter> painters) {
        return new PaintersStream(painters.stream());
    }
}
