/**
 * \package com.bluevia.android.rest.messaging.sms.data This package contains the classes in order to send SMS using Bluevia API.
 */
package com.bluevia.android.rest.messaging.sms.data;

import java.util.ArrayList;

import com.bluevia.android.rest.commons.data.UserId;
import com.bluevia.android.rest.messaging.AbstractMessage;


/**
 * Class representing abstract SMS class that represents the different SMS messages that
 * will be send using the SMS Client API
 * This type is composed of the following fields:
 * <ul>
 * <li>addressList: mandatory; max occurrences: minimum 1.
 * <li>originAddress: mandatory; String
 * </ul>
 *
 * This implementation is not synchronized
 *
 * @author Telefonica R&D
 * 
*/

//TODO Think about doing this classes immutables
//TODO Address are not strings but a common type (UserIdType). Change this following the XSD


public abstract class Sms extends AbstractMessage {

    ///@cond private
    private static final String TAG = "AbstractSms";
    ///@endcond
    
    /**
     * Enum class to implement sms format attribute
     */
    public enum SmsFormat { EMS, SMART_MESSAGING };

    /**
     * Instantiates a new SMS message type.
     */
    public Sms() {
        super();
    }
    
    /**
     * Instantiates a new SMS message type.
     * 
     * @param address Destination address
     */
    public Sms(UserId address) {
        super(address);
    }
    
    /**
     * Instantiates a new SMS message type.
     * 
     * @param address Destination address
     * @param endpoint Endpoint
     * @param correlator Correlator
     */
    public Sms(UserId address, String endpoint, String correlator) {
        super(address, endpoint, correlator);
    }

    /**
     * Instantiates a new SMS message type.
     * 
     * @param addressList List with the destination addresses
     */
    public Sms(ArrayList<UserId> addressList) {
        super (addressList);
    }
    
    /**
     * Instantiates a new SMS message type.
     * 
     * @param addressList List with the destination addresses
     * @param endpoint Endpoint
     * @param correlator Correlator
     */
    public Sms(ArrayList<UserId> addressList, String endpoint, String correlator) {
        super (addressList, endpoint, correlator);
    }

    /* (non-Javadoc)
     * @see com.bluevia.android.rest.Entity#isValid()
     */
    public abstract boolean isValid();



 }
