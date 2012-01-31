package com.bluevia.android.rest.commons.connector.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.connector.http.HttpException;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.http.authentication.AuthenticationInfo;
import com.bluevia.android.rest.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.android.rest.commons.data.UserId;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.commons.parser.xml.XmlParserSerializerUtils;
import com.bluevia.android.rest.messaging.AbstractMessage;


/**
 * Mock implementation of the communication with the gSDP server via HTTP gor the SMS and MMS APIs
 * This class simulates the behavior of the gSDP server when using SMS and MMS APIs. It is called from MockConnector
 *
 * @see MockConnector
 * @author Telefonica R&D
 * 
 */
public class BlueviaMessageMockConnector extends MockConnector {

	private static final String TAG = "MockBlueviaMessageConnector";

	/**
	 * Type of message to emulate. Possible values are SMS or MMS
	 */
	public enum MessageType {SMS, MMS};

	protected static final String FEED_SMS_BASE_URI = "REST/SMS";
	protected static final String FEED_MMS_BASE_URI = "REST/MMS";

	protected static final String MSG_OUTBOUND_URI = "outbound/requests";
	protected static final String MSG_INBOUND_URI = "inbound";

	protected static final String FEED_SMS_BASE_OUTBOUND_URI_BLUEVIA = FEED_SMS_BASE_URI + "/" + MSG_OUTBOUND_URI;
	protected static final String FEED_SMS_BASE_INBOUND_URI_BLUEVIA = FEED_SMS_BASE_URI + "/" + MSG_INBOUND_URI;

	protected static final String FEED_MMS_BASE_OUTBOUND_URI_BLUEVIA = FEED_MMS_BASE_URI + "/" + MSG_OUTBOUND_URI;
	protected static final String FEED_MMS_BASE_INBOUND_URI_BLUEVIA = FEED_MMS_BASE_URI + "/" + MSG_INBOUND_URI;

	protected static final String MSG_HEADER_LOCATION_NAME = "Location";
	protected static final String MSG_BLUEVIA_DELIVERYSTATUS_URIPATH = "deliverystatus";
	protected static final String MSG_BLUEVIA_RECEIVED_MSG_URIPATH = "messages";
	protected static final String MSG_BLUEVIA_RECEIVED_MSG_ATT_URIPATH = "attachments";

	/* ***********************************
	 * send{SMS|MMS} responses
	 * ***********************************
	 */
	private static final String SMS_BLUEVIA_HEADER_LOCATION_OK = GSDP_BASE_URL + FEED_SMS_BASE_OUTBOUND_URI_BLUEVIA +"/%s/"+MSG_BLUEVIA_DELIVERYSTATUS_URIPATH;
	private static final String MMS_BLUEVIA_HEADER_LOCATION_OK = GSDP_BASE_URL + FEED_MMS_BASE_OUTBOUND_URI_BLUEVIA +"/%s/"+MSG_BLUEVIA_DELIVERYSTATUS_URIPATH;

	/* ****************************************
	 * Sms|Mms getDeliveryStatus responses
	 * ****************************************
	 */
	protected static final String XML_SMS_DELIVERYSTATUS_TAG = "smsDeliveryStatus";
	protected static final String XML_MMS_DELIVERYSTATUS_TAG = "messageDeliveryStatus";

	protected static final String XML_MSG_DELIVERYSTATUS_DELIVERYSTATUS_SECTION =
		"    <tns:%s>" +
		"<tns:address>" +
		"%s" + //UserIdType section
		"</tns:address>" +
		"<tns:deliveryStatus>DeliveredToTerminal</tns:deliveryStatus>" +
		"    </tns:%s>";

	// Received SMS strings
	protected static final String ORIGIN_ADDRESS = "0cd9e8ab1ca7112cd3df0981c2";
	protected static final String DESTINATION_NUMBER = "34999666999";
	protected static final String MESSAGE_IDENTIFIER = "12345678";
	protected static final String MESSAGE = "Dumb mock message";
	protected static final String SUBJECT = "Dumb mock subject";
	protected static final String DATETIME = "2001-12-31T12:00:00";
	
