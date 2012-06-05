/**
 * \package com.bluevia.android.directory This package contains the classes in order to get Directory User Info using Bluevia API.
 * \package com.bluevia.android.directory.client This package contains REST client to get Directory User Info using Bluevia API.
 */
package com.bluevia.android.directory.client;

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
import com.bluevia.android.directory.data.AccessInfo;
import com.bluevia.android.directory.data.FilterUtils;
import com.bluevia.android.directory.data.PersonalInfo;
import com.bluevia.android.directory.data.Profile;
import com.bluevia.android.directory.data.TerminalInfo;
import com.bluevia.android.directory.data.UserInfo;
import com.bluevia.android.directory.parser.XmlDirectoryParser;

/**
 * 
 * Client interface for the REST binding of the Bluevia Directory Service.
 * 
 * @author Telefonica R&D
 *
 */
public abstract class BVDirectoryClient extends BVBaseClient {

	/**
	 * Data set key used to add the filters to UserInfo requets
	 */
	private static final String DATASETS_KEY = "dataSets";

	/**
	 * Filter Key used to add the filters on the query get request
	 */
	private static final String FILTER_KEY = "fields";

	private static final String USER_INFO_FEED_PATH = "/UserInfo";
	private static final String PERSONAL_INFO_FEED = USER_INFO_FEED_PATH + "/UserPersonalInfo";
	private static final String USER_PROFILE_FEED = USER_INFO_FEED_PATH + "/UserProfile";
	private static final String ACCESS_INFO_FEED = USER_INFO_FEED_PATH + "/UserAccessInfo";
	private static final String TERMINAL_INFO_FEED = USER_INFO_FEED_PATH + "/UserTerminalInfo";

	protected void init(){
		mEncoding = Encoding.APPLICATION_XML;
		mParser = new XmlDirectoryParser();
		mSerializer = null;		//No serializer
		mErrorParser = new BVRestXmlErrorParser();
	}

