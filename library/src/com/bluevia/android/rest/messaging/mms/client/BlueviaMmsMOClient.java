package com.bluevia.android.rest.messaging.mms.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import android.content.Context;
import android.util.Log;

import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.Utils;
import com.bluevia.android.rest.commons.client.AbstractRestClient;
import com.bluevia.android.rest.commons.connector.IConnector;
import com.bluevia.android.rest.commons.connector.http.HttpConnector;
import com.bluevia.android.rest.commons.connector.http.HttpException;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.mock.BlueviaMessageMockConnector;
import com.bluevia.android.rest.commons.connector.mock.BlueviaMessageMockConnector.MessageType;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.commons.parser.xml.XmlParserSerializerUtils;
import com.bluevia.android.rest.messaging.MessageryClientHelper;
import com.bluevia.android.rest.messaging.ReceivedMessageList;
import com.bluevia.android.rest.messaging.mms.data.MimeContent;
import com.bluevia.android.rest.messaging.mms.data.ReceivedMms;
import com.bluevia.android.rest.messaging.mms.data.ReceivedMmsInfo;
import com.bluevia.android.rest.messaging.mms.parser.BlueviaMmsParserFactory;
import com.bluevia.android.rest.messaging.mms.parser.xml.MultipartMmsParser;
import com.bluevia.android.rest.oauth.Oauth;

/**
 * Client interface for the REST binding of the Bluevia MMS Service.
 * 
 * @author Telefonica R&D
 * 
 */
public class BlueviaMmsMOClient extends MmsClient {

	///@cond private
	private static final String TAG = "BlueviaMmsMOClientnt";

	public static final String FEED_MMS_INBOUND_URI = "/inbound";
	public static final String RECEIVED_MMS_FEED_PATH = "/messages";
	public static final String ATTACHMENTS_FEED_PATH = "/attachments";
	///@endcond

	/**
	 * Creates a MmsClient object to be able to send MMS to the gSDP. This client is 
	 * designed to work with BlueVia's authentication pattern.
	 * 
	 * @param context the Android context of the application.
	 * @param mode the communication mechanism to communicate with the gSDP.
	 * @param consumerKey The oauth consumer key supplied by the developer.
	 * @param consumerSecret The oauth consumer secret supplied by the developer.
	 * @param accessToken The oauth access token returned by the getAccessToken call.
	 * 
	 * @throws BlueviaClientException if an exception occurs during the request.
	 */
	public BlueviaMmsMOClient(Context context, AbstractRestClient.Mode mode, String consumerKey, String consumerSecret) throws BlueviaClientException {
		super(context, (mode != Mode.SANDBOX) ? FEED_MMS_BASE_URI : FEED_MMS_SANDBOX_BASE_URI,
				isHttpMode(mode) ? new HttpConnector() : new BlueviaMessageMockConnector(mode.ordinal(), MessageType.MMS), 
						new Oauth(consumerKey, consumerSecret), new BlueviaMmsParserFactory());
	}

	/**
	 * 
	 * Retrieves the list of received messages without attachment ids.
	 * 
	 * @see getMessages(String registrationId, boolean useAttachmentsIds)
	 * 
	 * Note: the origin address of the received MMS will contain an alias, not a phone number.
	 * 
	 * @param registrationId the registration id (short number) that receives the messages
	 * @return the list of messages.
	 * @throws BlueviaClientException
	 */
	public ArrayList<ReceivedMmsInfo> getMessages(String registrationId) throws BlueviaClientException{
		return getMessages(registrationId, false);
	}

