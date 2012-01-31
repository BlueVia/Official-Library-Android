package com.bluevia.android.rest.payment.data;

import com.bluevia.android.rest.payment.data.PaymentStatus.TransactionStatusType;

/**
 * @class PaymentResult 
 * Class representing the PaymentResult type
 *
 * This type is composed of the following fields:
 * <ul>
 * <li>mTrasanctionId; mandatory: String</li>
 * <li>mTransactionStatus; mandatory: TransactionStatusType</li>
 * <li>mTtransactionStatusDescription; mandatory: String</li>
 * </ul>
 */
public class PaymentResult extends AbstractPaymentResult {

	///@cond private
	protected PaymentStatus mPaymentStatus;
	///@endcond
	
	public PaymentResult(){
		mPaymentStatus = new PaymentStatus();
	}
	
	/**
	 * Gets the mandatory transaction status of the payment result
	 * 
	 * @return the transaction status of the payment session
	 */
	public TransactionStatusType getTransactionStatus(){
		return mPaymentStatus.getTransactionStatus();
	}

	/**
	 * Sets the mandatory transaction status of the payment result	
	 * 
	 * @param transactionStatus the mandatory transaction status of the payment result
	 * @return this
	 */
	public void setTransactionStatus(TransactionStatusType transactionStatus){
		mPaymentStatus.setTransactionStatus(transactionStatus);
	}

	/**
	 * Sets the mandatory transaction status description of the payment result	
	 * 
	 * @param  transactionStatusDescription the transaction status description of the payment result
	 * @return this
	 */
	public void setTransactionStatusDescription(String transactionStatusDescription){
		mPaymentStatus.setTransactionStatusDescription(transactionStatusDescription);
	}

	/**
	 * Gets the mandatory transaction status description of the payment result	
	 * 
	 * @return the transaction status description of the payment result
	 */
	public String getTransactionStatusDescription(){
		return mPaymentStatus.getTransactionStatusDescription();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.Entity#isValid()
	 */
	public boolean isValid() {
		return (super.isValid() && mPaymentStatus.isValid());
	}

}