	/**
	 * Allows an application to get all the user context information.
	 * @param user the user whose information is going to be retrieved
	 * @param dataSet an array of the blocks to be retrieved
	 * 
	 * @return an entity containing all blocks of user context information.
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected UserInfo getUserInfo(UserId user, UserInfo.DataSet[] dataSet) throws BlueviaException, IOException {

		UserInfo qualifiedResponse = null;

		//UserId
		UserId userId = null;

		if (user == null)
			userId = new UserId(Type.ALIAS, ((IOAuth)mConnector).getOauthToken().getToken());
		else userId = user;

		if (dataSet != null && dataSet.length < 2)
			throw new BlueviaException("Bad request: data set must contain two elements at least.", BlueviaException.BAD_REQUEST_EXCEPTION);

		//Build the status feed uri
		String feedUri = "/" + userId.getStringType() + ":" +  userId.getUserIdValue() + USER_INFO_FEED_PATH;

		// Add version parameter
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		if (dataSet != null){
			parameters.put(DATASETS_KEY, FilterUtils.buildUserInfoFilter(dataSet));
		}

		Entity response = retrieve(feedUri, parameters);

		//Check if response is instance of Profile
		if (response == null){
			throw new BlueviaException("Error during request. Response received is null", BlueviaException.INTERNAL_CLIENT_ERROR);
		} else if (! (response instanceof UserInfo)){
			throw new BlueviaException("Error during request. Response received is not a " + UserInfo.class.getName()
					+ " but a "+response.getClass().getName(), BlueviaException.INTERNAL_CLIENT_ERROR);
		}

		//Set the response
		qualifiedResponse = (UserInfo) response;

		if (!qualifiedResponse.isValid()){
			throw new BlueviaException("The " + UserInfo.class.getName() + 
					" received is empty, incomplete or not valid", BlueviaException.INTERNAL_CLIENT_ERROR);
		}

		return qualifiedResponse;
	}

	/**
	 * Retrieves a subset of the User Profile resource block from the directory. 
	 * 
	 * @param user the user whose information is going to be retrieved
	 * @param fields a filter object to specify which information fields are required
	 * @return an entity object containing the user personal information
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected Profile getProfile(UserId user, Profile.Fields[] fields) throws BlueviaException, IOException {

		Profile qualifiedResponse = null;

		//UserId
		UserId userId = null;

		if (user == null)
			userId = new UserId(Type.ALIAS, ((IOAuth)mConnector).getOauthToken().getToken());
		else userId = user;

		//Build the status feed uri
		String feedUri = "/" + userId.getStringType() + ":" +  userId.getUserIdValue() + USER_PROFILE_FEED;

		// Add version parameter
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		if (fields != null){
			parameters.put(FILTER_KEY, "'" + FilterUtils.buildProfileFilter(fields) + "'");
		}

		Entity response = retrieve(feedUri, parameters);

		//Check if response is instance of Profile
		if (response == null){
			throw new BlueviaException("Error during request. Response received is null", BlueviaException.INTERNAL_CLIENT_ERROR);
		} else if (!(response instanceof Profile)){
			throw new BlueviaException("Error during request. Response received is not a " + Profile.class.getName()
					+ " but a " + response.getClass().getName(), BlueviaException.INTERNAL_CLIENT_ERROR);
		}
		//Set the response
		qualifiedResponse = (Profile) response;

		if (!qualifiedResponse.isValid()){
			throw new BlueviaException("The " + UserInfo.class.getName() 
					+ " received is empty, incomplete or not valid", BlueviaException.INTERNAL_CLIENT_ERROR);
		}

		return qualifiedResponse;
	}

	/**
	 * Retrieves a subset of the User Access Information resource block from the directory. 
	 * @param user the user whose information is going to be retrieved
	 * @param fields a filter object to specify which information fields are required
	 * @return an entity object containing the user access information
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected AccessInfo getAccessInfo(UserId user, AccessInfo.Fields[] fields) throws BlueviaException, IOException {

		AccessInfo qualifiedResponse = null;

		//UserId
		UserId userId = null;

		if (user == null)
			userId = new UserId(Type.ALIAS, ((IOAuth)mConnector).getOauthToken().getToken());
		else userId = user;

		//Build the status feed uri
		String feedAccessInfoUri = "/" + userId.getStringType() + ":" + userId.getUserIdValue() + ACCESS_INFO_FEED;

		// Add version parameter
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		if (fields != null){
			parameters.put(FILTER_KEY, "'" + FilterUtils.buildAccessInfoFilter(fields) + "'");
		}

		Entity response = retrieve(feedAccessInfoUri, parameters);

		//Check if response is instance of UserAccessInformation
		if (response == null){
			throw new BlueviaException("Error during request. Response received is null", BlueviaException.INTERNAL_CLIENT_ERROR);
		} else if (! (response instanceof AccessInfo)){
			throw new BlueviaException("Error during request. Response received is not a " + AccessInfo.class.getName()
					+ " but a " + response.getClass().getName(), BlueviaException.INTERNAL_CLIENT_ERROR);
		}

		//Set the response
		qualifiedResponse = (AccessInfo) response;

		if (!qualifiedResponse.isValid()){
			throw new BlueviaException("The " + AccessInfo.class.getName()
					+ " received is empty, incomplete or not valid", BlueviaException.INTERNAL_CLIENT_ERROR);
		}

		return qualifiedResponse;

	}

	/**
	 * Retrieves a subset of the User Terminal Information resource block from the directory. 
	 * 
	 * @param user the user whose information is going to be retrieved
	 * @param fields a filter object to specify which information fields are required
	 * @return an entity object containing the user terminal information
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected TerminalInfo getTerminalInfo(UserId user, TerminalInfo.Fields[] fields) throws BlueviaException, IOException {

		TerminalInfo qualifiedResponse = null;

		//UserId
		UserId userId = null;

		if (user == null)
			userId = new UserId(Type.ALIAS, ((IOAuth)mConnector).getOauthToken().getToken());
		else userId = user;

		//Build the status feed uri
		String feedUri = "/" + userId.getStringType() + ":" + userId.getUserIdValue() + TERMINAL_INFO_FEED;

		// Add version parameter
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		if (fields != null){
			parameters.put(FILTER_KEY, "'" + FilterUtils.buildTerminalInfoFilter(fields) + "'");
		}

		Entity response = retrieve(feedUri, parameters);

		//Check if response is instance of UserTerminalInformation
		if (response == null){
			throw new BlueviaException("Error during request. Response received is null", BlueviaException.INTERNAL_CLIENT_ERROR);
		} else if (! (response instanceof TerminalInfo)){
			throw new BlueviaException("Error during request. Response received is not a " + TerminalInfo.class.getName()
					+ " but a " + response.getClass().getName(), BlueviaException.INTERNAL_CLIENT_ERROR);
		}
		//Set the response
		qualifiedResponse = (TerminalInfo) response;

		if (!qualifiedResponse.isValid()){
			throw new BlueviaException("The " + TerminalInfo.class.getName()
					+ " received is empty, incomplete or not valid", BlueviaException.INTERNAL_CLIENT_ERROR);
		}

		return qualifiedResponse;
	}

	/**
	 * Retrieves a subset of the User Personal Information resource block from the directory.
	 * 
	 * @param user the user whose information is going to be retrieved
	 * @param filter a filter object to specify which information fields are required
	 * @return an entity object containing the user terminal information
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected PersonalInfo getPersonalInfo(UserId user, PersonalInfo.Fields[] fields) throws BlueviaException, IOException {

		PersonalInfo qualifiedResponse = null;

		//UserId
		UserId userId = null;

		if (user == null)
			userId = new UserId(Type.ALIAS, ((IOAuth)mConnector).getOauthToken().getToken());
		else userId = user;

		//Build the status feed uri
		String feedUri = "/" + userId.getStringType() + ":" + userId.getUserIdValue() + PERSONAL_INFO_FEED;

		// Add version parameter
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		if (fields != null){
			parameters.put(FILTER_KEY, "'" + FilterUtils.buildPersonalInfoFilter(fields) + "'");
		}

		Entity response = retrieve(feedUri, parameters);

		//Check if response is instance of UserTerminalInformation
		if (response == null){
			throw new BlueviaException("Error during request. Response received is null", BlueviaException.INTERNAL_CLIENT_ERROR);
		} else if (! (response instanceof PersonalInfo)){
			throw new BlueviaException("Error during request. Response received is not a " + PersonalInfo.class.getName()
					+ " but a " + response.getClass().getName(), BlueviaException.INTERNAL_CLIENT_ERROR);
		}
		//Set the response
		qualifiedResponse = (PersonalInfo) response;

		if (!qualifiedResponse.isValid()){
			throw new BlueviaException("The " + PersonalInfo.class.getName()
					+ " received is empty, incomplete or not valid", BlueviaException.INTERNAL_CLIENT_ERROR);
		}

		return qualifiedResponse;
	}

}
