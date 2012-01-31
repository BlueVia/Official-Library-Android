package com.bluevia.android.rest.location.data;

import com.bluevia.android.rest.commons.Entity;

/**
*
* Class representing the coordinates (longtude and latitude) of a location.
*
* @author Telefonica R&D
* 
*
*/
public class CoordinatesType implements Entity {

	///@cond private
	private float mLongitude;
	private float mLatitude;
	///@endcond
	
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
	
    /**
     * @see com.bluevia.android.rest.commons.Entity#isValid()
     */
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
