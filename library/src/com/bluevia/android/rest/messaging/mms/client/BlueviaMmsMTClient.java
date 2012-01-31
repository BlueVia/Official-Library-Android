package com.bluevia.android.rest.messaging.mms.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.client.AbstractRestClient;
import com.bluevia.android.rest.commons.connector.IConnector;
import com.bluevia.android.rest.commons.connector.http.HttpConnector;
import com.bluevia.android.rest.commons.connector.http.HttpException;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.android.rest.commons.connector.mock.BlueviaMessageMockConnector;
import com.bluevia.android.rest.commons.connector.mock.BlueviaMessageMockConnector.MessageType;
import com.bluevia.android.rest.commons.data.UserId;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.messaging.MessageDeliveryStatus;
import com.bluevia.android.rest.messaging.MessageryClientHelper;
import com.bluevia.android.rest.messaging.SendMessageResult;
import com.bluevia.android.rest.messaging.mms.data.MmsMessage;
import com.bluevia.android.rest.messaging.mms.parser.BlueviaMmsParserFactory;
import com.bluevia.android.rest.oauth.Oauth;
import com.bluevia.android.rest.oauth.data.Token;

/**
 * Client interface for the REST binding of the Bluevia MMS Service.
 * 
 * @author Telefonica R&D
 * 
 */
public class BlueviaMmsMTClient extends MmsClient {

	///@cond private
	private static final String TAG = "BlueviaMmsMTClientnt";
	
	private static final String FEED_MMS_OUTBOUND_REQUESTS = "/outbound/requests";
    ///@endcond
    
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
    public BlueviaMmsMTClient(Context context, AbstractRestClient.Mode mode, String consumerKey, String consumerSecret, Token accessToken) throws BlueviaClientException {
        super(context, (mode != Mode.SANDBOX) ? FEED_MMS_BASE_URI : FEED_MMS_SANDBOX_BASE_URI,
        		isHttpMode(mode) ? new HttpConnector() : new BlueviaMessageMockConnector(mode.ordinal(), MessageType.MMS), 
        		new Oauth(consumerKey, consumerSecret, accessToken), new BlueviaMmsParserFactory());
    }
    
    /**
     * Allows to send and MMS to the gSDP. It returns a String containing the mmsId of the MMS sent
     *
     * @param subject the subject of the mms to send
     * @param address the address of the recipient of the message
     * @param parts The contents of the MMS to sent (image, video, etc)
     * @return the sent MMS ID
     * @throws BlueviaClientException
     */
    public String sendMms (String subject, UserId address, BlueviaPartBase[] parts) throws BlueviaClientException {
        return sendMms(subject, address, parts, null, null);
    }
    
    /**
     * Allows to send and MMS to the gSDP. Sent MMS notifications will be received in the endpoint.
     * The MMSID of the sent MMS is returned in order to ask later for the status of the message as well.
     * 
     * @param subject the subject of the mms to send
     * @param address the address of the recipient of the message
     * @param parts The contents of the MMS to sent (image, video, etc)
     * @param endpoint the endpoint to receive notifications of sent SMSs
     * @param correlator the correlator
     * @return the sent MMS ID
     * @throws BlueviaClientException
     */
    public String sendMms (String subject, UserId address, BlueviaPartBase[] parts, String endpoint, 
    		String correlator) throws BlueviaClientException {
        
    	ArrayList<BlueviaPartBase> attachmentList = new ArrayList<BlueviaPartBase>();
        
        if (parts != null && parts.length > 0){
            attachmentList = new ArrayList<BlueviaPartBase>(parts.length);
            for (int i=0; i<parts.length; i++){
                attachmentList.add(parts[i]);
            }
        }
        
        return sendMms(new MmsMessage(subject, address, attachmentList, endpoint, correlator));
    }

    /**
     * Allows to send and MMS to the gSDP to multiple recipients. It returns a String containing the mmsId of the MMS sent
     *
     * @param subject the subject of the mms to send
     * @param addresses the addresses of the recipients of the message
     * @param parts The contents of the MMS to sent (image, video, etc)
     * @return the sent MMS ID
     * @throws BlueviaClientException
     */
    public String sendMms (String subject, UserId[] addresses, BlueviaPartBase[] parts) throws BlueviaClientException {
    	return sendMms(subject, addresses, parts, null, null);
    }
    
    /**
     * Allows to send and MMS to the gSDP to multiple recipients. Sent MMS notifications will be received in the endpoint.
     * The MMSID of the sent MMS is returned in order to ask later for the status of the message as well.
     * 
     * @param subject the subject of the mms to send
     * @param addresses the addresses of the recipients of the message
     * @param parts The contents of the MMS to sent (image, video, etc)
     * @param endpoint the endpoint to receive notifications of sent SMSs
     * @param correlator the correlator
     * @return the sent MMS ID
     * @throws BlueviaClientException
     */
    public String sendMms (String subject, UserId[] addresses, BlueviaPartBase[] parts, String endpoint,
    		String correlator) throws BlueviaClientException {
        if ((addresses == null) || (addresses.length<=0))
            throw new BlueviaClientException("addresses parameter should contain at least one valid address", BlueviaClientException.BAD_REQUEST_EXCEPTION);

        ArrayList<UserId> addressList = new ArrayList<UserId>();
        for (int i= 0; i<addresses.length; i++) {
            addressList.add (addresses[i]);
        }

        ArrayList<BlueviaPartBase> attachmentList = null;
        if (parts != null && parts.length > 0){
            attachmentList = new ArrayList<BlueviaPartBase>(parts.length);
            for (int i=0; i<parts.length; i++){
                attachmentList.add(parts[i]);
            }
        }

        return sendMms(new MmsMessage(subject, addressList, attachmentList, endpoint, correlator));
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
    
    ///@cond private
    /* (non-Javadoc)
	 * @see com.bluevia.android.rest.client.AbstractRestClient#createEntity(java.lang.String, com.bluevia.android.rest.Entity)
	 */
	protected Entity createEntity (String feedUri, Entity entity, HttpQueryParams params) throws ParseException, IOException, HttpException {
		HashMap<String, String> responseHeaders = new HashMap<String, String>();
		if (!(entity instanceof MmsMessage))
			return null;
		MmsMessage mms = (MmsMessage) entity;

		if (mms.getAttachementList() != null)
			Log.e(TAG, "Attachments number:" + mms.getAttachementList().size());

		InputStream is;
		if (mms.getAttachementList() != null)
			is = mConnector.createEntity(feedUri, entity, mFactory.createSerializer(), mAuthInfo, params, responseHeaders, mms.getAttachementList());
		else is = mConnector.createEntity(feedUri, entity, mFactory.createSerializer(), mAuthInfo, params, responseHeaders);

		if (is != null)
			is.close();

		SendMessageResult response = MessageryClientHelper.getResponseWithMessageId(responseHeaders);
		return response;
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
    protected BlueviaMmsMTClient(Context context, IConnector connector, String consumerKey, String consumerSecret, Token accessToken) throws BlueviaClientException {
        super(context, FEED_MMS_BASE_URI, connector, new Oauth(consumerKey, consumerSecret, accessToken), new BlueviaMmsParserFactory());
    }
    ///@endcond
}
