package com.pretius.jddl;

public class JddlCanNotDeserializeObjectException extends RuntimeException {

    private static final long serialVersionUID = 7577870568774690916L;

    public JddlCanNotDeserializeObjectException() {
    }

    public JddlCanNotDeserializeObjectException(String message) {
        super(message);
    }

    public JddlCanNotDeserializeObjectException(Throwable cause) {
        super(cause);
    }

    public JddlCanNotDeserializeObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public JddlCanNotDeserializeObjectException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
