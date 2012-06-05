package com.bluevia.android.location.data;

import com.bluevia.android.commons.Entity;

/**
*
* Class representing the coordinates (longtude and latitude) of a location.
*
* @author Telefonica R&D
* 
*
*/
public class CoordinatesType implements Entity {

	private float mLongitude;
	private float mLatitude;
	
	/**
	 * Instantiates a new CoordinatesType
	 * 
	 * @param longitude the longitude.
	 * @param latitude the latitude.
	 */
	public CoordinatesType(float longitude, float latitude){
		mLongitude = longitude;
		mLatitude = latitude;
	}
	
	public boolean isValid() {
		return true;
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
	
}
