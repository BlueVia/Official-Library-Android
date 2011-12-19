/// @cond private
package com.bluevia.android.rest.commons.connector.mock;


import android.graphics.Bitmap;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.mail.util.ByteArrayDataSource;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.client.AbstractRestClient;
import com.bluevia.android.rest.commons.client.AbstractRestClient.Mode;
import com.bluevia.android.rest.commons.connector.IConnector;
import com.bluevia.android.rest.commons.connector.http.HttpException;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.http.authentication.AuthenticationInfo;
import com.bluevia.android.rest.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;

/**
 * Mock implementation of the communication with the gSDP server via HTTP
 * MockClient can work emulating different behaviors
 * <ul>
 *   <li>TEST_OK: Emulates the connection where ok and the data received is correct</li>
 *   <li>TEST_ERROR_IOEXCEPTION: Emulates an IOException that occurs during the communication</li>
 *   <li>TEST_ERROR_HTTPEXCEPTION: Emulates a HttpException that occurs during the communication</li>
 * 	 <li>TEST_UNAUTHORIZED: Emulates a 401 error - Unauthorized</li>
 * </ul>
 * @see IConnector
 * @author Telefonica R&D
 * 
 *
 */
public abstract class MockConnector implements IConnector {

	private static final String TAG = "MockConnector";
	protected static final String GSDP_BASE_URL = "https://api.bluevia.com/services/";

	protected int mTestType;

	public static final int TEST_OK_ORDINAL = 2; 					//Mode.TEST_OK.ordinal();
	public static final int TEST_UNAUTHORIZED_ORDINAL = 3; 			//Mode.TEST_UNAUTHORIZED.ordinal();
	public static final int TEST_ERROR_IOEXCEPTION_ORDINAL = 4; 	//Mode.TEST_ERROR_IOEXCEPTION.ordinal();
	public static final int TEST_ERROR_HTTPEXCEPTION_ORDINAL = 5; 	//Mode.TEST_ERROR_HTTPEXCEPTION.ordinal();

