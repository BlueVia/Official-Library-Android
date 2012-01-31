package com.bluevia.android.rest.messaging;


import java.util.ArrayList;

import com.bluevia.android.rest.commons.Entity;

/**
 * Internal class representing a list of MessageDeliveryStatus
 *
 * @author Telefonica R&D
 * 
 */
public class MessageDeliveryStatusList implements Entity {
	///@cond private
    private ArrayList<MessageDeliveryStatus> mMessageDeliveryStatus;
    ///@endcond
    
    /**
     * Instantiates a new SmsDeliveryStatus
     */
    public MessageDeliveryStatusList() {
        super();

        mMessageDeliveryStatus = new ArrayList<MessageDeliveryStatus>();
    }

    /**
     * @see com.bluevia.android.rest.commons.Entity#isValid()
     */
    public boolean isValid() {
    	for (MessageDeliveryStatus mds : mMessageDeliveryStatus){
    		if (!mds.isValid())	return false;	
    	}
        return true;
    }

    /**
     * Adds a new single SMS delivery status for a particular recipient address
     * @param element
     * @return
     */
    public boolean add(MessageDeliveryStatus element){
        return this.mMessageDeliveryStatus.add(element);
    }

    /**
     * Return the Sms Delivery status list for all recipient addresses
     * @return the delivery status list
     */
    public ArrayList<MessageDeliveryStatus> getDeliveryStatusList ()  {
        return (ArrayList<MessageDeliveryStatus>) mMessageDeliveryStatus.clone();
    }

}


