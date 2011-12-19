package com.bluevia.android.rest.location.data;

import java.util.Date;

import com.bluevia.android.rest.commons.Entity;


/**
*
* Class representing the location info.
*
* @author Telefonica R&D
* 
*
*/
public class LocationInfoType implements Entity {

	///@cond private
	private CoordinatesType mCoordinates;
	private float mAltitude;
	private int mAccuracy;
	private Date mTimestamp;
	///@endcond
	
	/**
	 * Instantiates a new LocationInfoType
	 */
	public LocationInfoType() {}
	
	/**
	 * Instantiates a new LocationInfoType
	 * 
	 * @param coordinates Longitude and latitude
	 * @param accuracy Accuracy of location provided in meters
	 * @param timestamp Date and time that location was collected
	 */
	public LocationInfoType(CoordinatesType coordinates, int accuracy, Date timestamp){
		super();
		mCoordinates = coordinates;
		mAccuracy = accuracy;
		mTimestamp = timestamp;
	}
	
	/**
	 * Instantiates a new LocationInfoType
	 * 
	 * @param coordinates Longitude and latitude
	 * @param altitude Location altitude
	 * @param accurancy Accuracy of location provided in meters
	 * @param timestamp Date and time that location was collected
	 */
	public LocationInfoType(CoordinatesType coordinates, float altitude, 
			int accurancy, Date timestamp){
		this(coordinates, accurancy, timestamp);
		mAltitude = altitude;
	}

	/**
     * @see com.bluevia.android.rest.commons.Entity#isValid()
     */
	public boolean isValid() {
		return mCoordinates != null && mCoordinates.isValid() && mTimestamp != null;
	}

	/**
	 * @return the coordinates
	 */
	public CoordinatesType getCoordinates() {
		return mCoordinates;
	}

	/**
	 * @param coordinates the coordinates to set
	 */
	public void setCoordinates(CoordinatesType coordinates) {
		this.mCoordinates = coordinates;
	}

	/**
	 * @return the altitude
	 */
	public float getAltitude() {
		return mAltitude;
	}

	/**
	 * @param altitude the altitude to set
	 */
	public void setAltitude(float altitude) {
		this.mAltitude = altitude;
	}

	/**
	 * @return the accuracy
	 */
	public int getAccuracy() {
		return mAccuracy;
	}

	/**
	 * @param accuracy the accuracy to set
	 */
	public void setAccuracy(int accuracy) {
		this.mAccuracy = accuracy;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return mTimestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.mTimestamp = timestamp;
	}
	
}
