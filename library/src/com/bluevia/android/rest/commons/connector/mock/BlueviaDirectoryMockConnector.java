/// @cond private
package com.bluevia.android.rest.commons.connector.mock;


import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.connector.http.HttpException;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.http.authentication.AuthenticationInfo;
import com.bluevia.android.rest.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.directory.client.BlueviaDirectoryClient;
import com.bluevia.android.rest.directory.parser.xml.XmlConstants;

/**
 * Mock implementation of the communication with the gSDP server via HTTP for the Directory API
 * This class simulates the behavior of the gSDP server when using Directory API. It is called from MockConnector
 *
 * @see MockConnector
 * @author Telefonica R&D
 * 
 */
public class BlueviaDirectoryMockConnector extends MockConnector {

	private static final String TAG = "MockDirectoryConnector";

	/* ******************************************************************************+
	 * Directory Test data
	 * ******************************************************************************/
	protected static final String FEED_DIRECTORY_BASE_URI = "Directory";

	protected static final String USER_INFO_HEADER = "UserInfo";
	protected static final String USER_ACCESS_INFO_HEADER = "UserAccessInfo";
	protected static final String USER_TERMINAL_INFO_HEADER = "UserTerminalInfo";
	protected static final String USER_PROFILE_HEADER = "UserProfile";


	protected static final String XML_START = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	//UserInfo
	protected static final String USER_INFO_LEAD =
		"<tns:userInfo xsi:schemaLocation=\"http://www.telefonica.com/schemas/UNICA/REST/directory/v1/UNICA_API_REST_directory_types_v1_0.xsd\" "+
		"xmlns:uctr=\"http://www.telefonica.com/schemas/UNICA/REST/common/v1\" "+
		"xmlns:tns=\"http://www.telefonica.com/schemas/UNICA/REST/directory/v1/\" "+
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";

	protected static final String USER_INFO_TRAIL =
		"</tns:userInfo>";

	//UserAccessInfo
	protected static final String USER_ACCESS_INFO_LEAD_NS =
		"<tns:userAccessInfo xsi:schemaLocation=\"http://www.telefonica.com/schemas/UNICA/REST/directory/v1/UNICA_API_REST_directory_types_v1_0.xsd\" "+
		"xmlns:uctr=\"http://www.telefonica.com/schemas/UNICA/REST/common/v1\" "+
		"xmlns:tns=\"http://www.telefonica.com/schemas/UNICA/REST/directory/v1/\" "+
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";

	protected static final String USER_ACCESS_INFO_LEAD =
		"<tns:userAccessInfo>";

	private static final HashMap<String,String> USER_ACCESS_INFO_FIELDS = new HashMap<String, String>();
	static{
		USER_ACCESS_INFO_FIELDS.put("connected","<tns:connected>yes</tns:connected>");
		USER_ACCESS_INFO_FIELDS.put("ipAddress",
				"<tns:ipAddress>"+
				"<uctr:ipv4>195.235.74.4</uctr:ipv4>"+
		"</tns:ipAddress>");
		USER_ACCESS_INFO_FIELDS.put("accessType","<tns:accessType>UMTS</tns:accessType>");
		USER_ACCESS_INFO_FIELDS.put("connectionTime","<tns:connectionTime>1328</tns:connectionTime>");
		USER_ACCESS_INFO_FIELDS.put("apn"," <tns:apn>movistar Internet</tns:apn>");
		USER_ACCESS_INFO_FIELDS.put("roaming","<tns:roaming>yes</tns:roaming>");
		USER_ACCESS_INFO_FIELDS.put("extension",
				"<tns:extension>" +
				"<uctr:ServerException>" +
				"<uctr:exceptionCategory>SVR</uctr:exceptionCategory>"+
				"<uctr:exceptionId>0</uctr:exceptionId>" +
				"<uctr:text>text</uctr:text>" +
				"<uctr:variables>variables</uctr:variables>" +
				"</uctr:ServerException>" +
		"</tns:extension>");
	}
	protected static final String USER_ACCESS_INFO_TRAIL =
		"</tns:userAccessInfo>";



