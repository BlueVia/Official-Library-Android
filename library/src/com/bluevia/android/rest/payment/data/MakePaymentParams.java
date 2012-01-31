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

import android.util.Log;

/**
 * @class MakePaymentParams
 * Class representing the MakePaymentParams type
 *
 * This type is composed of the following fields:
 * <ul>
 * <li>mTimeStamp; mandatory: String</li>
 * <li>mPaymentInfo; mandatory: PaymentInfo</li>
 * </ul>
 */
public class MakePaymentParams implements Entity {

	private static final String TAG = "MakePaymentParams";
	
	private String mTimeStamp;
	private AbstractPaymentInfo mPaymentInfo;
	private String mEndpoint;
	private String mCorrelator;

	public MakePaymentParams(String timeStamp, AbstractPaymentInfo paymentInfo){
		mTimeStamp = timeStamp;
		mPaymentInfo = paymentInfo;
	}
	
	public MakePaymentParams(String timeStamp, AbstractPaymentInfo paymentInfo,
			String endpoint, String correlator){
		mTimeStamp = timeStamp;
		mPaymentInfo = paymentInfo;
		mEndpoint = endpoint;
		mCorrelator = correlator;
	}
	
	/**
	 * Gets the mandatory time stamp of the make payment	
	 * 
	 * @return the time stamp of the make payment
	 */
	public String getTimeStamp(){
		return mTimeStamp;
	}

	/**
	 * Sets the mandatory time stamp of the make payment			
	 * 
	 * @param timeStamp time stamp of the make payment
	 * @return this
	 */
	public MakePaymentParams setTimeStamp(String timeStamp){
		mTimeStamp = timeStamp;
		return this;
	}

	/**
	 * Gets the mandatory payment information of the make payment
	 * 
	 * @return the payment information of the make payment
	 */
	public AbstractPaymentInfo getPaymentInfo(){
		return mPaymentInfo;
	}

	/**
	 * Sets the mandatory payment information of the make payment	
	 * 
	 * @param paymentInfo the payment information of the make payment
	 * @return this
	 */
	public MakePaymentParams setPaymentInfo(AbstractPaymentInfo paymentInfo){
		mPaymentInfo = paymentInfo;
		return this;
	}

	/**
	 * @return the endpoint
	 */
	public String getEndpoint() {
		return mEndpoint;
	}

	/**
	 * @param endpoint the endpoint to set
	 */
	public void setEndpoint(String endpoint) {
		this.mEndpoint = endpoint;
	}

	/**
	 * @return the correlator
	 */
	public String getCorrelator() {
		return mCorrelator;
	}

	/**
	 * @param correlator the correlator to set
	 */
	public void setCorrelator(String correlator) {
		this.mCorrelator = correlator;
	}

	/**
	 * check if the make payment data is valid	
	 * 
	 * @return boolean result of the check
	 */	
	public boolean isValid() {
		if (mTimeStamp == null || mTimeStamp.trim().length() == 0)
			return false;
		if (mPaymentInfo == null || !mPaymentInfo.isValid())
			return false;
		
		//Valid endpoint, invalid correlator
		if ((mEndpoint != null && mEndpoint.trim().length() != 0)	
				&& (mCorrelator == null || mCorrelator.trim().length() == 0))
			return false;
		
		//Valid correlator, invalid endpoint
		if ((mCorrelator != null && mCorrelator.trim().length() != 0)	
				&& (mEndpoint == null || mEndpoint.trim().length() == 0))
			return false;
		
		return true;
	}

}
///@endcond
