package com.bluevia.android.rest.directory.data;

import java.io.Serializable;

import com.bluevia.android.rest.commons.Entity;


/**
 * * Class to hold the information of the User Terminal Information block
 * resource from the gSDP This type is composed of the following fields:
 * <ul>
 * <li>Brand: vendor of the device</li>
 * <li>Model: model s name</li>
 * <li>Version: model s version number</li>
 * <li>Screen Resolution: screen resolution in pixels</li>
 * <li>IMEI: mobile device identifier. This value is encrypted and it can only
 * be</li>
 * access throug an SSL secure channel.
 * <li>MMS: yes/no field that indicates if the device supports MMS client or
 * not.
 * <li>EMS: yes/no field that indicates if the device supports EMS or not.</li>
 * <li>Smart-messaging: yes/no field that indicates if the device supports smart
 * messaging or not.</li>
 * <li>WAP: yes/no field that indicates if the device supports WAP or not.</li>
 * <li>USSDPhase: it indicates if the device supports USSD Phase 1 (only permits
 * reception of USSDs), Phase 2 (permits both reception and response to USSDs)
 * or it does not support USSD at all.</li>
 * <li>SyncML: yes/no field that indicates if the device supports SyncML or not</li>
 * <li>SyncML-version: it indicates the SyncML version supported.</li>
 * <li>EMSmaxNumber: maximum number of consecutive SMSs.</li>
 * <li>Status: approved/not approved by the operator</li>
 * <li>Email = yes/no field that indicates if the device supports email or not</li>
 * <li>Emn = yes/no field that indicates if the device supports EMN
 * notifications or not 914104-826-6105 45 of 61</li>
 * <li>Adc_OTA = yes/no field that indicates if the device supports automatic
 * device configuration or not.</li>
 * <li>Last-updated: the last time this info was updated (or it s validity
 * checked) for this user.</li>
 * </ul>
 *
 * @author Telefonica R&D
 * 
 */
public class UserTerminalInfo implements Entity, Serializable {

	///@cond private
	private static final long serialVersionUID = -8093166004473518964L;
	
	private static final String EMPTY_VALUE_TEXT = "";

    private String mBrand;
    private String mModel;
    private String mVersion;
    private String mScreenResolution;
    private String mIMEI;
    private Boolean mMMS;
    private Boolean mEMS;
    private Boolean mSmartMessaging;
    private Boolean mWAP;
    private String mUSSDPhase;
    private Boolean mSyncMl;
    private String mSyncMlVersion;
    private Integer mEMSmaxNumber;
    private Boolean mEmail;
    private Boolean mEmn;
    private Boolean mAdcOTA;
    private Boolean mStatus;
    private String mLastUpdated;
    private Boolean mWapPush;
    private Boolean mMmsVideo;
    private Boolean mVideoStreaming;
    ///@endcond
    
    
    /**
     * @return the brand property
     */
    public String getBrand() {
        return mBrand;
    }

    /**
     * @param brand the brand property value to set
     */
    public void setBrand(String brand) {
        this.mBrand = brand;
    }

    /**
     * @return the model property
     */
    public String getModel() {
        return mModel;
    }

    /**
     * @param model the model property value to set
     */
    public void setModel(String model) {
        this.mModel = model;
    }

    /**
     * @return the version property
     */
    public String getVersion() {
        return mVersion;
    }

    /**
     * @param version the version property value to set
     */
    public void setVersion(String version) {
        this.mVersion = version;
    }

    /**
     * @return the screenResolution property
     */
    public String getScreenResolution() {
        return mScreenResolution;
    }

    /**
     * @param screenResolution the screenResolution property value to set
     */
    public void setScreenResolution(String screenResolution) {
        this.mScreenResolution = screenResolution;
    }

    /**
     * @return the iMEI property
     */
    public String getIMEI() {
        return mIMEI;
    }

    /**
     * @param iMEI the iMEI property value to set
     */
    public void setIMEI(String iMEI) {
        mIMEI = iMEI;
    }

