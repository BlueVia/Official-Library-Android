/**
 * \package com.bluevia.android.rest.commons.client This package contains basic classes for the implementation of RESTful clients for Bluevia APIs.
 */
package com.bluevia.android.rest.commons.client;

/*! \page api_reference API Reference
 *
 * Version 1.0
 * 
 * Welcome to Open Telefonica Application Framework!
 * 
 * If you are new, check the <strong>\ref mainpage "Developer Guide"</strong> to get started.
 * You can take a look to the individual API guides and the API Reference too:
 *
 * \section available_apis Available APIs:
 * 
 * <ul>
 *   <li><strong>\ref blv_sms_guide "SMS Client API"</strong>; it is composed by the following packages:
 *       <ul>
 *           <li>com.bluevia.android.rest.messaging.sms.client</li>
 *           <li>com.bluevia.android.rest.messaging.sms.data</li>
 *           <li>com.bluevia.android.rest.messaging.sms.parser</li> 
 *           <li>com.bluevia.android.rest.messaging.sms.parser.xml</li> 
 *       </ul>
 *   </li>
 *   <br />
 *   <li><strong>\ref blv_mms_guide "MMS Client API"</strong>; it is composed by the following packages:
 *       <ul>
 *           <li>com.bluevia.android.rest.messaging.mms.client</li>
 *           <li>com.bluevia.android.rest.messaging.mms.data</li>
 *           <li>com.bluevia.android.rest.messaging.mms.parser</li>
 *           <li>com.bluevia.android.rest.messaging.mms.parser.xml</li>
 *       </ul>
 *   </li>
 *   <br />
 *   <li><strong>\ref blv_directory_guide "Directory Client API"</strong>; it is composed by the following packages:
 *       <ul>
 *           <li>com.bluevia.android.rest.directory.client</li>
 *           <li>com.bluevia.android.rest.directory.data</li>
 *           <li>com.bluevia.android.rest.directory.parser.xml</li>
 *       </ul>
 *    </li>
 *   <br />
 *    <li><strong>\ref blv_ad_guide "Advertising Client API"</strong>; it is composed by the following packages:
 *       <ul>
 *           <li>com.bluevia.android.rest.ad.client</li>
 *           <li>com.bluevia.android.rest.ad.data</li>
 *           <li>com.bluevia.android.rest.ad.parser.xml</li>
 *       </ul>
 *    </li>
 *   <br />
 *    <li><strong>\ref blv_location_guide "Location Client API"</strong>; it is composed by the following packages:
 *       <ul>
 *           <li>com.bluevia.android.rest.location.client</li>
 *           <li>com.bluevia.android.rest.location.data</li>
 *           <li>com.bluevia.android.rest.location.parser</li>
 *           <li>com.bluevia.android.rest.location.parser.xml</li>
 *       </ul>
 *    </li>
 *   <br />
 *    <li><strong>\ref blv_payment_guide "Payment Client API"</strong>; it is composed by the following packages:
 *       <ul>
 *           <li>com.bluevia.android.rest.payment.client</li>
 *           <li>com.bluevia.android.rest.payment.data</li>
 *           <li>com.bluevia.android.rest.payment.parser</li>
 *           <li>com.bluevia.android.rest.payment.parser.xml</li>
 *       </ul>
 *    </li>
 *   <br />
 *    <li><strong>\ref blv_call_management_guide "Call Management Client API"</strong>; it is composed by the following packages:
 *       <ul>
 *           <li>com.bluevia.android.rest.call.client</li>
 *           <li>com.bluevia.android.rest.call.data</li>
 *           <li>com.bluevia.android.rest.call.parser.xml</li>
 *       </ul>
 *    </li>
 *   <br />
 *    <li>Other packages with common functionallity:
 *       <ul>
 *           <li>com.bluevia.android.rest.commons</li>
 *           <li>com.bluevia.android.rest.oauth.data</li>
 *       </ul>
 *    </li>
 * </ul>
 *
 */


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.connector.IConnector;
import com.bluevia.android.rest.commons.connector.http.HttpException;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.http.authentication.AuthenticationInfo;
import com.bluevia.android.rest.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParser;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory;
import com.bluevia.android.rest.commons.parser.ParseException;

import android.content.Context;
import android.util.Log;

/**
 * Abstract representation of an HTTP REST Client for the Bluevia API
 * 
 * Available clients may be
 * <ul>
 *   <li>SMS Client API</li>
 *   <li>MMS Client API</li>
 *   <li>Directory Client API</li>
 *   <li>Advertisement Client API</li>
 *   <li>Oauth Client API</li>
 *   <li>Location Client API</li>
 *   <li>Payment Client API</li>
 * </ul>
 * 
 * @author Telefonica R&D
 * @version 1.0
 * 
 *
 */
