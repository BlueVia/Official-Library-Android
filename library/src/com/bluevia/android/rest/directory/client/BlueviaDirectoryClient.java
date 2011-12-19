/**
 * \package com.bluevia.android.rest.directory This package contains the classes in order to get Directory User Info using Bluevia API.
 * \package com.bluevia.android.rest.directory.client This package contains REST client to get Directory User Info using Bluevia API.
 */
package com.bluevia.android.rest.directory.client;

import java.io.IOException;

import android.content.Context;
import android.util.Log;

import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.client.AbstractRestClient;
import com.bluevia.android.rest.commons.connector.IConnector;
import com.bluevia.android.rest.commons.connector.http.HttpConnector;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.mock.BlueviaDirectoryMockConnector;
import com.bluevia.android.rest.commons.data.UserId;
import com.bluevia.android.rest.commons.data.UserId.Type;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.directory.data.UserAccessInfo;
import com.bluevia.android.rest.directory.data.UserInfo;
import com.bluevia.android.rest.directory.data.UserProfile;
import com.bluevia.android.rest.directory.data.UserTerminalInfo;
import com.bluevia.android.rest.directory.data.filter.UserAccessInfoFilter;
import com.bluevia.android.rest.directory.data.filter.UserInfoFilter;
import com.bluevia.android.rest.directory.data.filter.UserProfileFilter;
import com.bluevia.android.rest.directory.data.filter.UserTerminalInfoFilter;
import com.bluevia.android.rest.directory.parser.xml.XmlDirectoryParserFactory;
import com.bluevia.android.rest.oauth.Oauth;
import com.bluevia.android.rest.oauth.data.Token;

/**
 * Client interface for the REST binding of the Bluevia Directory Service.
 *
 * @author Telefonica R&D
 * 
 */
public class BlueviaDirectoryClient extends AbstractRestClient {


	/**
	 * Data set key used to add the filters to UserInfo requets
	 */
	public static final String DATASETS_KEY = "dataSets";
	
	/**
	 * Filter Key used to add the filters on the query get request
	 */
	public static final String FILTER_KEY = "fields";
	

	///@cond private

	private static final String TAG = "DirectoryClient";

	private static final String FEED_DIRECTORY_BASE_URI = "REST/Directory";
	private static final String FEED_DIRECTORY_SANDBOX_BASE_URI = "REST/Directory_Sandbox";

	private static final String URI_PATH_SEPARATOR = "/";
	private static final String USER_INFO_FEED_PATH = "/UserInfo";
	private static final String USER_PROFILE_FEED_PATH = USER_INFO_FEED_PATH + "/UserProfile";
	private static final String USER_ACCESS_INFO_FEED_PATH = USER_INFO_FEED_PATH + "/UserAccessInfo";
	private static final String USER_TERMINAL_INFO_FEED_PATH = USER_INFO_FEED_PATH + "/UserTerminalInfo";

	///@endcond


	/**
	 * Creates a DirectoryClient object to be able to retrieve directory information from the gSDP
	 * @param context the Android context of the application.
	 * @param mode the communication mechanism to communicate with the gSDP.
	 * @param consumerKey The oauth consumer key supplied by the developer.
	 * @param consumerSecret The oauth consumer secret supplied by the developer.
	 * @param accessToken The oauth access token returned by the getAccessToken call.
	 * 
	 * @throws BlueviaClientException if an exception occurs during the request.
	 */
	public BlueviaDirectoryClient(Context context, AbstractRestClient.Mode mode, String consumerKey, String consumerSecret, Token accessToken) throws BlueviaClientException {
		super(context, (mode != Mode.HTTP_SANDBOX) ? FEED_DIRECTORY_BASE_URI : FEED_DIRECTORY_SANDBOX_BASE_URI, 
				(mode == Mode.HTTP || mode == Mode.HTTP_SANDBOX) ? new HttpConnector() : new BlueviaDirectoryMockConnector(mode.ordinal()), 
						new Oauth(consumerKey, consumerSecret, accessToken), new XmlDirectoryParserFactory());
	}
	
	/**
	 * TODO
	 * 
	 * @return
	 * @throws BlueviaClientException
	 */
	public UserInfo getUserInfo() throws BlueviaClientException {
		return getUserInfo(null);
	}
	