	protected static final String XML_RECEIVEDSMS_TAG = XmlConstants.XSD_RECEIVEDSMS;
	protected static final String XML_RECEIVEDMMS_TAG = XmlConstants.XSD_RECEIVEDMMS;
	protected static final String XML_RECEIVEDSMS_MESSAGE_TAG = XmlConstants.XSD_MMSTEXTTYPE_MMSMESSAGE;
	protected static final String XML_RECEIVEDMMS_MESSAGE_ID_TAG = XmlConstants.XSD_RECEIVEDMMS_IDENTIFIER;
	protected static final String XML_RECEIVEDMMS_SUBJECT = XmlConstants.XSD_MMSTEXTTYPE_SUBJECT;
	
	protected static final String CONTENT_ID = "<0.urn:uuid:636C752383BEC3AFDD1222262665929@apache.org>";
	
	
	protected static final String XML_MSG_MESSAGES_XML_BASE =
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
		"<tns:%s xsi:schemaLocation=\"%s\" " +
		"xmlns:uctr=\"http://www.telefonica.com/schemas/UNICA/REST/common/v1\" " +
		"xmlns:tns=\"%s\" " +
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> " +
		"%s" + // Content sections, optional. It may be empty.
		"</tns:%s>";
	
	protected static final String XML_MSG_RECEIVEDMESSAGE_COMMON_SECTION =
		"    <tns:%s>" +
		"%s" + // message/messageIdentifier section
		"%s" + // subject section, optional
		"<tns:originAddress>" +
		"%s" + // UserIdType section
		"</tns:originAddress>" +
		"<tns:destinationAddress>" +
		"%s" + // UserIdType section
		"</tns:destinationAddress>" +
		"%s" + // dateTime section, optional. It may be empty
		"%s" + // attachmentUris section, optional. It may be empty
		"    </tns:%s>";
	
	protected static final String XML_MSG_RECEIVEDMESSAGE_ATTACHMENT_URIS =
		"<tns:attachmentURL>" +
			"<tns:href>" +
				"%s" + 
			"</tns:href>" +
			"<tns:contentType>" +
				"%s" + 
			"</tns:contentType>" +
		"</tns:attachmentURL>";
	
	protected static final String XML_MSG_RECEIVEDMESSAGE_MESSAGE_SECTION = 	
		"<tns:%s>" +
		"%s" + // tns:message/messageIdentifier string
		"</tns:%s>";
		
	protected static final String XML_MSG_RECEIVEDMESSAGE_SUBJECT_SECTION = 
		"<tns:subject>" +
		"%s" + // subject string
		"</tns:subject>";
	
	protected static final String XML_MSG_RECEIVEDMESSAGE_DATETIME_SECTION =
		"<tns:dateTime>" +
		"%s" + // dateTime string : 2001-12-31T12:00:00
		"</tns:dateTime>";
	
	protected static final String XML_MSG_GETMESSAGE_BASE =
		"--MIMEBoundaryurn_uuid_%s\r\n" +	//Boundary
		"Content-Disposition: form-data; name=\"root-fields\"\r\n" +
		"Content-Type: application/xml; charset=UTF-8\r\n" +
		"Content-ID: %s\r\n\r\n" + 
		"%s\r\n" + // Message section
		"--MIMEBoundaryurn_uuid_%s\r\n" +	//Boundary
		"%s\r\n" +	//Attachments sections
		"--MIMEBoundaryurn_uuid_%s\r\n";	//Boundary
		
	protected static final String XML_MSG_GETMESSAGE_ATTACHMENTS_SECTION =
		"Content-disposition: form-data; name=\"attachments\"\r\n" +
		"Content-Type: multipart/mixed; boundary=%s" + //2nd Boundary 
		"\r\n\r\n" + 
		"%s";	//Attachments contents	
	
	protected static final String XML_MSG_GETMESSAGE_ATTACHMENT = 
		"--%s\r\n" + //Boundary 
		"Content-Type: %s name=%s\r\n" + 
		"Content-Transfer-Encoding: binary\r\n" +
		"Content-ID: <%s>\r\n\r\n" +
		"%s\r\n\r\n";	//ASCII content
	
	protected static final String MSG_GETMESSAGE_ATTACHMENT_TEXT_CT = "text/plain; charset=\"UTF-8\"";
	protected static final String MSG_GETMESSAGE_ATTACHMENT_TEXT_FILENAME = "message.txt";
	protected static final String MSG_GETMESSAGE_ATTACHMENT_TEXT = "This is the text of the MMS";

