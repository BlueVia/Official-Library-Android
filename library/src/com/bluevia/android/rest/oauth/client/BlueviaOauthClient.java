/**
 * \package com.bluevia.android.rest.oauth This package contains classes retrieve the necessary tokens with the gSDP
 * \package com.bluevia.android.rest.oauth.client This package contains the client to get the request and access tokens.
 *  
 */
package com.bluevia.android.rest.oauth.client;

import java.io.IOException;

import android.content.Context;
import android.util.Log;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.client.AbstractRestClient;
import com.bluevia.android.rest.commons.connector.IConnector;
import com.bluevia.android.rest.commons.connector.http.HttpConnector;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.mock.BlueviaOauthMockConnector;
import com.bluevia.android.rest.oauth.Oauth;
import com.bluevia.android.rest.oauth.data.RequestToken;
import com.bluevia.android.rest.oauth.data.Token;
import com.bluevia.android.rest.oauth.parser.xml.XmlOauthParserFactory;

/**
 * This class is used to retrieve the tokens needed to authorize an application into the rest of the API 
 * @author Telefonica R&D
 * 
 */
public class BlueviaOauthClient extends AbstractRestClient{

	///@cond private
	private static final String FEED_OAUTH_BASE_URI = "REST/Oauth";
	
	private static final String URI_PATH_SEPARATOR = "/";
	private static final String REQUEST_TOKEN_FEED_PATH = "getRequestToken";
	private static final String ACCESS_TOKEN_FEED_PATH = "getAccessToken";
	
	
	private static final String TAG = "OauthClient";
	
	///@endcond

    /**
     * Creates a OauthClient object to be able to get the request and access tokens from the gSDP.
     * @param context the Android context of the application.
     * @param mode the communication mechanism to communicate with the gSDP
     * @param consumerKey The oauth consumer key supplied by the developer.
     * @param consumerSecret The oauth consumer secret supplied by the developer.
     * 
     * @throws BlueviaClientException if an exception occurs during the instantiation.
     */
	public BlueviaOauthClient(Context context, AbstractRestClient.Mode mode, String consumerKey, String consumerSecret) throws BlueviaClientException {
        super(context, FEED_OAUTH_BASE_URI, 
        		(mode == Mode.HTTP || mode == Mode.HTTP_SANDBOX) ? new HttpConnector() : new BlueviaOauthMockConnector(mode.ordinal()), 
        				new Oauth(consumerKey, consumerSecret), new XmlOauthParserFactory());
        
        if (mode == Mode.HTTP_SANDBOX)
        	throw new BlueviaClientException("Oauth API cannot be used in Sandbox environment", BlueviaClientException.BAD_REQUEST_EXCEPTION);
	}
	
	/**
	 * 
	 * Gets the RequestToken
	 * 
	 * @return the Request Token
	 * @throws BlueviaClientException if an exception occurs during the request.
	 */
	public RequestToken getRequestToken() throws BlueviaClientException{
		return getRequestToken(null, null, null);
	}
	
	/**
	 * 
	 * Gets the Request Token using OAuth SMS handshake
	 * 
	 * @param phoneNumber user's phone number
	 * @return the Request Token
	 * @throws BlueviaClientException if an exception occurs during the request.
	 */
	public RequestToken getRequestToken(String phoneNumber) throws BlueviaClientException{
		return getRequestToken(phoneNumber, null, null);
	}
	
