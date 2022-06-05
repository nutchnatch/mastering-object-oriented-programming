package com.domain.logic.with.streams;

import com.composite.Velocity;
import com.composite.WorkAssignment;

import javax.xml.bind.ValidationEventLocator;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface Painter {

    // this method should not exist in this interface, it is not object oriented - should be replaced by available method
//    boolean isAvailable();
    Optional<Painter> available();
    Duration estimateTimeToPaint(double sqMeters);
    Money estimateCompensation(double sqMeters);
    String getName();

    /**
     * Assigns a sqMeters to a painter
     * @param sqMeters
     * @return
     */
    default WorkAssignment assign(double sqMeters) {
        return new WorkAssignment(this, sqMeters);
    }

    /**
     * Converts an array of Painters in a concrete PaintersSteam
     */
    static PaintersStream stream(List<Painter> painters) {
        return new PaintersStream(painters.stream());
    }

    default Velocity estimateVelocity(double sqMeters) {
        return new Velocity(sqMeters, this.estimateTimeToPaint(sqMeters));
    }
}