	//UserTerminallInfo
	protected static final String USER_TERMINAL_INFO_LEAD_NS = 
		"<tns:userTerminalInfo xsi:schemaLocation=\"http://www.telefonica.com/schemas/UNICA/REST/directory/v1/ UNICA_API_REST_directory_types_v1_0.xsd\" " +
		"xmlns:uctr=\"http://www.telefonica.com/schemas/UNICA/REST/common/v1\" " +
		"xmlns:tns=\"http://www.telefonica.com/schemas/UNICA/REST/directory/v1/\" " +
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";

	protected static final String USER_TERMINAL_INFO_LEAD = 
		"<tns:userTerminalInfo>";

	private static final HashMap<String,String> USER_TERMINAL_INFO_FIELDS = new HashMap<String, String>();
	static{
		USER_TERMINAL_INFO_FIELDS.put("brand","<tns:brand>Nexus</tns:brand>");
		USER_TERMINAL_INFO_FIELDS.put("model","<tns:model>One</tns:model>");
		USER_TERMINAL_INFO_FIELDS.put("version","<tns:version>402658</tns:version>");
		USER_TERMINAL_INFO_FIELDS.put("screenResolution","<tns:screenResolution>480x800</tns:screenResolution>");
		USER_TERMINAL_INFO_FIELDS.put("imei","<tns:imei>358987010052195</tns:imei>");
		USER_TERMINAL_INFO_FIELDS.put("mms","<tns:mms>yes</tns:mms>");
		USER_TERMINAL_INFO_FIELDS.put("ems","<tns:ems>yes</tns:ems>");
		USER_TERMINAL_INFO_FIELDS.put("smartMessaging","<tns:smartMessaging>yes</tns:smartMessaging>");
		USER_TERMINAL_INFO_FIELDS.put("wap","<tns:wap>yes</tns:wap>");
		USER_TERMINAL_INFO_FIELDS.put("ssdPhase","<tns:ussdPhase>0</tns:ussdPhase>");
		USER_TERMINAL_INFO_FIELDS.put("syncMl","<tns:syncMl>yes</tns:syncMl>");
		USER_TERMINAL_INFO_FIELDS.put("syncMlVersion","<tns:syncMlVersion>1.0.1</tns:syncMlVersion>");
		USER_TERMINAL_INFO_FIELDS.put("emsMaxNumber","<tns:emsMaxNumber>5</tns:emsMaxNumber>");
		USER_TERMINAL_INFO_FIELDS.put("email","<tns:email>yes</tns:email>");
		USER_TERMINAL_INFO_FIELDS.put("emn","<tns:emn>yes</tns:emn>");
		USER_TERMINAL_INFO_FIELDS.put("adc_OTA","<tns:adc_OTA>yes</tns:adc_OTA>");
		USER_TERMINAL_INFO_FIELDS.put("status","<tns:status>approved</tns:status>");
		USER_TERMINAL_INFO_FIELDS.put("lastUpdated","<tns:lastUpdated>2010-06-01T12:00:00</tns:lastUpdated>");
		USER_TERMINAL_INFO_FIELDS.put("extension","     <tns:extension>" +
				"       <uctr:ServerException>" +
				"           <uctr:exceptionCategory>SVR</uctr:exceptionCategory>" +
				"           <uctr:exceptionId>0</uctr:exceptionId>" +
				"           <uctr:text>tns1:text</uctr:text>" +
				"           <uctr:variables>tns1:variables</uctr:variables>" +
				"       </uctr:ServerException>" +
		"  </tns:extension>");
	}
	protected static final String USER_TERMINAL_INFO_TRAIL =
		"</tns:userTerminalInfo>";

