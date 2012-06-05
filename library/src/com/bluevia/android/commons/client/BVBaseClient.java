package com.bluevia.android.commons.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import android.util.Log;

import com.bluevia.android.commons.Encoding;
import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.GsdpConstants;
import com.bluevia.android.commons.connector.GenericResponse;
import com.bluevia.android.commons.connector.IConnector;
import com.bluevia.android.commons.connector.http.oauth.OAuthToken;
import com.bluevia.android.commons.connector.http.oauth.OauthHttpConnector;
import com.bluevia.android.commons.data.StringEntity;
import com.bluevia.android.commons.exception.BlueviaException;
import com.bluevia.android.commons.exception.ConnectorException;
import com.bluevia.android.commons.parser.IParser;
import com.bluevia.android.commons.parser.ISerializer;
import com.bluevia.android.commons.parser.ParseException;
import com.bluevia.android.commons.parser.SerializeException;


/**
 * Abstract representation of an HTTP REST Client for the Bluevia API
 * 
 * @author Telefonica R&D
 *
 */
public abstract class BVBaseClient {

	private static final String TAG = "BVBaseClient";

	/**
	 * The possible values to determinate the instantiated connector for the client.
	 * 
	 * Available values may be
	 * <ul>
	 *   <li>LIVE: In the Live environment your application uses the real network, which means 
	 * 		that you will be able to send real transactions to real Movistar, 
	 * 		O2 and Vivo customers in the applicable country.</li>
	 *   <li>TEST: The Test mode behave exactly like the Live mode, but the API calls are free of chargue, using a credits system. 
	 * 		You are required to have a Movistar, O2 or Vivo mobile number to get this monthly credits.</li>
	 *   <li>SANDBOX: The Sandbox environment offers you the exact same experience as the Live environment except 
	 * 		that no traffic is generated on the live network, meaning you can experiment and play until your heart’s content.</li>
	 * </ul>
	 * 
	 */
	public static enum Mode {LIVE, TEST, SANDBOX}
	
	/**
	 * The working mode for the client
	 */
	protected Mode mMode;
	
	/**
	 * The Connector to communicate and transmit the streams via REST
	 */
	protected IConnector mConnector;

	/**
	 * The parser to parse the responses from the connector
	 */
	protected IParser mParser;

	/**
	 * The serializer to serialize the requests to the connector
	 */
	protected ISerializer mSerializer;

	/**
	 * The error parser to parse error responses from the connector
	 */
	protected IParser mErrorParser;

	/**
	 * The default enconding for requests used by the client
	 */
	protected Encoding mEncoding;
	
	/**
	 * The base uri for the client
	 */
	protected String mBaseUri;
	
	/** 
	 * The Constant APPLICATION_XML.
	 */
	private static final String APPLICATION_XML_CONTENT_TYPE = "application/xml;charset=utf-8";

	/** 
	 * The Constant APPLICATION_JSON. 
	 */
	private static final String APPLICATION_JSON_CONTENT_TYPE = "application/json;charset=utf-8";

	/** 
	 * The Constant APPLICATION_DEFAULT_CONTENT_TYPE. 
	 */
	private static final String APPLICATION_DEFAULT_CONTENT_TYPE = "text/plain";

	/** 
	 *  The Constant APPLICATION_URL_ENCODED. 
	 */
	private static final String APPLICATION_URL_ENCODED_CONTENT_TYPE = "application/x-www-form-urlencoded";

	/**
	 * Common function to initialize trusted and untrusted common attributes:
	 * <ul>
	 * 	<li>Base URI</li>
	 *  <li>Mode</li>
	 * </ul>
	 * 
	 * @param mode
	 * @throws BlueviaException
	 */
	protected void initCommon(Mode mode) throws BlueviaException{
		mBaseUri = GsdpConstants.GSDP_BASE_URL;
		
		if (mode == null)
			throw new BlueviaException("Invalid mode", BlueviaException.INVALID_MODE_EXCEPTION);
		mMode = mode;
	}
	
	/**
	 * Function to initialize trusted clients
	 * 
	 * @param mode
	 * @param consumerKey
	 * @param consumerSecret
	 * @throws BlueviaException
	 */
	protected void initTrusted(Mode mode, String consumerKey, String consumerSecret) throws BlueviaException{
		initCommon(mode);
		
		mConnector = new OauthHttpConnector(new OAuthToken(consumerKey, consumerSecret));
	}
	
	/**
	 * Function to initialize untrusted clients
	 * 
	 * @param mode
	 * @param consumerKey
	 * @param consumerSecret
	 * @param token
	 * @param tokenSecret
	 * @throws BlueviaException
	 */
	protected void initUntrusted(Mode mode, String consumerKey, String consumerSecret, String token, String tokenSecret) throws BlueviaException{
		initCommon(mode);
		
		mConnector = new OauthHttpConnector(new OAuthToken(consumerKey, consumerSecret), new OAuthToken(token, tokenSecret));
	}
	
