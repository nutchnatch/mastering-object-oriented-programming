package com.composite;

import com.domain.logic.with.streams.Painter;

import java.util.List;
import java.util.stream.Stream;

/**
 * This might be called a substitutable object, since it will have multiple implementations
 * It is easy to substitute behavior with strategy
 * We can remove entire hardcoded implementation from consumer
 */
public interface PaintingScheduler {

    Stream<WorkAssignment> schedule(List<Painter> painters, double sqMeters);
}
