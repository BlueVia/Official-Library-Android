package com.bluevia.android.rest.messaging.mms.client;

import java.io.IOException;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

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
import com.bluevia.android.rest.messaging.mms.data.MmsMessage;
import com.bluevia.android.rest.messaging.mms.data.ReceivedMms;
import com.bluevia.android.rest.messaging.mms.data.ReceivedMmsInfo;
import com.bluevia.android.rest.messaging.mms.parser.BlueviaMmsParserFactory;
import com.bluevia.android.rest.messaging.mms.parser.xml.MultipartMmsParser;
import com.bluevia.android.rest.oauth.Oauth;
import com.bluevia.android.rest.oauth.data.Token;

/**
 * Client interface for the REST binding of the Bluevia MMS Service.
 * 
 * @author Telefonica R&D
 * 
 */
public class BlueviaMmsClient extends MmsClient {

	private static final String TAG = "BlueviaMmsClient";

	
	private static final String FEED_MMS_BASE_URI = "REST/MMS";
    private static final String FEED_MMS_SANDBOX_BASE_URI = "REST/MMS_Sandbox";
	private static final String FEED_MMS_OUTBOUND_REQUESTS = "/outbound/requests";
	private static final String FEED_MMS_INBOUND_URI = "/inbound";
    private static final String RECEIVED_MMS_FEED_PATH = "/messages";
    
    /**
     * Creates a MmsClient object to be able to send MMS to the gSDP. This client is 
     * designed to work with BlueVia's authentication pattern.
     * 
     * @param context the Android context of the application.
     * @param mode the communication mechanism to communicate with the gSDP.
     * @param consumerKey The oauth consumer key supplied by the developer.
     * @param consumerSecret The oauth consumer secret supplied by the developer.
     * @param accessToken The oauth access token returned by the getAccessToken call.
     * 
     * @throws BlueviaClientException if an exception occurs during the request.
     */
    public BlueviaMmsClient(Context context, AbstractRestClient.Mode mode, String consumerKey, String consumerSecret, Token accessToken) throws BlueviaClientException {
        super(context, (mode != Mode.HTTP_SANDBOX) ? FEED_MMS_BASE_URI : FEED_MMS_SANDBOX_BASE_URI,
        		(mode == Mode.HTTP || mode == Mode.HTTP_SANDBOX) ? new HttpConnector() : new BlueviaMessageMockConnector(mode.ordinal(), MessageType.MMS), 
        		new Oauth(consumerKey, consumerSecret, accessToken), new BlueviaMmsParserFactory());
    }
    
    /**
    *
    * Allows to know the delivery status of a previous sent MMS using Bluevia API
    * @param mmsId the id of the mms previously sent using this API
    * @return the delivery status of the MMS message id
    * @throws BlueviaClientException
    */
    public String sendMms(MmsMessage mmsMessage) throws BlueviaClientException {
        String result = null;
        
        //Set the token for origin address
        if (mmsMessage != null)
        	mmsMessage.setToken(((Oauth)mAuthInfo).getOauthToken());

        MessageryClientHelper.checkMessage(mmsMessage);
        
        String feedUri = mBaseUri + FEED_MMS_OUTBOUND_REQUESTS;
        HttpQueryParams params = new HttpQueryParams();
        params.addParameter(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

        try {
            Entity response = this.createEntity(feedUri, mmsMessage, params);

            result = MessageryClientHelper.getMesageIdFromResponse(response, feedUri);

        } catch (IOException iox) {
            Log.e(TAG, "Error during IO", iox);
            throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);
        }

        return result;

    }
    
    /**
    *
    * Allows to know the delivery status of a previous sent MMS using Bluevia API
    * @param mmsId the id of the mms previously sent using this API
    * @return the delivery status of the MMS message id
    * @throws BlueviaClientException
    */
    public ArrayList<MessageDeliveryStatus> getMmsDeliveryStatus (String mmsId) throws BlueviaClientException {
        ArrayList<MessageDeliveryStatus> status = null;

        MessageryClientHelper.checkMessageId(mmsId);

        //Build the status feed uri
        String feedUri = mBaseUri + FEED_MMS_OUTBOUND_REQUESTS + MessageryClientHelper.URI_PATH_SEPARATOR
            	+ mmsId + MessageryClientHelper.DELIVERY_STATUS_FEED_PATH_BLUEVIA;
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
     * Gets a list of Received MMS sent to the registrationId.
     * 
     * @param registrationId
     * @return the list of Received MMSs (list will be empty if the are no messages)
     * @throws BlueviaClientException
     */
    public ArrayList<ReceivedMmsInfo> getMessages(String registrationId) throws BlueviaClientException{
    	ArrayList<ReceivedMmsInfo> receivedMmsList = null;
    	
    	// Check params
    	MessageryClientHelper.checkRegistrationId(registrationId);
    	
    	// Build feed uri for the request
    	String feedUri = null;
    	HttpQueryParams parameters = null;
    	
		feedUri = mBaseUri + FEED_MMS_INBOUND_URI + MessageryClientHelper.URI_PATH_SEPARATOR
			+ registrationId + RECEIVED_MMS_FEED_PATH;
    	parameters = new HttpQueryParams();
        parameters.addParameter(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);    		
        try {
            Entity response = retrieveEntity(feedUri, parameters);

            receivedMmsList = getReceivedMmsListFromResponse(response);

        } catch (IOException iox) {
            throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);
        }
        
        return receivedMmsList;
    }
    
