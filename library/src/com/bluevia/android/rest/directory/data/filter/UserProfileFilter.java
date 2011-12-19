package com.bluevia.android.rest.directory.data.filter;

import com.bluevia.android.rest.directory.parser.xml.XmlConstants;

/**
 * This class works as a filter to let developers notify the library about which fields of the
 * UserAccesProfile block they want to retrieve. Developers must set a true value in those
 * fields they whish the library to retrieve from the gSDP and pass the object to the
 * getUserProfile method of their DirectoryClient instance

 * @author Telefonica R&D
 * 
 */
public class UserProfileFilter extends RequestFilter{

    /**
     * @return the userType filter
     */
    public boolean isUserType() {
        return getFilter(XmlConstants.XSD_PROFILE_USERTYPE);
    }
    /**
     * @param userType the userType filter to set
     */
    public void setUserType(boolean userType) {
        setFilter(XmlConstants.XSD_PROFILE_USERTYPE, userType);
    }
    /**
     * @return the iMSI filter
     */
    public boolean isIMSI() {
        return getFilter(XmlConstants.XSD_PROFILE_IMSI);
    }
    
    /**
     * @param iMSI the iMSI filter to set
     */
    public void setIMSI(boolean iMSI) {
        setFilter(XmlConstants.XSD_PROFILE_IMSI, iMSI);
    }
    
    /**
     * @return the iCB filter
     */
    public boolean isICB() {
        return getFilter(XmlConstants.XSD_PROFILE_ICB);
    }

    /**
     * @param iCB the iCB filter to set
     */
    public void setICB(boolean iCB) {
        setFilter(XmlConstants.XSD_PROFILE_ICB, iCB);
    }

    /**
     * @return the oCB filter
     */
    public boolean isOCB() {
        return getFilter(XmlConstants.XSD_PROFILE_OCB);
    }
    
    /**
     * @param oCB the oCB filter to set
     */
    public void setOCB(boolean oCB) {
        setFilter(XmlConstants.XSD_PROFILE_OCB, oCB);
    }

    /**
     * @return the language filter
     */
    public boolean isLanguage() {
        return getFilter(XmlConstants.XSD_PROFILE_LANGUAGE);
    }

    /**
     * @param lang the language filter to set
     */
    public void setLanguage(boolean lang) {
        setFilter(XmlConstants.XSD_PROFILE_LANGUAGE, lang);
    }

    /**
     * @return the simType filter
     */
    public boolean isSimType() {
        return getFilter(XmlConstants.XSD_PROFILE_SIMTYPE);
    }

    /**
     * @param simType the simType filter to set
     */
    public void setSimType(boolean simType) {
        setFilter(XmlConstants.XSD_PROFILE_SIMTYPE, simType);
    }

    /**
     * @return the parentalControl filter
     */
    public boolean isParentalControl() {
        return getFilter(XmlConstants.XSD_PROFILE_PARENTALCONTROL);
    }
    
    /**
     * @param parentalControl the parentalControl filter to set
     */
    public void setParentalControl(boolean parentalControl) {
        setFilter(XmlConstants.XSD_PROFILE_PARENTALCONTROL, parentalControl);
    }

    /**
     * @return the creditControl filter
     */
    public boolean isCreditControl() {
        return getFilter(XmlConstants.XSD_PROFILE_CREDITCONTROL);
    }
    
    /**
     * @param creditControl the creditControl filter to set
     */
    public void setCreditControl(boolean creditControl) {
        setFilter(XmlConstants.XSD_PROFILE_CREDITCONTROL, creditControl);
    }
    
    /**
     * @return the diversionMSISDN filter
     */
    public boolean isDiversionMSISDN() {
        return getFilter(XmlConstants.XSD_PROFILE_DIVERSIONMSISDN);
    }

    /**
     * @param diversionMSISDN the diversionMSISDN filter to set
     */
    public void setDiversionMSISDN(boolean diversionMSISDN) {
        setFilter(XmlConstants.XSD_PROFILE_DIVERSIONMSISDN, diversionMSISDN);
    }

    /**
     * @return the enterpriseName filter
     */
    public boolean isEnterpriseName() {
        return getFilter(XmlConstants.XSD_PROFILE_ENTERPRISENAME);
    }

    /**
     * @param enterpriseName the enterpriseName filter to set
     */
    public void setEnterpriseName(boolean enterpriseName) {
        setFilter(XmlConstants.XSD_PROFILE_ENTERPRISENAME, enterpriseName);
    }

    /**
     * @return the roaming filter
     */
    public boolean isRoaming() {
        return getFilter(XmlConstants.XSD_PROFILE_ROAMING);
    }

    /**
     * @param roaming the roaming filter to set
     */
    public void setRoaming(boolean roaming) {
        setFilter(XmlConstants.XSD_PROFILE_ROAMING, roaming);
    }

    /**
     * @return the operatorId filter
     */
    public boolean isOperatorId() {
        return getFilter(XmlConstants.XSD_PROFILE_OPERATORID);
    }

    /**
     * @param operatorId the operatorId filter to set
     */
    public void setOperatorId(boolean operatorId) {
        setFilter(XmlConstants.XSD_PROFILE_OPERATORID, operatorId);
    }

    /**
     * @return the mmsStatus filter
     */
    public boolean isMmsStatus() {
        return getFilter(XmlConstants.XSD_PROFILE_MMSSTATUS);
    }

    /**
     * @param mmsStatus the mmsStatus filter to set
     */
    public void setMmsStatus(boolean mmsStatus) {
        setFilter(XmlConstants.XSD_PROFILE_MMSSTATUS, mmsStatus);
    }

    /**
     * @return the segment filter
     */
    public boolean isSegment() {
        return getFilter(XmlConstants.XSD_PROFILE_SEGMENT);
    }

    /**
     * @param segment the segment filter to set
     */
    public void setSegment(boolean segment) {
        setFilter(XmlConstants.XSD_PROFILE_SEGMENT, segment);
    }

    /**
     * @return the subSegment filter
     */
    public boolean isSubSegment() {
        return getFilter(XmlConstants.XSD_PROFILE_SUBSEGMENT);
    }

    /**
     * @param subSegment the subSegment filter to set
     */
    public void setSubSegment(boolean subSegment) {
        setFilter(XmlConstants.XSD_PROFILE_SUBSEGMENT, subSegment);
    }

    /**
     * @return the subscribedServices filter
     */
    public boolean isSubscribedService() {
        return getFilter(XmlConstants.XSD_PROFILE_SUBSCRIBEDSERVICE);
    }

    /**
     * @param subscribedServices the subscribedServices filter to set
     */
    public void setSubscribedServices(boolean subscribedServices) {
        setFilter(XmlConstants.XSD_PROFILE_SUBSCRIBEDSERVICE, subscribedServices);
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
     * 
     * @return the icc filter
     */
    public boolean isIcc(){
    	return getFilter(XmlConstants.XSD_PROFILE_ICC);
    }
    
    /**
     * 
     * @param icc the icc filter to set 
     */
    public void setIcc(boolean icc){
    	setFilter(XmlConstants.XSD_PROFILE_ICB, icc);
    }
    

    /**
     * 
     * @return the restMens filter
     */
    public boolean isRestMens(){
    	return getFilter(XmlConstants.XSD_PROFILE_RESTMENS);
    }
    
    /**
     * 
     * @param icc the restMens filter to set 
     */
    public void setRestMens(boolean restMens){
    	setFilter(XmlConstants.XSD_PROFILE_RESTMENS, restMens);
    }
}