public abstract class AbstractRestClient {
	
	/**
	 * The possible values to determinate the instantiated connector for the client.
	 * 
	 * Available values may be
	 * <ul>
	 *   <li>LIVE: In the Live environment your application uses the real network, which means 
	 * 		that you will be able to send real transactions to real Movistar, 
	 * 		O2 and Vivo customers in the applicable country.</li>
	 *   <li>TEST: The Test mode behave exactly like the Live mode, but the API calls are free of chargue, using a credits system. 
	 * 		You are required to have a Movistar, O2 or Vivo mobile number to get this monthly credits.</li>
	 *   <li>SANDBOX: The Sandbox environment offers you the exact same experience as the Live environment except 
	 * 		that no traffic is generated on the live network, meaning you can experiment and play until your heart’s content.</li>
	 *   <li>MOCK_OK: Emulates the connection where ok and the data received is correct</li>
	 *   <li>MOCK_ERROR_IOEXCEPTION: Emulates an IOException that occurs during the communication</li>
	 *   <li>MOCK_ERROR_HTTPEXCEPTION: Emulates a HttpException that occurs during the communication</li>
	 * 	 <li>MOCK_UNAUTHORIZED: Emulates a 401 error - Unauthorized</li>
	 * </ul>
	 * 
	 */
	public static enum Mode {LIVE, TEST, SANDBOX, MOCK_OK, MOCK_UNAUTHORIZED, MOCK_ERROR_IOEXCEPTION, MOCK_ERROR_HTTPEXCEPTION}
    
	/// @cond private
	private static final String TAG = "AbstractRestClient";
	
    /**
     * The object to create security headers
     */
    protected final AuthenticationInfo mAuthInfo;
    
    /**
     * The base uri of the client
     */
    protected final String mBaseUri;
    
    /**
     * The data client to communicate and transmit the streams to the gSDP
     */
    protected final IConnector mConnector;
    
    /**
     * The factory to serialize and parser entities into streams and vice-versa
     */
    protected final BlueviaEntityParserFactory mFactory;
    
    /**
     * The Android context of the application
     */
    protected Context mContext;
    
    /// @endcond
    
    /**
     * Closes the client and frees all the resources it might have used.
     */
    public void close() {
    	mConnector.close();
    }
    
    /// @cond private
    
    /**
     * Creates an AbstractRestClient object with a specific IConnector
     * @param context the context of the application
     * @param uri the base uri of the client
     * @param connector the connector for communication mechanism
     * @param authenticationInfo the parameters with the security info supplied by the developer.
     * @param factory the factory that parse and serialize entities to stream and vice-versa.
     * 
     * @throws BlueviaClientException when any of the above parameters are null
     */
    protected AbstractRestClient(Context context, String uri, IConnector connector, 
            AuthenticationInfo authenticationInfo, BlueviaEntityParserFactory factory) throws BlueviaClientException {
    	if ((authenticationInfo == null) || (connector == null) || (factory == null))
            throw new BlueviaClientException("ApiMode, Connector, OauthParams and ParserFactory should not be null", BlueviaClientException.BAD_REQUEST_EXCEPTION);
    	
    	mContext = context;
    	mAuthInfo = authenticationInfo;
        mFactory = factory;
        mConnector = connector;
        mBaseUri = uri;
    }
    
    /**
     * Creates an AbstractRestClient object with a specific IConnector and no oauth information (specific for Oauth API)
     * @param context the context of the application
     * @param uri the base uri of the client
     * @param connector the connector for communication mechanism
     * @param factory the factory that parse and serialize entities to stream and vice-versa.
     * 
     * @throws BlueviaClientException when any of the above parameters are null
     */
    protected AbstractRestClient(Context context,  String uri, IConnector connector, BlueviaEntityParserFactory factory) throws BlueviaClientException {
        if ((connector == null) || (factory == null))
            throw new BlueviaClientException("ApiMode, Connector and ParserFactory should not be null", BlueviaClientException.BAD_REQUEST_EXCEPTION);
        
        mAuthInfo = null;
        mBaseUri = uri;
        mContext = context;
        mFactory = factory;
        mConnector = connector;
    }

    /**
     * Creates a request using HTTP REST to the gSDP server in order to create an entity
     * using POST method
     *
     * @param feedUri the REST URI of the entity to create
     * @param entity the entity to create using REST POST into the gSDP server
     * @return the response entity object
     * @throws ParseException
     * @throws IOException
     * @throws HttpException
     */
    protected Entity createEntity (String feedUri, Entity entity) throws ParseException, IOException, HttpException {
    	Log.d(TAG, "Setting ImputStream");
        InputStream is = mConnector.createEntity(feedUri, entity, mFactory.createSerializer(), mAuthInfo);
        
        
        try {
        	return parseEntity(mFactory.createParser(), is);
	    } finally {
	    	if (is != null)
	    		is.close();      	
	    }
    }
    
