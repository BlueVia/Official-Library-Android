package com.bluevia.android.payment.data;

import com.bluevia.android.commons.Utils;

/**
 * @class PaymentResult 
 * Class representing the PaymentResult type
 *
 * This type is composed of the following fields:
 * <ul>
 * <li> trasanctionId; mandatory: String</li>
 * <li> transactionStatus; mandatory: TransactionEnumerationType</li>
 * <li> transactionStatusDescription; mandatory: String</li>
 * </ul>
 */
public class PaymentResult extends PaymentStatus {

	protected String mTransactionId;

	/**
	 * Gets the mandatory transaction identifier of the payment result	
	 * 
	 * @return the mandatory transaction identifier of the payment result
	 */
	public String getTransactionId(){
		return mTransactionId;
	}

	/**
	 * Sets the mandatory transaction identifier of the payment result	
	 * 
	 * @param transactionId transaction identifier of the payment result
	 * @return this
	 */
	public PaymentResult setTransactionId(String transactionId){
		mTransactionId = transactionId;
		return this;
	}
	
	public boolean isValid(){
		return !Utils.isEmpty(mTransactionId) && super.isValid();
	}

}
