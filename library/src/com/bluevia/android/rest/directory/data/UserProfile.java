package com.bluevia.android.rest.directory.data;

import java.io.Serializable;
import java.util.ArrayList;

import com.bluevia.android.rest.commons.Entity;


/**
 * * Class to hold the information of the User Profile block resource from the gSDP
 * <ul>
 * <li>User Type: it indicates the billing conditions of the user (pre-paid, post-paid, corporate,
 * etc.)
 * <li>IMSI: International Mobile Subscriber Identity</li>
 * <li>ICB: Incoming Communication Barring</li>
 * <li>OCB: Outgoing Communication Barring</li>
 * <li>Language: language, provisioned in the HLR (Home Location Register)</li>
 * <li>SIM-TYPE: it indicates the SIM card profile. This field will have the format PXX,
 * where X is a digit. The list of possible SIM card profiles is offered in Annex A.
 * <li>Parental Control: it indicates if the parental control is activated and the
 * associated control level. If it is activated, it will be necessary to check the age (e.g. using
 * the information from user profile or through other mean), but it is out of scope for this
 * API interface.
 * <li>Credit Control: yes/no field that indicates whether consumption control in real time is
 * needed.
 * <li>Diversion MSISDN: it indicates the destination MSISDN for diverted calls.</li>
 * <li>Enterprise Name: for corporate users.</li>
 * <li>Roaming: It indicates if the roaming is activated or not</li>
 * <li>Operator ID: It indicates the operator the user belongs to. The list of possible Operator
 * IDs is offered in described in [5].
 * <li>MMS Status: it indicates if the reception of MMS messages is activated or not.</li>
 * <li>Segment: Class the user belongs to in a social/age/geographical classification.</li>
 * <li>Subsegment: subclass the user belongs to within a given segment.</li>
 * <li>Subscribed Services: Information on basic services available for the user as well as
 * other VAS.</li>
 * <li>Last-updated: the last time this info was updated (or its validity checked) for this user.</li>
 * </ul>
 *
 * @author Telefonica R&D
 * 
 */
public class UserProfile implements Entity, Serializable {
	
	///@cond private

	private static final long serialVersionUID = -1041657297987647068L;

	private static final String EMPTY_VALUE_TEXT = "";
	
	public enum RestMens {AD, STA, NR};

    private String mUserType;
    private String mIMSI;
    private String mICB;
    private String mOCB;
    private String mLanguage;
    private String mSimType;
    private String mParentalControl;
    private Boolean mCreditControl;
    private String mDiversionMSISDN;
    private String mEnterpriseName;
    private Boolean mRoaming;
    private String mOperatorId;
    private Boolean mMmsStatus;
    private String mSegment;
    private String mSubSegment;
    private ArrayList<String> mSubscribedServices;
    private String mLastUpdated;
    private String mICC;
    private RestMens mRestMens;
    ///@endcond
    
    
    /**
     *
     */
    public UserProfile(){
        super();
        mSubscribedServices = new ArrayList<String>();
    }

    /**
     * Gets the user type property
     * 
     * @return the mUserType property
     */
    public String getUserType() {
        return mUserType;
    }


    /**
     * Sets the user type property
     * 
     * @param mUserType the mUserType property value to set
     */
    public void setUserType(String userType) {
        this.mUserType = userType;
    }

    /**
     * Gets the iMSI property
     * 
     * @return the iMSI property
     */
    public String getIMSI() {
        return mIMSI;
    }

    /**
     * Sets the iMSI property
     * 
     * @param iMSI the iMSI property value to set
     */
    public void setIMSI(String iMSI) {
        mIMSI = iMSI;
    }

    /**
     * Gets the iCB property
     * 
     * @return the iCB property
     */
    public String getICB() {
        return mICB;
    }

    /**
     * Sets the iCB property
     * 
     * @param iCB the iCB property value to set
     */
    public void setICB(String iCB) {
        mICB = iCB;
    }

    /**
     * Gets the oCB property
     * 
     * @return the oCB property
     */
    public String getOCB() {
        return mOCB;
    }

    /**
     * Sets the oCB property
     * 
     * @param oCB the oCB property value to set
     */
    public void setOCB(String oCB) {
        mOCB = oCB;
    }

    /**
     * Gets the lang property
     * 
     * @return the lang property
     */
    public String getLang() {
        return mLanguage;
    }

