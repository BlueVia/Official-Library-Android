package com.bluevia.android.messagery.mt.sms.data;


import java.util.ArrayList;

import android.util.Log;

import com.bluevia.android.commons.data.UserId;

/**
 * 
 * Class representing the Sms Message request that will be send using the SMS Client API
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
public class SmsMessageReq extends Sms {

    private static final String TAG = "SmsMessageReq";

    private String mMessage;
    
    /**
     * 
     * Instantiates a new SMSMessage type.
     *
     */
    public SmsMessageReq() {
        super();
    }
    
    /**
     * Instantiates a new SMSMessage type.
     * 
     * @param address Destination address
     */
    protected SmsMessageReq(UserId address){
    	super(address);
    }
    
    /**
     * Instantiates a new SMSMessage type.
     * 
     * @param addressList List with the destination addresses
     */
    protected SmsMessageReq(ArrayList<UserId> addressList){
    	super(addressList);
    }

    /**
     * Instantiates a new SMSMessage type.
     * 
     * @param message Text of the message
     * @param address Destination address
     */
    public SmsMessageReq(String message, UserId address) {
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
    public SmsMessageReq(String message, UserId address, String endpoint,
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
    public SmsMessageReq(String message, ArrayList<UserId> addressList) {
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
    public SmsMessageReq(String message, ArrayList<UserId> addressList, String endpoint,
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

    public boolean isValid() {
   		return (validateMessage() && validateOriginAddress() && validateAddressList());
    }

    protected boolean validateMessage() {
        if ((mMessage == null) || (mMessage.length() <= 0)){
            Log.e (TAG,"Message is either null or empty");
            return false;
        }
        return true;
    }
 }
