package com.pretius.jddl;

public class JddlCanNotDeserializeCollectionException extends RuntimeException {

    private static final long serialVersionUID = 7577870568774690916L;

    public JddlCanNotDeserializeCollectionException() {
    }

    public JddlCanNotDeserializeCollectionException(String message) {
        super(message);
    }

    public JddlCanNotDeserializeCollectionException(Throwable cause) {
        super(cause);
    }

    public JddlCanNotDeserializeCollectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public JddlCanNotDeserializeCollectionException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
