package com.bluevia.android.rest.messaging.sms.client;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.client.AbstractRestClient;
import com.bluevia.android.rest.commons.connector.IConnector;
import com.bluevia.android.rest.commons.connector.http.HttpConnector;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.mock.BlueviaMessageMockConnector;
import com.bluevia.android.rest.commons.connector.mock.BlueviaMessageMockConnector.MessageType;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.messaging.MessageDeliveryStatus;
import com.bluevia.android.rest.messaging.MessageryClientHelper;
import com.bluevia.android.rest.messaging.ReceivedMessageList;
import com.bluevia.android.rest.messaging.sms.data.ReceivedSms;
import com.bluevia.android.rest.messaging.sms.data.Sms;
import com.bluevia.android.rest.messaging.sms.parser.BlueviaSmsParserFactory;
import com.bluevia.android.rest.oauth.Oauth;
import com.bluevia.android.rest.oauth.data.Token;

/**
 * Client interface for the REST binding of the Bluevia SMS Service.
 *
 * @author Telefonica R&D
 * 
 */
public class BlueviaSmsClient extends SmsClient {

    private static final String TAG = "BlueviaSmsClient";
	
	private static final String FEED_SMS_BASE_URI = "REST/SMS";
    private static final String FEED_SMS_SANDBOX_BASE_URI = "REST/SMS_Sandbox";
	private static final String FEED_SMS_OUTBOUND_REQUESTS = "/outbound/requests";
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
    public BlueviaSmsClient(Context context, AbstractRestClient.Mode mode, String consumerKey, String consumerSecret, Token accessToken) throws BlueviaClientException {
        super(context, (mode != Mode.HTTP_SANDBOX) ? FEED_SMS_BASE_URI : FEED_SMS_SANDBOX_BASE_URI, 
        		(mode == Mode.HTTP || mode == Mode.HTTP_SANDBOX) ? new HttpConnector() : new BlueviaMessageMockConnector(mode.ordinal(), MessageType.SMS), 
        				new Oauth(consumerKey, consumerSecret, accessToken), new BlueviaSmsParserFactory());
    }
    
    /**
     * Allows to send and SMS to the gSDP.  It returns a String containing the SMSID of the sent SMS,
     * in order to ask later for the status of the message.
     * The max length of the message must be less than 160 characters.
     *
     * @param message the message to send via SMS. Message includes both message and message properties like senders, etc
     * @return the sent SMS ID
     * @throws BlueviaClientException
     */
    protected String sendSmsMessage(Sms message) throws BlueviaClientException {
        String result = null;

        //Set the token for origin address
        if (message != null)
        	message.setToken(((Oauth)mAuthInfo).getOauthToken());
        
        MessageryClientHelper.checkMessage(message);

        String feedUri = mBaseUri +  FEED_SMS_OUTBOUND_REQUESTS;
        HttpQueryParams params = new HttpQueryParams();
       	params.addParameter(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);
        
        try {
            Entity response = this.createEntity(feedUri, message, params);

            result = MessageryClientHelper.getMesageIdFromResponse(response, feedUri);

        } catch (IOException iox) {
            Log.e(TAG, "Error during IO", iox);
            throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);
        }

        return result;
    }
    
    /**
     * 
     * Allows to know the delivery status of a previous sent SMS using Bluevia API
     * @param smsId the id of the sms previously sent using this API
     * @return an arrayList containing the MessageDeliveryStatus for each destination address from the sent sms.
     * @throws BlueviaClientException
     *
     */
    public ArrayList<MessageDeliveryStatus> getSmsDeliveryStatus (String smsId) throws BlueviaClientException {
   		ArrayList<MessageDeliveryStatus> status = null;

        MessageryClientHelper.checkMessageId(smsId);

        //Build the status feed uri
        String feedUri = mBaseUri + FEED_SMS_OUTBOUND_REQUESTS + MessageryClientHelper.URI_PATH_SEPARATOR
           	+ smsId + MessageryClientHelper.DELIVERY_STATUS_FEED_PATH_BLUEVIA;
        
        HttpQueryParams parameters = new HttpQueryParams();
        parameters.addParameter(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);
        
        try {
            Entity response = retrieveEntity(feedUri, parameters);

            status = MessageryClientHelper.getStatusFromResponse(response);

        } catch (IOException iox) {
            throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);
        }

        return status;
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
    protected BlueviaSmsClient(Context context, IConnector connector, String consumerKey, String consumerSecret, Token accessToken) throws BlueviaClientException {
        super(context, FEED_SMS_BASE_URI, connector, new Oauth(consumerKey, consumerSecret, accessToken), new BlueviaSmsParserFactory());
    }
    ///@endcond
}