    /**
     * Creates a request using HTTP REST to the gSDP server in order to create an entity
     * using POST method
     *
     * @param feedUri the REST URI of the entity to create
     * @param entity the entity to create using REST POST into the gSDP server
     * @return the response entity object
     * @throws ParseException
     * @throws IOException
     * @throws HttpException
     */
    protected Entity createEntity (String feedUri, Entity entity, HttpQueryParams parameters) throws ParseException, IOException, HttpException {
        InputStream is = mConnector.createEntity(feedUri, entity, mFactory.createSerializer(), mAuthInfo, parameters, null);
        try {
        	return parseEntity(mFactory.createParser(), is);
	    } finally {
	    	if (is != null)
	    		is.close();      	
	    }
    }

    /**
     * Creates a request using HTTP REST to the gSDP server in order to create an entity using POST method
     * This method allows for the creation on entities based on a set of parameters instead of an Bluevia entity.
     * Thus, the entity parameter may be null.
     * @param feedUri The REST URI of the entity to create
     * @param entity The entity to create using REST POST into the gSDP server.
     * 		May be null if the relevant information is contained solely in the parameters and headers of the reques.
     * @param parameters Parameters to be added to the request
     * @param requestHeaders Headers to be added to the request
     * @return The response entity object
     * @throws ParseException
     * @throws IOException
     * @throws HttpException
     */
    protected Entity createEntity (String feedUri, Entity entity, HttpQueryParams parameters,
            HashMap<String, String> requestHeaders) throws ParseException, IOException, HttpException {

        HashMap<String, String> responseHeaders = new HashMap<String, String>();
        InputStream is = mConnector.createEntity(feedUri, entity, mFactory.createSerializer(), mAuthInfo,
        		parameters, requestHeaders, responseHeaders);
        if (!responseHeaders.isEmpty())
            Log.w(TAG, "Ignoring response headers");
        try {
        	return parseEntity(mFactory.createParser(), is);
	    } finally {
	    	if (is != null)
	    		is.close();      	
	    }
    }

    /**
     * Creates a request using HTTP REST to the gSDP server in order to create an entity
     * with multiple parts using POST method
     *
     * @param feedUri the REST URI of the entity to create
     * @param entity the entity to create using REST POST into the gSDP server
     * @param parts the multiple parts associated with the entity
     * @return the response entity object
     * @throws ParseException
     * @throws IOException
     * @throws HttpException
     */
    protected Entity createEntity (String feedUri, Entity entity, HttpQueryParams parameters, ArrayList<BlueviaPartBase> parts) throws ParseException, IOException, HttpException {
        InputStream is = mConnector.createEntity(feedUri, entity, mFactory.createSerializer(), mAuthInfo, parameters, null, parts);
        try {
        	return parseEntity(mFactory.createParser(), is);
	    } finally {
	    	if (is != null)
	    		is.close();      	
	    }
    }


    /**
     * Creates a request using HTTP REST to the gSDP server in order to retrieve an entity
     * from the server using GET method
     * @param feedUri the REST URI of the entity to get
     * @return the response entity object
     * @throws BlueviaClientException
     */
    protected Entity retrieveEntity (String feedUri) throws ParseException, IOException, HttpException {
        InputStream is = mConnector.retrieveEntity(feedUri, mAuthInfo);
        try {
        	return parseEntity(mFactory.createParser(), is);
	    } finally {
	    	if (is != null)
	    		is.close();      	
	    }
    }

    /**
     * Creates a request using HTTP REST to the gSDP server in order to retrieve an entity
     * from the server using GET method and adds some filtering parameters in the
     * request URI
     * @param feedUri the REST URI of the entity to get
     * @param parameters the parameters to be added to the URI
     * @return the response entity object
     * @throws ParseException
     * @throws HttpException
     * @throws IOException
     */
    protected Entity retrieveEntity (String feedUri, HttpQueryParams parameters) throws ParseException, HttpException, IOException {
        InputStream is = mConnector.retrieveEntity(feedUri, mAuthInfo, parameters);
        try {
            return parseEntity(mFactory.createParser(), is);
        } finally {
        	if (is != null)
        		is.close();      	
        }
    }
    
    private Entity parseEntity(BlueviaEntityParser parser, InputStream is) throws ParseException, IOException {
    	Log.d(TAG, "Start parseEntity");
        if (parser == null)
            return null;

        try {
            return parser.parseEntry(is);
        } finally {
        	if (is != null)
        		is.close();
        }
    }
    
    protected static boolean isHttpMode(Mode mode){
    	return mode == Mode.LIVE || mode == Mode.TEST || mode == Mode.SANDBOX;
    }
    /// @endcond
    

}
