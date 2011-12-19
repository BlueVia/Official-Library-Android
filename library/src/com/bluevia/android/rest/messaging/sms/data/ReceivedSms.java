package com.bluevia.android.rest.messaging.sms.data;

import java.util.ArrayList;

import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.data.UserId;
import com.bluevia.android.rest.messaging.AbstractReceivedMessage;


/**
 * 
 * Class representing the ReceivedSms elements that will be received in ReceivedSmsList using the SMS Client API
 * This type is composed of the following fields:
 * <ul>
 * <li>mDateTime; String
 * </ul>
 *
 * This implementation is not synchronized
 *
 * @author Telefonica R&D
 * 
 *
 */
public final class ReceivedSms extends AbstractReceivedMessage {

	///@cond private
    private static final String TAG = "ReceivedSms";
	///@endcond
    
    /**
     * Instantiates a new empty ReceivedSMS message.
     */
    public ReceivedSms(){
    	super();
    	mMessage = new SmsMessage();
    }
	
	/**
	 * Instantiates a new ReceivedSMS message.
	 * 
	 * @param message The body of the received message 
	 * @param addressList The list of recipients to whom the message will be sent.
     * Allowed UserId instances are those such as:
     * <ul>
     *   <li> UserId type is UserId::PHONE_NUMBER and has a phone number -following the E164 format- as 'userIdValue'.
     * </ul>
     *
     * Any other UserId instance type will cause this function throws an BlueviaClientException.
	 * @param dateTime The date and time when the message was sent
     * 
	 */
	public ReceivedSms(String message, ArrayList<UserId> addressList, String dateTime) throws BlueviaClientException{
		mMessage = new SmsMessage(message, addressList);
		mDateTime = dateTime;
	}
	
	/**
	 * Instantiates a new ReceivedSMS message.
	 * 
	 * @param message The body of the received message 
	 * @param address The recipient to whom the message will be sent.
     * Allowed UserId instances are those such as:
     * <ul>
     *   <li> UserId type is UserId::PHONE_NUMBER and has a phone number -following the E164 format- as 'userIdValue'.
     * </ul>
     *
     * Any other UserId instance type will cause this function throws an BlueviaClientException.
	 * @param dateTime The date and time when the message was sent
     * 
	 */
	public ReceivedSms(String message, UserId address, String dateTime) throws BlueviaClientException{
		mMessage = new SmsMessage(message, address);
		mDateTime = dateTime;
	}
	
    /**
     * Sets the message
     *
     * @param message
     */
    public void setMessage(String message) {
        ((SmsMessage)mMessage).setMessage(message);
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return ((SmsMessage)mMessage).getMessage();
    }
    
	public boolean isValid() {
		return ((SmsMessage)mMessage).getMessage() != null && super.isValid();
	}
	
}
