package com.pretius.jddl.model.impl;

import com.pretius.jddl.model.DeserializationAction;
import com.pretius.jddl.model.DeserializationCriterion;
import com.pretius.jddl.model.DeserializationRule;

public class DeserializationRuleImpl implements DeserializationRule{

    private int priority;

    private DeserializationCriterion criterion;
    
    private DeserializationAction action;
    

    public DeserializationRuleImpl(int priority, DeserializationCriterion criterion, DeserializationAction action) {
        this.priority = priority;
        this.criterion = criterion;
        this.action = action;
    }

    
    @Override
    public DeserializationCriterion getCriterion() {
        return criterion;
    }
    
    @Override
    public DeserializationAction getAction() {
        return action;
    }

    @Override
    public int getPriority() {
        return priority;
    }
    

}
