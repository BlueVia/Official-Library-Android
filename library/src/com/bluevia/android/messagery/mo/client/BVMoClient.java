/**
 * \package com.bluevia.android.messagery.mo This package contains the classes in order to receive SMS and MMS using Bluevia API.
 * \package com.bluevia.android.messagery.mo.client This package contains REST client to receive SMS and MMS using Bluevia API.
 */
package com.bluevia.android.messagery.mo.client;

import com.bluevia.android.commons.Utils;
import com.bluevia.android.commons.client.BVBaseClient;
import com.bluevia.android.commons.exception.BlueviaException;

/**
 * 
 * Abstract client interface for the REST binding of the Bluevia Messagery MO Service.
 * 
 * @author Telefonica R&D
 *
 */
public abstract class BVMoClient extends BVBaseClient {

	protected static final String FEED_INBOUND_REQUESTS = "/inbound";
    protected static final String RECEIVED_MESSAGES = "/messages";

    /**
     * Check if the registrationId passed as parameter is valid.
     *
     * @param id registrationId to check.
     * @throws BlueviaException if the id is not valid.
     */
    protected static void checkRegistrationId(String id) throws BlueviaException {
        if (Utils.isEmpty(id))
            throw new BlueviaException("Bad request: RegistrationId is either null or empty", BlueviaException.BAD_REQUEST_EXCEPTION);
    }
}
