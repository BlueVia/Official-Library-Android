///@cond private
/**
 * @package com.bluevia.android.rest.payment.data This package contains entity data types of Bluevia Payment Management Service
 */
package com.bluevia.android.rest.payment.data;

/**
 * @author Telefonica R&D
 * 
 */

import com.bluevia.android.rest.commons.Entity;

/**
 * @class PaymentResult 
 * Class representing the PaymentResult type
 *
 * This type is composed of the following fields:
 * <ul>
 * <li>mTrasanctionId; mandatory: String</li>
 * <li>mTransactionStatus; mandatory: TransactionEnumerationType</li>
 * <li>mTtransactionStatusDescription; mandatory: String</li>
 * </ul>
 */
public abstract class AbstractPaymentResult implements Entity {

	protected String mTransactionId;

	private static final String TAG = "AbstractPaymentResult";

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
	public AbstractPaymentResult setTransactionId(String transactionId){
		mTransactionId = transactionId;
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.Entity#isValid()
	 */
	public boolean isValid(){
		return mTransactionId != null && mTransactionId.trim().length() != 0;
	}

}
///@endcond
