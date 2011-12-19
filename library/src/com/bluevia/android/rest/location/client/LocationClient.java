/**
 * \package com.bluevia.android.rest.location This package contains the classes in order to get Location Info using Bluevia API.
 * \package com.bluevia.android.rest.location.client This package contains REST client to get Location Info using Bluevia API.
 */
package com.bluevia.android.rest.location.client;

import java.io.IOException;

import android.content.Context;
import android.util.Log;

import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.client.AbstractRestClient;
import com.bluevia.android.rest.commons.connector.IConnector;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.http.authentication.AuthenticationInfo;
import com.bluevia.android.rest.commons.data.UserId;
import com.bluevia.android.rest.commons.data.UserId.Type;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.location.data.LocationDataType;
import com.bluevia.android.rest.oauth.Oauth;

/**
 * @class LocationClient
 * Client interface for the REST binding of the Bluevia Location Service.
 *
 * @author Telefonica R&D
 * 
 */
public abstract class LocationClient extends AbstractRestClient {

	///@cond private
	
	private static final String TAG = "LocationClient";

	protected static final String URI_PATH_SEPARATOR = "/";

	protected static final String LOCATED_PARTY_KEY = "locatedParty";
	protected static final String ACCEPTABLE_ACCURACY_KEY = "acceptableAccuracy";

	/**
	 * Creates a LocationClient object to retrieve location information from the gSDP.
	 * 
	 * @param context the Android context of the application.
     * @param uri the base uri of the client
     * @param connector the connector for communication mechanism.
     * @param auth the parameters with the security info supplied by the developer.
     * @param factory the factory that parse and serialize entities to stream and vice-versa.
	 * @throws BlueviaClientException
	 */
	public LocationClient(Context context, String uri, IConnector connector, AuthenticationInfo auth, BlueviaEntityParserFactory factory) throws BlueviaClientException {
		super(context, uri, connector, auth, factory);
	}

	
	
	///@endcond
}