	/**
	 * 
	 * Gets the RequestToken
	 * 
	 * @param callback
	 * @param apiName
	 * @param request
	 * 
	 * @return the Request Token
	 * @throws BlueviaClientException if an exception occurs during the request.
	 */
	protected RequestToken getRequestToken(String callback, String apiName, Entity request) throws BlueviaClientException {
		
		// Build the uri
		String feedUri = mBaseUri + URI_PATH_SEPARATOR + REQUEST_TOKEN_FEED_PATH;
		
		HttpQueryParams params = new HttpQueryParams();
		
		//Callback
		if (callback != null && callback.trim().length() != 0)
			params.addParameter(Oauth.HEADER_OAUTH_CALLBACK, callback);
		else params.addParameter(Oauth.HEADER_OAUTH_CALLBACK, Oauth.CALLBACK);
		
		if (apiName != null && apiName.trim().length() > 0)
			params.addParameter(Oauth.HEADER_OAUTH_API_NAME, apiName);
		
		((Oauth)mAuthInfo).setAdditionalParameters(params);
		
		// Perform the request
		Entity response;
		try {
			response = createEntity(feedUri, request);
		} catch (IOException e) {
			throw new BlueviaClientException("I/O Exception during request", e, 
	                BlueviaClientException.INTERNAL_CLIENT_ERROR);
		}
		
		if (response == null){
	    	throw new BlueviaClientException("Error during request: response received is null", 
	                BlueviaClientException.INTERNAL_CLIENT_ERROR);
	    } else if (! (response instanceof Token)){
	        throw new BlueviaClientException("Error during request: response received is not a Token but a " + response.getClass().getName(),  
	                BlueviaClientException.INTERNAL_CLIENT_ERROR);
	    }	    
		
		RequestToken requestToken = new RequestToken((Token)response);
		
		//If SMS Handshake verification url must be null
		if (callback != null)
			requestToken.setVerificationUrl(null);
		
		response = null;
		
	    if (!requestToken.isValid()){
	        throw new BlueviaClientException("The Token object received is empty, incomplete or not valid", 
                    BlueviaClientException.INTERNAL_CLIENT_ERROR);
	    } return requestToken;
	}

	/**
	 * 
	 * Gets the AccessToken
	 *
	 * @param oauthToken the oauthToken
	 * @param oauthTokenSecret the oauthTokenSecret
	 * @param oauthVerifier the oauthVerifier
	 * @return the AccessToken
	 * @throws BlueviaClientException if an exception occurs during the request.
	 */
	public Token getAccessToken(String oauthToken, String oauthTokenSecret, String oauthVerifier) throws BlueviaClientException{
		
		// Build the uri
		String feedUri = mBaseUri + URI_PATH_SEPARATOR + ACCESS_TOKEN_FEED_PATH;

		if (oauthToken == null || oauthTokenSecret == null || oauthVerifier == null)
			throw new BlueviaClientException("Oauth parameters cannot be null", BlueviaClientException.BAD_REQUEST_EXCEPTION);
		
		// Add oauth token to oauth params
		((Oauth)mAuthInfo).setOauthToken(new Token(oauthToken, oauthTokenSecret));

		HttpQueryParams params = new HttpQueryParams();
		params.addParameter(Oauth.HEADER_OAUTH_VERIFIER, oauthVerifier);
		((Oauth)mAuthInfo).setAdditionalParameters(params);
		
		// Perform the request
		Entity response = null;
		try {
			response = createEntity(feedUri, null);
		} catch (IOException e) {
			throw new BlueviaClientException("I/O Exception during request", e, 
	                BlueviaClientException.INTERNAL_CLIENT_ERROR);
		}
		
		if (response == null){
	    	throw new BlueviaClientException("Error during request. " +
	        		"Response received is null", 
	                BlueviaClientException.INTERNAL_CLIENT_ERROR);
	    } else if (! (response instanceof Token)){
	        throw new BlueviaClientException("Error during request. " +
	        		"Response received is not a Token but a "+response.getClass().getName(), 
	                BlueviaClientException.INTERNAL_CLIENT_ERROR);
	    }	    
		
		Token accessToken = (Token) response;

	    Log.d(TAG,"Token: "+accessToken.toString());
	    		    
	    if (!accessToken.isValid()){
	        throw new BlueviaClientException("The access token received is empty, incomplete or not valid", 
                    BlueviaClientException.INTERNAL_CLIENT_ERROR);
	    }
	    
	    return accessToken;
	}
	

	///@cond private
    /**
     * Creates an OauthClient with a specific IConnector object
     * @param context the Android context of the application.
     * @param connector the connector for communication mechanism
     * @param consumerKey The oauth consumer key supplied by the developer.
     * @param consumerSecret The oauth consumer secret supplied by the developer.
     * 
     * @throws BlueviaClientException if an exception occurs during the request.
     */
	protected BlueviaOauthClient(Context context, IConnector connector, String consumerKey, String consumerSecret) throws BlueviaClientException {
        super(context, FEED_OAUTH_BASE_URI, connector, new Oauth(consumerKey,consumerSecret), new XmlOauthParserFactory());
	}
	///@endcond
	
}
