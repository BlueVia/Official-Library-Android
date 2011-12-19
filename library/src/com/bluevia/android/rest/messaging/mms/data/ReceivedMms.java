package com.bluevia.android.rest.messaging.mms.data;

import java.util.ArrayList;

public class ReceivedMms {
	
	private ReceivedMmsInfo mMessage;
	private ArrayList<MimeContent> mAttachments;
	
	public ReceivedMms(){
		mAttachments = new ArrayList<MimeContent>();
	}
	
	/**
	 * 
	 * @return
	 */
	public ReceivedMmsInfo getMessage(){
		return mMessage;
	}
	
	public void setMessage(ReceivedMmsInfo message){
		this.mMessage = message;
	}
	
	/**
	 * @return the mAttachments
	 */
	public ArrayList<MimeContent> getAttachments() {
		return mAttachments;
	}
	
	/**
	 * @param mAttachments the list of mAttachments to set
	 */
	public void setAttachments(ArrayList<MimeContent> attachments) {
		this.mAttachments = attachments;
	}
	
	/**
	 * @param attachment the attachment to add
	 */
	public void addAttachment(MimeContent attachment){
		if (mAttachments == null)
			mAttachments = new ArrayList<MimeContent>();
		this.mAttachments.add(attachment);
	}
	
	

}
