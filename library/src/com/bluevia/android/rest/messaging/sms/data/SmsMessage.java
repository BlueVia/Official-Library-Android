package com.bluevia.android.rest.messaging.sms.data;


import android.util.Log;

import java.util.ArrayList;

import com.bluevia.android.rest.commons.data.UserId;

/**
 * 
 * Class representing the SMSMesaage that will be send using the SMS Client API
 * This type is composed of the following fields:
 * <ul>
 * <li>addressList: mandatory; max occurrences: minimum 1.
 * <li>message: mandatory; String
 * <li>originAddress: mandatory; String
 * </ul>
 *
 * This implementation is not synchronized
 *
 * @author Telefonica R&D
 * 
 *
 */

//TODO Think about doing this classes immutables
//TODO All optional variables has been commented because it is not clear what are they for. All their getters and setters has been also commented


public class SmsMessage extends Sms {

    ///@cond private
    private static final String TAG = "SmsMessage";

    //private static final int SMS_MAX_LENGTH = 160;

    private String mMessage;
    ///@endcond
    
    /**
     * 
     * Instantiates a new SMSMessage type.
     *
     */
    public SmsMessage() {
        super();
    }
    
    ///@cond private
    /**
     * Instantiates a new SMSMessage type.
     * 
     * @param address Destination address
     */
    protected SmsMessage(UserId address){
    	super(address);
    }
    
    /**
     * Instantiates a new SMSMessage type.
     * 
     * @param addressList List with the destination addresses
     */
    protected SmsMessage(ArrayList<UserId> addressList){
    	super(addressList);
    }
    ///@endcond

    /**
     * Instantiates a new SMSMessage type.
     * 
     * @param message Text of the message
     * @param address Destination address
     */
    public SmsMessage(String message, UserId address) {
        super(address);
        mMessage = message;
    }
    
    /**
     * Instantiates a new SMSMessage type.
     * 
     * @param message Text of the message
     * @param address Destination address
     * @param endpoint Endpoint
     * @param correlator Correlator
     */
    public SmsMessage(String message, UserId address, String endpoint,
    		String correlator) {
        super(address, endpoint, correlator);
        mMessage = message;
    }
    
    /**
     * Instantiates a new SMSMessage type.
     * 
     * @param message Text of the message
     * @param addressList List with the destination addresses
     */
    public SmsMessage(String message, ArrayList<UserId> addressList) {
        super(addressList);
        mMessage = message;
    }

    /**
     * Instantiates a new SMSMessage type.
     * 
     * @param message Text of the message
     * @param addressList List with the destination addresses
     * @param endpoint Endpoint
     * @param correlator Correlator
     */
    public SmsMessage(String message, ArrayList<UserId> addressList, String endpoint,
    		String correlator) {
        super(addressList, endpoint, correlator);
        mMessage = message;
    }
    
    /**
     * Sets the message
     *
     * @param message
     */
    public void setMessage(String message) {
        this.mMessage = message;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return mMessage;
    }

    /* (non-Javadoc)
     * @see com.bluevia.android.rest.Entity#isValid()
     */
    public boolean isValid() {
   		return (validateMessage() && validateOriginAddress() && validateAddressList());
    }

    ///@cond private
    protected boolean validateMessage() {
        if ((mMessage == null) || (mMessage.length() <= 0)){
            Log.e (TAG,"Message is either null or empty");
            return false;
        }
        return true;
    }
    ///@endcond
 }
