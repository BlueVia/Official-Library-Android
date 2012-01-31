package com.bluevia.android.rest.payment.data;

import com.bluevia.android.rest.commons.Entity;

public class GetPaymentStatusParams implements Entity {
	
	///@cond private
	private String mTransationId;
	///@endcond
	
	public GetPaymentStatusParams(String transactionId){
		mTransationId = transactionId;
	}
	
	public void setTransactionId(String transactionId){
		mTransationId = transactionId;
	}
	
	public String getTransactionId(){
		return mTransationId;
	}
	
	@Override
	public boolean isValid() {
		return mTransationId != null && mTransationId.trim().length() != 0;
	}

}