	/**
	 * Retrieves the list of received messages. Depending on the value of the useAttachmentsIds parameter, the response will
	 * include the IDs of the attachments or not. 
	 * If the ids are retrieved, the function 'getAttachment' can be used; otherwise, the attachments must be obtained throught the getMessage function.
	 * 
	 * @see getMessage(String registrationId, String messageId)
	 * @see getAttachment(String registrationId, String messageId, String attachmentId)
	 * 
	 * Note: the origin address of the received MMS will contain an alias, not a phone number.
	 * 
	 * @param registrationId the registration id (short number) that receives the messages
	 * @param useAttachmentIds the boolean parameter to retrieve the IDs of the attachments or not
	 * @return the list of Received MMSs (list will be empty if the are no messages)
	 * @throws BlueviaClientException
	 */
	public ArrayList<ReceivedMmsInfo> getMessages(String registrationId, boolean useAttachmentIds) throws BlueviaClientException{
		ArrayList<ReceivedMmsInfo> receivedMmsList = null;

		// Check params
		MessageryClientHelper.checkRegistrationId(registrationId);

		// Build feed uri for the request
		String feedUri = null;

		feedUri = mBaseUri + FEED_MMS_INBOUND_URI + MessageryClientHelper.URI_PATH_SEPARATOR
				+ registrationId + RECEIVED_MMS_FEED_PATH;

		HttpQueryParams parameters = null;
		parameters = new HttpQueryParams();
		if (useAttachmentIds)
			parameters.addParameter(XmlConstants.PARAM_USE_ATTACHMENT_URLS, "true");
		parameters.addParameter(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);    		

		try {
			Entity response = retrieveEntity(feedUri, parameters);

			receivedMmsList = getReceivedMmsListFromResponse(response);

		} catch (IOException iox) {
			throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);
		}

