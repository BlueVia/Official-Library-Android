/**
 * \package com.bluevia.android.rest.directory.data.filter This package contains REST filters to notify the Directory library which fileds of the User Infromation to retrieve
 *  using Bluevia API.
 */
package com.bluevia.android.rest.directory.data.filter;

import com.bluevia.android.rest.directory.parser.xml.XmlConstants;

/**
 * This class works as a filter to let developers notify the library about which fields of the
 * UserAccesInformation block they want to retrieve. Developers must set a true value in those
 * fields they whish the library to retrieve from the gSDP and pass the object to the
 * getUserAccessInformation method of their DirectoryClient instance

 * @author Telefonica R&D
 * 
 */
public class UserAccessInfoFilter extends RequestFilter {
	
    /**
     * Gets the connected filter
     * 
     * @return the connected filter
     */
    public boolean isConnected() {
        return getFilter(XmlConstants.XSD_ACCESSINFO_CONNECTED);
    }
    /**
     * Sets the connected filter
     * 
     * @param connected the connected filter to set
     */
    public void setConnected(boolean connected) {
        setFilter(XmlConstants.XSD_ACCESSINFO_CONNECTED, connected);
    }
    /**
     * Gets the IP filter
     * 
     * @return the iP filter
     */
    public boolean isIpAddress() {
        return getFilter(XmlConstants.XSD_ACCESSINFO_IPADDRESS);
    }
    /**
     * Sets the IP filter
     * 
     * @param iP the iP filter to set
     */
    public void setIpAddress(boolean iP) {
        setFilter(XmlConstants.XSD_ACCESSINFO_IPADDRESS, iP);
    }
    /**
     * Gets the access type filter
     * 
     * @return the accessType filter
     */
    public boolean isAccessType() {
        return getFilter(XmlConstants.XSD_ACCESSINFO_ACCESSTYPE);
    }
    /**
     * Sets the access type filter
     * 
     * @param accessType the accessType filter to set
     */
    public void setAccessType(boolean accessType) {
        setFilter(XmlConstants.XSD_ACCESSINFO_ACCESSTYPE, accessType);
    }
    /**
     * Gets the connection time filter
     * 
     * @return the connectionTime filter
     */
    public boolean isConnectionTime() {
        return getFilter(XmlConstants.XSD_ACCESSINFO_CONNECTIONTIME);
    }
    /**
     * Sets the connection time filter
     * 
     * @param connectionTime the connectionTime filter to set
     */
    public void setConnectionTime(boolean connectionTime) {
        setFilter(XmlConstants.XSD_ACCESSINFO_CONNECTIONTIME, connectionTime);
    }
    /**
     * Gets the aPN filter
     * 
     * @return the aPN filter
     */
    public boolean isAPN() {
        return getFilter(XmlConstants.XSD_ACCESSINFO_APN);
    }
    /**
     * Sets the aPN filter
     * 
     * @param aPN the aPN filter to set
     */
    public void setAPN(boolean aPN) {
        setFilter(XmlConstants.XSD_ACCESSINFO_APN, aPN);
    }
    /**
     * Gets the roaming filter
     * 
     * @return the roaming filter
     */
    public boolean isRoaming() {
        return getFilter(XmlConstants.XSD_ACCESSINFO_ROAMING);
    }
    /**
     * Sets the roaming filter
     * 
     * @param roaming the roaming filter to set
     */
    public void setRoaming(boolean roaming) {
        setFilter(XmlConstants.XSD_ACCESSINFO_ROAMING, roaming);
    }
}