    /**
     * @return the mMS property
     */
    public boolean isMMS() {
        return mMMS;
    }

    /**
     * @param mMS the mMS property value to set
     */
    public void setMMS(boolean mMS) {
        mMMS = mMS;
    }

    /**
     * @return the eMS property
     */
    public boolean isEMS() {
        return mEMS;
    }

    /**
     * @param eMS the eMS property value to set
     */
    public void setEMS(boolean eMS) {
        mEMS = eMS;
    }

    /**
     * @return the smartMessaging property
     */
    public boolean isSmartMessaging() {
        return mSmartMessaging;
    }

    /**
     * @param smartMessaging the smartMessaging property value to set
     */
    public void setSmartMessaging(boolean smartMessaging) {
        this.mSmartMessaging = smartMessaging;
    }

    /**
     * @return the wAP property
     */
    public boolean isWAP() {
        return mWAP;
    }

    /**
     * @param wAP the wAP property value to set
     */
    public void setWAP(boolean wAP) {
        mWAP = wAP;
    }

    /**
     * @return the uSSDPhase property
     */
    public String getUSSDPhase() {
        return mUSSDPhase;
    }

    /**
     * @param uSSDPhase the uSSDPhase property value to set
     */
    public void setUSSDPhase(String uSSDPhase) {
        mUSSDPhase = uSSDPhase;
    }

    /**
     * @return the syncMl property
     */
    public boolean isSyncMl() {
        return mSyncMl;
    }

    /**
     * @param syncMl the syncMl property value to set
     */
    public void setSyncMl(boolean syncMl) {
        this.mSyncMl = syncMl;
    }

    /**
     * @return the syncMlVersion property
     */
    public String getSyncMlVersion() {
        return mSyncMlVersion;
    }

    /**
     * @param syncMlVersion the syncMlVersion property value to set
     */
    public void setSyncMlVersion(String syncMlVersion) {
        this.mSyncMlVersion = syncMlVersion;
    }

    /**
     * @return the eMSmaxNumber property
     */
    public int getEMSmaxNumber() {
        return mEMSmaxNumber;
    }

    /**
     * @param eMSmaxNumber the eMSmaxNumber property value to set
     */
    public void setEMSmaxNumber(int eMSmaxNumber) {
        mEMSmaxNumber = eMSmaxNumber;
    }

    /**
     * @return the email property
     */
    public boolean isEmail() {
        return mEmail;
    }

    /**
     * @param email the email property value to set
     */
    public void setEmail(boolean email) {
        this.mEmail = email;
    }

    /**
     * @return the emn property
     */
    public boolean isEmn() {
        return mEmn;
    }

    /**
     * @param emn the emn property value to set
     */
    public void setEmn(boolean emn) {
        this.mEmn = emn;
    }

    /**
     * @return the adcOta property
     */
    public boolean isAdcOta() {
        return mAdcOTA;
    }

    /**
     * @param adcOta the adcOta property value to set
     */
    public void setAdcOta(boolean adcOta) {
        this.mAdcOTA = adcOta;
    }
    
    /**
     * @return the status property
     */
    public Boolean isStatus() {
        return mStatus;
    }

    /**
     * @param status the status property value to set
     */
    public void setStatus(Boolean status) {
        this.mStatus = status;
    }

    /**
     * @return the lastUpdated property
     */
    public String getLastUpdated() {
        return mLastUpdated;
    }

    /**
     * @param lastUpdated the lastUpdated property value to set
     */
    public void setLastUpdated(String lastUpdated) {
        this.mLastUpdated = lastUpdated;
    }
    
    /**
     * @return the WAP push property
     */
    public Boolean isWapPush() {
        return mWapPush;
    }

    /**
     * @param wapPush the WAP push property value to set
     */
    public void setWapPush(Boolean wapPush) {
        this.mWapPush = wapPush;
    }
    
    /**
     * @return the MMS Video property
     */
    public Boolean isMmsVideo() {
        return mMmsVideo;
    }

