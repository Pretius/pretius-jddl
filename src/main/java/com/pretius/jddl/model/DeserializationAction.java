package com.pretius.jddl.model;

import java.io.IOException;

@FunctionalInterface
public interface DeserializationAction {
    
    DeserializationResult deserialize(DeserializationEvent event) throws InstantiationException, IllegalAccessException, IOException;
    
}
