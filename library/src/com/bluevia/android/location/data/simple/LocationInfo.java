package com.bluevia.android.location.data.simple;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.location.data.LocationDataType.ReportStatus;

/**
 * Class representing the info of the location
 *
 * @author Telefonica R&D
 */
public class LocationInfo implements Entity {
	
	private ReportStatus mReportStatus;
	private float mLatitude;
	private float mLongitude;
	private int mAccuracy;
	private String mTimestamp;
	
	/**
	 * 
	 * @param status
	 */
	public LocationInfo(ReportStatus status){
		mReportStatus = status;
	}

	/**
	 * 
	 * @param status
	 * @param timestamp
	 * @param latitude
	 * @param longitude
	 * @param accuracy
	 */
	public LocationInfo(ReportStatus status, String timestamp, 
			float latitude, float longitude, int accuracy){
		this(status);
		
		mLatitude = latitude;
		mLongitude = longitude;
		mAccuracy = accuracy;
		mTimestamp = timestamp;
	}
	
	
	/**
	 * @return the reportStatus
	 */
	public ReportStatus getReportStatus() {
		return mReportStatus;
	}

	/**
	 * @param reportStatus the reportStatus to set
	 */
	public void setReportStatus(ReportStatus reportStatus) {
		this.mReportStatus = reportStatus;
	}

	/**
	 * @return the latitude
	 */
	public float getLatitude() {
		return mLatitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(float latitude) {
		this.mLatitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public float getLongitude() {
		return mLongitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(float longitude) {
		this.mLongitude = longitude;
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
	public String getTimestamp() {
		return mTimestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.mTimestamp = timestamp;
	}

	@Override
	public boolean isValid() {
		return mReportStatus != null;
	}

}
