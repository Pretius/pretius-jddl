package com.pretius.jddl.model;

import java.util.Objects;
import java.util.function.Predicate;

public interface DeserializationCriterion extends Predicate<DeserializationEvent> {
    
    @Override
    default DeserializationCriterion and(Predicate<? super DeserializationEvent> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) && other.test(t);
    }

}
