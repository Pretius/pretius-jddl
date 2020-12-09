package com.pretius.jddl.model;

public interface DeserializationRule {

    /**
     * Rules with arithmetically higher priority get processed earlier than those
     * with lower priority. Rule with priority 10 will be processed before rule with
     * priority 5.<br>
     * Criteria with priorities &lt; 0 are used internally for default behaviors. 0
     * is the default priority. It is advised to use values &gt; 0 for custom
     * criteria, so that they are processed before defaults.<br>
     * Note that priority only matters in case there are at least two different
     * which criteria match the same event.
     * 
     * @return the priority of processing of the criterion
     */
    int getPriority();

    /**
     * the Predicate to test on the DeserializationEvent to determine whether this
     * rule's DeserializationAction should be ran
     * 
     * @return the Preticate to test
     */
    DeserializationCriterion getCriterion();

    /** This method will be called only if the DeserializationCriterion associated with this rule has been tested true.  
     * 
     * @return the action to perform on the deserialization event
     */
    DeserializationAction getAction();

}
