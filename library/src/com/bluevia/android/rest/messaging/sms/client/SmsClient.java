/**
 * \package com.bluevia.android.rest.messaging.sms This package contains the classes in order to send SMS using Bluevia API.
 * \package com.bluevia.android.rest.messaging.sms.client This package contains the classes in order to send SMS using Bluevia API.
 */
package com.bluevia.android.rest.messaging.sms.client;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.client.AbstractRestClient;
import com.bluevia.android.rest.commons.connector.IConnector;
import com.bluevia.android.rest.commons.connector.http.HttpException;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.http.authentication.AuthenticationInfo;
import com.bluevia.android.rest.commons.data.UserId;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.messaging.MessageDeliveryStatus;
import com.bluevia.android.rest.messaging.MessageryClientHelper;
import com.bluevia.android.rest.messaging.SendMessageResult;
import com.bluevia.android.rest.messaging.sms.data.Sms;
import com.bluevia.android.rest.messaging.sms.data.SmsMessage;

/**
 * Client interface for the REST binding of the Bluevia SMS Service.
 *
 * @author Telefonica R&D
 * 
 */

public abstract class SmsClient extends AbstractRestClient {

	///@cond private
    private static final String TAG = "SmsClient";
   
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
     * 
     * Allows to know the delivery status of a previous sent SMS using Bluevia API
     * @param smsId the id of the sms previously sent using this API
     * @return an arrayList containing the MessageDeliveryStatus for each destination address from the sent sms.
     * @throws BlueviaClientException
     *
     */
    public abstract ArrayList<MessageDeliveryStatus> getSmsDeliveryStatus (String smsId) throws BlueviaClientException;
    
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
        if ((message instanceof SmsMessage)) {
            return sendSmsMessage ((SmsMessage) message);
        } else throw new BlueviaClientException("message class in not supported", BlueviaClientException.BAD_REQUEST_EXCEPTION);
    }

    
    ///@cond private
    /**
     * Sends a single Sms message that less than 160 chars
     * @param message the message to send via SMS. Message is less than 160 chars
     * @return the result of the SMS send operation
     * @throws BlueviaClientException
     */
    protected abstract String sendSmsMessage(Sms message) throws BlueviaClientException;
    
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
    
    ///@endcond
    

}
