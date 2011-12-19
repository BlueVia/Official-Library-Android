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
import com.bluevia.android.rest.commons.data.UserId;

/**
 * @class PaymentInfo
 * Class representing the PaymentInfo type
 *
 * This type is composed of the following fields:
 * <ul>
 * <li>mEndUserIdentifier; optional: UserId</li>
 * <li>mAmount; mandatory: Integer</li>
 * <li>mCurrency; mandatory: String</li>
 * <li>mTaxes; optional: Integer</li>
 * <li>mCode; optional: String</li>
 * <li>mReferenceCode; optional: String</li>
 * <li>mDescription; optional: String</li>
 * </ul>
 */
public abstract class AbstractPaymentInfo implements Entity {
	
	protected Integer mAmount;
	protected String mCurrency;
	
	/**
	 * Gets the mandatory amount of the payment session	
	 * 
	 * @return the amount of the payment session
	 */
	public Integer getAmount(){
		return mAmount;
	}

	/**
	 * Sets the mandatory amount of the payment session	
	 * 
	 * @param amount the amount of the payment session
	 * @return this
	 */
	public AbstractPaymentInfo setAmount(Integer amount){
		mAmount = amount;
		return this;
	}

	/**
	 * Gets the mandatory currency of the payment session	
	 * 
	 * @return the currency of the payment session
	 */
	public String getCurrency(){
		return mCurrency;
	}

	/**
	 * Sets the mandatory currency of the payment session	
	 * 
	 * @param currency the currency of the payment session
	 * @return this
	 */
	public AbstractPaymentInfo setCurrency(String currency){
		mCurrency = currency;
		return this;
	}

	
	
}
///@endcond