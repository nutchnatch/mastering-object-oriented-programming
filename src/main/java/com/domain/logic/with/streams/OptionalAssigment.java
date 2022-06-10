package com.domain.logic.with.streams;

import com.composite.WorkAssignment;

import java.util.Optional;
import java.util.stream.Stream;

public class OptionalAssigment {

    private Optional<WorkAssignment> content;

    private OptionalAssigment(Optional<WorkAssignment> content) {
        this.content = content;
    }

    public static OptionalAssigment of(WorkAssignment assignment) {
        return new OptionalAssigment(Optional.of(assignment));
    }

    public static OptionalAssigment empty() {
        return new OptionalAssigment(Optional.empty());
    }

    public WorkStream stream() {
        return WorkAssignment.stream(this.content.map(Stream::of).orElse(Stream.empty()));
    }

    @Override
    public String toString() {
        return this.content.map(WorkAssignment::toString).orElse("");
    }
}
