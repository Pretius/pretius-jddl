package com.pretius.jddl.model;

import com.pretius.jddl.JddlException;

public class DuplicateDeserializationRuleException extends JddlException {

    private static final long serialVersionUID = 37532700648739859L;

    public DuplicateDeserializationRuleException() {
    }

    public DuplicateDeserializationRuleException(String message) {
        super(message);
    }

    public DuplicateDeserializationRuleException(Throwable cause) {
        super(cause);
    }

    public DuplicateDeserializationRuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateDeserializationRuleException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