	/**
	 * TODO
	 * @param filter
	 * 
	 * @return
	 * @throws BlueviaClientException
	 */
	public UserInfo getUserInfo(UserInfoFilter filter) throws BlueviaClientException {
		
		UserInfo qualifiedResponse = null;
		
		UserId userId = new UserId(Type.ALIAS, ((Oauth)mAuthInfo).getOauthToken());
		
		if (filter != null && !filter.isValid())
			throw new BlueviaClientException("UserInfoFilter must contain two elements at least.", BlueviaClientException.BAD_REQUEST_EXCEPTION);


		Log.d(TAG, "UserId: "+userId.getUserIdValue());


		//Build the status feed uri
		String feedUri = mBaseUri + URI_PATH_SEPARATOR + userId.getStringType() + ":" +  userId.getUserIdValue() + USER_INFO_FEED_PATH;
		try {

			// Add version parameter
			HttpQueryParams httpQueryParameters = new HttpQueryParams();
			httpQueryParameters.addParameter(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

			if (filter != null && !filter.getActivatedFilters().trim().equals("")){
				httpQueryParameters.addParameter(DATASETS_KEY, filter.getActivatedFilters().trim());
			}
			Entity response = retrieveEntity (feedUri, httpQueryParameters);

			//Check if response is instance of UserProfile
			if (response == null){
				throw new BlueviaClientException("Error during request. " +
						"Response received is null",
						BlueviaClientException.INTERNAL_CLIENT_ERROR);
			} else if (! (response instanceof UserInfo)){
				throw new BlueviaClientException("Error during request. " +
						"Response received is not a UserInfo but a "+response.getClass().getName(),
						BlueviaClientException.INTERNAL_CLIENT_ERROR);
			}
			//Set the response
			qualifiedResponse = (UserInfo) response;

			Log.d(TAG,"QualifiedResponse: "+qualifiedResponse.toString());


			if (!qualifiedResponse.isValid()){
				throw new BlueviaClientException("The UserInfo received is empty, incomplete or not valid",
						BlueviaClientException.INTERNAL_CLIENT_ERROR);
			}

		} catch (IOException iox) {
			throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);
		}

		return qualifiedResponse;
	}


	/**
	 * Retrieves the whole User Profile resource block from the directory. Applications
	 * will only be able to retrieve directory information on themselves.
	 * @return an entity object containing the user profile information
	 * @throws BlueviaClientException
	 */
	public UserProfile getUserProfile () throws BlueviaClientException {
		return getUserProfile(null);
	}

	/**
	 * Retrieves a subset of the User Profile resource block from the directory. Applications
	 * will only be able to retrieve directory information on themselves.
	 * @param filter a filter object to specify which information fields are required
	 * @return an entity object containing the user personal information
	 * @throws BlueviaClientException
	 */
	public UserProfile getUserProfile (UserProfileFilter filter) throws BlueviaClientException {

		UserProfile qualifiedResponse = null;

		UserId userId = new UserId(Type.ALIAS, ((Oauth)mAuthInfo).getOauthToken());


		Log.d(TAG, "UserId: "+userId.getUserIdValue());


		//Build the status feed uri
		String feedUri = mBaseUri + URI_PATH_SEPARATOR + userId.getStringType() + ":" +  userId.getUserIdValue() + USER_PROFILE_FEED_PATH;

		try {

			// Add version parameter
			HttpQueryParams httpQueryParameters = new HttpQueryParams();
			httpQueryParameters.addParameter(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

			if (filter != null && !filter.getActivatedFilters().trim().equals("")){
				httpQueryParameters.addParameter(FILTER_KEY, "'"+filter.getActivatedFilters().trim()+"'");
			}
			Entity response = retrieveEntity (feedUri, httpQueryParameters);

			//Check if response is instance of UserProfile
			if (response == null){
				throw new BlueviaClientException("Error during request. " +
						"Response received is null",
						BlueviaClientException.INTERNAL_CLIENT_ERROR);
			} else if (! (response instanceof UserProfile)){
				throw new BlueviaClientException("Error during request. " +
						"Response received is not a UserProfile but a "+response.getClass().getName(),
						BlueviaClientException.INTERNAL_CLIENT_ERROR);
			}
			//Set the response
			qualifiedResponse = (UserProfile) response;

			Log.d(TAG,"QualifiedResponse: "+qualifiedResponse.toString());


			if (!qualifiedResponse.isValid()){
				throw new BlueviaClientException("The UserProfile received is empty, incomplete or not valid",
						BlueviaClientException.INTERNAL_CLIENT_ERROR);
			}

		} catch (IOException iox) {
			throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);
		}

		return qualifiedResponse;

	}

	/**
	 * Retrieves the whole User Access Information resource block from the directory. Applications
	 * will only be able to retrieve directory information on themselves.
	 * @return an entity object containing the user access information
	 * @throws BlueviaClientException
	 */
	public UserAccessInfo getUserAccessInformation () throws BlueviaClientException {
		return getUserAccessInformation(null);
	}

