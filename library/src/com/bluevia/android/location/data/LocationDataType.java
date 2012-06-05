/**
 * \package com.bluevia.android.location.data This package contains entities to hold data related to the location API
 */
package com.bluevia.android.location.data;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.data.UserId;

/**
*
* Class representing data of a location retrieval
*
* @author Telefonica R&D
* 
*
*/
public class LocationDataType implements Entity {
	
	/**
     * Enum representing the values of the status of a location retrieval.
     * Possible values are:
     * <ul>
     * 	<li> RETRIEVED: The location has been retrieved </li>
     *  <li> NON_RETRIEVED: The location has not been retrieved </li>
     *  <li> ERROR: There was an error retrieving the location </li>
     * </ul>
     * 
     */
	public enum ReportStatus {
		RETRIEVED,
		NON_RETRIEVED,
		ERROR
	}
	
	private UserId mLocatedParty;
	private ReportStatus mReportStatus;
	private LocationInfoType mCurrentLocation;
	private ServiceErrorType mErrorInformation;
	
	/**
	 * Instantiates a new empty LocationDataType
	 */
	public LocationDataType(){
		super();
	}
	
	/**
	 * Instantiates a new LocationDataType
	 * 
	 * @param locatedParty Address of the terminal device to which the location information applies.
	 * @param reportStatus Status of retrieval for this terminal device address.
	 */
	public LocationDataType(UserId locatedParty, ReportStatus reportStatus){
		super();
		mLocatedParty = locatedParty;
		mReportStatus = reportStatus;
		mCurrentLocation = null;
		mErrorInformation = null;
	}
	
	/**
	 * Instantiates a new LocationDataType
	 * 
	 * @param locationParty Address of the terminal device to which the location information applies.
	 * @param reportStatus Status of retrieval for this terminal device address.
	 * @param currentLocation Location of terminal. It is only provided if ReportStatus=Retrieved.
	 * @param errorInformation If ReportStatus=Error, this is the reason for the error.
	 */
	public LocationDataType(UserId locationParty, ReportStatus reportStatus,
			LocationInfoType currentLocation, ServiceErrorType errorInformation){
		this(locationParty, reportStatus);
		mCurrentLocation = currentLocation;
		mErrorInformation = errorInformation;
	}
	
	public boolean isValid() {
		logLocation();
		return mLocatedParty != null && mLocatedParty.isValid() && mReportStatus != null;		
	}

	/**
	 * 
	 */
	private void logLocation() {
		
	}

	/**
	 * @return the LocationParty
	 */
	public UserId getLocationParty() {
		return mLocatedParty;
	}

	/**
	 * @param locationParty the LocationParty to set
	 */
	public void setLocationParty(UserId locationParty) {
		this.mLocatedParty = locationParty;
	}

	/**
	 * @return the ReportStatus
	 */
	public ReportStatus getReportStatus() {
		return mReportStatus;
	}

	/**
	 * @param reportStatus the ReportStatus to set
	 */
	public void setReportStatus(ReportStatus reportStatus) {
		this.mReportStatus = reportStatus;
	}

	/**
	 * @return the CurrentLocation
	 */
	public LocationInfoType getCurrentLocation() {
		return mCurrentLocation;
	}

	/**
	 * @param currentLocation the CurrentLocation to set
	 */
	public void setCurrentLocation(LocationInfoType currentLocation) {
		this.mCurrentLocation = currentLocation;
	}

	/**
	 * @return the ErrorInformation
	 */
	public ServiceErrorType getErrorInformation() {
		return mErrorInformation;
	}

	/**
	 * @param errorInformation the ErrorInformation to set
	 */
	public void setErrorInformation(ServiceErrorType errorInformation) {
		this.mErrorInformation = errorInformation;
	}
	
	

}
