/**
 * \package com.bluevia.android.rest.messaging.mms This package contains the classes in order to send MMS using Bluevia API.
 * \package com.bluevia.android.rest.messaging.mms.client This package contains the classes in order to send MMS using Bluevia API.
 */
package com.bluevia.android.rest.messaging.mms.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.client.AbstractRestClient;
import com.bluevia.android.rest.commons.connector.IConnector;
import com.bluevia.android.rest.commons.connector.http.HttpException;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.http.authentication.AuthenticationInfo;
import com.bluevia.android.rest.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.android.rest.commons.data.UserId;
import com.bluevia.android.rest.commons.data.UserId.Type;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.messaging.MessageDeliveryStatus;
import com.bluevia.android.rest.messaging.MessageryClientHelper;
import com.bluevia.android.rest.messaging.SendMessageResult;
import com.bluevia.android.rest.messaging.mms.data.MmsMessage;
import com.bluevia.android.rest.oauth.Oauth;

/**
 * Client interface for the REST binding of the Bluevia MMS Service.
 * 
 * @author Telefonica R&D
 * 
 */
public abstract class MmsClient extends AbstractRestClient {

	///@cond private
    private static final String TAG = "MmsClient";
    ///@endcond

	/**
	 * Creates a MmsClient object, with a explicit IConnector, to send MMSs and retrieve its status using the gSDP.
	 * @param context the Android context of the application.
     * @param uri the base uri of the client
     * @param connector the connector for communication mechanism.
     * @param auth the parameters with the security info supplied by the developer.
     * @param factory the factory that parse and serialize entities to stream and vice-versa.
	 * @throws BlueviaClientException
	 */
	public MmsClient(Context context, String uri, IConnector connector, AuthenticationInfo auth, BlueviaEntityParserFactory factory) throws BlueviaClientException {
        super(context, uri, connector, auth, factory);
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
        for (int i=0; i<parts.length; i++){
            attachmentList.add(parts[i]);
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
     * Allows to send and MMS to the gSDP. It returns a String containing the mmsId of the MMS sent
     *
     * @param mmsMessage the MMS Message to send using the object class
     * @return the sent MMS ID
     * @throws BlueviaClientException
     */
    public abstract String sendMms(MmsMessage mmsMessage) throws BlueviaClientException;

    /**
     *
     * Allows to know the delivery status of a previous sent MMS using Bluevia API
     * @param mmsId the id of the mms previously sent using this API
     * @return the delivery status of the MMS message id
     * @throws BlueviaClientException
     */
    public abstract ArrayList<MessageDeliveryStatus> getMmsDeliveryStatus (String mmsId) throws BlueviaClientException;
    
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
    
    ///@endcond
}
