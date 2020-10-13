package com.pretius.jddl.util;

import java.util.Collections;
import java.util.LinkedList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.pretius.jddl.model.DeserializationRule;
import com.pretius.jddl.model.impl.DeserializationRuleImpl;

public class RulePriorityComparatorTest {

    final RulePriorityComparator comparator = new RulePriorityComparator();
    
    @Test
    public void rulePrioritySort_variousPriorityRules_largestPriorityFirstSorted() {
        LinkedList<DeserializationRule> rulesList = new LinkedList<>();
        rulesList.add(new DeserializationRuleImpl(-5, null, null));
        rulesList.add(new DeserializationRuleImpl(-2, null, null));
        rulesList.add(new DeserializationRuleImpl(7, null, null));
        rulesList.add(new DeserializationRuleImpl(-4, null, null));
        rulesList.add(new DeserializationRuleImpl(11, null, null));
        rulesList.add(new DeserializationRuleImpl(0, null, null));
        
        Collections.sort(rulesList, comparator);

        int minPriority = rulesList.stream()
                .mapToInt(DeserializationRule::getPriority)
                .min()
                .getAsInt();
        int maxPriority = rulesList.stream()
                .mapToInt(DeserializationRule::getPriority)
                .max()
                .getAsInt();

        Assertions.assertEquals(minPriority, rulesList.getLast().getPriority());
        Assertions.assertEquals(maxPriority, rulesList.getFirst().getPriority());
        
    }
}
