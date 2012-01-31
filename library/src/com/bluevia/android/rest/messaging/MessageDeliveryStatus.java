package com.bluevia.android.rest.messaging;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.data.UserId;

/**
 *
 * Class representing the delivery status of a previous sent message either for SMS or MMS
 *
 * @author Telefonica R&D
 * 
 *
 */
public class MessageDeliveryStatus implements Entity {

	/**
     * 
     * Enum representing the values of the status of a particular SMS sent
     * Possible values are:
     * <ul>
     * 	<li>DELIVERED_TO_NETWORK: The message was delivered to network</li>
     * 	<li>DELIVERY_UNCERTAIN: It is not possible to ascertain whether the message was delivered</li>
     *  <li>DELIVERY_IMPOSSIBLE: It is not possible to deliver the message</li>
     *  <li>MESSAGE_WAITING: The message is waiting to be delivered</li>
     *  <li>DELIVERED_TO_TERMINAL: The message was successfully delivered to the recipient terminal</li>
     *  <li>DELIVERY_NOTIFICATION_NOT_SUPPORTED: Delivery notification is not supported by the network</li>
     * </ul>
     * @author Telefonica R&D
     * 
     *
     */
    public static enum Status {
        DELIVERED_TO_NETWORK,
        DELIVERY_UNCERTAIN,
        DELIVERY_IMPOSSIBLE,
        MESSAGE_WAITING,
        DELIVERED_TO_TERMINAL,
        DELIVERY_NOTIFICATION_NOT_SUPPORTED
    }
    
    /**
     * Translates a string into the corresponding enumerated type
     * @param raw A string with the human readable representation of the
     * enumerated type
     * @return The corresponding enumerated type
     * @throws BlueviaClientException Thrown when the raw parameter does not correspond
     * to any enumerated type 
     */
    public static Status translate (String raw) throws BlueviaClientException{
    	if (raw.equals("DeliveredToNetwork") || raw.equals("DELIVERED_TO_NETWORK"))
    		return Status.DELIVERED_TO_NETWORK;
    	else if (raw.equals("DeliveryUncertain") || raw.equals("DELIVERY_UNCERTAIN"))
    		return Status.DELIVERY_UNCERTAIN;
    	else if (raw.equals("DeliveryImpossible") || raw.equals("DELIVERY_IMPOSSIBLE"))
    		return Status.DELIVERY_IMPOSSIBLE;
    	else if (raw.equals("MessageWaiting") || raw.equals("MESSAGE_WAITING"))
    		return Status.MESSAGE_WAITING;
    	else if (raw.equals("DeliveredToTerminal") || raw.equals("DELIVERED_TO_TERMINAL"))
    		return Status.DELIVERED_TO_TERMINAL;
    	else if (raw.equals("DeliveryNotificationNotSupported") || raw.equals("DELIVERY_NOTIFICATION_NOT_SUPPORTED"))
    		return Status.DELIVERY_NOTIFICATION_NOT_SUPPORTED;
    	else throw new BlueviaClientException("Internal client error- Unrecognized delivery status: "+raw,
    			BlueviaClientException.INTERNAL_CLIENT_ERROR);
    			
    }

    ///@cond private
    private static final String TAG = "MessageDeliveryStatus";

    private Status mStatus = null;
    private UserId mAddress = null;
    private String mDescription = null;
    ///@endcond

    /**
     * 
     * Instantiates a new SMS status single type.
     *
     */
    public MessageDeliveryStatus(){

    }
    /**
     * 
     * Instantiates a new SMS status single type.
     * @param address the address of the sender
     * @param status the status of the message 
     */
    public MessageDeliveryStatus (UserId address, Status status){

        this.mAddress=address;
        this.mStatus=status;
    }

    /**
     * Gets the address associated to the status
     * @return the address
     */
    public UserId getAddress(){
        return this.mAddress;
    }

    /**
     * Gets the delivery status of the SMS sent to the address part of the object
     * @return the status
     */
    public Status getStatus(){
        return this.mStatus;
    }

    /**
     * Gets the delivery status description of the SMS sent to the address part of the object
     * @return the description
     */
    public String getDescription(){
        return this.mDescription;
    }

    /**
     * Sets the status of the delivery
     * @param status
     */
    public void setStatus(Status status){
        this.mStatus=status;
    }

    /**
     * Sets the address of the delivery status
     * @param address
     */
    public void setAddress(UserId address){
        this.mAddress=address;
    }

    /**
     * Sets the description of the delivery status
     * @param description
     */
    public void setDescription(String description) {
        this.mDescription=description;
    }

    /**
     * @see com.bluevia.android.rest.commons.Entity#isValid()
     */
    public boolean isValid() {
        if ((mStatus != null) && (mAddress!=null) &&
                (mAddress.isValid()))
            return true;
        return false;
    }
    
    
}