	/**
	 * Instantiates the MockConnector
	 * 
	 * @param testType the code to indicate the MockConnector the behavior to emulate
	 */
	public MockConnector(int testType) {
		if (testType == AbstractRestClient.Mode.HTTP.ordinal())
			throw new IllegalArgumentException("HTTP mode not supported for MockConnector");

		mTestType = testType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.connector.IConnector#createEntity(java.lang.String, com.bluevia.android.rest.commons.Entity, com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer, com.bluevia.android.rest.oauth.Oauth)
	 */
	public InputStream createEntity(String feedUri, Entity entity, BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo)
	throws IOException, HttpException, ParseException {
		return createEntity(feedUri, entity, serializer, authenticationInfo, null, null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.connector.IConnector#createEntity(java.lang.String, com.bluevia.android.rest.commons.Entity, com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer, com.bluevia.android.rest.oauth.Oauth, java.util.HashMap)
	 */
	public InputStream createEntity(String feedUri, Entity entity, BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo,
			HttpQueryParams parameters, HashMap<String, String> responseHeaders) throws IOException, HttpException,
			ParseException {
		return createEntity(feedUri, entity, serializer, authenticationInfo, parameters, null, responseHeaders);
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.connector.IConnector#createEntity(java.lang.String, com.bluevia.android.rest.commons.Entity, com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer, com.bluevia.android.rest.oauth.Oauth, java.util.HashMap, java.util.ArrayList)
	 */
	public InputStream createEntity(String feedUri, Entity entity, BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo,
			HttpQueryParams parameters, HashMap<String, String> responseHeaders, ArrayList<BlueviaPartBase> parts) throws IOException,
			HttpException, ParseException {
		InputStream res = null;

		// Call first the serialization. We do nothing with the result but it is just for testing that
		// the entity serializes without error
		OutputStream out = new ByteArrayOutputStream();
		serializer.serialize(entity, out);
		out.close();

		String feedUrl = MockConnector.GSDP_BASE_URL + feedUri;

		//TODO regular expressions
		//		String regex = "^(https?)://(.*)/gSDP/(.*)/?$";
		//
		//		Pattern pattern = Pattern.compile(regex);
		//		Matcher matcher = pattern.matcher(feedUrl);
		//		if (matcher.matches()) {
		//			res = createMockEntity(feedUri, entity, serializer, authenticationInfo, parameters, null, responseHeaders, parts);
		//		} else {
		//			Log.e(TAG, "Error creating entity: Feed Url does not match (regex: " + regex + ", feedUrl: " + feedUrl +")");
		//		}

		res = createMockEntity(feedUri, entity, serializer, authenticationInfo, parameters, null, responseHeaders, parts);

		return res;
	}

	/* (non-Javadoc)
	 * @see com.bluevia.android.rest.client.IConnector#createEntity(java.lang.String, com.bluevia.android.rest.Entity, com.bluevia.android.rest.parser.BlueviaEntitySerializer, com.bluevia.android.rest.client.HttpQueryParams, java.util.HashMap, java.util.HashMap)
	 */
	public InputStream createEntity(String feedUri, Entity entity,
			BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo, 
			HttpQueryParams parameters,
			HashMap<String, String> requestHeaders,
			HashMap<String, String> responseHeaders) throws IOException,
			HttpException, ParseException {

		InputStream res = null;

		// Call first the serialization. We do nothing with the result but it is just for testing that
		// the entity serializes without error
		OutputStream out = new ByteArrayOutputStream();
		if (serializer != null && entity != null){
			serializer.serialize(entity, out);
			out.close();
		}

		String feedUrl = MockConnector.GSDP_BASE_URL + feedUri;

		//TODO regular expressions
		//		String regex = "^(https?)://(.*)/gSDP/(.*)/(.*)/?$";
		//
		//		Pattern pattern = Pattern.compile(regex);
		//		Matcher matcher = pattern.matcher(feedUrl);
		//		if (matcher.matches()) {
		//			res = createMockEntity(feedUri, entity, serializer, authenticationInfo, parameters, requestHeaders, responseHeaders, null);
		//		} else {
		//			Log.e(TAG, "Error creating entity: Feed Url does not match (regex: " + regex + ", feedUrl: " + feedUrl +")");
		//		}

		res = createMockEntity(feedUri, entity, serializer, authenticationInfo, parameters, requestHeaders, responseHeaders, null);


		return res;
	}

	/**
	 * The createEntity method to be implemented by the extended MockConnectors of each BlueVia API.
	 * 
	 * @param feedUri the Uri to create the entity remotely via REST
	 * @param entity The entity object that will be created remotely
	 * @param serializer The serializer to serialize the entity into a stream
	 * @param authenticationInfo The oauth info to generate the security headers
	 * @param parameters The http query parameters for the request
	 * @param requestHeaders The http headers for the request
	 * @param responseHeaders the response headers received. This object should be created by the method invocator
	 * @param parts the multiple parts that are part of the entity
	 * @return the response stream received
	 * @throws IOException
	 * @throws HttpException
	 * @throws ParseException
	 */
	protected abstract InputStream createMockEntity(String feedUri, Entity entity, BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo,
			HttpQueryParams parameters, HashMap<String, String> requestHeaders, HashMap<String, String> responseHeaders, ArrayList<BlueviaPartBase> parts) 
	throws IOException, HttpException, ParseException;


	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.connector.IConnector#retrieveEntity(java.lang.String, com.bluevia.android.rest.oauth.Oauth)
	 */
	public InputStream retrieveEntity(String feedUri, AuthenticationInfo authenticationInfo) throws IOException, HttpException {
		return retrieveEntity(feedUri, authenticationInfo, null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.connector.IConnector#retrieveEntity(java.lang.String, com.bluevia.android.rest.oauth.Oauth, com.bluevia.android.rest.commons.connector.http.HttpQueryParams)
	 */
	public InputStream retrieveEntity(String feedUri, AuthenticationInfo authenticationInfo, HttpQueryParams parameters)
	throws IOException, HttpException {
		InputStream res = null;

		String feedUrl = MockConnector.GSDP_BASE_URL + feedUri;

		//TODO regular expressions
		//		String regex = "^(https?)://(.*)/gSDP/(.*)/(.*)/?$";
		//
		//		Pattern pattern = Pattern.compile(regex);
		//		Matcher matcher = pattern.matcher(feedUrl);
		//
		//		Log.d(TAG, "feedUrl: "+feedUrl);
		//		Log.d(TAG, "regex: "+regex);
		//		if (matcher.matches()) {
		//			res = retrieveMockEntity(feedUrl, authenticationInfo, parameters);
		//		} else {
		//			Log.e(TAG, "Error retrieving entity: Feed Url does not match (regex: " + regex + ", feedUrl: " + feedUrl +")");
		//		}


		res = retrieveMockEntity(feedUrl, authenticationInfo, parameters);

		return res;
	}

	/**
	 * The retrieveEntity method to be implemented by the extended MockConnectors of each BlueVia API.
	 * 
	 * @param feedUri the Uri to create the entity remotely via REST
	 * @param authenticationInfo The oauth info to generate the security headers
	 * @param parameters The http query parameters for the request
	 * @return the response stream received
	 * @throws IOException
	 * @throws HttpException
	 */
	protected abstract InputStream retrieveMockEntity(String feedUri, AuthenticationInfo authenticationInfo, HttpQueryParams parameters)
	throws IOException, HttpException;

	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.connector.IConnector#close()
	 */
	public abstract void close();

	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.connector.IConnector#getMediaContentAsStream(java.lang.String)
	 */
	public InputStream getMediaContentAsStream(String uri) throws IOException, HttpException {

		switch (mTestType){
		case TEST_OK_ORDINAL:
			ByteArrayOutputStream bos = createBitmap();
			InputStream is = new ByteArrayInputStream(bos.toByteArray());

			//Close bitmap an streams
			bos.close();
			return is;

		case TEST_ERROR_IOEXCEPTION_ORDINAL:
			throw new IOException("Mock IO Exception launched");
		case TEST_ERROR_HTTPEXCEPTION_ORDINAL:
			throw new HttpException("Mock Http Exception launched", HttpException.INTERNAL_SERVER_ERROR, null);
		case TEST_UNAUTHORIZED_ORDINAL:
			throw new HttpException("Mock Unauthorized Exception launched", HttpException.UNAUTHORIZED, null);
		default:
			throw new IllegalArgumentException("Mode not supported: " + mTestType);
		}

	}

	public ByteArrayDataSource getMediaContentAsDataSource(String uri, AuthenticationInfo authenticationInfo, HttpQueryParams parameters) throws IOException, HttpException {

		switch (mTestType){
		case TEST_OK_ORDINAL:
			String contentType = "multipart/form-data; " +
			"boundary=MIMEBoundaryurn_uuid_636C752383BEC3AFDD1222262665928; " +
			"start=\"<0.urn:uuid:636C752383BEC3AFDD1222262665929@apache.org>\"";

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(os);

			dos.writeBytes("\r\n");
			dos.writeBytes("--MIMEBoundaryurn_uuid_636C752383BEC3AFDD1222262665928");
			dos.writeBytes("\r\n");
			dos.writeBytes("Content-Disposition: form-data; name=\"root-fields\"");
			dos.writeBytes("\r\n");
			dos.writeBytes("Content-Type: application/xml;charset=UTF-8");
			dos.writeBytes("\r\n");
			dos.writeBytes("Content-ID: <0.urn:uuid:636C752383BEC3AFDD1222262665929@apache.org>");
			dos.writeBytes("\r\n");
			dos.writeBytes("\r\n");
			dos.writeBytes("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
					"<NS1:message xmlns:NS1=\"http://www.telefonica.com/schemas/UNICA/REST/mms/v1/\">" +
					"<NS1:address>" +
					"<NS2:phoneNumber xmlns:NS2=\"http://www.telefonica.com/schemas/UNICA/REST/common/v1\">3422777</NS2:phoneNumber>" +
					"</NS1:address>" +
					"<NS1:originAddress>" +
					"<v1:alias xmlns:v1=\"http://www.telefonica.com/schemas/UNICA/REST/common/v1\">F70CD99201189AD2FEF3F5A23DB60342</v1:alias>" +
					"</NS1:originAddress>" +
					"<NS1:subject>Hello Bluevia!</NS1:subject>" +
			"</NS1:message>");
			dos.writeBytes("\r\n");
			dos.writeBytes("--MIMEBoundaryurn_uuid_636C752383BEC3AFDD1222262665928");
			dos.writeBytes("\r\n");
			dos.writeBytes("Content-disposition: form-data; name=\"attachments\"");
			dos.writeBytes("\r\n");
			dos.writeBytes("Content-Type: multipart/related; boundary=BbC04y");
			dos.writeBytes("\r\n");
			dos.writeBytes("\r\n");
			dos.writeBytes("--BbC04y");
			dos.writeBytes("\r\n");
			dos.writeBytes("Content-Type: text/plain;charset=\"UTF-8\" name=message.txt");
			dos.writeBytes("\r\n");
			dos.writeBytes("Content-Transfer-Encoding: 8bit");
			dos.writeBytes("\r\n");
			dos.writeBytes("Content-ID: <1002>");
			dos.writeBytes("\r\n");
			dos.writeBytes("\r\n");

			//Text
			dos.writeBytes("This is the text of the MMS");
			dos.writeBytes("\r\n");
			dos.writeBytes("\r\n");

			dos.writeBytes("--BbC04y");
			dos.writeBytes("\r\n");
			dos.writeBytes("Content-Type: image/jpeg; name=image.jpeg");
			dos.writeBytes("\r\n");
			dos.writeBytes("Content-Transfer-Encoding: 8bit");
			dos.writeBytes("\r\n");
			dos.writeBytes("Content-ID: <1003>");
			dos.writeBytes("\r\n");
			dos.writeBytes("\r\n");

			//Image content
			ByteArrayOutputStream bos = createBitmap();
			dos.write(bos.toByteArray());
			bos.close();

			dos.writeBytes("\r\n");
			dos.writeBytes("\r\n");
			dos.writeBytes("--BbC04y");
			dos.writeBytes("\r\n");
			dos.writeBytes("--MIMEBoundaryurn_uuid_636C752383BEC3AFDD1222262665928--");

			return new ByteArrayDataSource(os.toByteArray(), contentType);

		case TEST_ERROR_IOEXCEPTION_ORDINAL:
			throw new IOException("Mock IO Exception launched");
		case TEST_ERROR_HTTPEXCEPTION_ORDINAL:
			throw new HttpException("Mock Http Exception launched", HttpException.INTERNAL_SERVER_ERROR, null);
		case TEST_UNAUTHORIZED_ORDINAL:
			throw new HttpException("Mock Unauthorized Exception launched", HttpException.UNAUTHORIZED, null);
		default:
			throw new IllegalArgumentException("Mode not supported: " + mTestType);
		}
	}

	/**
	 * 
	 * @return true if the value of the mTestType is in the range of the Mock values,
	 * false in case it is in the range of the JUnit values.
	 */
	protected boolean isMock(){
		return mTestType < Mode.values().length;
	}

	private ByteArrayOutputStream createBitmap(){
		// Create the bitmap dynamically
		int[] imageData = new int[120 * 120]; // here i create new image data

		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(6);

		// different images image
		for (int i = 0; i < 14400; i++) {
			switch (randomInt) {
			case 0:
				imageData[i] = 0xFFFF0000;
				break;
			case 1:
				imageData[i] = 0xFF00FF00;
				break;
			case 2:
				imageData[i] = 0xFFFFFF00;
				break;
			case 3:
				imageData[i] = 0xFFFF00FF;
				break;
			case 4:
				imageData[i] = 0xFF00FFFF;
				break;
			default:
				imageData[i] = 0xFF0000FF;
			}

		}

		Bitmap bitmap = Bitmap.createBitmap(imageData, 240, 60, Bitmap.Config.ARGB_8888);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
		bitmap.recycle();
		return bos;
	}

}
///@endcond