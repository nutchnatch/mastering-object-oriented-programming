package com.composite;

import com.domain.logic.with.streams.Money;
import com.domain.logic.with.streams.Painter;
import java.time.Duration;

/**
 * A type should always correspond to a word in the domain-related language
 * This class tell us how much of an area, a painter has been assigned to work on
 */
public class WorkAssignment {

    private Painter painter;
    private double sqMeters;

    public WorkAssignment(Painter painter, double sqMeters) {
        this.painter = painter;
        this.sqMeters = sqMeters;
    }

    public Painter getPainter() {
        return painter;
    }

    public double getSqMeters() {
        return sqMeters;
    }

    /**
     * We should encapsulate state in such a way that objects are combined correctly
     * We shall hide state and expose behavior
     * @return
     */
    public Money estimateCompensation() {
        return this.painter.estimateCompensation(this.sqMeters);
    }

    public Duration estimateTimeToPaint() {
        return this.painter.estimateTimeToPaint(this.sqMeters);
    }
}