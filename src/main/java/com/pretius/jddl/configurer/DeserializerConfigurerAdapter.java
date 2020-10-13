package com.pretius.jddl.configurer;

import java.util.Collection;

import com.pretius.jddl.model.DeserializationRule;
import com.pretius.jddl.model.DeserializationRules;

public abstract class DeserializerConfigurerAdapter implements DeserializerConfigurer {

    
    protected abstract Collection<DeserializationRule> getRules();

    
    @Override
    public void configure(DeserializationRules rules) {
        rules.addRules(getRules());
    }


}
