/// @cond private
package com.bluevia.android.rest.commons.connector.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.util.Log;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.connector.http.HttpException;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.http.authentication.AuthenticationInfo;
import com.bluevia.android.rest.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;


/**
 * Mock implementation of the communication with the gSDP server via HTTP for the Location API
 * This class simulates the behavior of the gSDP server when using Location API. It is called from MockConnector
 *
 * @see MockConnector
 * @author Telefonica R&D
 * 
 */
public class BlueviaLocationMockConnector extends MockConnector {

	private static final String TAG = "MockLocationConnector";

	private static final String REQUESTER_KEY = "requester";
	private static final String LOCATED_PARTY_KEY = "locatedParty";
	private static final String REQUESTED_ACCURACY_KEY = "requestedAccuracy";
	private static final String ACCEPTABLE_ACCURACY_KEY = "acceptableAccuracy";
	private static final String MAXIMUM_AGE_KEY = "maximumAge";
	private static final String RESPONSE_TIME_KEY = "responseTime";
	private static final String TOLERANCE_KEY = "tolerance";
	private static final String ADDRESS_KEY = "address";

	private static final String ALTITUDE_VALUE = "600.0";
	private static final String ACCURACY_VALUE = "1";
	private static final String LATITUDE_VALUE = "40.514";
	private static final String LONGITUDE_VALUE = "-3.663";
	private static final String PHONENUMBER_VALUE = "34916666666";


	protected static final String LOCATION_HEADER =
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<tns:terminalLocation xmlns:tns=\"http://www.telefonica.com/schemas/UNICA/REST/location/v1/\" " +
		"xmlns:tns1=\"http://www.telefonica.com/schemas/UNICA/REST/common/v1\" " +
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xsi:schemaLocation=\"http://www.telefonica.com/schemas/UNICA/REST/location/v1/" +
		"UNICA_API_REST_location_types_v1_0.xsd \">";

	private static final String LOCATED_PARTY_HEADER = 
		"<tns:locatedParty>" +
		"<tns1:alias>";
	private static final String LOCATED_PARTY_TRAIL =
		"</tns1:alias>" +
		"</tns:locatedParty>";
	private static final String REPORT_STATUS = 
		"<tns:reportStatus>Retrieved</tns:reportStatus>";
	private static final String CURRENT_LOCATION_HEADER = "<tns:currentLocation>";
	private static final String COORDINATES = "<tns:coordinates>" +
	"<tns:latitude>"+LATITUDE_VALUE+"</tns:latitude>" +
	"<tns:longitude>"+LONGITUDE_VALUE+"</tns:longitude>" +
	"</tns:coordinates>";
	private static final String ALTITUDE = 	"<tns:altitude>"+ALTITUDE_VALUE+"</tns:altitude>";
	private static final String ACCURACY = "<tns:accuracy>"+ACCURACY_VALUE+"</tns:accuracy>";
	private static final String TIMESTAMP_HEAD = "<tns:timestamp>";
	private static final String TIMESTAMP_TRAIL = "</tns:timestamp>";
	private static final String CURRENT_LOCATION_TRAIL = "</tns:currentLocation>";
	private static final String LOCATION_TRAIL = "</tns:terminalLocation>";

	static final String FEED_LOCATION_BASE_URI = "Location_Sandbox";

	/**
	 * Instantiates the MockConnector
	 *
	 * @param testType the code to indicate the MockConnector the behavior to
	 *            emulate
	 */
	public BlueviaLocationMockConnector(int testType){
		super(testType);
	}

	protected InputStream createMockEntity(String feedUri, Entity entity, BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo,
			HttpQueryParams parameters, HashMap<String, String> requestHeaders, HashMap<String, String> responseHeaders, ArrayList<BlueviaPartBase> parts) 
	throws IOException, HttpException, ParseException {

		Log.i(TAG, "Create Entity method not implemented for MockLocationConnector");
		return null;
	}

	protected InputStream retrieveMockEntity(String feedUri, AuthenticationInfo authenticationInfo, HttpQueryParams parameters)
	throws IOException, HttpException {

		InputStream res = null;
		Log.d(TAG,"entrando");

		String result = "";
		ByteArrayInputStream stringIs = null;

		Format formatter;
		String timestamp;

		switch (mTestType){
		case TEST_OK_ORDINAL:

			Log.d(TAG,"case: BLV_TEST_OK_ORDINAL");
			//Mandatory params		
			String locatedParty = parameters.getParameterValue(LOCATED_PARTY_KEY);

			formatter = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss");
			timestamp = formatter.format(new Date());
			Log.d(TAG, "Timestamp: "+timestamp);


			result = LOCATION_HEADER;

			String alias = locatedParty.split(":")[1];
			result +=	LOCATED_PARTY_HEADER + alias + LOCATED_PARTY_TRAIL +
			REPORT_STATUS +
			CURRENT_LOCATION_HEADER +
			COORDINATES + 
			ALTITUDE +
			ACCURACY +
			TIMESTAMP_HEAD + timestamp + TIMESTAMP_TRAIL +
			CURRENT_LOCATION_TRAIL +
			LOCATION_TRAIL;

			stringIs = new ByteArrayInputStream(result.getBytes());
			res = stringIs; 		        
			break;

		case TEST_UNAUTHORIZED_ORDINAL:

			Log.d(TAG,"case: BLV_TEST_UNAUTHORIZED_ORDINAL");
			throw new HttpException("Mock unauthorized exception", HttpException.UNAUTHORIZED,null);

		case TEST_ERROR_HTTPEXCEPTION_ORDINAL:
			Log.d(TAG,"case: BLV_TEST_ERROR_HTTPEXCEPTION_ORDINAL");
			throw new HttpException("Mock httpException",HttpException.BAD_REQUEST,null);

		case TEST_ERROR_IOEXCEPTION_ORDINAL:
			Log.d(TAG,"case: BLV_TEST_ERROR_IOEXCEPTION_ORDINAL");
			throw new IOException("Mock IOException");
		}

		return res;		

	}

	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.connector.mock.MockConnector#close()
	 */
	public void close() {
		Log.d(TAG, "MockLocationConnector closed");
	}

}