	//UserProfile
	protected static final String USER_PROFILE_LEAD_NS = 
		"<tns:userProfile xmlns:tns=\"http://www.telefonica.com/schemas/UNICA/REST/directory/v1/\" "+
		"xmlns:tns1=\"http://www.telefonica.com/schemas/UNICA/REST/common/v1\" " +
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xsi:schemaLocation=\"http://www.telefonica.com/schemas/UNICA/REST/directory/v1/ UNICA_API_REST_directory_types_v1_0.xsd \"> ";
	protected static final String USER_PROFILE_LEAD = 
		"<tns:userProfile> ";

	private static final HashMap<String,String> USER_PROFILE_FIELDS = new HashMap<String, String>();
	static{
		USER_PROFILE_FIELDS.put("userType","<tns:userType>pre-paid</tns:userType>");
		USER_PROFILE_FIELDS.put("imsi","<tns:imsi>52952503786</tns:imsi>");
		USER_PROFILE_FIELDS.put("icb","<tns:icb>no</tns:icb>");
		USER_PROFILE_FIELDS.put("ocb","<tns:ocb>no</tns:ocb>");
		USER_PROFILE_FIELDS.put("language","<tns:language>english</tns:language>");
		USER_PROFILE_FIELDS.put("simType","<tns:simType>P55</tns:simType>");
		USER_PROFILE_FIELDS.put("parentalControl","<tns:parentalControl>none</tns:parentalControl>");
		USER_PROFILE_FIELDS.put("creditControl","<tns:creditControl>yes</tns:creditControl>");
		USER_PROFILE_FIELDS.put("diversionMsisdn","<tns:diversionMsisdn>587118742687743</tns:diversionMsisdn>");
		USER_PROFILE_FIELDS.put("enterpriseName","<tns:enterpriseName>telefonicaI+D</tns:enterpriseName>");
		USER_PROFILE_FIELDS.put("roaming","<tns:roaming>yes</tns:roaming>");
		USER_PROFILE_FIELDS.put("operatorId","<tns:operatorId>555+001</tns:operatorId>");
		USER_PROFILE_FIELDS.put("mmsStatus","<tns:mmsStatus>activated</tns:mmsStatus>");
		USER_PROFILE_FIELDS.put("segment","<tns:segment>segment</tns:segment>");
		USER_PROFILE_FIELDS.put("subsegment","<tns:subsegment>subsegment</tns:subsegment>");
		USER_PROFILE_FIELDS.put("subscribedService","<tns:subscribedService name=\"subscribedService\"/>");
		USER_PROFILE_FIELDS.put("lastUpdated","<tns:lastUpdated>2010-06-03T12:00:00</tns:lastUpdated>");
	}
	protected static final String USER_PROFILE_TRAIL =
		"</tns:userProfile>";


