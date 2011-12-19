///@cond private
package com.bluevia.android.rest.commons.data.xmlrpc;

import com.bluevia.android.rest.commons.Entity;

/**
 * This class extends the RPC Method for a MethodResponse. 
 * It contains the information of the Entity response, which can be empty (null).
 * 
 */
public class MethodResponse extends RpcMethod {

	private Entity mResponseEntity;
	
	/**
	 * Gets the call entity asocciated to the method
	 * 
	 * @return the call entity
	 */
	public Entity getResponseEntity(){
		return mResponseEntity;
	}
	
	/**
	 * Sets the call entity asocciated to the method
	 * 
	 * @param entity the call entity
	 */
	public void setResponseEntity(Entity entity){
		mResponseEntity = entity;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.Entity#isValid()
	 */
	public boolean isValid() {
		if (mResponseEntity != null)
			return super.isValid() && mResponseEntity.isValid();
		return super.isValid(); // Empty response is a valid option, for the common object. But may be mandatory for derived objects.
	}
}
///@endcond
