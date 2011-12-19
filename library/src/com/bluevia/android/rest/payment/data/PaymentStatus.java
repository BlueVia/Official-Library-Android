package com.bluevia.android.rest.payment.data;

import com.bluevia.android.rest.commons.Entity;

/**
 * @class PaymentStatus 
 * Class representing the PaymentStatus type
 *
 * This type is composed of the following fields:
 * <ul>
 * <li>mTransactionStatus; mandatory: TransactionStatusType</li>
 * <li>mTtransactionStatusDescription; mandatory: String</li>
 * </ul>
 */
public class PaymentStatus implements Entity {

	/**
	 * @enum PaymentResult::TransactionStatusType
	 * Information about the final status of payment transaction. 
	 * @var PaymentResult::TransactionStatusType PaymentResult::SUCCESS:
	 * Transaction has had successful result
	 * @var PaymentResult::TransactionStatusType PaymentResult::FAILURE:
	 * Transaction result has been a failure
	 * @var PaymentResult::TransactionStatusType PaymentResult::PENDING:
	 * TODO
	 */
	public enum TransactionStatusType { SUCCESS, FAILURE, PENDING };	

	///@cond private
	protected TransactionStatusType mTransactionStatus;
	protected String mTransactionStatusDescription;
	///@endcond

	/**
	 * Gets the mandatory transaction status of the payment result
	 * 
	 * @return the transaction status of the payment session
	 */
	public TransactionStatusType getTransactionStatus(){
		return mTransactionStatus;
	}

	/**
	 * Sets the mandatory transaction status of the payment result	
	 * 
	 * @param transactionStatus the mandatory transaction status of the payment result
	 */
	public void setTransactionStatus(TransactionStatusType transactionStatus){
		mTransactionStatus = transactionStatus;
	}

	/**
	 * Sets the mandatory transaction status description of the payment result	
	 * 
	 * @param  transactionStatusDescription the transaction status description of the payment result
	 * @return this
	 */
	public void setTransactionStatusDescription(String transactionStatusDescription){
		mTransactionStatusDescription = transactionStatusDescription;
	}

	/**
	 * Gets the mandatory transaction status description of the payment result	
	 * 
	 * @return the transaction status description of the payment result
	 */
	public String getTransactionStatusDescription(){
		return mTransactionStatusDescription;
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.Entity#isValid()
	 */
	public boolean isValid() {
		return mTransactionStatus != null;
	}

}
