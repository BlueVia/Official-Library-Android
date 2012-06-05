package com.bluevia.android.payment.data;

import com.bluevia.android.commons.Entity;

public class GetPaymentStatusParams implements Entity {
	
	private String mTransationId;
	
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
