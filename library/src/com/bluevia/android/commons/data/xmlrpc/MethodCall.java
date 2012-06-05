package com.bluevia.android.commons.data.xmlrpc;

import java.util.Random;

import android.util.Log;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.Utils;

/**
 * This class extends the RPC Method for a MethodCall.
 * It contains the name of the method and its parameters. 
 */
public class MethodCall extends RpcMethod {

	private static final String TAG = "MethodCall";
	
	//This entity can be refactored to ArrayList<Entity> if needed
	private Entity mParamsEntity;
	private String mMethod;
	
	/**
	 * Constructor
	 */
	public MethodCall(){
		mId = String.valueOf(new Random().nextInt(99999));
	}
	
	/**
	 * Gets the call entity asocciated to the method
	 * 
	 * @return the call entity
	 */
	public Entity getParamsEntity(){
		return mParamsEntity;
	}
	
	/**
	 * Sets the call entity associated to the method
	 * 
	 * @param entity the call entity
	 */
	public MethodCall setParamsEntity(Entity entity){
		mParamsEntity = entity;
		return this;
	}
	
	/**
	 * Gets the String associated to the method call
	 * 
	 * @return the String of the method
	 */
	public String getMethod(){ 
		return mMethod;
	}
	
	/**
	 * Sets the String associated to the method call
	 * 
	 * @param method the String of the method
	 */
	public MethodCall setMethod(String method){
		mMethod = method;
		return this;
	}
	
	public boolean isValid() {
		Log.d(TAG, "mMethod==: " + mMethod);
		return super.isValid() && !Utils.isEmpty(mMethod) && (mParamsEntity == null || mParamsEntity.isValid());

	}

}
