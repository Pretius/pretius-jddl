package com.pretius.jddl;

public class JddlCanNotDeserializeEnumException extends RuntimeException {

    private static final long serialVersionUID = 7577870568774690916L;

    public JddlCanNotDeserializeEnumException() {
    }

    public JddlCanNotDeserializeEnumException(String message) {
        super(message);
    }

    public JddlCanNotDeserializeEnumException(Throwable cause) {
        super(cause);
    }

    public JddlCanNotDeserializeEnumException(String message, Throwable cause) {
        super(message, cause);
    }

    public JddlCanNotDeserializeEnumException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