	protected static final String MSG_GETMESSAGE_ATTACHMENT_IMG_CT = "image/jpeg;";
	protected static final String MSG_GETMESSAGE_ATTACHMENT_IMG_FILENAME = "image.jpeg";
	
	protected static final String MSG_GETMESSAGE_MAIN_BOUNDARY_START = "multipart/form-data; boundary=MIMEBoundaryurn_uuid_";
		
	
	protected static final String XML_SMS_SCHEMALOCATION ="http://www.telefonica.com/schemas/UNICA/REST/sms/v1/UNICA_API_REST_sms_types_v1_0.xsd";
	protected static final String XML_SMS_NAMESPACE="http://www.telefonica.com/schemas/UNICA/REST/sms/v1/";
	protected static final String XML_MMS_SCHEMALOCATION ="http://www.telefonica.com/schemas/UNICA/REST/mms/v1/UNICA_API_REST_mms_types_v1_0.xsd";
	protected static final String XML_MMS_NAMESPACE="http://www.telefonica.com/schemas/UNICA/REST/mms/v1/";

	//To avoid mock parser to implement an alternative parser for the delivery status xml
	protected static final String XML_MSG_NAMESPACE_SINGLE="http://www.telefonica.com/schemas/UNICA/REST/SINGLE_OK";
	protected static final String XML_MSG_NAMESPACE_MULTIPLE="http://www.telefonica.com/schemas/UNICA/REST/MULTIPLE_OK";

	protected static final String XML_MSG_DELIVERYSTATUS_BASE =
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
		"<tns:%s xsi:schemaLocation=\"%s\" " +
		"xmlns:code=\"%s\" " +
		"xmlns:uctr=\"http://www.telefonica.com/schemas/UNICA/REST/common/v1\" " +
		"xmlns:tns=\"%s\" " +
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> " +
		"%s" + //Delivery Status section
		"</tns:%s>";

	protected HashMap<String, ArrayList<UserId>> mMsgIdsCache = new HashMap<String, ArrayList<UserId>>();
	protected MessageType mMessageType;

	/**
	 * Instantiates the MockConnector
	 * @param testType the code to indicate the mockclient the behavior to emulate
	 * @param messageType Indicates if the message is an SMS or an MMS
	 */
	public BlueviaMessageMockConnector(int testType, MessageType messageType) {
		super(testType);
		mMessageType = messageType;
	}

	protected InputStream createMockEntity(String feedUri, Entity entity, BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo,
			HttpQueryParams parameters, HashMap<String, String> requestHeaders, HashMap<String, String> responseHeaders, ArrayList<BlueviaPartBase> parts) 
	throws IOException, HttpException, ParseException {
		InputStream res = null;

		if (responseHeaders == null)
			return null;

		if ((entity == null) || !(entity instanceof AbstractMessage))
			return null;

		AbstractMessage message = (AbstractMessage) entity;

		String smsId = null;

		switch (mTestType) {
		case TEST_OK_ORDINAL:
			smsId = Integer.toString((new Random()).nextInt(9999999));
			mMsgIdsCache.put(smsId, message.getAddressList());
			responseHeaders.put(MSG_HEADER_LOCATION_NAME,
					String.format(mMessageType==MessageType.SMS?SMS_BLUEVIA_HEADER_LOCATION_OK:MMS_BLUEVIA_HEADER_LOCATION_OK,
							(Object[])new String[] {smsId}));
			break;
		case TEST_ERROR_IOEXCEPTION_ORDINAL:
			throw new IOException("Mock IO Exception launched");
		case TEST_ERROR_HTTPEXCEPTION_ORDINAL:
			throw new HttpException("Mock Http Exception launched", HttpException.INTERNAL_SERVER_ERROR, res);
		case TEST_UNAUTHORIZED_ORDINAL:
			throw new HttpException("Mock Unauthorized Exception launched", HttpException.UNAUTHORIZED, null);
		default:
			throw new IllegalArgumentException("Mode not supported");
		}
		return res;
	}