    /**
     * @param mmsVideo the MMS Video property value to set
     */
    public void setMmsVideo(Boolean mmsVideo) {
        this.mMmsVideo = mmsVideo;
    }
    
    /**
     * @return the Video streaming property
     */
    public Boolean isVideoStreaming() {
        return mVideoStreaming;
    }

    /**
     * @param videoStreaming the Video streaming property value to set
     */
    public void setVideoStreaming(Boolean videoStreaming) {
        this.mVideoStreaming = videoStreaming;
    }

    public boolean isValid() {
        return true;
    }
    
    /**
     * Returns a string representation of the values in this class
     * @return a comma-separated list of the values of the fields in this class
     */
    public String toString() {
        return mBrand + "," + mModel + "," + mVersion + "," + mScreenResolution
                + "," + mIMEI + "," + mMMS + "," + mEMS + "," + mSmartMessaging
                + "," + mWAP + "," + mUSSDPhase + "," + mSyncMl + ","
                + mSyncMlVersion + "," + mEMSmaxNumber + "," + "," 
                + mEmail + "," + mEmn + "," + mAdcOTA + "," 
                + mStatus + "," + mLastUpdated;
    }

    /**
     * An array containing keys and values for this object
     * @return an array where each element is a key:value string
     */
    public String[] toStringArray() {

        String[] result = new String[21];
        result[0] = "Brand: "+(mBrand!=null?mBrand:EMPTY_VALUE_TEXT);
        result[1] = "Model: "+(mModel!=null?mModel:EMPTY_VALUE_TEXT);
        result[2] = "Version: "+(mVersion!=null?mVersion:EMPTY_VALUE_TEXT);
        result[3] = "Screen Resolution: "+(mScreenResolution!=null?mScreenResolution:EMPTY_VALUE_TEXT);
        result[4] = "IMEI: "+(mIMEI!=null?mIMEI:EMPTY_VALUE_TEXT);
        result[5] = "MMS: "+(mMMS!=null?mMMS.toString():EMPTY_VALUE_TEXT);
        result[6] = "EMS: "+(mEMS!=null?mEMS.toString():EMPTY_VALUE_TEXT);
        result[7] = "Smart Messaging: "+(mSmartMessaging!=null?mSmartMessaging.toString():EMPTY_VALUE_TEXT);
        result[8] = "WAP: "+(mWAP!=null?mWAP.toString():EMPTY_VALUE_TEXT);
        result[9] = "USSDPhase: "+(mUSSDPhase!=null?mUSSDPhase.toString():EMPTY_VALUE_TEXT);
        result[10] = "SyncMl: "+(mSyncMl!=null?mSyncMl.toString():EMPTY_VALUE_TEXT);
        result[11] = "SyncMl Version: "+(mSyncMlVersion!=null?mSyncMlVersion:EMPTY_VALUE_TEXT);
        result[12] = "EMS Max Number: "+(mEMSmaxNumber!=null?mEMSmaxNumber.toString():EMPTY_VALUE_TEXT);
        result[13] = "Email: "+(mEmail!=null?mEmail.toString():EMPTY_VALUE_TEXT);
        result[14] = "EMN: "+(mEmn!=null?mEmn.toString():EMPTY_VALUE_TEXT);
        result[15] = "adc_OTA: "+(mAdcOTA!=null?mAdcOTA.toString():EMPTY_VALUE_TEXT);
        result[16] = "Status: "+(mStatus!=null?mStatus.toString():EMPTY_VALUE_TEXT);
        result[17] = "Last Updated: "+(mLastUpdated!=null?mLastUpdated:EMPTY_VALUE_TEXT);
        result[18] = "Wap push: "+(mWapPush!=null?mWapPush:EMPTY_VALUE_TEXT);
        result[19] = "MMS Video: "+(mMmsVideo!=null?mMmsVideo:EMPTY_VALUE_TEXT);
        result[20] = "Video streaming: "+(mVideoStreaming!=null?mVideoStreaming:EMPTY_VALUE_TEXT);
        return result;
    }

}
