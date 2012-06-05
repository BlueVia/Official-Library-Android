/**
 * \package com.bluevia.android.messagery.mt.mms.data This package contains the data classes in order to send MMS using Bluevia API.
 */
package com.bluevia.android.messagery.mt.mms.data;

import java.util.ArrayList;

import com.bluevia.android.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.android.commons.data.UserId;
import com.bluevia.android.messagery.data.AbstractMessage;

/**
 * Class representing the MmsMessageReq that will be send using the MMS Client API
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
public class MmsMessageReq extends AbstractMessage {

    private String mSubject;
    private ArrayList<BlueviaPartBase> mAttachmentList = new ArrayList<BlueviaPartBase>(1);

    /**
     * Instantiates a new MMS message type.
     */
    public MmsMessageReq() {
        super();
    }
    
    /**
     * Instantiates a new message
     * @param address Destination address
     * @param mAttachmentList List with the attachments
     */
    public MmsMessageReq(String subject, UserId address, ArrayList<BlueviaPartBase> attachmetList) {
        this(subject, address, attachmetList, null, null);
    }
    
    /**
     * Instantiates a new message
     * @param address Destination address
     * @param endpoint the endpoint to receive notifications of sent SMSs
     * @param correlator the correlator
     * @param mAttachmentList List with the attachments
     */
    public MmsMessageReq(String subject, UserId address, ArrayList<BlueviaPartBase> attachmentList, String endpoint,
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
    public MmsMessageReq(String subject, ArrayList<UserId> addressList, ArrayList<BlueviaPartBase> attachmentList) {
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
    public MmsMessageReq(String subject, ArrayList<UserId> addressList, ArrayList<BlueviaPartBase> attachmentList, String endpoint, 
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

    public boolean isValid() {
    	return (validateSubject() && validateOriginAddress() && validateAddressList() && validateAttachements());
    }

    private boolean validateSubject() {
        //TODO Verificar si existe algun limite de longitud en el subject
        return true;
    }

    private boolean validateAttachements() {
        //TODO Verificar si existe algun limite en el numero o tamanio de los attachements
        return true;
    }
 }
