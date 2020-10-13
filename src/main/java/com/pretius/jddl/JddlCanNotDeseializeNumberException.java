package com.pretius.jddl;

public class JddlCanNotDeseializeNumberException extends RuntimeException {

    private static final long serialVersionUID = 8154073590884083770L;

    public JddlCanNotDeseializeNumberException() {
    }

    public JddlCanNotDeseializeNumberException(String message) {
        super(message);
    }

    public JddlCanNotDeseializeNumberException(Throwable cause) {
        super(cause);
    }

    public JddlCanNotDeseializeNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public JddlCanNotDeseializeNumberException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
