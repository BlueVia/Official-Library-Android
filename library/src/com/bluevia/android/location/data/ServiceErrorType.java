package com.bluevia.android.location.data;

import java.util.ArrayList;

import com.bluevia.android.commons.Entity;


/**
*
* Class representing the error response information of a location retrieval.
*
* @author Telefonica R&D
* 
*
*/
public class ServiceErrorType implements Entity {

	private String mMessageId;
	private String mText;
	private ArrayList<String> mVariables;
	
	/**
	 * Instantiates a new ServiceErrorType
	 * 
	 * @param messageId Message identifier.
	 * @param text Message text, with replacement variables marked with %#
	 */
	public ServiceErrorType(String messageId, String text){
		super();
		mMessageId = messageId;
		mText = text;
		mVariables = null;
	}
	
	/**
	 * Instantiates a new ServiceErrorType
	 * 
	 * @param messageId Message identifier.
	 * @param text Message text, with replacement variables marked with %#
	 * @param variables Variables to substitute into Text string
	 */
	public ServiceErrorType(String messageId, String text, ArrayList<String> variables){
		this(messageId, text);
		mVariables = variables;
	}
	
	public boolean isValid() {
		return mMessageId != null && mText != null;
	}

	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return mMessageId;
	}

	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.mMessageId = messageId;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return mText;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.mText = text;
	}

	/**
	 * @return the variables
	 */
	public ArrayList<String> getVariables() {
		return mVariables;
	}

	/**
	 * @param variables the variables to set
	 */
	public void setVariables(ArrayList<String> variables) {
		this.mVariables = variables;
	}
	
	/**
	 * @return The composed text of the error, including the variables into it.
	 */
	public String getComposedText(){
		return mText;
	}
	

}
