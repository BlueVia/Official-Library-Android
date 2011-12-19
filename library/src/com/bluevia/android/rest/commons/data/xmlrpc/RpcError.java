///@cond private
package com.bluevia.android.rest.commons.data.xmlrpc;

import com.bluevia.android.rest.commons.Entity;

public class RpcError implements Entity {
	
	private String mCode;
	private String mMessage;
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return mCode;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.mCode = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return mMessage;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.mMessage = message;
	}

	@Override
	public boolean isValid() {
		return mCode != null && mCode.trim().length() != 0
			&& mMessage != null && mMessage.trim().length() != 0;
	}

}
///@endcond