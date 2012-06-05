package com.bluevia.android.commons.data.xmlrpc;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.parser.xml.XmlConstants;

/**
 * Class representing the common information in a RPC method (call/response).
 * 
 */
public abstract class RpcMethod implements Entity {

	protected String mId;
	protected String mVersion = XmlConstants.VERSION_1;
	
	/**
	 * Gets the id of the method
	 * 
	 * @return the id of the method
	 * 
	 */
	public String getId(){
		return mId;
	}
	
	/**
	 * Sets the id of the method
	 * 
	 * @param id
	 */
	public void setId(String id){
		mId = id;
	}
	
	/**
	 * Gets the version of the method
	 * 
	 * @return the version of the method
	 */
	public String getVersion(){
		return mVersion;
	}
	
	/**
	 * Sets the version of the method
	 * 
	 * @param version
	 */
	public void setVersion(String version){
		mVersion = version;
	}
	
	public boolean isValid(){
		return mVersion != null;
	}
	
}