    /**
     * Gets the content of a message with a 'messageIdentifier' sent to the 'registrationId'
     * @param registrationId
     * @param messageIdentifier
     * @return the ReceivedMms 
     * @throws BlueviaClientException
     */
    public ReceivedMms getMessage(String registrationId, String messageIdentifier) throws BlueviaClientException{
    	ReceivedMms result = null;
    	
    	// Check params
    	MessageryClientHelper.checkRegistrationId(registrationId);
    	
    	if ((messageIdentifier == null) || (messageIdentifier.trim().length() == 0))
            throw new BlueviaClientException("Message identifier is either null or empty", BlueviaClientException.BAD_REQUEST_EXCEPTION);
    	
    	// Build feed uri for the request
    	String feedUri = null;
    	HttpQueryParams parameters = null;
    	
		feedUri = mBaseUri + FEED_MMS_INBOUND_URI + MessageryClientHelper.URI_PATH_SEPARATOR
			+ registrationId + RECEIVED_MMS_FEED_PATH + MessageryClientHelper.URI_PATH_SEPARATOR
			+ messageIdentifier;
		
    	parameters = new HttpQueryParams();
        parameters.addParameter(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);    		
        try {
        	
        	ByteArrayDataSource ds = mConnector.getMediaContentAsDataSource(feedUri, mAuthInfo, parameters);
        	MimeMultipart multipart = new MimeMultipart(ds);
        	MultipartMmsParser parser = new MultipartMmsParser(mFactory);
        	result = parser.parseMultipart(multipart);
        	
        } catch (IOException iox) {
            throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);
        } catch (MessagingException e) {
        	throw new BlueviaClientException("Error parsing muiltipart", BlueviaClientException.INTERNAL_CLIENT_ERROR);
		}
        
        return result;
    }
    
    ///@cond private
    /**
     * Gets the received mms list from the response Entity
     * 
     * @param response response Entity that contains the result
     * @return the received mms list
     * @throws BlueviaClientException if the response or the list are not valid.
     */
    private ArrayList<ReceivedMmsInfo> getReceivedMmsListFromResponse(Entity response) throws BlueviaClientException{
    	ArrayList<ReceivedMmsInfo> receivedMmsList = null;
    	
        //If there are no messages, return empty list
        if (response == null)
        	return new ArrayList<ReceivedMmsInfo>();
        		
        //Check if response is instance of ReceivedSmsList
        if (!(response instanceof ReceivedMessageList))
            throw new BlueviaClientException("Error during request. Response received does not correspond to a ReceivedSmsList",
            		BlueviaClientException.INTERNAL_CLIENT_ERROR);
        
        //Get received data
        receivedMmsList = ((ReceivedMessageList)response).getList();
    	
        // Check list
        if (receivedMmsList == null)
    		throw new BlueviaClientException("ReceivedSMS list is null",
    				BlueviaClientException.INTERNAL_CLIENT_ERROR);
        
        return receivedMmsList;
    }
    
    /**
     * 
     * Creates a MmsClient object, with a explicit IConnector, to send MMSs and retrieve its status using the gSDP.
	 * 
	 * @param context the Android context of the application.
     * @param connector the connector for communication mechanism.
     * @param consumerKey The oauth consumer key supplied by the developer.
     * @param consumerSecret The oauth consumer secret supplied by the developer.
     * @param accessToken The oauth access token returned by the getAccessToken call.
     * @throws BlueviaClientException
     */
    protected BlueviaMmsClient(Context context, IConnector connector, String consumerKey, String consumerSecret, Token accessToken) throws BlueviaClientException {
        super(context, FEED_MMS_BASE_URI, connector, new Oauth(consumerKey, consumerSecret, accessToken), new BlueviaMmsParserFactory());
    }
    ///@endcond
}
