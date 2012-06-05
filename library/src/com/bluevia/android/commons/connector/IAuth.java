package com.bluevia.android.commons.connector;

import com.bluevia.android.commons.exception.BlueviaException;

/**
 * Interface that will be implemented by REST clients that will allow to
 * authenticate requests.
 *
 */
public interface IAuth {

	/**
	 * Authenticates the request
	 */
	public void authenticate() throws BlueviaException;
	
}
