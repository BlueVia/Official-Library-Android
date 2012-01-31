package com.bluevia.android.rest.directory.data;

import java.io.Serializable;

import com.bluevia.android.rest.commons.Entity;


/**
 * * Class to hold the information of the User Access Information block resource from the gSDP
 * This type is composed of the following fields:
 * <ul>
 *  <li> Connected: yes/no field that assesses if the user is connected or not.</li>
 *  <li>IP address: IP address currently assigned to the user.</li>
 *  <li>Access Type: it indicates the access network used to get connected. Possible values are:
 *  GSM, GPRS, UMTS, HSPA, LTE, WIMAX, etc.</li>
 *  <li> Connection Time: Duration in seconds of user current connection. </li>
 *  <li> APN: Access Point Name. </li>
 *  <li> Roaming: It indicates if the user is attached to an access network different from its
 *  home network.</li>
 * </ul>
 *
 * This implementation is not synchronized
 * @author Telefonica R&D
 * 
 *
 */
public class UserAccessInfo implements Entity, Serializable{
	
	///@cond private
    private static final long serialVersionUID = 8971596941315073483L;
    
    private Boolean mConnected;
    private String mIpAddress;
    private String mAccessType;
    private Integer mConnectionTime;
    private String APN;
    private Boolean mRoaming;

    private static final String EMPTY_VALUE_TEXT = "";
    ///@endcond

    /**
     * Gets the connected property
     * 
     * @return the connected property
     */
    public Boolean isConnected() {
        return mConnected;
    }

    /**
     * Sets the connected property	
     * 
     * @param connected the connected property to set
     */
    public void setConnected(boolean connected) {
        this.mConnected = connected;
    }

    /**
     * Gets the IP property
     * 
     * @return the IP property
     */
    public String getIP() {
        return mIpAddress;
    }

    /**
     * Sets the IP property	
     * 
     * @param iP connected the ip property to set
     */
    public void setIP(String iP) {
        mIpAddress = iP;
    }

    /**
     * Gets the access type property
     * 
     * @return the access type property
     */
    public String getAccessType() {
        return mAccessType;
    }

    /**
     * Sets the accessType property
     * 
     * @param accessType the accessType property to set
     */
    public void setAccessType(String accessType) {
        this.mAccessType = accessType;
    }



    /**
     * Gets the connection time property
     * 
     * @return the Connection time property
     */
    public Integer getConnectionTime() {
        return mConnectionTime;
    }

    /**
     * Sets the connectionTime property	
     * 
     * @param connectionTime the connectionTime property to set
     */
    public void setConnectionTime(int connectionTime) {
        this.mConnectionTime = connectionTime;
    }

    /**
     * Gets the access point name property
     * 
     * @return the access point name property
     */
    public String getAPN() {
        return APN;
    }

    /**
     * Sets the apn property	
     * 
     * @param aPN the apn property to set
     */
    public void setAPN(String aPN) {
        APN = aPN;
    }

    /**
     * Gets the roaming property
     * 
     * @return the roaming property
     */
    public Boolean isRoaming() {
        return mRoaming;
    }

    /**
     * Sets the roaming property	
     * 
     * @param roaming the roaming property to set
     */
    public void setRoaming(boolean roaming) {
        this.mRoaming = roaming;
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
        String res = "";
        if (mConnected != null){
            res += mConnected + ",";
        }
        if (mIpAddress != null){
            res += mIpAddress + ",";
        }
        if (mAccessType != null){
            res += mAccessType + ",";
        }
        if (mConnectionTime != null){
            res += mConnectionTime + ",";
        }
        if (APN != null){
            res += APN + ",";
        }
        return res;
    }

    /**
     * Returns an array containing keys and values for this object
     * @return an array where each element is a key:value string
     */
    public String[] toStringArray() {
        String[] result = new String[6];
        result[0] = "Connected: "+(mConnected!=null?mConnected.toString():EMPTY_VALUE_TEXT);
        result[1] = "IP address: "+(mIpAddress!=null?mIpAddress:EMPTY_VALUE_TEXT);
        result[2] = "Access Type: "+(mAccessType!=null?mAccessType:EMPTY_VALUE_TEXT);
        result[3] = "Connection Time: "+(mConnectionTime!=null?mConnectionTime.toString():EMPTY_VALUE_TEXT);
        result[4] = "APN: "+(APN!=null?APN:EMPTY_VALUE_TEXT);
        result[5] = "Roaming: "+(mRoaming!=null?mRoaming.toString():EMPTY_VALUE_TEXT);
        return result;
    }

}
