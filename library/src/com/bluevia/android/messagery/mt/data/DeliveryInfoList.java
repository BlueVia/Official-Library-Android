package com.bluevia.android.messagery.mt.data;


import java.util.ArrayList;

import com.bluevia.android.commons.Entity;

/**
 * Internal class representing a list of DeliveryInfo
 *
 * @author Telefonica R&D
 * 
 */
public class DeliveryInfoList implements Entity {
    
	private ArrayList<DeliveryInfo> mMessageDeliveryStatus;
    
    /**
     * Instantiates a new SmsDeliveryStatus
     */
    public DeliveryInfoList() {
        super();

        mMessageDeliveryStatus = new ArrayList<DeliveryInfo>();
    }

    public boolean isValid() {
    	for (DeliveryInfo mds : mMessageDeliveryStatus){
    		if (!mds.isValid())	return false;	
    	}
        return true;
    }

    /**
     * Adds a new single SMS delivery status for a particular recipient address
     * @param element
     * @return
     */
    public boolean add(DeliveryInfo element){
        return this.mMessageDeliveryStatus.add(element);
    }

    /**
     * Return the Sms Delivery status list for all recipient addresses
     * @return the delivery status list
     */
    @SuppressWarnings("unchecked")
	public ArrayList<DeliveryInfo> getDeliveryStatusList ()  {
        return (ArrayList<DeliveryInfo>) mMessageDeliveryStatus.clone();
    }

}