	/**
	 * Retrieves a subset of the User Access Information resource block from the directory. Applications
	 * will only be able to retrieve directory information on themselves.
	 * @param filter a filter object to specify which information fields are required
	 * @return an entity object containing the user access information
	 * @throws BlueviaClientException
	 */
	public UserAccessInfo getUserAccessInformation (UserAccessInfoFilter filter) throws BlueviaClientException {

		UserAccessInfo qualifiedResponse = null;

		UserId userId = new UserId(Type.ALIAS, ((Oauth)mAuthInfo).getOauthToken());


		Log.d(TAG, "UserId: "+userId.getUserIdValue());


		//Build the status feed uri
		String feedAccessInfoUri = mBaseUri + URI_PATH_SEPARATOR + userId.getStringType() + ":" + userId.getUserIdValue() + USER_ACCESS_INFO_FEED_PATH;

		try {

			// Add version parameter
			HttpQueryParams httpQueryParameters = new HttpQueryParams();
			httpQueryParameters.addParameter(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

			if (filter != null && !filter.getActivatedFilters().trim().equals("")){
				httpQueryParameters.addParameter(FILTER_KEY, "'"+filter.getActivatedFilters().trim()+"'");
			}
			Entity response = retrieveEntity (feedAccessInfoUri, httpQueryParameters);

			//Check if response is instance of UserAccessInformation
			if (response == null){
				throw new BlueviaClientException("Error during request. " +
						"Response received is null",
						BlueviaClientException.INTERNAL_CLIENT_ERROR);
			} else if (! (response instanceof UserAccessInfo)){
				throw new BlueviaClientException("Error during request. " +
						"Response received is not a UserAccessInformation but a "+response.getClass().getName(),
						BlueviaClientException.INTERNAL_CLIENT_ERROR);
			}
			//Set the response
			qualifiedResponse = (UserAccessInfo) response;

			Log.d(TAG,"QualifiedResponse: "+qualifiedResponse.toString());



			if (!qualifiedResponse.isValid()){
				throw new BlueviaClientException("The UserAccessInformation received is empty, incomplete or not valid",
						BlueviaClientException.INTERNAL_CLIENT_ERROR);
			}

		} catch (IOException iox) {
			Log.e(TAG, "Error during IO");
			throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);
		}

		return qualifiedResponse;

	}

	/**
	 * Retrieves the whole User Terminal Information resource block from the directory. Applications
	 * will only be able to retrieve directory information on themselves.
	 * @return an entity object containing the user terminal information
	 * @throws BlueviaClientException
	 */
	public UserTerminalInfo getUserTerminalInformation () throws BlueviaClientException {
		return getUserTerminalInformation(null);
	}

	/**
	 * Retrieves a subset of the User Terminal Information resource block from the directory. Applications
	 * will only be able to retrieve directory information on themselves.
	 * @param filter a filter object to specify which information fields are required
	 * @return an entity object containing the user terminal information
	 * @throws BlueviaClientException
	 */
	public UserTerminalInfo getUserTerminalInformation (UserTerminalInfoFilter filter) throws BlueviaClientException {


		UserTerminalInfo qualifiedResponse = null;

		UserId userId = new UserId(Type.ALIAS, ((Oauth)mAuthInfo).getOauthToken());


		Log.d(TAG, "UserId: "+userId.getUserIdValue());


		//Build the status feed uri
		String feedUri = mBaseUri + URI_PATH_SEPARATOR + userId.getStringType() + ":" + userId.getUserIdValue() + USER_TERMINAL_INFO_FEED_PATH;

		try {

			// Add version parameter
			HttpQueryParams httpQueryParameters = new HttpQueryParams();
			httpQueryParameters.addParameter(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

			if (filter != null && !filter.getActivatedFilters().trim().equals("")){
				httpQueryParameters.addParameter(FILTER_KEY, "'"+filter.getActivatedFilters().trim()+"'");
			}
			Entity response = retrieveEntity (feedUri, httpQueryParameters);

			//Check if response is instance of UserTerminalInformation
			if (response == null){
				throw new BlueviaClientException("Error during request. " +
						"Response received is null",
						BlueviaClientException.INTERNAL_CLIENT_ERROR);
			} else if (! (response instanceof UserTerminalInfo)){
				throw new BlueviaClientException("Error during request. " +
						"Response received is not a UserTerminalInformation but a "+response.getClass().getName(),
						BlueviaClientException.INTERNAL_CLIENT_ERROR);
			}
			//Set the response
			qualifiedResponse = (UserTerminalInfo) response;

			Log.d(TAG,"QualifiedResponse: "+qualifiedResponse.toString());


			if (!qualifiedResponse.isValid()){
				throw new BlueviaClientException("The UserTerminalInformation received is empty, incomplete or not valid",
						BlueviaClientException.INTERNAL_CLIENT_ERROR);
			}

		} catch (IOException iox) {
			throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);
		}

		return qualifiedResponse;
	}

	///@cond private

	/**
	 * Creates a DirectoryClient with a specific IConnector object
	 *      
	 * @param context the Android context of the application.
	 * @param connector the connector for communication mechanism
	 * @param consumerKey The oauth consumer key supplied by the developer.
	 * @param consumerSecret The oauth consumer secret supplied by the developer.
	 * @param accessToken The oauth access token returned by the getAccessToken call.
	 * 
	 * @throws BlueviaClientException if an exception occurs during the request.
	 */
	protected BlueviaDirectoryClient(Context context, IConnector connector, String consumerKey, String consumerSecret, Token accessToken) throws BlueviaClientException {
		super(context, FEED_DIRECTORY_BASE_URI, connector, new Oauth(consumerKey, consumerSecret, accessToken), new XmlDirectoryParserFactory());
	}
	///@endcond

}
