package com.pretius.jddl.model;

import java.util.Objects;
import java.util.Optional;

@FunctionalInterface
public interface DeserializationCriterionElement {
    
    static final DeserializationCriterionElement INCONCLUSIVE = ((event)->null); 
    
    /**
     * @param e Event
     * @return Boolean.TRUE if decision is conclusive-positive, Boolean.FALSE if
     *         decision is conclusive-negative and null if decision is inconclusive
     */
    Boolean test(DeserializationEvent e);

    default DeserializationCriterionElement then(DeserializationCriterionElement other) {
        Objects.requireNonNull(other);
        return (event) -> {
            Boolean res = test(event);
            if (res != null) {
                return res; 
            } else {
                return other.test(event);
            }
        };
    }

    static DeserializationCriterion buildCriterion(DeserializationCriterionElement... elements) {
        return buildCriterion(false, elements);
    }
    
    static DeserializationCriterion buildCriterion(boolean valueIfInconclusive, DeserializationCriterionElement... elements) {
        DeserializationCriterionElement tempCriterium =DeserializationCriterionElement.INCONCLUSIVE;
        for (DeserializationCriterionElement elt : elements) {
            tempCriterium = tempCriterium.then(elt);
        }
        // "must be final" requirement workaround
        final DeserializationCriterionElement finalCriterium = tempCriterium;
        return (event) -> Optional.ofNullable(finalCriterium.test(event)).orElse(valueIfInconclusive);
    }
}
