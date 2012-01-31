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
import com.bluevia.android.rest.commons.data.UserId;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.messaging.MessageDeliveryStatus;
import com.bluevia.android.rest.messaging.MessageryClientHelper;
import com.bluevia.android.rest.messaging.sms.data.Sms;
import com.bluevia.android.rest.messaging.sms.data.SmsMessage;
import com.bluevia.android.rest.messaging.sms.parser.BlueviaSmsParserFactory;
import com.bluevia.android.rest.oauth.Oauth;
import com.bluevia.android.rest.oauth.data.Token;

public class BlueviaSmsMTClient extends SmsClient {
	
	///@cond private
    private static final String TAG = "BlueviaSmsMTClient";
    
    private static final String FEED_SMS_OUTBOUND_REQUESTS = "/outbound/requests";
    ///@endcond
	
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
    public BlueviaSmsMTClient(Context context, AbstractRestClient.Mode mode, String consumerKey, String consumerSecret, Token accessToken) throws BlueviaClientException {
        super(context, (mode != Mode.SANDBOX) ? FEED_SMS_BASE_URI : FEED_SMS_SANDBOX_BASE_URI, 
        		isHttpMode(mode) ? new HttpConnector() : new BlueviaMessageMockConnector(mode.ordinal(), MessageType.SMS), 
        				new Oauth(consumerKey, consumerSecret, accessToken), new BlueviaSmsParserFactory());
    }

    /**
     * Allows to send and SMS to the gSDP. It returns a String containing the SMSID of the sent SMS,
     * in order to ask later for the status of the message.
     * The max length of the message must be less than 160 characters.
     *
     * @param message the text of the message
     * @param address the address of the recipient of the message
     * @return the sent SMS ID
     * @throws BlueviaClientException
     */
    public String sendSms (String message, UserId address) throws BlueviaClientException {
        return sendSms (new SmsMessage(message, address));
    }
    
    
    /**
     * Allows to send and SMS to the gSDP. Sent SMS notifications will be received in the endpoint
     * The SMSID of the sent SMS is returned in order to ask later for the status of the message as well.
     * The max length of the message must be less than 160 characters.
     *
     * @param message the text of the message
     * @param address the address of the recipient of the message
     * @param endpoint the endpoint to receive notifications of sent SMSs
     * @param correlator the correlator
     * @return the sent SMS ID
     * @throws BlueviaClientException
     */
    public String sendSms (String message, UserId address, String endpoint,
    		String correlator) throws BlueviaClientException {
        return sendSms (new SmsMessage(message, address, endpoint, correlator));
    }
    
    /**
     * Allows to send and SMS to the gSDP. It returns a String containing the SMSID of the sent SMS,
     * in order to ask later for the status of the message.
     * The max length of the message must be less than 160 characters.
     *
     * @param message the text of the message
     * @param addresses the addresses of the recipients of the message
     * @return the sent SMS ID
     * @throws BlueviaClientException
     */
    public String sendSms (String message, UserId[] addresses) throws BlueviaClientException {
        return sendSms (message, addresses, null, null);
    }

    /**
     * 
     * Allows to send and SMS to the gSDP. Sent SMS notifications will be received in the endpoint.
     * The SMSID of the sent SMS is returned in order to ask later for the status of the message as well.
     * The max length of the message must be less than 160 characters.
     *
     * @param message the text of the message
     * @param addresses the addresses of the recipients of the message
     * @param endpoint the endpoint to receive notifications of sent SMSs
     * @param correlator the correlator
     * @return the sent SMS ID
     * @throws BlueviaClientException
     */
    public String sendSms (String message, UserId[] addresses, String endpoint, 
    		String correlator) throws BlueviaClientException {
        if ((addresses == null) || (addresses.length<=0))
            throw new BlueviaClientException("addresses parameter should contain at least one valid address", BlueviaClientException.BAD_REQUEST_EXCEPTION);

        ArrayList<UserId> addressList = new ArrayList<UserId>();
        for (int i= 0; i<addresses.length; i++) {
            addressList.add (addresses[i]);
        }
        return sendSms(new SmsMessage(message, addressList, endpoint, correlator));
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
    public String sendSms(SmsMessage message) throws BlueviaClientException {
    	return sendSmsMessage (message);
    }
    
    /**
     * Allows to send and SMS to the gSDP.  It returns a String containing the SMSID of the sent SMS,
     * in order to ask later for the status of the message.
     * The max length of the message must be less than 160 characters.
     * 
     * @param message The Sms object containing the message info.
     * @return
     * @throws BlueviaClientException
     */
    public String sendSms(Sms message) throws BlueviaClientException {
        if (message instanceof SmsMessage) {
            return sendSmsMessage ((SmsMessage) message);
        }

        throw new BlueviaClientException("message class in not supported", BlueviaClientException.BAD_REQUEST_EXCEPTION);
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
    
    ///@cond private
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
     * Creates an SmsClient object, with an explicit IConnector, to be able to send SMS to the gSDP
     * @param context the Android context of the application.
     * @param connector the connector for communication mechanism.
     * @param consumerKey The oauth consumer key supplied by the developer.
     * @param consumerSecret The oauth consumer secret supplied by the developer.
     * @param accessToken The oauth access token returned by the getAccessToken call.
     * 
     * @throws BlueviaClientException if an exception occurs during the request.
     */
    protected BlueviaSmsMTClient(Context context, IConnector connector, String consumerKey, String consumerSecret, Token accessToken) throws BlueviaClientException {
        super(context, FEED_SMS_BASE_URI, connector, new Oauth(consumerKey, consumerSecret, accessToken), new BlueviaSmsParserFactory());
    }
    ///@endcond

}
