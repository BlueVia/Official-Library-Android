/**
 * \package com.bluevia.android.rest.directory.data This package contains entities data types of Bluevia Directory Service
 */
package com.bluevia.android.rest.directory.data;

import com.bluevia.android.rest.commons.Entity;

/**
 * * Class to hold the information of the complete User Information block from the gSDP
 * This type is composed of the following information blocks:
 * <ul>
 *  <li> User Access Info</li>
 *  <li> User Profile</li>
 *  <li> User Terminal Info</li>
 * </ul>
 *
 * This implementation is not synchronized
 * @author Telefonica R&D
 * 
 *
 */
public class UserInfo implements Entity {

	private UserAccessInfo mAccessInfo;
	private UserProfile mUserProfile;
	private UserTerminalInfo mUserTerminalInfo;
	
	
	/**
	 * @return the accessInfo
	 */
	public UserAccessInfo getAccessInfo() {
		return mAccessInfo;
	}

	/**
	 * @param accessInfo the accessInfo to set
	 */
	public void setAccessInfo(UserAccessInfo accessInfo) {
		this.mAccessInfo = accessInfo;
	}

	/**
	 * @return the userProfile
	 */
	public UserProfile getUserProfile() {
		return mUserProfile;
	}

	/**
	 * @param userProfile the userProfile to set
	 */
	public void setUserProfile(UserProfile userProfile) {
		this.mUserProfile = userProfile;
	}

	/**
	 * @return the userTerminalInfo
	 */
	public UserTerminalInfo getUserTerminalInfo() {
		return mUserTerminalInfo;
	}

	/**
	 * @param userTerminalInfo the userTerminalInfo to set
	 */
	public void setUserTerminalInfo(UserTerminalInfo userTerminalInfo) {
		this.mUserTerminalInfo = userTerminalInfo;
	}

	public boolean isValid() {
		return (mAccessInfo == null || mAccessInfo.isValid()) &&
			(mUserProfile == null || mUserProfile.isValid()) &&
			(mUserTerminalInfo == null || mUserTerminalInfo.isValid());
	}

}
