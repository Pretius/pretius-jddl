package com.pretius.jddl.util;

import java.util.Comparator;

import com.pretius.jddl.JddlException;
import com.pretius.jddl.model.DeserializationRule;

public class RulePriorityComparator implements Comparator<DeserializationRule> {

    @Override
    public int compare(DeserializationRule o1, DeserializationRule o2) {
        if (o1 == null || o2 == null) {
            throw new JddlException("Model does not accept null rules");
        }
        return -Integer.compare(o1.getPriority(), o2.getPriority());
    }
    
}
