package com.pretius.jddl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.pretius.jddl.action.impl.DeserializeObjectAction;
import com.pretius.jddl.configurer.DeserializerConfigurer;
import com.pretius.jddl.configurer.impl.DefaultDeserializerConfigurer;
import com.pretius.jddl.model.DeserializationEvent;
import com.pretius.jddl.model.DeserializationResult;
import com.pretius.jddl.model.DeserializationRule;
import com.pretius.jddl.model.DeserializationRules;
import com.pretius.jddl.model.impl.DeserializationEventImpl;
import com.pretius.jddl.model.impl.DeserializationRulesImpl;

/**
 * DynamicObjectDeserializer internally uses
 * {@link ObjectMapper#readTree(String)} to deserialize json to intermediate
 * objects. Jackson configuration, properties, annotations will work (without
 * additional JDDL code/confiuguration) only if they are taken into account by
 * that method or are included in custom deserialization logic.
 * <p>As of right now there are a few annotations handled in {@link 
 * DeserializeObjectAction#deserialize(DeserializationEvent)} (check the javadoc for detais).
 * <p> {@link DeserializationRule} can be implemented and added to include more
 *  annotations, as the determined class object is passed in the {@link DeserializationEvent}. 
 * 
 * @author Dariusz Wawer
 *
 */
public class DynamicObjectDeserializer {

    public static final String ROOT_NODE_NAME = "_root";

    private static final Logger logger = LoggerFactory.getLogger(DynamicObjectDeserializer.class);

    
    protected DeserializationRules deserializationRules = new DeserializationRulesImpl();

    private Supplier<ObjectMapper> objectMapperSupplier;

    
    /**
     * Uses the {@link DefaultDeserializerConfigurer} to configure defuault behavior and {@code () -> new ObjectMapper}
     * as the default objectMapperSupplier.
     */
    public DynamicObjectDeserializer() {
        this.objectMapperSupplier = () -> new ObjectMapper();
        configure(new DefaultDeserializerConfigurer());
    }

    /**
     * Uses the {@link DefaultDeserializerConfigurer} to configure defuault behavior
     * @param objectMapperSupplier the objectMapper supplier to use for obtaining instances of objectMapper
     */
    public DynamicObjectDeserializer(Supplier<ObjectMapper> objectMapperSupplier) {
        this.objectMapperSupplier = objectMapperSupplier;
        configure(new DefaultDeserializerConfigurer());
    }

    /** Createas a new DynamicObjectDeserializer. Does not use the {@link DefaultDeserializerConfigurer} like the parameterless constructor.
     * @param objectMapperSupplier the objectMapper supplier to use for obtaining instances of objectMapper
     * @param configurers Configuration
     */
    public DynamicObjectDeserializer(Supplier<ObjectMapper> objectMapperSupplier, DeserializerConfigurer... configurers) {
        this.objectMapperSupplier = objectMapperSupplier;
        for (DeserializerConfigurer deserializerConfigurer : configurers) {
            configure(deserializerConfigurer);
        }
    }

    /** Createas a new DynamicObjectDeserializer. Does not use the {@link DefaultDeserializerConfigurer} like the parameterless constructor.
     * @param configurers Configuration
     */
    public DynamicObjectDeserializer(DeserializerConfigurer... configurers) {
        this.objectMapperSupplier = () -> new ObjectMapper();
        for (DeserializerConfigurer deserializerConfigurer : configurers) {
            configure(deserializerConfigurer);
        }
    }

    /** use the specified configurer to configure the deserialization rules
     * @param dc the configurer to visit the rules
     */
    public void configure(DeserializerConfigurer dc) {
        dc.configure(deserializationRules);
    }
    
    /** as in {@link Collection#add(Object)} returns a boolean 
     * @param rule Rule to be added
     * @return true when the rule was added and false when rule was not added
     */
    public boolean addRule(DeserializationRule rule) {
        return deserializationRules.addRule(rule);
    }

    public <T> T deserialize(String json, Class<T> resultClass) {
        try {
            JsonNode node = getObjectMapper().readTree(json);
            DeserializationEventImpl arg = new DeserializationEventImpl(ROOT_NODE_NAME, node, (JsonNode)null, resultClass, objectMapperSupplier, this::deserializeInternal);
            return (T) deserializeInternal(arg).getValue();
        } catch (Exception e) {
            throw new JddlException("Error deserializing json: " + json, e);
        }
    }

    public <T> List<T> deserializeArray(String json, Class<T> resultClass) {
        try {
            JsonNode node = getObjectMapper().readTree(json);
            if (!(node instanceof ArrayNode)) {
                throw new JddlException("List deserialization expects [] json, not:\n" + json);
            }

            DeserializationEventImpl arg = new DeserializationEventImpl(ROOT_NODE_NAME, node, (JsonNode)null, resultClass, ArrayList.class, objectMapperSupplier, this::deserializeInternal);
            return (List<T>) deserializeInternal(arg).getValue();
        } catch (Exception e) {
            throw new JddlException("Error deserializing json: " + json, e);
        }
    }


    private DeserializationResult deserializeInternal(DeserializationEvent deserializationEvent) throws InstantiationException, IllegalAccessException, IOException {
        for (DeserializationRule rule : deserializationRules.getAsList()) {
            if (rule.getCriterion().test(deserializationEvent)) {
                logger.debug("running action {}", rule.getAction().getClass());
                DeserializationResult result = rule.getAction().deserialize(deserializationEvent);
                return result;
            }
        }
        throw new JddlException("No deserialization rule for event=[" + deserializationEvent + "]");
    }

    
    protected ObjectMapper getObjectMapper() {
        return this.objectMapperSupplier.get();
    }

    public void setObjectMapperSupplier(Supplier<ObjectMapper> objectMapperSupplier) {
        this.objectMapperSupplier = objectMapperSupplier;
    }
}
