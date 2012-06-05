/**
 * \package com.bluevia.android.location This package contains the classes in order to get Location using Bluevia API.
 * \package com.bluevia.android.location.client This package contains REST client to get Location using Bluevia API.
 */
package com.bluevia.android.location.client;

import java.io.IOException;

import java.util.HashMap;

import com.bluevia.android.commons.Encoding;
import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.client.BVBaseClient;
import com.bluevia.android.commons.connector.http.oauth.IOAuth;
import com.bluevia.android.commons.data.UserId;
import com.bluevia.android.commons.data.UserId.Type;
import com.bluevia.android.commons.exception.BlueviaException;
import com.bluevia.android.commons.parser.xml.BVRestXmlErrorParser;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.location.data.LocationDataType;
import com.bluevia.android.location.data.LocationInfoType;
import com.bluevia.android.location.data.simple.LocationInfo;
import com.bluevia.android.location.parser.xml.XmlLocationParser;

/**
 * Client interface for the REST binding of the Bluevia Location Service.
 *
 * @author Telefonica R&D
 * 
 */
public abstract class BVLocationClient extends BVBaseClient {

	protected static final String GET_LOCATION_FEED_PATH = "/TerminalLocation";

	protected static final String LOCATED_PARTY_KEY = "locatedParty";
	protected static final String ACCEPTABLE_ACCURACY_KEY = "acceptableAccuracy";

	/**
	 * Initializer for common attributes
	 */
	protected void init(){
		mEncoding = Encoding.APPLICATION_XML;
		mParser = new XmlLocationParser();
		mSerializer = null;	//No serializer
		mErrorParser = new BVRestXmlErrorParser();
	}

	/**
	 * Retrieves the location of the terminal.
	 * 
	 * @param user the user to be located
	 * @param acceptableAccuracy Accuracy, in meters, that is acceptable for a response (optional).
	 * @return An entity object containing the Location Data.
	 * @throws BlueviaException 
	 * @throws IOException 
	 */
	protected LocationDataType getLocation(UserId user, Integer acceptableAccuracy) throws BlueviaException, IOException {

		LocationDataType res = null;

		//Common mandatory params
		HashMap<String, String> httpQueryParameters = new HashMap<String, String>();

		if (acceptableAccuracy != null)
			httpQueryParameters.put(ACCEPTABLE_ACCURACY_KEY, String.valueOf(acceptableAccuracy));


		//Located party
		UserId locatedParty = null;
		if (user == null){
			locatedParty = new UserId(Type.ALIAS, ((IOAuth)mConnector).getOauthToken().getToken());
		} else locatedParty = user;
		String lp = locatedParty.getStringType() + ":" + locatedParty.getUserIdValue();
		httpQueryParameters.put(LOCATED_PARTY_KEY, lp);

		//Version
		httpQueryParameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		Entity response = retrieve("", httpQueryParameters);

		//Check if response is instance of LocationDataType or TerminalLocationListType
		if (response == null){
			throw new BlueviaException("Error during request. Response received is null", BlueviaException.INTERNAL_CLIENT_ERROR);
		} else if (response instanceof LocationDataType){

			//Set the response
			res = (LocationDataType) response;
			if (!res.isValid()){
				throw new BlueviaException("The " + LocationDataType.class.getName()
						+ " received is empty, incomplete or not valid", BlueviaException.INTERNAL_CLIENT_ERROR);
			}

		} else throw new BlueviaException("Error during request. Response received is not a " + LocationDataType.class.getName()
				+ " but a "	+ response.getClass().getName(), BlueviaException.INTERNAL_CLIENT_ERROR);

		return res;
	}

	/**
	 * Transforms the response received from the server in a simple response object
	 * 
	 * @param data the complete response
	 * @return a simple LocationInfo object
	 */
	protected LocationInfo simplifyResponse(LocationDataType data){
		if (data == null || data.getReportStatus() == null)
			return null;

		LocationInfo res = new LocationInfo(data.getReportStatus());

		LocationInfoType location = data.getCurrentLocation();
		if (location != null){
			if (location.getCoordinates() != null){
				res.setLatitude(location.getCoordinates().getLatitude());
				res.setLongitude(location.getCoordinates().getLongitude());
			}
			res.setAccuracy(location.getAccuracy());
			res.setTimestamp(location.getTimestamp().toString());
		}
		return res;
	}

}