		return receivedMmsList;
	}

	/**
	 * Gets the content of a message with a 'messageIdentifier' sent to the 'registrationId'
	 * 
	 * @param registrationId
	 * @param messageId
	 * @return the ReceivedMms 
	 * @throws BlueviaClientException
	 */
	public ReceivedMms getMessage(String registrationId, String messageId) throws BlueviaClientException{

		// Check params
		MessageryClientHelper.checkRegistrationId(registrationId);

		if (Utils.isEmpty(messageId))
			throw new BlueviaClientException("Message identifier is either null or empty", BlueviaClientException.BAD_REQUEST_EXCEPTION);

		// Build feed uri for the request
		String feedUri = null;
		HttpQueryParams parameters = null;

		feedUri = mBaseUri + FEED_MMS_INBOUND_URI + MessageryClientHelper.URI_PATH_SEPARATOR
				+ registrationId + RECEIVED_MMS_FEED_PATH + MessageryClientHelper.URI_PATH_SEPARATOR
				+ messageId;

		parameters = new HttpQueryParams();
		parameters.addParameter(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);    		

		try {

			Entity response = this.retrieveEntity(feedUri, parameters);

			if (response == null || !(response instanceof ReceivedMms))
				throw new BlueviaClientException("Error during request. Response received does not correspond to an ReceivedMms",
					BlueviaClientException.INTERNAL_CLIENT_ERROR);
			
			return (ReceivedMms) response;

		} catch (IOException iox) {
			throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);
		}

	}

	/**
	 * 
	 * Gets the attachment with the specified id of the received message.
	 * 
	 * @param registrationId the registration id (short number) that receives the messages
	 * @param messageId the message id (obtained in getMessages function)
	 * @param attachmentId the attachment id (obtained in getMessages function)
	 * @return the attachment of the received MMS.
	 * @throws BlueviaClientException
	 */
	public MimeContent getAttachment(String registrationId, String messageId, String attachmentId) throws BlueviaClientException {

		// Check params
		MessageryClientHelper.checkRegistrationId(registrationId);

		if (Utils.isEmpty(messageId))
			throw new BlueviaClientException("Message identifier is either null or empty", BlueviaClientException.BAD_REQUEST_EXCEPTION);

		if (Utils.isEmpty(attachmentId))
			throw new BlueviaClientException("Attachment identifier is either null or empty", BlueviaClientException.BAD_REQUEST_EXCEPTION);

		// Build feed uri for the request
		String feedUri = null;
		HttpQueryParams parameters = new HttpQueryParams();
		parameters.addParameter(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);   

		feedUri = mBaseUri + FEED_MMS_INBOUND_URI + MessageryClientHelper.URI_PATH_SEPARATOR + registrationId 
				+ RECEIVED_MMS_FEED_PATH + MessageryClientHelper.URI_PATH_SEPARATOR + messageId
				+ ATTACHMENTS_FEED_PATH + MessageryClientHelper.URI_PATH_SEPARATOR + attachmentId;

		try {
			Entity response = this.retrieveEntity(feedUri, parameters);

			if (response == null || !(response instanceof MimeContent))
				throw new BlueviaClientException("Error during request. Response received does not correspond to an MimeContent",
					BlueviaClientException.INTERNAL_CLIENT_ERROR);
			
			return (MimeContent) response;
			
		} catch (IOException iox) {
			Log.e(TAG, "Error during IO", iox);
			throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);
		}

	}

	///@cond private
	/* (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.client.AbstractRestClient#retrieveEntity(java.lang.String, com.bluevia.android.rest.commons.connector.http.HttpQueryParams)
	 */
	@Override
	protected Entity retrieveEntity(String feedUri, HttpQueryParams parameters)
			throws ParseException, HttpException, IOException {

		if (feedUri.endsWith(BlueviaMmsMOClient.RECEIVED_MMS_FEED_PATH)){
			
			return super.retrieveEntity(feedUri, parameters);
			
		} else {
			
			HashMap<String, String> responseHeaders = new HashMap<String, String>();
			InputStream is = mConnector.retrieveEntity(feedUri, mAuthInfo, parameters, responseHeaders);

			try {
				
				String contentType = responseHeaders.get("Content-Type");
				String contentDisp = responseHeaders.get("Content-Disposition");

				MultipartMmsParser parser = new MultipartMmsParser(mFactory);

				if (feedUri.contains(BlueviaMmsMOClient.ATTACHMENTS_FEED_PATH)){
				
					if (contentType.contains("xml") || contentType.contains("smil") || contentType.contains("text"))
						return (Entity) parser.buildMimeContent(contentType, contentDisp, XmlParserSerializerUtils.convertStreamToString(is), true);
					else return (Entity) parser.buildMimeContent(contentType, contentDisp, is, true);

				} else {
					ByteArrayDataSource ds = new ByteArrayDataSource(is, contentType);
					MimeMultipart multipart = new MimeMultipart(ds);
					return (Entity) parser.parseMultipart(multipart);
				}
				
			} catch (MessagingException e) {
	    		throw new ParseException("Error parsing multipart: " + e.getLocalizedMessage(), e);
			} finally {
				if (is != null)
					is.close();
			}
		}
	}
	
	/**
	 * Gets the received mms list from the response Entity
	 * 
	 * @param response response Entity that contains the result
	 * @return the received mms list
	 * @throws BlueviaClientException if the response or the list are not valid.
	 */
	private ArrayList<ReceivedMmsInfo> getReceivedMmsListFromResponse(Entity response) throws BlueviaClientException{
		ArrayList<ReceivedMmsInfo> receivedMmsList = null;

		//If there are no messages, return empty list
		if (response == null)
			return new ArrayList<ReceivedMmsInfo>();

		//Check if response is instance of ReceivedSmsList
		if (!(response instanceof ReceivedMessageList))
			throw new BlueviaClientException("Error during request. Response received does not correspond to a ReceivedSmsList",
					BlueviaClientException.INTERNAL_CLIENT_ERROR);

		//Get received data
		receivedMmsList = ((ReceivedMessageList)response).getList();

		// Check list
		if (receivedMmsList == null)
			throw new BlueviaClientException("ReceivedSMS list is null",
					BlueviaClientException.INTERNAL_CLIENT_ERROR);

		return receivedMmsList;
	}

	/**
	 * 
	 * Creates a MmsClient object, with a explicit IConnector, to send MMSs and retrieve its status using the gSDP.
	 * 
	 * @param context the Android context of the application.
	 * @param connector the connector for communication mechanism.
	 * @param consumerKey The oauth consumer key supplied by the developer.
	 * @param consumerSecret The oauth consumer secret supplied by the developer.
	 * @param accessToken The oauth access token returned by the getAccessToken call.
	 * @throws BlueviaClientException
	 */
	protected BlueviaMmsMOClient(Context context, IConnector connector, String consumerKey, String consumerSecret) throws BlueviaClientException {
		super(context, FEED_MMS_BASE_URI, connector, new Oauth(consumerKey, consumerSecret), new BlueviaMmsParserFactory());
	}
	///@endcond
}
