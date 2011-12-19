///@cond private
package com.bluevia.android.rest.commons.connector.http;


import java.io.InputStream;

import com.bluevia.android.rest.commons.BlueviaClientException;

/**
 * This class encapsulates HTTP errors returned by Bluevia API REST server
 *
 * @author Telefonica R&D
 * 
 */
public class HttpException extends BlueviaClientException {

	/**
     */
    private static final long serialVersionUID = 5791050574131117948L;

    /**
     * Error code for a BAD REQUEST.
     */
    public static final int BAD_REQUEST = 400;

    /**
     * Error code for a UNAUTHORIZED.
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * Error code for a FORBIDDEN.
     */
    public static final int FORBIDDEN = 403;

    /**
     * Error code for a NOT FOUND.
     */
    public static final int NOT_FOUND = 404;

    /**
     * Error code for a REQUEST TIME OUT.
     */
    public static final int REQUEST_TIME_OUT = 408;

    /**
     * Error code for a CONFLICT.
     */
    public static final int CONFLICT = 409;

    /**
     * Error code for a GONE.
     */
    public static final int GONE = 410;

    /**
     * Error code for a REQUEST ENTITY TOO LARGE.
     */
    public static final int REQUEST_ENTITY_TOO_LARGE = 413;

    /**
     * Error code for a UNSUPORTED MEDIA TYPE.
     */
    public static final int UNSUPORTED_MEDIA_TYPE = 415;

    /**
     * Error code for a INTERNAL SERVER ERROR.
     */
    public static final int INTERNAL_SERVER_ERROR = 500;

    /**
     * Error code for a NOT IMPLEMENTED.
     */
    public static final int NOT_IMPLEMENTED = 501;

    private final int statusCode;

    private final InputStream responseStream;

    /**
     * Creates an HttpException with the given message, statusCode and
     * responseStream.
     */
    public HttpException(String message, int statusCode,
        InputStream responseStream) {
      super(message, statusCode);
      this.statusCode = statusCode;
      this.responseStream = responseStream;
    }

    /**
     * Gets the status code associated with this exception.
     * @return the status code returned by the server, typically one of the SC_*
     * constants.
     */
    public int getStatusCode() {
      return statusCode;
    }

    /**
     * @return the error response stream from the server.
     */
    public InputStream getResponseStream() {
      return responseStream;
    }
}


///@endcond
