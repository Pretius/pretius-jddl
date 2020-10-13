package com.pretius.jddl;

/** Superclass for all Exceptions thrown by <b>JDDL</b>
 *
 */
public class JddlClassInstantiationException extends RuntimeException {

    private static final long serialVersionUID = 4131646556706039986L;

    public JddlClassInstantiationException() {
    }

    public JddlClassInstantiationException(String message) {
        super(message);
    }

    public JddlClassInstantiationException(Throwable cause) {
        super(cause);
    }

    public JddlClassInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JddlClassInstantiationException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