	/**
	 * Builds the uri depending on the mode used
	 * 
	 * @param mode the mode
	 * @param uri the uri path from the API
	 * @return
	 */
	protected String buildUri(Mode mode, String uri){
		switch (mode){
		case LIVE:
		case TEST:
			return uri;
		case SANDBOX:
			return uri + "_Sandbox";
		}
		return null;
	}

	/**
	 * Creates a request using REST to the server in order to create an entity
	 *
	 * @param feedUri the REST URI of the entity to create
	 * @param entity the entity to create using REST into the server
	 * @param parameters Parameters to be added to the request
	 * @param headers headers of the request
	 * @return the response entity object
	 * @throws BlueviaException 
	 * @throws IOException 
	 */
	protected Entity create(String feedUri, Entity entity, HashMap<String, String> parameters, HashMap<String, String> headers) throws BlueviaException, IOException {

		GenericResponse response = null;
		InputStream is = null;

		try {

			byte[] bytes = serializeEntity(entity);

			String uri = mBaseUri + feedUri;
			
			if (headers == null)
				headers = new HashMap<String, String>();
			headers.put(GsdpConstants.HEADER_CONTENT_TYPE, getEncoding(mEncoding));
			
			response = mConnector.create(uri, parameters, bytes, headers);

			is = response.getAdditionalData().getBody();
			return parseEntity(is);

		} catch (ConnectorException e){
			if (e.getAdditionalData().getBody() == null)
				throw e;
			else {
				is = e.getAdditionalData().getBody();
				String error = parseError(is);
				String completeError = e.getMessage();
				if (error != null)
					completeError += ": " + error;
				throw new ConnectorException(completeError, e.getCode());
			}
		} finally {
			closeStream(is);   	
		}
	}


	/**
	 * Creates a request using REST to the server in order to retrieve an entity
	 * from the server
	 * 
	 * @param feedUri the REST URI of the entity to get
	 * @return the response entity object
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected Entity retrieve(String feedUri) throws BlueviaException, IOException {
		return retrieve(feedUri, null);
	}

	/**
	 * Creates a request using REST to the server in order to retrieve an entity
	 * from the server
	 * 
	 * @param feedUri the REST URI of the entity to get
	 * @param parameters the parameters to be added to the URI
	 * @return the response entity object
	 * @throws BlueviaException 
	 * @throws IOException 
	 */
	protected Entity retrieve(String feedUri, HashMap<String, String> parameters) throws BlueviaException, IOException {
		return retrieve(feedUri, parameters, null);
	}
	
	/**
	 * Creates a request using REST to the server in order to retrieve an entity
	 * from the server
	 * 
	 * @param feedUri the REST URI of the entity to get
	 * @param parameters the parameters to be added to the URI
	 * @param headers headers of the request
	 * @return the response entity object
	 * @throws BlueviaException 
	 * @throws IOException 
	 */
	protected Entity retrieve(String feedUri, HashMap<String, String> parameters, HashMap<String, String> headers) throws BlueviaException, IOException {

		GenericResponse response = null;
		InputStream is = null;

		try {

			String uri = mBaseUri + feedUri;
			
			if (headers == null)
				headers = new HashMap<String, String>();
			headers.put(GsdpConstants.HEADER_CONTENT_TYPE, getEncoding(mEncoding));
			
			response = mConnector.retrieve(uri, parameters);

			is = response.getAdditionalData().getBody();
			return parseEntity(is);
		} catch (ConnectorException e){
			if (e.getAdditionalData().getBody() == null)
				throw e;
			else {
				is = e.getAdditionalData().getBody();
				String error = parseError(is);
				String completeError = e.getMessage();
				if (error != null)
					completeError += ": " + error;
				throw new ConnectorException(completeError, e.getCode());
			}
		} finally {
			closeStream(is);
		}
	}
	
	/**
	 * Creates a request using REST to the server in order to create an entity
	 *
	 * @param feedUri the REST URI of the entity to create
	 * @param entity the entity to create using REST into the server
	 * @param parameters Parameters to be added to the request
	 * @param headers headers of the request
	 * @return the response entity object
	 * @throws BlueviaException 
	 * @throws IOException 
	 */
	protected Entity update(String feedUri, Entity entity, HashMap<String, String> parameters, HashMap<String, String> headers) throws BlueviaException, IOException {

		GenericResponse response = null;
		InputStream is = null;

		try {

			byte[] bytes = serializeEntity(entity);
			
			String uri = mBaseUri + feedUri;
			
			if (headers == null)
				headers = new HashMap<String, String>();
			headers.put(GsdpConstants.HEADER_CONTENT_TYPE, getEncoding(mEncoding));

			response = mConnector.update(uri, parameters, bytes, headers);

			is = response.getAdditionalData().getBody();
			return parseEntity(is);

		} catch (ConnectorException e){
			if (e.getAdditionalData().getBody() == null)
				throw e;
			else {
				is = e.getAdditionalData().getBody();
				String error = parseError(is);
				String completeError = e.getMessage();
				if (error != null)
					completeError += ": " + error;
				throw new ConnectorException(completeError, e.getCode());
			}
		} finally {
			closeStream(is);
		}
	}
	
	
	/**
	 * Creates a request using REST to the server in order to retrieve an entity
	 * from the server
	 * 
	 * @param feedUri the REST URI of the entity to get
	 * @return the response entity object
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected Entity delete(String feedUri) throws BlueviaException, IOException {
		return delete(feedUri, null);
	}

	/**
	 * Creates a request using REST to the server in order to retrieve an entity
	 * from the server
	 * 
	 * @param feedUri the REST URI of the entity to get
	 * @param parameters the parameters to be added to the URI
	 * @return the response entity object
	 * @throws BlueviaException 
	 * @throws IOException 
	 */
	protected Entity delete(String feedUri, HashMap<String, String> parameters) throws BlueviaException, IOException {
		return delete(feedUri, parameters, null);
	}
	