	/**
	 * Instantiates the MockConnector
	 * @param testType the code to indicate the mockclient the behavior to emulate
	 */
	public BlueviaDirectoryMockConnector(int testType) {
		super(testType);
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.connector.mock.MockConnector#createMockEntity(java.lang.String, com.bluevia.android.rest.commons.Entity, com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer, com.bluevia.android.rest.oauth.Oauth, com.bluevia.android.rest.commons.connector.http.HttpQueryParams, java.util.HashMap, java.util.HashMap, java.util.ArrayList)
	 */
	@Override
	protected InputStream createMockEntity(String feedUri, Entity entity,
			BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo,
			HttpQueryParams parameters, HashMap<String, String> requestHeaders,
			HashMap<String, String> responseHeaders,
			ArrayList<BlueviaPartBase> parts) throws IOException, HttpException,
			ParseException {
		Log.i(TAG, "Create Entity method not implemented for MockDirectoryConnector");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.connector.mock.MockConnector#retrieveMockEntity(java.lang.String, com.bluevia.android.rest.oauth.Oauth, com.bluevia.android.rest.commons.connector.http.HttpQueryParams)
	 */
	@Override
	protected InputStream retrieveMockEntity(String feedUri, AuthenticationInfo authenticationInfo, HttpQueryParams parameters)
	throws IOException, HttpException {

		InputStream res = null;
		ByteArrayInputStream stringIs = null;

		String result = "";
		switch (mTestType) {
		case TEST_OK_ORDINAL:

			Log.d(TAG, "case test_ok");

			Pattern mainPattern = Pattern.compile("^(.*)/" + FEED_DIRECTORY_BASE_URI + "/(.*)/(.*)?$");
			Matcher mainMathcer = mainPattern.matcher(feedUri);
			if (mainMathcer.matches()){

				Log.d(TAG, mainMathcer.group(1));
				Log.d(TAG, mainMathcer.group(2));
				Log.d(TAG, mainMathcer.group(3));
				String resource = mainMathcer.group(3);
				if (resource.equalsIgnoreCase(USER_INFO_HEADER)){

					String dataSetsString = parameters!=null ? parameters.getParameterValue(BlueviaDirectoryClient.DATASETS_KEY): null;
					if (dataSetsString == null || dataSetsString.equals("")){
						dataSetsString = getAllKeys(USER_INFO_HEADER);
					}
					String[] dataSets = dataSetsString.split(",");

					for (String set : dataSets){
						result += buildXmlSection(set, getAllKeys(set).split(","), false);
					}
					result = XML_START + USER_INFO_LEAD + 
						result + USER_INFO_TRAIL;
					
				} else {

					//Individual resource

					String parametersString = parameters!=null ? parameters.getParameterValue(BlueviaDirectoryClient.FILTER_KEY): null;
					if (parametersString == null || parametersString.equals("")){
						parametersString = getAllKeys(resource);
					}
					String[] paramArray = parametersString.split(",");

					result = XML_START + buildXmlSection(resource, paramArray, true);

				}
				res = new ByteArrayInputStream(result.getBytes());
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

	private String buildXmlSection(String resource, String[] paramArray, boolean ns){
		String result = "";
		if (resource.equalsIgnoreCase(USER_ACCESS_INFO_HEADER)){

			if (ns)
				result += USER_ACCESS_INFO_LEAD_NS;
			else result += USER_ACCESS_INFO_LEAD;

			for (int i = 0; i<paramArray.length; i++){
				if (USER_ACCESS_INFO_FIELDS.containsKey(paramArray[i]))
					result += USER_ACCESS_INFO_FIELDS.get(paramArray[i]);
			}
			result += USER_ACCESS_INFO_TRAIL;

		} else if (resource.equalsIgnoreCase(USER_TERMINAL_INFO_HEADER)){

			if (ns)
				result += USER_TERMINAL_INFO_LEAD_NS;
			else result += USER_TERMINAL_INFO_LEAD;

			for (int i = 0; i<paramArray.length; i++){
				if (USER_TERMINAL_INFO_FIELDS.containsKey(paramArray[i]))
					result += USER_TERMINAL_INFO_FIELDS.get(paramArray[i]);
			}
			result += USER_TERMINAL_INFO_TRAIL;
		} else if (resource.equalsIgnoreCase(USER_PROFILE_HEADER)){

			if (ns)
				result += USER_PROFILE_LEAD_NS;
			else result += USER_PROFILE_LEAD;

			for (int i = 0; i<paramArray.length; i++){
				if (USER_PROFILE_FIELDS.containsKey(paramArray[i]))
					result = result + USER_PROFILE_FIELDS.get(paramArray[i]);
			}
			result += USER_PROFILE_TRAIL;


		}

		Log.d(TAG, "result :"+result);
		return result;
	}

	/**
	 * Returns all available keys to to filter the entity query
	 * @return a comma-separated list of all the keys in this class
	 */
	private String getAllKeys(String resource){
		if (resource.equalsIgnoreCase(USER_INFO_HEADER)){
			return XmlConstants.XSD_ACCESSINFO + "," +
			XmlConstants.XSD_PROFILE  + "," +
			XmlConstants.XSD_TERMINALINFO;
		} else if (resource.equalsIgnoreCase(USER_ACCESS_INFO_HEADER)){
			return XmlConstants.XSD_ACCESSINFO_CONNECTED + "," +
			XmlConstants.XSD_ACCESSINFO_IPADDRESS  + "," +
			XmlConstants.XSD_ACCESSINFO_ACCESSTYPE + "," +
			XmlConstants.XSD_ACCESSINFO_CONNECTIONTIME + "," +
			XmlConstants.XSD_ACCESSINFO_APN + "," +
			XmlConstants.XSD_ACCESSINFO_ROAMING + "," +
			XmlConstants.XSD_EXTENSION;
		} else if (resource.equalsIgnoreCase(USER_PROFILE_HEADER)) {
			return XmlConstants.XSD_PROFILE_USERTYPE + "," +
			XmlConstants.XSD_PROFILE_IMSI  + "," +
			XmlConstants.XSD_PROFILE_ICB  + "," +
			XmlConstants.XSD_PROFILE_OCB  + "," +
			XmlConstants.XSD_PROFILE_LANGUAGE + "," +
			XmlConstants.XSD_PROFILE_SIMTYPE  + "," +
			XmlConstants.XSD_PROFILE_PARENTALCONTROL  + "," +
			XmlConstants.XSD_PROFILE_CREDITCONTROL  + "," +
			XmlConstants.XSD_PROFILE_DIVERSIONMSISDN  + "," +
			XmlConstants.XSD_PROFILE_ENTERPRISENAME  + "," +
			XmlConstants.XSD_PROFILE_ROAMING  + "," +
			XmlConstants.XSD_PROFILE_OPERATORID  + "," +
			XmlConstants.XSD_PROFILE_MMSSTATUS  + "," +
			XmlConstants.XSD_PROFILE_SEGMENT  + "," +
			XmlConstants.XSD_PROFILE_SUBSEGMENT  + "," +
			XmlConstants.XSD_PROFILE_SUBSCRIBEDSERVICE  + "," +
			XmlConstants.XSD_LASTUPDATED;
		} else if (resource.equalsIgnoreCase(USER_TERMINAL_INFO_HEADER)) {
			return XmlConstants.XSD_TERMINALINFO_BRAND + "," +
			XmlConstants.XSD_TERMINALINFO_MODEL  + "," +
			XmlConstants.XSD_TERMINALINFO_VERSION + "," +
			XmlConstants.XSD_TERMINALINFO_SCREENRESOLUTION  + "," +
			XmlConstants.XSD_TERMINALINFO_IMEI + "," +
			XmlConstants.XSD_TERMINALINFO_MMS + "," +
			XmlConstants.XSD_TERMINALINFO_EMS + "," +
			XmlConstants.XSD_TERMINALINFO_SMARTMESSAGING  + "," +
			XmlConstants.XSD_TERMINALINFO_WAP  + "," +
			XmlConstants.XSD_TERMINALINFO_USSDPHASE + "," +
			XmlConstants.XSD_TERMINALINFO_SYNCML  + "," +
			XmlConstants.XSD_TERMINALINFO_SYNCMLVERSION + "," +
			XmlConstants.XSD_TERMINALINFO_EMSMAXNUMBER  + "," +
			XmlConstants.XSD_TERMINALINFO_STATUS + "," +
			XmlConstants.XSD_TERMINALINFO_EMAIL + "," +
			XmlConstants.XSD_TERMINALINFO_EMN + "," +
			XmlConstants.XSD_TERMINALINFO_ADCOTA + "," +
			XmlConstants.XSD_LASTUPDATED;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.connector.mock.MockConnector#close()
	 */
	public void close() {
		Log.d(TAG, "MockDirectoryConnector closed");
	}

}
///@endcond