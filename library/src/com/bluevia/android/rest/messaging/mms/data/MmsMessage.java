/**
 * \package com.bluevia.android.rest.messaging.mms.data This package contains the classes in order to send MMS using Bluevia API.
 **/
package com.bluevia.android.rest.messaging.mms.data;


import java.util.ArrayList;

import com.bluevia.android.rest.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.android.rest.commons.data.UserId;
import com.bluevia.android.rest.messaging.AbstractMessage;

/**
 * Class representing the MmsMesaage that will be send using the SMS Client API
 * This type is composed of the following fields:
 * <ul>
 * <li>addressList: mandatory; max occurrences: minimum 1.
 * <li>subject: optional; String
 * <li>originAddress: mandatory; String
 * </ul>
 *
 * This implementation is not synchronized
 *
 * @author Telefonica R&D
 * 
 */

//TODO Think about doing this classes immutables
//TODO All optional variables has been commented because it is not clear what are they for. All their getters and setters has been also commented


public class MmsMessage extends AbstractMessage {

	///@cond private
    private static final String TAG = "MmsMessage";

    private String mSubject;
    private ArrayList<BlueviaPartBase> mAttachmentList = new ArrayList<BlueviaPartBase>(1);
    ///@endcond

    /**
     * Instantiates a new MMS message type.
     */
    public MmsMessage() {
        super();
    }
    
    /**
     * Instantiates a new message
     * @param address Destination address
     * @param mAttachmentList List with the attachments
     */
    public MmsMessage(String subject, UserId address, ArrayList<BlueviaPartBase> attachmetList) {
        this(subject, address, attachmetList, null, null);
    }
    
    /**
     * Instantiates a new message
     * @param address Destination address
     * @param endpoint the endpoint to receive notifications of sent SMSs
     * @param correlator the correlator
     * @param mAttachmentList List with the attachments
     */
    public MmsMessage(String subject, UserId address, ArrayList<BlueviaPartBase> attachmentList, String endpoint,
    		String correlator) {
        super(address, endpoint, correlator);
        mSubject = subject;
        mAttachmentList = attachmentList;
    }
    
    /**
     * Instantiates a new message
     * @param subject the subjec of the message
     * @param addressList Destination addresses list
     * @param mAttachmentList List with the attachments
     * @param endpoint the endpoint to receive notifications of sent SMSs
     * @param correlator the correlator
     */
    public MmsMessage(String subject, ArrayList<UserId> addressList, ArrayList<BlueviaPartBase> attachmentList) {
        this(subject, addressList, attachmentList, null, null);
    }

    /**
     * Instantiates a new message
     * @param subject the subjec of the message
     * @param addressList Destination addresses list
     * @param endpoint the endpoint to receive notifications of sent SMSs
     * @param correlator the correlator
     * @param mAttachmentList List with the attachments
     */
    public MmsMessage(String subject, ArrayList<UserId> addressList, ArrayList<BlueviaPartBase> attachmentList, String endpoint, 
    		String correlator) {
        super(addressList, endpoint, correlator);
        mSubject = subject;
        mAttachmentList =  attachmentList;
    }

    /**
     * Sets the subject
     *
     * @param subject
     */
    public void setSubject(String subject) {
        this.mSubject = subject;
    }

    /**
     * Gets the subject.
     *
     * @return the subject
     */
    public String getSubject() {
        return mSubject;
    }

    /**
     * Adds the attachment to the Mms
     *
     * @param attachment attachment to be added
     */
    public void addAttachment (BlueviaPartBase attachment) {
        mAttachmentList.add(attachment);
    }

    /**
     * Removes the attachment
     *
     * @param attachment
     * @return
     */
    public boolean removeAttachment (BlueviaPartBase attachment) {
        return mAttachmentList.remove(attachment);
    }

    /**
     * Clear attachement list.
     */
    public void clearAttachmentList () {
        mAttachmentList.clear();
    }

    /**
     * Gets the attachments.
     *
     * @return the attachment list
     */
    public ArrayList<BlueviaPartBase> getAttachementList () {
        return mAttachmentList;
    }

    /* (non-Javadoc)
     * @see com.bluevia.android.rest.Entity#isValid()
     */
    public boolean isValid() {
    	return (validateSubject() && validateOriginAddress() && validateAddressList() && validateAttachements());
    }

    ///@cond private
    private boolean validateSubject() {
        //TODO Verificar si existe algun limite de longitud en el subject
        return true;
    }

    private boolean validateAttachements() {
        //TODO Verificar si existe algun limite en el numero o tamanio de los attachements
        return true;
    }
    ///@endcond
 }
