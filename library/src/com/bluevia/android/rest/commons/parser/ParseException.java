///@cond private
package com.bluevia.android.rest.commons.parser;

import com.bluevia.android.rest.commons.BlueviaClientException;

/**
 * This class encapsulates parse errors returned during the parse of the streams
 * into an entity or during the serialization of an entity into a stream
 * Code error for ParseExceptions are always INTERNAL_CLIENT_ERROR
 *
 * @author Telefonica R&D
 * 
 *
 */
public class ParseException extends BlueviaClientException {

    /**
     *
     */
    private static final long serialVersionUID = 5107010397481278895L;

    /**
     * Creates a ParseException
     */
    public ParseException() {
        super(BlueviaClientException.INTERNAL_CLIENT_ERROR);
    }

    /**
     * Creates a ParseException with the given message.
     */
    public ParseException(String detailMessage) {
        super(detailMessage, BlueviaClientException.INTERNAL_CLIENT_ERROR);
    }

    /**
     * Creates a ParseException with the given message and exception.
     */
    public ParseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable, BlueviaClientException.INTERNAL_CLIENT_ERROR);
    }

    /**
     * Creates a ParseException with the given exception.
     */
    public ParseException(Throwable throwable) {
        super(throwable, BlueviaClientException.INTERNAL_CLIENT_ERROR);
    }
}
///@endcond