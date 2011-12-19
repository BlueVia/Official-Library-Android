/**
 * \package com.bluevia.android.rest This package contains basic classes for the implementation of the Bluevia API.
 */
package com.bluevia.android.rest.commons;

/**
 * This class encapsulates errors returned by Bluevia API REST server and
 * internal client errors.
 *
 * @author Telefonica R&D
 * 
 *
 */
public class BlueviaClientException extends Exception {


    /**
     * Error code for INTERNAL SERVER ERROR.
     */
    public static final int INTERNAL_CLIENT_ERROR = -1;
    /**
     * Error code for BAD REQUEST EXCEPTION
     */
    public static final int BAD_REQUEST_EXCEPTION = -2;
    /**
     * Error code for FAILURE RESULT EXCEPTION
     */
    public static final int FAILURE_RESULT_EXCEPTION = -3;
    ///@cond private
	
	/**
     */
    private static final long serialVersionUID = 7895370761834116911L;
    
    private final int code;
    
    ///@endcond

    /**
     * Sets de Bluevia error code
     * 
     * @param code Bluevia Error Code
     *
     */
    public BlueviaClientException(int code) {
        this.code = code;
    }
    
    ///@cond private

    /**
     * @param detailMessage message to be included in the exception
     * @param code Bluevia Error Code
     */
    public BlueviaClientException(String detailMessage, int code) {
        super("[" + code + "] " + detailMessage);
        this.code = code;
    }

    /**
     * @param throwable cause of the exception
     * @param code Bluevia Error Code
     */
    public BlueviaClientException(Throwable throwable, int code) {
        super("[" + code + "]", throwable);
        this.code = code;
    }

    /**
     * @param detailMessage message to be included in the exception
     * @param throwable cause of the exception
     * @param code Bluevia Error Code
     */
    public BlueviaClientException(String detailMessage, Throwable throwable, int code) {
        super("[" + code + "] " + detailMessage, throwable);
        this.code = code;
    }

    /**
     * Gets the Bluevia exception code
     * @return exception code
     */
    public int getCode() {
        return code;
    }
    
    ///@endcond
}