	protected InputStream retrieveMockEntity(String feedUri,
			AuthenticationInfo authenticationInfo, HttpQueryParams parameters, HashMap<String, String> responseHeaders) throws IOException,
			HttpException {
		InputStream res = null;
		ByteArrayInputStream stringIs = null;

		String regex =  "(.*)/" + (mMessageType==MessageType.SMS?FEED_SMS_BASE_URI:FEED_MMS_BASE_URI) + "/(.*)/?$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(feedUri);
		matcher.find();
		Log.v(TAG, "Group count: " + matcher.groupCount());
		String resource = matcher.group(2);

		ArrayList<UserId> addresses;
		StringBuffer deliveryStatusSection;
		switch (mTestType) {
		case TEST_OK_ORDINAL:

			// DELIVERY STATUS
			if (resource.startsWith(MSG_OUTBOUND_URI)){

				// Location Url is like  "https://telefonica.com/gSDP/REST/{SMS|MMS}/outbound/requests/MessageId/deliverystatus"
				regex =  "(.*)/" + (mMessageType==MessageType.SMS?FEED_SMS_BASE_OUTBOUND_URI_BLUEVIA:FEED_MMS_BASE_OUTBOUND_URI_BLUEVIA) + "/(.*)/" + MSG_BLUEVIA_DELIVERYSTATUS_URIPATH + "/?$";

				pattern = Pattern.compile(regex);
				matcher = pattern.matcher(feedUri);
				matcher.find();
				String smsId = matcher.group(2);
				if (smsId == null)
					throw new HttpException("Wrong Uri. Sms|Mms Id is either null or empty", HttpException.NOT_FOUND,  null);

				addresses = mMsgIdsCache.get(smsId);

				deliveryStatusSection = new StringBuffer();
				if (addresses != null) {
					for (UserId address : addresses) {
						deliveryStatusSection.append(String.format(XML_MSG_DELIVERYSTATUS_DELIVERYSTATUS_SECTION,
								mMessageType==MessageType.SMS?XML_SMS_DELIVERYSTATUS_TAG:XML_MMS_DELIVERYSTATUS_TAG, buildUserIdSection (address),
										mMessageType==MessageType.SMS?XML_SMS_DELIVERYSTATUS_TAG:XML_MMS_DELIVERYSTATUS_TAG));
					}
				}

				if (deliveryStatusSection.length() == 0)
					throw new HttpException("Sms|Mms Id Uri not found", HttpException.NOT_FOUND,  null);

				//Distinguishes between simple and multiple for mock parsers in junit
				//to avoid mocks parsers to parse the input
				if (addresses.size() >1) {
					stringIs = new ByteArrayInputStream(String.format(XML_MSG_DELIVERYSTATUS_BASE,
							mMessageType==MessageType.SMS?XML_SMS_DELIVERYSTATUS_TAG:XML_MMS_DELIVERYSTATUS_TAG,
									mMessageType==MessageType.SMS?XML_SMS_SCHEMALOCATION:XML_MMS_SCHEMALOCATION,
											XML_MSG_NAMESPACE_MULTIPLE,
											mMessageType==MessageType.SMS?XML_SMS_NAMESPACE:XML_MMS_NAMESPACE,
													deliveryStatusSection,
													mMessageType==MessageType.SMS?XML_SMS_DELIVERYSTATUS_TAG:XML_MMS_DELIVERYSTATUS_TAG).getBytes());
				} else {
					stringIs = new ByteArrayInputStream(String.format(XML_MSG_DELIVERYSTATUS_BASE,
							mMessageType==MessageType.SMS?XML_SMS_DELIVERYSTATUS_TAG:XML_MMS_DELIVERYSTATUS_TAG,
									mMessageType==MessageType.SMS?XML_SMS_SCHEMALOCATION:XML_MMS_SCHEMALOCATION,
											XML_MSG_NAMESPACE_SINGLE,
											mMessageType==MessageType.SMS?XML_SMS_NAMESPACE:XML_MMS_NAMESPACE,
													deliveryStatusSection,
													mMessageType==MessageType.SMS?XML_SMS_DELIVERYSTATUS_TAG:XML_MMS_DELIVERYSTATUS_TAG).getBytes());
				}
				res = stringIs;

			} else if (resource.startsWith(MSG_INBOUND_URI)){ //MESSAGE MO
				
				String registrationId = null;
				String messageIdentifier = null;
				String attIdentifier = null;
				
				if (feedUri.contains(MSG_BLUEVIA_RECEIVED_MSG_ATT_URIPATH)){	//GET ATTACHMENT
					
					regex =  "(.*)/" + (mMessageType==MessageType.SMS?FEED_SMS_BASE_INBOUND_URI_BLUEVIA:FEED_MMS_BASE_INBOUND_URI_BLUEVIA) + "/(.*)/" 
							+ MSG_BLUEVIA_RECEIVED_MSG_URIPATH +"/(.*)/" + MSG_BLUEVIA_RECEIVED_MSG_ATT_URIPATH +"(.*)/?$";
					pattern = Pattern.compile(regex);
					matcher = pattern.matcher(feedUri); 
					
					// check if it matches
					if (matcher.matches()){		
						// registrationId value corresponds to the 2nd group on the pattern
						// messageIdentifier value corresponds to the 3nd group on the pattern
						// attachmentIdentifier value corresponds to the 3nd group on the pattern
						registrationId = matcher.group(2);
						messageIdentifier = matcher.group(3);
						attIdentifier = matcher.group(4);
						Log.e(TAG, "Registration ID: " + registrationId);
						Log.e(TAG, "Message ID: " + messageIdentifier);
						Log.e(TAG, "Message ID: " + attIdentifier);
						
						if (registrationId == null || messageIdentifier == null || attIdentifier == null)
							throw new HttpException("Wrong Uri. Registration Id or message identifier or attachment identifier " +
								"are either null or empty", HttpException.NOT_FOUND,  null);
						
						String boundary = Integer.toString((new Random()).nextInt(Integer.MAX_VALUE));
						String id = Integer.toString((new Random()).nextInt(9999));
						
						if (attIdentifier.contains("1")){	//Txt
							stringIs = new ByteArrayInputStream(MSG_GETMESSAGE_ATTACHMENT_TEXT.getBytes());

							//Response Headers
							if (responseHeaders != null){
								responseHeaders.put("Content-Type", MSG_GETMESSAGE_ATTACHMENT_TEXT_CT + " " + MSG_GETMESSAGE_ATTACHMENT_TEXT_FILENAME);
								responseHeaders.put("Content-Disposition", "form-data; name=\"attachments\"");
							}
							
						} else if (attIdentifier.contains("2")){	//Img
							ByteArrayOutputStream imageContent = createBitmap();
							stringIs = new ByteArrayInputStream(imageContent.toByteArray());
							
							//Response Headers
							if (responseHeaders != null){
								responseHeaders.put("Content-Type", MSG_GETMESSAGE_ATTACHMENT_IMG_CT + " " + MSG_GETMESSAGE_ATTACHMENT_IMG_FILENAME);
								responseHeaders.put("Content-Disposition", "form-data; name=\"attachments\"");
							}
						}
						
						res = stringIs;
						
					}
					
				} else {
					
					regex =  "(.*)/" + (mMessageType==MessageType.SMS?FEED_SMS_BASE_INBOUND_URI_BLUEVIA:FEED_MMS_BASE_INBOUND_URI_BLUEVIA) + "/(.*)/" + MSG_BLUEVIA_RECEIVED_MSG_URIPATH +"/(.*)/?$";
					pattern = Pattern.compile(regex);
					matcher = pattern.matcher(feedUri);  

					// check if it matches
					if (matcher.matches()){		// GET MESSAGE (only MMS)
						
						// registrationId value corresponds to the 2nd group on the pattern
						// messageIdentifier value corresponds to the 3nd group on the pattern
						registrationId = matcher.group(2);
						messageIdentifier = matcher.group(3);
						Log.e(TAG, "Registration ID: " + registrationId);
						Log.e(TAG, "Message ID: " + messageIdentifier);

						if (registrationId == null || messageIdentifier == null)
							throw new HttpException("Wrong Uri. Registration Id or message identifier are either null or empty", HttpException.NOT_FOUND,  null);

						UserId destinationAddress = new UserId( UserId.Type.PHONE_NUMBER, DESTINATION_NUMBER );
						UserId originAddress = new UserId( UserId.Type.ALIAS, ORIGIN_ADDRESS );

						String subjectSection = String.format(XML_MSG_RECEIVEDMESSAGE_SUBJECT_SECTION, SUBJECT);
						
						StringBuffer commonMessageSection = new StringBuffer();
						commonMessageSection.append(String.format(XML_MSG_RECEIVEDMESSAGE_COMMON_SECTION,
								XML_RECEIVEDSMS_MESSAGE_TAG,
										"",	//no message section
										subjectSection, 
										buildUserIdSection(originAddress),
										buildUserIdSection(destinationAddress),
										"",
										"",
										XML_RECEIVEDSMS_MESSAGE_TAG));
						
						String xmlSection = String.format(XML_MSG_MESSAGES_XML_BASE, 
								XML_RECEIVEDSMS_MESSAGE_TAG,
								XML_MMS_SCHEMALOCATION,
								XML_MMS_NAMESPACE,
								commonMessageSection, 
								XML_RECEIVEDSMS_MESSAGE_TAG);
						
						String boundary = Integer.toString((new Random()).nextInt(Integer.MAX_VALUE));
						String id = Integer.toString((new Random()).nextInt(9999));
						
						String txtAttachment = buildTxtAttachment(boundary, id);
						String imgAttachment = buildImageAttachment(boundary, id);
						
						String attachments = String.format(XML_MSG_GETMESSAGE_ATTACHMENTS_SECTION, 
								boundary, txtAttachment + imgAttachment);
						
						String boundary2 = Integer.toString((new Random()).nextInt(Integer.MAX_VALUE));
						id = Integer.toString((new Random()).nextInt(9999));
						
						stringIs = new ByteArrayInputStream(String.format(XML_MSG_GETMESSAGE_BASE, 
								boundary2, id, xmlSection,
								boundary2, attachments, boundary2).getBytes());
						
						res = stringIs;
						
						//Response Headers
						if (responseHeaders != null){
							responseHeaders.put("Content-Type", MSG_GETMESSAGE_MAIN_BOUNDARY_START +  boundary2);
						}

					} else {	// GET MESSAGES
						regex =  "(.*)/" + (mMessageType==MessageType.SMS?FEED_SMS_BASE_INBOUND_URI_BLUEVIA:FEED_MMS_BASE_INBOUND_URI_BLUEVIA) + "/(.*)/" + MSG_BLUEVIA_RECEIVED_MSG_URIPATH +"/?$";
						pattern = Pattern.compile(regex);
						matcher = pattern.matcher(feedUri);

						if (matcher.matches()){
							// registrationId value corresponds to the 2nd group on the pattern
							registrationId = matcher.group(2);
							Log.e(TAG, "Registration ID: " + registrationId);

							if (registrationId == null)
								throw new HttpException("Wrong Uri. Registration Id is either null or empty", HttpException.NOT_FOUND,  null);
							// prepare ReceivedSMS mock stream
							String dateTimeSection = String.format(XML_MSG_RECEIVEDMESSAGE_DATETIME_SECTION, DATETIME );
							UserId destinationAddress = new UserId( UserId.Type.PHONE_NUMBER, DESTINATION_NUMBER );
							UserId originAddress = new UserId( UserId.Type.ALIAS, ORIGIN_ADDRESS );

							String messageSection = String.format(XML_MSG_RECEIVEDMESSAGE_MESSAGE_SECTION, 
											mMessageType==MessageType.SMS?XML_RECEIVEDSMS_MESSAGE_TAG:XML_RECEIVEDMMS_MESSAGE_ID_TAG,
											mMessageType==MessageType.SMS?MESSAGE:MESSAGE_IDENTIFIER,
											mMessageType==MessageType.SMS?XML_RECEIVEDSMS_MESSAGE_TAG:XML_RECEIVEDMMS_MESSAGE_ID_TAG);
							String subjectSection = String.format(XML_MSG_RECEIVEDMESSAGE_SUBJECT_SECTION, SUBJECT);
							
							//Attachment URLS
							String attachmentUris = "";
							String useAtt = parameters.getParameterValue(XmlConstants.PARAM_USE_ATTACHMENT_URLS);
							if (useAtt != null && useAtt.equals("true")){
								//Text attachment
								attachmentUris += String.format(XML_MSG_RECEIVEDMESSAGE_ATTACHMENT_URIS, 
										"attachment_1", MSG_GETMESSAGE_ATTACHMENT_TEXT_CT + " " + MSG_GETMESSAGE_ATTACHMENT_TEXT_FILENAME);
								
								//Image attachment
								attachmentUris += String.format(XML_MSG_RECEIVEDMESSAGE_ATTACHMENT_URIS, 
										"attachment_2", MSG_GETMESSAGE_ATTACHMENT_IMG_CT + " " + MSG_GETMESSAGE_ATTACHMENT_IMG_FILENAME);
							}
							
							StringBuffer commonReceivedMessageSection = new StringBuffer();
							commonReceivedMessageSection.append(String.format(XML_MSG_RECEIVEDMESSAGE_COMMON_SECTION,
									mMessageType==MessageType.SMS?XML_RECEIVEDSMS_TAG:XML_RECEIVEDMMS_TAG,
											messageSection,
											mMessageType==MessageType.SMS?"":subjectSection,
											buildUserIdSection(originAddress),
											buildUserIdSection(destinationAddress),
											dateTimeSection,
											attachmentUris,
											mMessageType==MessageType.SMS?XML_RECEIVEDSMS_TAG:XML_RECEIVEDMMS_TAG
							));
							
							stringIs = new ByteArrayInputStream(String.format(XML_MSG_MESSAGES_XML_BASE, 
									mMessageType==MessageType.SMS?XML_RECEIVEDSMS_TAG:XML_RECEIVEDMMS_TAG,
											mMessageType==MessageType.SMS?XML_SMS_SCHEMALOCATION:XML_MMS_SCHEMALOCATION,
													mMessageType==MessageType.SMS?XML_SMS_NAMESPACE:XML_MMS_NAMESPACE,
															commonReceivedMessageSection, 
															mMessageType==MessageType.SMS?XML_RECEIVEDSMS_TAG:XML_RECEIVEDMMS_TAG).getBytes());
							res = stringIs;

						} else throw new HttpException("Uri not found", HttpException.NOT_FOUND,  null);
							
					}
				}
			}

			break;
		case TEST_ERROR_IOEXCEPTION_ORDINAL:
			throw new IOException("Mock IO Exception launched");
		case TEST_ERROR_HTTPEXCEPTION_ORDINAL:
			throw new HttpException("Mock Http Exception launched", HttpException.INTERNAL_SERVER_ERROR, res);
		case TEST_UNAUTHORIZED_ORDINAL:
			throw new HttpException("Mock Unauthorized Exception launched", HttpException.UNAUTHORIZED, null);
		default:
			throw new IllegalArgumentException("Mode not supported: " + mTestType);
		}

		return res;
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.connector.mock.MockConnector#close()
	 */
	public void close() {
		if (mMsgIdsCache != null)
			mMsgIdsCache.clear();
	}


	protected String buildUserIdSection (UserId userId) {
		if (userId ==null || !userId.isValid())
			return null;
		switch (userId.getType()) {
		case PHONE_NUMBER:
			return "<uctr:phoneNumber>" + userId.getUserIdValue()+"</uctr:phoneNumber>";
		case ANY_URI:
			return "<uctr:anyUri>" +userId.getUserIdValue() +"</uctr:anyUri>";
		case ALIAS:
			return "<uctr:alias>" +userId.getUserIdValue() +"</uctr:alias>";
		case IPV4_ADDRESS:
			return "<uctr:ipAddress><uctr:ipv4>" +userId.getUserIdValue() +"</uctr:ipv4></uctr:ipAddress>";
		case IPV6_ADDRESS:
			return "<uctr:ipAddress><uctr:ipv6>" +userId.getUserIdValue() +"</uctr:ipv6></uctr:ipAddress>";
		case OTHER_ID:
			return "<uctr:otherId><uctr:type>" +userId.getOtherType() +"</uctr:type><uctr:value>" +userId.getUserIdValue() +"</uctr:value></uctr:otherId>";
		default:
			break;
		}
		return null;
	}
	
	protected String buildTxtAttachment(String boundary, String id){
		return 	String.format(XML_MSG_GETMESSAGE_ATTACHMENT, boundary, 
				MSG_GETMESSAGE_ATTACHMENT_TEXT_CT, MSG_GETMESSAGE_ATTACHMENT_TEXT_FILENAME, 
				id, MSG_GETMESSAGE_ATTACHMENT_TEXT);
	}
	
	protected String buildImageAttachment(String boundary, String id) throws IOException{
		ByteArrayOutputStream imageContent = createBitmap();
		ByteArrayInputStream bis = new ByteArrayInputStream(imageContent.toByteArray());
		return String.format(XML_MSG_GETMESSAGE_ATTACHMENT, boundary, 
				MSG_GETMESSAGE_ATTACHMENT_IMG_CT, MSG_GETMESSAGE_ATTACHMENT_IMG_FILENAME, 
				id, XmlParserSerializerUtils.convertStreamToString(bis));
	}

}