    /**
     * Sets the lang property
     * 
     * @param lang the lang property value to set
     */
    public void setLang(String lang) {
        this.mLanguage = lang;
    }

    /**
     * Gets the sim type property
     * 
     * @return the simType property
     */
    public String getSimType() {
        return mSimType;
    }

    /**
     * Sets the sim type property
     * 
     * @param simType the simType property value to set
     */
    public void setSimType(String simType) {
        this.mSimType = simType;
    }


    /**
     * Gets the parental control property
     * 
     * @return the parentalControl property
     */
    public String getParentalControl() {
        return mParentalControl;
    }

    /**
     * Sets the parental control property
     * 
     * @param parentalControl the parentalControl property value to set
     */
    public void setParentalControl(String parentalControl) {
        this.mParentalControl = parentalControl;
    }

    /**
     * Gets the credit control property
     * 
     * @return the creditControl property
     */
    public Boolean isCreditControl() {
        return mCreditControl;
    }

    /**
     * Sets the credit control property
     * 
     * @param creditControl the creditControl property value to set
     */
    public void setCreditControl(boolean creditControl) {
        this.mCreditControl = creditControl;
    }

    /**
     * Gets the diversion MSISDN property
     * 
     * @return the diversionMSISDN property
     */
    public String getDiversionMSISDN() {
        return mDiversionMSISDN;
    }

    /**
     * Sets the diversion MSISDN property
     * 
     * @param diversionMSISDN the diversionMSISDN property value to set
     */
    public void setDiversionMSISDN(String diversionMSISDN) {
        this.mDiversionMSISDN = diversionMSISDN;
    }

    /**
     * Gets the enterprise name property
     * 
     * @return the enterpriseName property
     */
    public String getEnterpriseName() {
        return mEnterpriseName;
    }

    /**
     * Sets the enterprise name property
     * 
     * @param enterpriseName the enterpriseName property value to set
     */
    public void setEnterpriseName(String enterpriseName) {
        this.mEnterpriseName = enterpriseName;
    }

    /**
     * Gets the roaming property
     * 
     * @return the roaming property
     */
    public boolean isRoaming() {
        return mRoaming;
    }

    /**
     * Sets the roaming property
     * 
     * @param roaming the roaming property value to set
     */
    public void setRoaming(boolean roaming) {
        this.mRoaming = roaming;
    }

    /**
     * Gets the operator Id property
     * 
     * @return the operatorId property
     */
    public String getOperatorId() {
        return mOperatorId;
    }

    /**
     * Sets the operator Id property
     * 
     * @param operatorId the operatorId property value to set
     */
    public void setOperatorId(String operatorId) {
        this.mOperatorId = operatorId;
    }

    /**
     * Gets the mms status property
     * 
     * @return the mmsStatus property
     */
    public Boolean isMmsStatus() {
        return mMmsStatus;
    }


    /**
     * Sets the mms status property
     * 
     * @param mmsStatus the mmsStatus property value to set
     */
    public void setMmsStatus(boolean mmsStatus) {
        this.mMmsStatus = mmsStatus;
    }


    /**
     * Gets the segment property
     * 
     * @return the segment property
     */
    public String getSegment() {
        return mSegment;
    }


    /**
     * Sets the segment property
     * 
     * @param segment the segment property value to set
     */
    public void setSegment(String segment) {
        this.mSegment = segment;
    }


    /**
     * Gets the sub segment property
     * 
     * @return the subSegment property
     */
    public String getSubSegment() {
        return mSubSegment;
    }


    /**
     * Sets the sub segment property
     * 
     * @param subSegment the subSegment property value to set
     */
    public void setSubSegment(String subSegment) {
        this.mSubSegment = subSegment;
    }


    /**
     * Gets the subscribed services property
     * 
     * @return the subscribedServices property
     */
    public ArrayList<String> getSubscribedServices() {
        return (ArrayList<String>)this.mSubscribedServices;
    }

    /**
     * @param subscribedServices
     * @return
     */
    public boolean addSubscribedService(String subscribedServices){
        return this.mSubscribedServices.add(subscribedServices);
    }

    /**
     * Gets the last updated property
     * 
     * @return the lastUpdated property
     */
    public String getLastUpdated() {
        return mLastUpdated;
    }

