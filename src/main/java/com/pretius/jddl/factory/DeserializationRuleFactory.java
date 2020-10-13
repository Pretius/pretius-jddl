package com.pretius.jddl.factory;

import com.pretius.jddl.model.DeserializationAction;
import com.pretius.jddl.model.DeserializationCriterion;
import com.pretius.jddl.model.DeserializationRule;
import com.pretius.jddl.model.impl.DeserializationRuleImpl;

public class DeserializationRuleFactory {

    public static DeserializationRule newRule(int priority, DeserializationCriterion criterion, DeserializationAction action) {
        return new DeserializationRuleImpl(priority, criterion, action);
    }

    public static DeserializationRule typeByFieldValue(int priority, String fieldName, String fieldValue,
            Class<?> clazz)
    {
        return new DeserializationRuleImpl(
            priority, 
            DeserializationCriterionFactory.fieldTextEquals(fieldName, fieldValue),
            DeserializationActionFactory.objectToType(clazz));
    }
    
    
    private DeserializationRuleFactory() {}

}
