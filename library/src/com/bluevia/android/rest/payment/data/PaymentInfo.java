///@cond private
package com.bluevia.android.rest.payment.data;


/**
 * TODO
 * 
 * @author Telefonica R&D
 * 
 */
public class PaymentInfo extends AbstractPaymentInfo {
	
	/** TODO
	 * 
	 * @param amount
	 * @param currency
	 */
	public PaymentInfo(Integer amount, String currency){
		mAmount = amount;
		mCurrency = currency;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.Entity#isValid()
	 */
	public boolean isValid() {	
		return mAmount != null && mAmount > 0 && mCurrency != null && mCurrency.trim().length() != 0;
	}

}
///@endcond