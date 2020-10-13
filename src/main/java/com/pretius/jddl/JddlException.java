package com.pretius.jddl;

/** Superclass for all Exceptions thrown by <b>JDDL</b>
 *
 */
public class JddlException extends RuntimeException {

    private static final long serialVersionUID = 6069342562091779990L;

    public JddlException() {
    }

    public JddlException(String message) {
        super(message);
    }

    public JddlException(Throwable cause) {
        super(cause);
    }

    public JddlException(String message, Throwable cause) {
        super(message, cause);
    }

    public JddlException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
