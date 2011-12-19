package com.bluevia.android.rest.messaging.mms.data;

import com.bluevia.android.rest.messaging.AbstractReceivedMessage;

/**
 * 
 * Class representing the ReceivedMmsInfo elements that will be received in ReceivedMmsList using the MMS Client API
 * This type is composed of the following fields:
 * <ul>
 * <li>dateTime; String
 * <li>messageIdentifier; String
 * </ul>
 *
 * This implementation is not synchronized
 *
 * @author Telefonica R&D
 * 
 *
 */
public class ReceivedMmsInfo extends AbstractReceivedMessage {
	
	///@cond private
    private static final String TAG = "ReceivedMmsInfo";
    
	private String mMessageIdentifier;
	///@endcond

	/**
     * Instantiates a new empty ReceivedSMS message.
     */
    public ReceivedMmsInfo(){
    	super();
    	mMessage = new MmsMessage();
    }
	
	/**
	 * @return the messageIdentifier
	 */
	public String getMessageIdentifier() {
		return mMessageIdentifier;
	}
	
	/**
	 * @param messageIdentifier the messageIdentifier to set
	 */
	public void setMessageIdentifier(String messageIdentifier) {
		mMessageIdentifier = messageIdentifier;
	}
	
	/**
	 * @return the subject
	 */
	public String getSubject(){
		return ((MmsMessage)mMessage).getSubject();
	}
	
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject){
		((MmsMessage)mMessage).setSubject(subject);
	}

	public boolean isValid() {
		return super.isValid();
	}

}
