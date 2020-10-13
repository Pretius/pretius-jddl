package com.pretius.jddl.model;

import java.util.Collection;
import java.util.List;

public interface DeserializationRules {

    /** creates and returns a list that contains all the rules in the order they will be processed in the deserializer. Changes to the collection are not possible.
     * @return
     */
    List<DeserializationRule> getAsList();

    /**
     * @param rule the rule to be removed
     * @return {@code true} if the rules changed as a result of the call
     */
    boolean removeRule(DeserializationRule rule);

    /**
     * @param rule the rule to add
     * @return true, as specified by {@link List#add(Object)}
     */
    boolean addRule(DeserializationRule rule);

    /**
     * @param rulesCollection rules to add
     * @return true, as specified by {@link List#add(Object)}
     */
    boolean addRules(Collection<DeserializationRule> rulesCollection);

}