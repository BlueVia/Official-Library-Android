package com.bluevia.android.messagery.mo.mms.data;

import java.util.ArrayList;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.Utils;
import com.bluevia.android.messagery.mo.data.AbstractReceivedMessage;
import com.bluevia.android.messagery.mt.mms.data.MmsMessageReq;

/**
 * 
 * Class representing the MmsMessageInfo elements that will be received in ReceivedMmsList using the MMS Client API
 * This type is composed of the following fields:
 * <ul>
 * <li>dateTime; String </li>
 * <li>messageIdentifier; String </li>
 * </ul>
 *
 * This implementation is not synchronized
 *
 * @author Telefonica R&D
 *
 */
public class MmsMessageInfo extends AbstractReceivedMessage {
	
	private String mMessageIdentifier;
	private ArrayList<AttachmentInfo> mAttachmentsInfo;

	/**
     * Instantiates a new empty ReceivedSMS message.
     */
    public MmsMessageInfo(){
    	super();
    	mMessage = new MmsMessageReq();
    	mAttachmentsInfo = new ArrayList<MmsMessageInfo.AttachmentInfo>();
    }
	
	/**
	 * @return the messageIdentifier
	 */
	public String getMessageId() {
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
		return ((MmsMessageReq)mMessage).getSubject();
	}
	
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject){
		((MmsMessageReq)mMessage).setSubject(subject);
	}

	/**
	 * @return the attachmentUrls
	 */
	public ArrayList<AttachmentInfo> getAttachmentsInfo() {
		return mAttachmentsInfo;
	}
	
	/**
	 * @param attachmentInfo
	 */
	public void addAttachmentInfo(AttachmentInfo attachmentInfo){
		mAttachmentsInfo.add(attachmentInfo);
	}

	/**
	 * @param attachmentsInfo the attachmentUrls to set
	 */
	public void setAttachmentsInfo(ArrayList<AttachmentInfo> attachmentsInfo) {
		this.mAttachmentsInfo = attachmentsInfo;
	}

	public boolean isValid() {
		return super.isValid();
	}

	public class AttachmentInfo implements Entity {
		
		private String mUrl;
		private String mContentType;
		
		/**
		 * @return the uri
		 */
		public String getUrl() {
			return mUrl;
		}
		
		/**
		 * @param uri the uri to set
		 */
		public void setUrl(String uri) {
			this.mUrl = uri;
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
			return !Utils.isEmpty(mUrl) && !Utils.isEmpty(mContentType);
		}
		
	}
}
