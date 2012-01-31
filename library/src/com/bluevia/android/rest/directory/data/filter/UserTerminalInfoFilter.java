package com.bluevia.android.rest.directory.data.filter;

import com.bluevia.android.rest.directory.parser.xml.XmlConstants;

/**
 * This class works as a filter to let developers notify the library about which fields of the
 * UserTerminalInformation block they want to retrieve. Developers must set a true value in those
 * fields they whish the library to retrieve from the gSDP and pass the object to the
 * getUserTerminalInformation method of their DirectoryClient instance

 * @author Telefonica R&D
 * 
 */
public class UserTerminalInfoFilter extends RequestFilter{

    /**
     * @return the brand filter
     */
    public boolean isBrand() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_BRAND);
    }
    
    /**
     * @param brand the brand filter to set
     */
    public void setBrand(boolean brand) {
        setFilter(XmlConstants.XSD_TERMINALINFO_BRAND, brand);
    }

    /**
     * @return the model filter
     */
    public boolean isModel() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_MODEL);
    }

    /**
     * @param model the model filter to set
     */
    public void setModel(boolean model) {;
        setFilter(XmlConstants.XSD_TERMINALINFO_MODEL, model);
    }
    
    /**
     * @return the version filter
     */
    public boolean isVersion() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_VERSION);
    }
    
    /**
     * @param version the version filter to set
     */
    public void setVersion(boolean version) {
        setFilter(XmlConstants.XSD_TERMINALINFO_VERSION, version);
    }

    /**
     * @return the screenResolution filter
     */
    public boolean isScreenResolution() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_SCREENRESOLUTION);
    }

    /**
     * @param screenResolution the screenResolution filter to set
     */
    public void setScreenResolution(boolean screenResolution) {
        setFilter(XmlConstants.XSD_TERMINALINFO_SCREENRESOLUTION, screenResolution);
    }

    /**
     * @return the iMEI filter
     */
    public boolean isIMEI() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_IMEI);
    }

    /**
     * @param iMEI the iMEI filter to set
     */
    public void setIMEI(boolean iMEI) {
        setFilter(XmlConstants.XSD_TERMINALINFO_IMEI, iMEI);
    }

    /**
     * @return the mMS filter
     */
    public boolean isMMS() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_MMS);
    }

    /**
     * @param mMS the mMS filter to set
     */
    public void setMMS(boolean mMS) {
        setFilter(XmlConstants.XSD_TERMINALINFO_MMS, mMS);
    }

    /**
     * @return the eMS filter
     */
    public boolean isEMS() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_EMS);
    }

    /**
     * @param eMS the eMS filter to set
     */
    public void setEMS(boolean eMS) {
        setFilter(XmlConstants.XSD_TERMINALINFO_EMS, eMS);
    }

    /**
     * @return the smartMessaging filter
     */
    public boolean isSmartMessaging() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_SMARTMESSAGING);
    }

    /**
     * @param smartMessaging the smartMessaging filter to set
     */
    public void setSmartMessaging(boolean smartMessaging) {
        setFilter(XmlConstants.XSD_TERMINALINFO_SMARTMESSAGING, smartMessaging);
    }

    /**
     * @return the wAP filter
     */
    public boolean isWAP() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_WAP);
    }

    /**
     * @param wAP the wAP filter to set
     */
    public void setWAP(boolean wAP) {
        setFilter(XmlConstants.XSD_TERMINALINFO_WAP, wAP);
    }

    /**
     * @return the uSSDPhase filter
     */
    public boolean isUSSDPhase() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_USSDPHASE);
    }

    /**
     * @param uSSDPhase the uSSDPhase filter to set
     */
    public void setUSSDPhase(boolean uSSDPhase) {
        setFilter(XmlConstants.XSD_TERMINALINFO_USSDPHASE, uSSDPhase);
    }

    /**
     * @return the syncMl filter
     */
    public boolean isSyncMl() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_SYNCML);
    }

    /**
     * @param syncMl the syncMl filter to set
     */
    public void setSyncMl(boolean syncMl) {
        setFilter(XmlConstants.XSD_TERMINALINFO_SYNCML, syncMl);
    }

    /**
     * @return the syncMlVersion filter
     */
    public boolean isSyncMlVersion() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_SYNCMLVERSION);
    }

    /**
     * @param syncMlVersion the syncMlVersion filter to set
     */
    public void setSyncMlVersion(boolean syncMlVersion) {
        setFilter(XmlConstants.XSD_TERMINALINFO_SYNCMLVERSION, syncMlVersion);
    }

    /**
     * @return the eMSmaxNumber filter
     */
    public boolean isEMSmaxNumber() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_EMSMAXNUMBER);
    }

    /**
     * @param eMSmaxNumber the eMSmaxNumber filter to set
     */
    public void setEMSmaxNumber(boolean eMSmaxNumber) {
        setFilter(XmlConstants.XSD_TERMINALINFO_EMSMAXNUMBER, eMSmaxNumber);
    }

    /**
     * @return the email filter
     */
    public boolean isEmail() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_EMAIL);
    }

    /**
     * @param email the email filter to set
     */
    public void setEmail(boolean email) {
        setFilter(XmlConstants.XSD_TERMINALINFO_EMAIL, email);
    }

    /**
     * @return the emn filter
     */
    public boolean isEmn() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_EMN);
    }

    /**
     * @param emn the emn filter to set
     */
    public void setEmn(boolean emn) {
        setFilter(XmlConstants.XSD_TERMINALINFO_EMN, emn);
    }

    /**
     * @return the adcOta filter
     */
    public boolean isAdcOta() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_ADCOTA);
    }

    /**
     * @param adcOta the adcOta filter to set
     */
    public void setAdcOta(boolean adcOta) {
        setFilter(XmlConstants.XSD_TERMINALINFO_ADCOTA, adcOta);
    }

    /**
     * @return the status filter
     */
    public boolean isStatus() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_STATUS);
    }

    /**
     * @param status the status filter to set
     */
    public void setStatus(boolean status) {
        setFilter(XmlConstants.XSD_TERMINALINFO_STATUS, status);
    }
    
    /**
     * @return the lastUpdated filter
     */
    public boolean isLastUpdated() {
        return getFilter(XmlConstants.XSD_LASTUPDATED);
    }

    /**
     * @param lastUpdated the lastUpdated filter to set
     */
    public void setLastUpdated(boolean lastUpdated) {
        setFilter(XmlConstants.XSD_LASTUPDATED, lastUpdated);
    }
    
    /**
     * @return the WAP push filter
     */
    public boolean isWapPush() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_WAP_PUSH);
    }

    /**
     * @param wapPush the WAP push filter to set
     */
    public void setWapPush(boolean wapPush) {
        setFilter(XmlConstants.XSD_TERMINALINFO_WAP_PUSH, wapPush);
    }
    
    /**
     * @return the MMS Video filter
     */
    public boolean isMmsVideo() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_MMS_VIDEO);
    }

    /**
     * @param mmsVideo the MMS Video filter to set
     */
    public void setMmsVideo(boolean mmsVideo) {
        setFilter(XmlConstants.XSD_TERMINALINFO_MMS_VIDEO, mmsVideo);
    }
    
    /**
     * @return the video streaming filter
     */
    public boolean isVideoStreaming() {
        return getFilter(XmlConstants.XSD_TERMINALINFO_VIDEO_STREAMING);
    }

    /**
     * @param videoStreaming the video streaming filter to set
     */
    public void setVideoStreaming(boolean videoStreaming) {
        setFilter(XmlConstants.XSD_TERMINALINFO_VIDEO_STREAMING, videoStreaming);
    }

}
