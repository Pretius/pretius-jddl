package com.pretius.jddl.configurer.impl;

import static com.pretius.jddl.factory.DeserializationActionFactory.stringParseFunction;
import static com.pretius.jddl.factory.DeserializationCriterionFactory.nodeTypeEquals;
import static com.pretius.jddl.factory.DeserializationCriterionFactory.targetClassEquals;
import static com.pretius.jddl.factory.DeserializationRuleFactory.newRule;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MILLI_OF_SECOND;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.pretius.jddl.configurer.DeserializerConfigurerAdapter;
import com.pretius.jddl.model.DeserializationRule;


public class JavaDateTimeConfigurer extends DeserializerConfigurerAdapter {
    
    protected DateTimeFormatter offsetDateTimeFormatter;
    protected DateTimeFormatter offsetTimeFormatter;

    protected DateTimeFormatter zonedDateTimeFormatter;

    protected DateTimeFormatter localDateTimeFormatter;
    protected DateTimeFormatter localDateFormatter;
    protected DateTimeFormatter localTimeFormatter;
    
    public JavaDateTimeConfigurer() {
        super();
        
        offsetDateTimeFormatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
                .appendLiteral('-')
                .appendValue(MONTH_OF_YEAR, 2)
                .appendLiteral('-')
                .appendValue(DAY_OF_MONTH, 2)
                .appendLiteral("T")
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .appendLiteral('.')
                .appendFraction(MILLI_OF_SECOND, 3, 3, false)
                .appendOffsetId()
                .toFormatter();

        offsetTimeFormatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .appendLiteral('.')
                .appendFraction(MILLI_OF_SECOND, 3, 3, false)
                .appendOffsetId()
                .toFormatter();
        
        localDateTimeFormatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
                .appendLiteral('-')
                .appendValue(MONTH_OF_YEAR, 2)
                .appendLiteral('-')
                .appendValue(DAY_OF_MONTH, 2)
                .appendLiteral("T")
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .toFormatter();
        
        localDateFormatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
                .appendLiteral('-')
                .appendValue(MONTH_OF_YEAR, 2)
                .appendLiteral('-')
                .appendValue(DAY_OF_MONTH, 2)
                .toFormatter();
        

        localTimeFormatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .toFormatter();

        zonedDateTimeFormatter = offsetDateTimeFormatter;   
    }

    
    public JavaDateTimeConfigurer(DateTimeFormatter offsetDateTimeFormatter, DateTimeFormatter offsetTimeFormatter,
            DateTimeFormatter zonedDateTimeFormatter, DateTimeFormatter localDateTimeFormatter,
            DateTimeFormatter localDateFormatter, DateTimeFormatter localTimeFormatter)
    {
        super();
        this.offsetDateTimeFormatter = offsetDateTimeFormatter;
        this.offsetTimeFormatter = offsetTimeFormatter;
        this.zonedDateTimeFormatter = zonedDateTimeFormatter;
        this.localDateTimeFormatter = localDateTimeFormatter;
        this.localDateFormatter = localDateFormatter;
        this.localTimeFormatter = localTimeFormatter;
    }

    
    @Override
    protected Collection<DeserializationRule> getRules() { 
        int priority = -1000;
        return Arrays.asList(
                    newRule(++priority, 
                            nodeTypeEquals(JsonNodeType.STRING).and(targetClassEquals(OffsetDateTime.class)),
                            stringParseFunction(dateTimeParseLambda(OffsetDateTime::parse, offsetDateTimeFormatter))),
                    newRule(++priority, 
                            nodeTypeEquals(JsonNodeType.STRING).and(targetClassEquals(OffsetTime.class)),
                            stringParseFunction(dateTimeParseLambda(OffsetTime::parse, offsetTimeFormatter))),
                    newRule(++priority, 
                            nodeTypeEquals(JsonNodeType.STRING).and(targetClassEquals(LocalDateTime.class)),
                            stringParseFunction(dateTimeParseLambda(LocalDateTime::parse, localDateTimeFormatter))),
                    newRule(++priority, 
                            nodeTypeEquals(JsonNodeType.STRING).and(targetClassEquals(LocalDate.class)),
                            stringParseFunction(dateTimeParseLambda(LocalDate::parse, localDateFormatter))),
                    newRule(++priority, 
                            nodeTypeEquals(JsonNodeType.STRING).and(targetClassEquals(LocalTime.class)),
                            stringParseFunction(dateTimeParseLambda(LocalTime::parse, localTimeFormatter))),
                    newRule(++priority, 
                            nodeTypeEquals(JsonNodeType.STRING).and(targetClassEquals(ZonedDateTime.class)),
                            stringParseFunction(dateTimeParseLambda(ZonedDateTime::parse, zonedDateTimeFormatter)))
                );
    }


    private static Function<String, Object> dateTimeParseLambda(BiFunction<CharSequence, DateTimeFormatter, Object> parseFunction, DateTimeFormatter formatter) {
        return (s) -> {
            if (s == null || s.length() == 0) {
                return null;
            }
            return parseFunction.apply(s, formatter);
        };
    }


    public DateTimeFormatter getOffsetDateTimeFormatter() {
        return offsetDateTimeFormatter;
    }


    public void setOffsetDateTimeFormatter(DateTimeFormatter offsetDateTimeFormatter) {
        this.offsetDateTimeFormatter = offsetDateTimeFormatter;
    }


    public DateTimeFormatter getOffsetTimeFormatter() {
        return offsetTimeFormatter;
    }


    public void setOffsetTimeFormatter(DateTimeFormatter offsetTimeFormatter) {
        this.offsetTimeFormatter = offsetTimeFormatter;
    }


    public DateTimeFormatter getZonedDateTimeFormatter() {
        return zonedDateTimeFormatter;
    }


    public void setZonedDateTimeFormatter(DateTimeFormatter zonedDateTimeFormatter) {
        this.zonedDateTimeFormatter = zonedDateTimeFormatter;
    }


    public DateTimeFormatter getLocalDateTimeFormatter() {
        return localDateTimeFormatter;
    }


    public void setLocalDateTimeFormatter(DateTimeFormatter localDateTimeFormatter) {
        this.localDateTimeFormatter = localDateTimeFormatter;
    }


    public DateTimeFormatter getLocalDateFormatter() {
        return localDateFormatter;
    }


    public void setLocalDateFormatter(DateTimeFormatter localDateFormatter) {
        this.localDateFormatter = localDateFormatter;
    }


    public DateTimeFormatter getLocalTimeFormatter() {
        return localTimeFormatter;
    }


    public void setLocalTimeFormatter(DateTimeFormatter localTimeFormatter) {
        this.localTimeFormatter = localTimeFormatter;
    }


    
}
