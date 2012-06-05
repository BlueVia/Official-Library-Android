/**
 * \package com.bluevia.android.directory.data This package contains entities data types of Bluevia Directory Service
 */
package com.bluevia.android.directory.data;

import com.bluevia.android.commons.Entity;

/**
 * * Class to hold the information of the complete User Information block from the gSDP
 * This type is composed of the following information blocks:
 * <ul>
 *  <li> User Access Info</li>
 *  <li> User Profile</li>
 *  <li> User Terminal Info</li>
 *  <li> User Personal Info</li>
 * </ul>
 *
 * This implementation is not synchronized
 * @author Telefonica R&D
 * 
 */
public class UserInfo implements Entity {
	
	public enum DataSet {ACCESS_INFO, PROFILE, TERMINAL_INFO, PERSONAL_INFO};

	private PersonalInfo mPersonalInfo;
	private AccessInfo mAccessInfo;
	private Profile mProfile;
	private TerminalInfo mTerminalInfo;
	
	/**
	 * @return the accessInfo
	 */
	public AccessInfo getAccessInfo() {
		return mAccessInfo;
	}

	/**
	 * @param accessInfo the accessInfo to set
	 */
	public void setAccessInfo(AccessInfo accessInfo) {
		this.mAccessInfo = accessInfo;
	}

	/**
	 * @return the userProfile
	 */
	public Profile getProfile() {
		return mProfile;
	}

	/**
	 * @param userProfile the userProfile to set
	 */
	public void setProfile(Profile userProfile) {
		this.mProfile = userProfile;
	}

	/**
	 * @return the terminalInfo
	 */
	public TerminalInfo getTerminalInfo() {
		return mTerminalInfo;
	}

	/**
	 * @param terminalInfo the userTerminalInfo to set
	 */
	public void setTerminalInfo(TerminalInfo terminalInfo) {
		this.mTerminalInfo = terminalInfo;
	}
	
	/**
	 * @return the personal info
	 */
	public PersonalInfo getPersonalInfo() {
		return mPersonalInfo;
	}

	/**
	 * @param personalInfo the personal info to set
	 */
	public void setPersonalInfo(PersonalInfo personalInfo) {
		this.mPersonalInfo = personalInfo;
	}

	public boolean isValid() {
		return (mAccessInfo == null || mAccessInfo.isValid()) &&
			(mProfile == null || mProfile.isValid()) &&
			(mTerminalInfo == null || mTerminalInfo.isValid());
	}

	@Override
	public String toString() {
		// TODO User Personal
		
		StringBuilder sb = new StringBuilder();

		sb.append("*** USER INFO ***:");
		sb.append("--- Access Info ---");
		if (mAccessInfo != null)
			sb.append(mAccessInfo.toString());
		sb.append("--- Personal Info ---");
		if (mPersonalInfo != null)
			sb.append(mPersonalInfo.toString());
		sb.append("--- Profile ---");
		if (mProfile != null)
			sb.append(mProfile.toString());
		sb.append("--- Terminal Info ---");
		if (mTerminalInfo != null)
			sb.append(mTerminalInfo.toString());
		
		return sb.toString();
	}
	
	

}
