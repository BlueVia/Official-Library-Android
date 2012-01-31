package com.bluevia.android.rest.messaging.sms.client;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;

import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.client.AbstractRestClient;
import com.bluevia.android.rest.commons.connector.IConnector;
import com.bluevia.android.rest.commons.connector.http.HttpConnector;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.mock.BlueviaMessageMockConnector;
import com.bluevia.android.rest.commons.connector.mock.BlueviaMessageMockConnector.MessageType;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.messaging.MessageryClientHelper;
import com.bluevia.android.rest.messaging.ReceivedMessageList;
import com.bluevia.android.rest.messaging.sms.data.ReceivedSms;
import com.bluevia.android.rest.messaging.sms.parser.BlueviaSmsParserFactory;
import com.bluevia.android.rest.oauth.Oauth;

/**
 * Client interface for the REST binding of the Bluevia SMS Service.
 *
 * @author Telefonica R&D
 * 
 */
public class BlueviaSmsMOClient extends SmsClient {

    private static final String TAG = "BlueviaSmsMOClient";
	
	private static final String FEED_SMS_INBOUND_URI = "/inbound";
    private static final String RECEIVED_SMS_FEED_PATH = "/messages";
	
    /**
     * Creates an SmsClient object to be able to send SMS to the gSDP
     * @param context the Android context of the application.
     * @param mode the communication mechanism to communicate with the gSDP
     * @param consumerKey The oauth consumer key supplied by the developer.
     * @param consumerSecret The oauth consumer secret supplied by the developer.
     * @param accessToken The oauth access token returned by the getAccessToken call.
     * 
     * @throws BlueviaClientException if an exception occurs during the request.
     */
    public BlueviaSmsMOClient(Context context, AbstractRestClient.Mode mode, String consumerKey, String consumerSecret) throws BlueviaClientException {
        super(context, (mode != Mode.SANDBOX) ? FEED_SMS_BASE_URI : FEED_SMS_SANDBOX_BASE_URI, 
        		isHttpMode(mode) ? new HttpConnector() : new BlueviaMessageMockConnector(mode.ordinal(), MessageType.SMS), 
        				new Oauth(consumerKey, consumerSecret), new BlueviaSmsParserFactory());
    }
    
    /**
     * 
     * Allow to request for the list of the received smss for the app provisioned and authorized
     * @param registrationId The Bluevia service number for your country, including the country code without the + symbol
     * @return the list of the received messages
     * @throws BlueviaClientException
     */
    public ArrayList<ReceivedSms> getMessages(String registrationId) throws BlueviaClientException {
    	ArrayList<ReceivedSms> receivedSmsList = null;
    	
    	// Check params
    	MessageryClientHelper.checkRegistrationId(registrationId);
    	
    	// Build feed uri for the request
    	String feedUri = null;
    	HttpQueryParams parameters = null;
    	
		feedUri = mBaseUri + FEED_SMS_INBOUND_URI + MessageryClientHelper.URI_PATH_SEPARATOR
			+ registrationId + RECEIVED_SMS_FEED_PATH;
    	parameters = new HttpQueryParams();
        parameters.addParameter(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);    		
        try {
            Entity response = retrieveEntity(feedUri, parameters);

            receivedSmsList = getReceivedSmsListFromResponse(response);

        } catch (IOException iox) {
            throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);
        }
    	
    	return receivedSmsList;
    }

    ///@cond private
    /**
     * Gets the received sms list from the response Entity
     * 
     * @param response response Entity that contains the result
     * @return the received sms list
     * @throws BlueviaClientException if the response or the list are not valid.
     */
    private ArrayList<ReceivedSms> getReceivedSmsListFromResponse(Entity response) throws BlueviaClientException{
    	ArrayList<ReceivedSms> receivedSmsList = null;
    	

        //If there are no messages, return empty list
        if (response == null)
        	return new ArrayList<ReceivedSms>();
    	
        //Check if response is instance of ReceivedSmsList
        if ((response == null) || (! (response instanceof ReceivedMessageList)))
            throw new BlueviaClientException("Error during request. Response received does not correspond to a ReceivedSmsList",
            		BlueviaClientException.INTERNAL_CLIENT_ERROR);
        
        //Get received data
        receivedSmsList = ((ReceivedMessageList)response).getList();
    	
        // Check list
        if (receivedSmsList == null)
    		throw new BlueviaClientException("ReceivedSMS list is null",
    				BlueviaClientException.INTERNAL_CLIENT_ERROR);
        
        return receivedSmsList;
    }
    
    
    /**
     * Creates an SmsClient object, with an explicit IConnector, to be able to send SMS to the gSDP
     * @param context the Android context of the application.
     * @param connector the connector for communication mechanism.
     * @param consumerKey The oauth consumer key supplied by the developer.
     * @param consumerSecret The oauth consumer secret supplied by the developer.
     * @param accessToken The oauth access token returned by the getAccessToken call.
     * 
     * @throws BlueviaClientException if an exception occurs during the request.
     */
    protected BlueviaSmsMOClient(Context context, IConnector connector, String consumerKey, String consumerSecret) throws BlueviaClientException {
        super(context, FEED_SMS_BASE_URI, connector, new Oauth(consumerKey, consumerSecret), new BlueviaSmsParserFactory());
    }
    ///@endcond
}
