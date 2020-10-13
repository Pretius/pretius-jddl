package com.pretius.jddl.configurer.impl;

import static com.pretius.jddl.factory.DeserializationCriterionFactory.nodeTypeEquals;
import static com.pretius.jddl.factory.DeserializationCriterionFactory.nodeTypeIn;
import static com.pretius.jddl.factory.DeserializationCriterionFactory.targetClassEquals;
import static com.pretius.jddl.factory.DeserializationCriterionFactory.targetClassIn;
import static com.pretius.jddl.factory.DeserializationCriterionFactory.targetClassIsEnum;
import static com.pretius.jddl.factory.DeserializationRuleFactory.newRule;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.pretius.jddl.configurer.DeserializerConfigurerAdapter;
import com.pretius.jddl.factory.DeserializationActionFactory;
import com.pretius.jddl.model.DeserializationRule;

public class DefaultDeserializerConfigurer extends DeserializerConfigurerAdapter {

    @Override
    protected Collection<DeserializationRule> getRules() {
        int priority = -2000;
        return Arrays.asList(
            newRule(++priority, 
                    nodeTypeIn(JsonNodeType.OBJECT, JsonNodeType.POJO), 
                    DeserializationActionFactory.defaultObjectHandle()),
            newRule(++priority, 
                    nodeTypeIn(JsonNodeType.STRING), 
                    DeserializationActionFactory.defaultStringHandle()),
            newRule(++priority,
                    nodeTypeEquals(JsonNodeType.ARRAY),
                    DeserializationActionFactory.defaultArrayHandle()),
            newRule(++priority,
                    nodeTypeEquals(JsonNodeType.STRING).and(targetClassIsEnum()),
                    DeserializationActionFactory.defaultEnumHandle()),
            newRule(++priority,
                    nodeTypeEquals(JsonNodeType.NUMBER),
                    DeserializationActionFactory.defaultNumberHandle()),
            newRule(++priority,
                    nodeTypeEquals(JsonNodeType.BOOLEAN),
                    DeserializationActionFactory.defaultBooleanHandle()),
            newRule(++priority,
                    nodeTypeEquals(JsonNodeType.STRING).and(targetClassIn(Boolean.class, Boolean.TYPE)),
                    DeserializationActionFactory.defaultBooleanHandle()),
            newRule(++priority,
                    nodeTypeEquals(JsonNodeType.STRING).and(targetClassEquals(BigDecimal.class)),
                    DeserializationActionFactory.defaultBooleanHandle()),
            
            newRule(++priority,
                    nodeTypeIn(JsonNodeType.MISSING, JsonNodeType.NULL),
                    DeserializationActionFactory.defaultNullHandle())
        );
    }

}
