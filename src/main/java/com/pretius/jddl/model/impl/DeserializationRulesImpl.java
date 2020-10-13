package com.pretius.jddl.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.pretius.jddl.model.DeserializationRule;
import com.pretius.jddl.model.DeserializationRules;
import com.pretius.jddl.util.RulePriorityComparator;

public class DeserializationRulesImpl implements DeserializationRules {
    
    private RulePriorityComparator comparator = new RulePriorityComparator();
    
    private List<DeserializationRule> rulesList = new ArrayList<>(16);

    
    @Override
    public List<DeserializationRule> getAsList() {
        return Collections.unmodifiableList(rulesList);
    }
    
    @Override
    public boolean removeRule(DeserializationRule rule) {
        return rulesList.remove(rule);
    }
    
    @Override
    public boolean addRule(DeserializationRule rule) {
        boolean changed = rulesList.add(rule);
        sortRules();
        return changed;
    }

    protected void sortRules() {
        Collections.sort(rulesList, comparator);
    }

    @Override
    public boolean addRules(Collection<DeserializationRule> rulesCollection) {
        boolean changed = rulesList.addAll(rulesCollection);
        sortRules();
        return changed;
    }
    
}
