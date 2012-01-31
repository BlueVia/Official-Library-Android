///@cond private
package com.bluevia.android.rest.messaging;

import java.util.ArrayList;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.data.UserId;

public abstract class AbstractReceivedMessage implements Entity {

	protected String mDateTime;
    protected AbstractMessage mMessage;
    
	/**
	 * 
	 * @param dateTime
	 */
	public void setDateTime(String dateTime) {
		mDateTime = dateTime;
	}
	
	/**
	 * 
	 * @return the date-time stamp
	 */
	public String getDateTime()	{
		return mDateTime;
	}
	
	/**
     * Gets the origin address.
     *
     * @return the origin address
     */
    public UserId getOriginAddress() {
        return mMessage.getOriginAddress();
    }
    
    /**
     * Sets the origin address.
     *
     * @param originAddress the origin address to set
     */
    public void setOriginAddress(UserId originAddress) {
        mMessage.setOriginAddress(originAddress);
    }
    
    /**
     * Gets the addresses.
     *
     * @return the address list
     */
    public ArrayList<UserId> getAddressList () {
        return mMessage.getAddressList();
    }
    
    /**
     * Adds the address.
     *
     * @param address the address for this message
     */
    public void addAddress (UserId address) {
        getAddressList().add(address);
    }

    /**
     * Removes the address
     *
     * @param address the address for this message
     * @return
     */
    public boolean removeAddress (UserId address) {
        return getAddressList().remove(address);
    }
    
	public boolean isValid(){
		if (mMessage.getAddressList() == null || mMessage.getAddressList().isEmpty())
			return false;
		for (UserId address : mMessage.getAddressList())
			if (address == null || !address.isValid())
				return false;
		if (mMessage == null || mMessage.getOriginAddress() == null)
			return false;
		else return mMessage.getOriginAddress().isValid();
	}

}
///@endcond