    /**
     * Sets the last updated property
     * 
     * @param lastUpdated the lastUpdated property value to set
     */
    public void setLastUpdated(String lastUpdated) {
        this.mLastUpdated = lastUpdated;
    }
    
	/**
	 * @return the mICC
	 */
	public String getICC() {
		return mICC;
	}

	/**
	 * @param ICC the ICC to set
	 */
	public void setICC(String ICC) {
		this.mICC = ICC;
	}

	/**
	 * @return the RestMens
	 */
	public RestMens getRestMens() {
		return mRestMens;
	}

	/**
	 * @param restMens the restMens to set
	 */
	public void setRestMens(RestMens restMens) {
		this.mRestMens = restMens;
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.Entity#isValid()
	 */
    public boolean isValid() {
        return true;
    }

    /**
     * Returns a string representation of the values in this class
     * @return a comma-separated list of the values of the fields in this class
     */
    public String toString(){
        int num = mSubscribedServices.size();
        StringBuffer sb = new StringBuffer();
        String services = null;
        for (int i=0;i<num;i++){
        	sb.append(mSubscribedServices.get(i));
        	sb.append(",");
        }
        services = sb.toString();
        return mUserType+","+mIMSI+","+mICB+","+mOCB+","+mLanguage+","+mSimType+","+mParentalControl+","+
        mCreditControl+","+mDiversionMSISDN+","+mEnterpriseName+","+mRoaming+","+mOperatorId+","+
        mMmsStatus+","+mSegment+","+mSubSegment+","+services+mLastUpdated;
    }


    /**
     * An array containing keys and values for this object
     * @return an array where each element is a key:value string
     */
    public String[] toStringArray() {

        int elements = 18 + mSubscribedServices.size();
        String[] result = new String[elements];
        result[0] = "User Type: "+(mUserType!=null?mUserType:EMPTY_VALUE_TEXT);
        result[1] = "IMSI: "+(mIMSI!=null?mIMSI:EMPTY_VALUE_TEXT);
        result[2] = "ICB: "+(mICB!=null?mICB:EMPTY_VALUE_TEXT);
        result[3] = "OCB: "+(mOCB!=null?mOCB:EMPTY_VALUE_TEXT);
        result[4] = "Language: "+(mLanguage!=null?mLanguage:EMPTY_VALUE_TEXT);
        result[5] = "SIM Type: "+(mSimType!=null?mSimType:EMPTY_VALUE_TEXT);
        result[6] = "Parental Control: "+(mParentalControl!=null?mParentalControl:EMPTY_VALUE_TEXT);
        result[7] = "Credit Control: "+(mCreditControl!=null?mCreditControl.toString():EMPTY_VALUE_TEXT);
        result[8] = "Diversion MSISDN: "+(mDiversionMSISDN!=null?mDiversionMSISDN:EMPTY_VALUE_TEXT);
        result[9] = "Enterprise Name: "+(mEnterpriseName!=null?mEnterpriseName:EMPTY_VALUE_TEXT);
        result[10] = "Roaming: "+(mRoaming!=null?mRoaming.toString():EMPTY_VALUE_TEXT);
        result[11] = "OperatorId: "+(mOperatorId!=null?mOperatorId:EMPTY_VALUE_TEXT);
        result[12] = "MMS Status: "+(mMmsStatus!=null?mMmsStatus.toString():EMPTY_VALUE_TEXT);
        result[13] = "Segment: "+(mSegment!=null?mSegment:EMPTY_VALUE_TEXT);
        result[14] = "SubSegment: "+(mSubSegment!=null?mSubSegment:EMPTY_VALUE_TEXT);
        result[15] = "Last Updated: "+(mLastUpdated!=null?mLastUpdated:EMPTY_VALUE_TEXT);
        result[16] = "ICC : "+(mICC!=null?mICC:EMPTY_VALUE_TEXT);
        result[17] = "RestMens: "+(mRestMens!=null?mRestMens:EMPTY_VALUE_TEXT);
        for (int i = 0;i<mSubscribedServices.size();i++){
            int index = i+18;
            result[index] = "Subscribed Services: "+(mSubscribedServices.get(i)!=null?mSubscribedServices.get(i):EMPTY_VALUE_TEXT);
        }
        return result;
    }

}
