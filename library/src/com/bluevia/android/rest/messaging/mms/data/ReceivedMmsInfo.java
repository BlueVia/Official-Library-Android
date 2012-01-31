package com.bluevia.android.rest.messaging.mms.data;

import java.util.ArrayList;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.Utils;
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
	private ArrayList<AttachmentURL> mAttachmentUrls;
	///@endcond

	/**
     * Instantiates a new empty ReceivedSMS message.
     */
    public ReceivedMmsInfo(){
    	super();
    	mMessage = new MmsMessage();
    	mAttachmentUrls = new ArrayList<ReceivedMmsInfo.AttachmentURL>();
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

	/**
	 * @return the attachmentUrls
	 */
	public ArrayList<AttachmentURL> getAttachmentUrls() {
		return mAttachmentUrls;
	}
	
	/**
	 * @param attachmentUrls
	 */
	public void addAttachmentUrl(AttachmentURL attachmentUrls){
		mAttachmentUrls.add(attachmentUrls);
	}

	/**
	 * @param attachmentUrls the attachmentUrls to set
	 */
	public void setAttachmentUris(ArrayList<AttachmentURL> attachmentUrls) {
		this.mAttachmentUrls = attachmentUrls;
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.messaging.AbstractReceivedMessage#isValid()
	 */
	public boolean isValid() {
		return super.isValid();
	}

	public class AttachmentURL implements Entity {
		
		private String mUri;
		private String mContentType;
		
		/**
		 * @return the uri
		 */
		public String getUri() {
			return mUri;
		}
		
		/**
		 * @param uri the uri to set
		 */
		public void setUri(String uri) {
			this.mUri = uri;
		}
		
		/**
		 * @return the contentType
		 */
		public String getContentType() {
			return mContentType;
		}
		
		/**
		 * @param contentType the contentType to set
		 */
		public void setContentType(String contentType) {
			this.mContentType = contentType;
		}

		@Override
		public boolean isValid() {
			return !Utils.isEmpty(mUri) && !Utils.isEmpty(mContentType);
		}
		
	}
}
