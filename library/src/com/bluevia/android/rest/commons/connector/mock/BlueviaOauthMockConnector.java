/// @cond private
package com.bluevia.android.rest.commons.connector.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.connector.http.HttpException;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.http.authentication.AuthenticationInfo;
import com.bluevia.android.rest.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;

import android.util.Log;

/**
 * Mock implementation of the communication with the gSDP server via HTTP for the Oauth APIs
 * This class simulates the behavior of the gSDP server when using Oauth API. It is called from MockConnector
 *
 * @see MockConnector
 * @author Telefonica R&D
 * 
 */
public class BlueviaOauthMockConnector extends MockConnector {

	private static final String TAG = "MockOauthConnector";

	private static final String REQUEST_TOKEN_FEED_PATH = "getRequestToken";
	private static final String ACCESS_TOKEN_FEED_PATH = "getAccessToken";	

	public static final String FEED_OAUTH_BASE_URI = "Oauth";

	/**
	 * Instantiates the MockConnector
	 *
	 * @param testType the code to indicate the MockConnector the behavior to
	 *            emulate
	 */
	public BlueviaOauthMockConnector(int testType){
		super(testType);
	}

	/* (non-Javadoc)
	 * @see com.bluevia.android.rest.client.IConnector#createEntity(java.lang.String, com.bluevia.android.rest.Entity, com.bluevia.android.rest.parser.BlueviaEntitySerializer, com.bluevia.android.rest.client.HttpQueryParams, java.util.HashMap, java.util.HashMap)
	 */
	protected InputStream createMockEntity(String feedUri, Entity entity, BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo,
			HttpQueryParams parameters, HashMap<String, String> requestHeaders, HashMap<String, String> responseHeaders, ArrayList<BlueviaPartBase> parts) 
	throws IOException, HttpException, ParseException {

		InputStream res = null;
		ByteArrayInputStream stringIs = null;

		Pattern pattern = Pattern.compile("^(.*)/" + FEED_OAUTH_BASE_URI + "/(.*)/?$");
		Matcher matcher = pattern.matcher(feedUri);
		matcher.find();
		String resource = matcher.group(2);

		String result = null;
		switch (mTestType) {
		case TEST_OK_ORDINAL:

			Log.d(TAG, "case test_ok");

			Log.d(TAG, "Resource: "+resource);
			if (resource.equals(REQUEST_TOKEN_FEED_PATH)){

				//Generate a random request token

				Random r = new Random();
				String token = Long.toString(Math.abs(r.nextLong()), 36);
				String secret = Long.toString(Math.abs(r.nextLong()), 36);

				result = "oauth_callback_confirmed=true&oauth_token_secret="+
				secret+"&oauth_token="+token;

				Log.d(TAG, "Generated request token :"+result);

			} else if (resource.equals(ACCESS_TOKEN_FEED_PATH)){

				//Generate a random access token
				Random r = new Random();
				String accessToken = Long.toString(Math.abs(r.nextLong()), 36);
				String secret = Long.toString(Math.abs(r.nextLong()), 36);

				result = "oauth_token=" + accessToken + "&oauth_token_secret=" + secret 
				+ "&oauth_callback_confirmed=true";


				Log.d(TAG, "Generated request token :"+result);
			} 
			if (result != null){
				stringIs = new ByteArrayInputStream(result.getBytes());
				res = stringIs;
			}
			break;

		case TEST_ERROR_IOEXCEPTION_ORDINAL:
			Log.d(TAG, "case error_ioexception");
			throw new IOException("Mock IO Exception launched");
		case TEST_ERROR_HTTPEXCEPTION_ORDINAL:
			Log.d(TAG, "case httpexception");
			throw new HttpException("Mock Http Exception launched", HttpException.INTERNAL_SERVER_ERROR, res);
		case TEST_UNAUTHORIZED_ORDINAL:
			throw new HttpException("Mock Unauthorized Exception launched", HttpException.UNAUTHORIZED, null);
		default:
			throw new IllegalArgumentException("Mode not supported");
		}
		return res;

	}

	@Override
	protected InputStream retrieveMockEntity(String feedUri,
			AuthenticationInfo authenticationInfo, HttpQueryParams parameters, HashMap<String, String> responseHeaders) throws IOException,
			HttpException {
		Log.i(TAG, "Retrieve Entity method not implemented for MockOauthConnector");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.connector.mock.MockConnector#close()
	 */
	public void close() {
		Log.d(TAG, "MockOauthConnector closed");
	}

}
///@endcond
