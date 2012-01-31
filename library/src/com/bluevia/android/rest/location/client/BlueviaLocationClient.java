package com.bluevia.android.rest.location.client;

import java.io.IOException;

import android.content.Context;
import android.util.Log;

import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.client.AbstractRestClient;
import com.bluevia.android.rest.commons.connector.IConnector;
import com.bluevia.android.rest.commons.connector.http.HttpConnector;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.mock.BlueviaLocationMockConnector;
import com.bluevia.android.rest.commons.data.UserId;
import com.bluevia.android.rest.commons.data.UserId.Type;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.location.data.LocationDataType;
import com.bluevia.android.rest.location.parser.BlueviaLocationParserFactory;
import com.bluevia.android.rest.oauth.Oauth;
import com.bluevia.android.rest.oauth.data.Token;

/**
 * @class BlueviaLocationClient
 * Client interface for the REST binding of the Bluevia Location Service.
 *
 * @author Telefonica R&D
 * 
 */
public class BlueviaLocationClient extends LocationClient {
	
	private static final String TAG = "BlueviaLocationClient";

	private static final String FEED_LOCATION_BASE_URI = "REST/Location";
	private static final String FEED_LOCATION_SANDBOX_BASE_URI = "REST/Location_Sandbox";
	
	private static final String GET_LOCATION_FEED_PATH = "TerminalLocation";
	
	/**
	 * Creates a LocationClient object to retrieve location information from the gSDP.
	 * @param context the Android context of the application.
	 * @param mode The communication mechanism to communicate with the gSDP.
	 * @param consumerKey The oauth consumer key supplied by the developer.
	 * @param consumerSecret The oauth consumer secret supplied by the developer.
	 * @param accessToken The oauth access token returned by the getAccessToken call.
	 * 
	 * @throws BlueviaClientException if an exception occurs during the request.
	 */
	public BlueviaLocationClient(Context context, AbstractRestClient.Mode mode, String consumerKey, String consumerSecret, Token accessToken) throws BlueviaClientException {
		super(context, (mode != Mode.SANDBOX) ? FEED_LOCATION_BASE_URI : FEED_LOCATION_SANDBOX_BASE_URI, 
				isHttpMode(mode) ? new HttpConnector() : new BlueviaLocationMockConnector(mode.ordinal()), 
						new Oauth(consumerKey, consumerSecret, accessToken), new BlueviaLocationParserFactory());
	}
	
	///cond private

	protected String composeUri() {
		return mBaseUri + URI_PATH_SEPARATOR + GET_LOCATION_FEED_PATH;
	}
	
	/**
	 * Creates a LocationClient object, with a explicit IConnector, to retrieve location information from the gSDP.
	 * @param context the Android context of the application.
     * @param connector the connector for communication mechanism.
	 * @param consumerKey The oauth consumer key supplied by the developer.
	 * @param consumerSecret The oauth consumer secret supplied by the developer.
	 * @param accessToken The oauth access token returned by the getAccessToken call.
	 * @throws BlueviaClientException
	 */
	public BlueviaLocationClient(Context context, IConnector connector, String consumerKey, String consumerSecret, Token accessToken) throws BlueviaClientException {
		super(context, FEED_LOCATION_BASE_URI, connector, new Oauth(consumerKey, consumerSecret, accessToken), new BlueviaLocationParserFactory());
	}
	
	/**
	 * Retrieves the location of the terminal.
	 * @param acceptableAccuracy Accuracy, in meters, that is acceptable for a response (optional).
	 * 
	 * @return An entity object containing the Location Data.
	 * @throws BlueviaClientException 
	 */
	public LocationDataType getLocation(Integer acceptableAccuracy) throws BlueviaClientException {

		LocationDataType res = null;

		//Build the status feed uri
		String feedUri = null;
		HttpQueryParams httpQueryParameters = new HttpQueryParams();
		
		//Common mandatory params
		if (acceptableAccuracy != null)
			httpQueryParameters.addParameter(ACCEPTABLE_ACCURACY_KEY, String.valueOf(acceptableAccuracy));


		//Located party
		UserId locatedParty = new UserId(Type.ALIAS, ((Oauth)mAuthInfo).getOauthToken());
		String lp = locatedParty.getStringType() + ":" + locatedParty.getUserIdValue();
		httpQueryParameters.addParameter(LOCATED_PARTY_KEY, lp);
		
		//Version
		httpQueryParameters.addParameter(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);
		
		feedUri = composeUri();
		
		Log.d(TAG, "Starting retrieveEntity");
		Entity response;
		try {
			response = retrieveEntity(feedUri, httpQueryParameters);
		} catch (IOException e) {
			throw new BlueviaClientException("IO error retrieving entity",e,
					BlueviaClientException.INTERNAL_CLIENT_ERROR);
		}

		//Check if response is instance of LocationDataType or TerminalLocationListType
		if (response == null){
			throw new BlueviaClientException("Error during request. " +
					"Response received is null",
					BlueviaClientException.INTERNAL_CLIENT_ERROR);
		} else if (response instanceof LocationDataType){
			
			//Set the response
			res = (LocationDataType) response;
			if (!res.isValid()){
				throw new BlueviaClientException("The LocationData received is empty, incomplete or not valid",
						BlueviaClientException.INTERNAL_CLIENT_ERROR);
			}
			
		} else throw new BlueviaClientException("Error during request. " +
			"Response received is not a LocationDataType nor TerminalLocationListType but a "
				+ response.getClass().getName(), BlueviaClientException.INTERNAL_CLIENT_ERROR);

		return res;
	}
	
	///@endcond

}
