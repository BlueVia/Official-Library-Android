///@cond private
/**
 * \package com.bluevia.android.rest.messaging.sms This package contains the classes in order to send SMS using Bluevia API.
 * \package com.bluevia.android.rest.messaging.sms.client This package contains the classes in order to send SMS using Bluevia API.
 */
package com.bluevia.android.rest.messaging.sms.client;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import android.content.Context;

import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.client.AbstractRestClient;
import com.bluevia.android.rest.commons.connector.IConnector;
import com.bluevia.android.rest.commons.connector.http.HttpException;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.http.authentication.AuthenticationInfo;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.messaging.MessageryClientHelper;
import com.bluevia.android.rest.messaging.SendMessageResult;

/**
 * Client interface for the REST binding of the Bluevia SMS Service.
 *
 * @author Telefonica R&D
 * 
 */

public abstract class SmsClient extends AbstractRestClient {

    private static final String TAG = "SmsClient";
   
	protected static final String FEED_SMS_BASE_URI = "REST/SMS";
    protected static final String FEED_SMS_SANDBOX_BASE_URI = "REST/SMS_Sandbox";
    
    /**
     * Creates an SmsClient object to be able to send SMS to the gSDP
	 * @param context the Android context of the application.
     * @param uri the base uri of the client
     * @param connector the connector for communication mechanism.
     * @param auth the parameters with the security info supplied by the developer.
     * @param factory the factory that parse and serialize entities to stream and vice-versa.
     * 
     * @throws BlueviaClientException if an exception occurs during the request.
     */
    public SmsClient(Context context, String uri, IConnector connector, AuthenticationInfo auth, BlueviaEntityParserFactory factory) throws BlueviaClientException {
        super(context, uri, connector, auth, factory);
    }
    
    /* (non-Javadoc)
     * @see com.bluevia.android.rest.client.AbstractRestClient#createEntity(java.lang.String, com.bluevia.android.rest.Entity)
     */
    protected Entity createEntity (String feedUri, Entity entity, HttpQueryParams params) throws ParseException, IOException, HttpException {

    	HashMap<String, String> responseHeaders = new HashMap<String, String>();

        InputStream is = mConnector.createEntity(feedUri, entity, mFactory.createSerializer(), mAuthInfo, params, responseHeaders);
        if (is != null)
        	is.close();
        
        SendMessageResult response = MessageryClientHelper.getResponseWithMessageId(responseHeaders);
        return response;
    }
    
}
///@endcond