	/**
	 * Creates a request using REST to the server in order to retrieve an entity
	 * from the server
	 * 
	 * @param feedUri the REST URI of the entity to get
	 * @param parameters the parameters to be added to the URI
	 * @param headers headers of the request
	 * @return the response entity object
	 * @throws BlueviaException 
	 * @throws IOException 
	 */
	protected Entity delete(String feedUri, HashMap<String, String> parameters, HashMap<String, String> headers) throws BlueviaException, IOException {

		GenericResponse response = null;
		InputStream is = null;

		try {

			String uri = mBaseUri + feedUri;
			
			if (headers == null)
				headers = new HashMap<String, String>();
			headers.put(GsdpConstants.HEADER_CONTENT_TYPE, getEncoding(mEncoding));
			
			response = mConnector.delete(uri, parameters, headers);

			is = response.getAdditionalData().getBody();
			return parseEntity(is);
		} catch (ConnectorException e){
			if (e.getAdditionalData().getBody() == null)
				throw e;
			else {
				is = e.getAdditionalData().getBody();
				String error = parseError(is);
				String completeError = e.getMessage();
				if (error != null)
					completeError += ": " + error;
				throw new ConnectorException(completeError, e.getCode());
			}
		} finally {
			closeStream(is);
		}
	}
	
	/**
	 * Closes the client and frees all the resources it might have used.
	 */
	public void close() {
		mConnector.close();
	}
	
	/**
	 * Serializes an Entity
	 * 
	 * @param entity the entity to serialize
	 * @return the contents of the Entity in a byte array
	 * @throws SerializeException
	 */
	protected byte[] serializeEntity(Entity entity) throws SerializeException {
		if (mSerializer == null)
			return null;

		ByteArrayOutputStream out = null;
		
		try {
			
			out = mSerializer.serialize(entity);

			byte[] entryBytes = out.toByteArray();
			out.close();

			if (entryBytes != null && Log.isLoggable(TAG, Log.DEBUG)) {
				try {
					Log.d(TAG, "Serialized entry: " + new String(entryBytes, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// should not happen
					throw new IllegalStateException("UTF-8 should be supported!", e);
				}
			}

			return entryBytes;
		}catch (IOException e) {
			Log.i(TAG, e.getMessage(), e);
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					Log.i(TAG, e.getMessage(), e);
				}
		}
		return null;
	}

	/**
	 * Parses the response into an Entity
	 * 
	 * @param is the InputStream containing the stream with the entity information
	 * @return the Entity object
	 * @throws ParseException when an error occurs converting the stream into an object
	 */
	protected Entity parseEntity(InputStream is) throws ParseException {
		if (mParser == null)
			return null;

		try {
			return mParser.parse(is);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					Log.i(TAG, e.getMessage(), e);
				}      	
		}
	}

	/**
	 * Parses the error message
	 * 
	 * @param is the InputStream containing the stream with the entity information
	 * @return the Entity object
	 * @throws ParseException when an error occurs converting the stream into an object
	 */
	protected String parseError(InputStream is) throws ParseException {
		Log.d(TAG, "Start parseError");
		if (mErrorParser == null)
			return null;

		try {
			Entity entity = mErrorParser.parse(is);
			if (entity instanceof StringEntity){
				StringEntity error = (StringEntity) entity;
				return error.getText();
			}
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					Log.i(TAG, e.getMessage(), e);
				}      	
		}
		return null;
	}
	
	/**
	 * Closes an input stream
	 * 
	 * @param is
	 */
	protected void closeStream(InputStream is){
		if (is != null)
			try {
				is.close();
			} catch (IOException e) {
				Log.i(TAG, e.getMessage(), e);
			}   
	}
	
	/**
	 * Translates the Encoding enum into the string representation for the request
	 * 
	 * @param encoding
	 * @return the string representation of the Encoding enum
	 */
	protected String getEncoding(Encoding encoding){
		switch (encoding) {
		case APPLICATION_XML:
			return APPLICATION_XML_CONTENT_TYPE;
		case APPLICATION_JSON:
			return APPLICATION_JSON_CONTENT_TYPE;
		case APPLICATION_URL_ENCODED:
			return APPLICATION_URL_ENCODED_CONTENT_TYPE;
		default:
			return APPLICATION_DEFAULT_CONTENT_TYPE;
		}
	}

}
