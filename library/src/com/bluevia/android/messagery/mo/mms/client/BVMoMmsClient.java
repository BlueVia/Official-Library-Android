/**
 * \package com.bluevia.android.messagery.mo.mms This package contains the classes in order to receive MMS using Bluevia API.
 * \package com.bluevia.android.messagery.mo.mms.client This package contains REST client to receive MMS using Bluevia API.
 */
package com.bluevia.android.messagery.mo.mms.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.bluevia.android.commons.Encoding;
import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.Utils;
import com.bluevia.android.commons.connector.GenericResponse;
import com.bluevia.android.commons.exception.BlueviaException;
import com.bluevia.android.commons.exception.ConnectorException;
import com.bluevia.android.commons.parser.ParseException;
import com.bluevia.android.commons.parser.xml.BVRestXmlErrorParser;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.messagery.commons.parser.XmlMmsParser;
import com.bluevia.android.messagery.mo.client.BVMoClient;
import com.bluevia.android.messagery.mo.data.ReceivedMessageList;
import com.bluevia.android.messagery.mo.mms.data.MimeContent;
import com.bluevia.android.messagery.mo.mms.data.MmsMessage;
import com.bluevia.android.messagery.mo.mms.data.MmsMessageInfo;
import com.bluevia.android.messagery.mo.mms.parser.MultipartMmsParser;

/**
 * 
 * Abstract client interface for the REST binding of the Bluevia MMS MO Service.
 * 
 * @author Telefonica R&D
 *
 */
public abstract class BVMoMmsClient extends BVMoClient {

	private static final String ATTACHMENTS_FEED_PATH = "/attachments";

	private MultipartMmsParser mMultipartParser;

	/**
	 * Initializer of common attributes
	 */
	protected void init(){
		mEncoding = Encoding.APPLICATION_XML;

		mParser = new XmlMmsParser();
		mMultipartParser = new MultipartMmsParser(mParser);

		mSerializer = null;	//No serializer
		mErrorParser = new BVRestXmlErrorParser();
	}

	/**
	 * 
	 * Retrieves the list of received messages without attachment ids.
	 * 
	 * @see getAllMessages(String registrationId, boolean attachUrl)
	 * 
	 * Note: the origin address of the received MMS will contain an alias, not a phone number.
	 * 
	 * @param registrationId the registration id (short number) that receives the messages
	 * @return the list of messages.
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public ArrayList<MmsMessageInfo> getAllMessages(String registrationId) throws BlueviaException, IOException{
		return getAllMessages(registrationId, false);
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
	 * @param attachUrl the boolean parameter to retrieve the IDs of the attachments or not
	 * @return the list of Received MMSs (list will be empty if the are no messages)
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public ArrayList<MmsMessageInfo> getAllMessages(String registrationId, boolean attachUrl) throws BlueviaException, IOException{
		ArrayList<MmsMessageInfo> receivedMmsList = null;

		// Check params
		checkRegistrationId(registrationId);

		// Build feed uri for the request
		String feedUri = "/" + registrationId + RECEIVED_MESSAGES;


		HashMap<String, String> parameters = new HashMap<String, String>();
		if (attachUrl)
			parameters.put(XmlConstants.PARAM_USE_ATTACHMENT_URLS, "true");
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);    		

		Entity response = retrieve(feedUri, parameters);

		receivedMmsList = getReceivedMmsListFromResponse(response);

		return receivedMmsList;
	}

	/**
	 * Gets the received MMS list from the response Entity
	 * 
	 * @param response response Entity that contains the result
	 * @return the received MMS list
	 * @throws BlueviaException if the response or the list are not valid.
	 */
	private ArrayList<MmsMessageInfo> getReceivedMmsListFromResponse(Entity response) throws BlueviaException{
		ArrayList<MmsMessageInfo> receivedMmsList = null;

		//If there are no messages, return empty list
		if (response == null)
			return new ArrayList<MmsMessageInfo>();

		//Check if response is instance of ReceivedSmsList
		if (!(response instanceof ReceivedMessageList))
			throw new BlueviaException("Error during request. Response received does not correspond to a " 
					+ ReceivedMessageList.class.getName(), BlueviaException.INTERNAL_CLIENT_ERROR);

		//Get received data
		receivedMmsList = ((ReceivedMessageList)response).getList();

		// Check list
		if (receivedMmsList == null)
			throw new BlueviaException(ReceivedMessageList.class.getName() + " object is null", BlueviaException.INTERNAL_CLIENT_ERROR);

		return receivedMmsList;
	}


	/**
	 * Gets the content of a message with a 'messageId' sent to the 'registrationId'
	 * 
	 * @param registrationId the registration id (short number) that receives the messages
	 * @param messageId the message id (obtained in getAllMessages function)
	 * @return the MmsMessage  the complete MmsMessage (including attachments)
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public MmsMessage getMessage(String registrationId, String messageId) throws BlueviaException, IOException{

		// Check params
		checkRegistrationId(registrationId);

		if (Utils.isEmpty(messageId))
			throw new BlueviaException("Bad request: Message identifier is either null or empty", BlueviaException.BAD_REQUEST_EXCEPTION);

		// Build feed uri for the request
		String feedUri = "/" + registrationId + RECEIVED_MESSAGES +
				"/" + messageId;

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);    		

		MmsMessage res = null;
		InputStream is = null;

		try {
			
			String uri = mBaseUri + feedUri;

			GenericResponse response = mConnector.retrieve(uri, parameters);

			is = response.getAdditionalData().getBody();
			HashMap<String, String> responseHeaders = response.getAdditionalData().getHeaders();

			String contentType = responseHeaders.get("Content-Type");

			ByteArrayDataSource ds = new ByteArrayDataSource(is, contentType);
			MimeMultipart multipart = new MimeMultipart(ds);
			res = mMultipartParser.parseMultipart(multipart);

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
		} catch (MessagingException e) {
			throw new ParseException("Unable to parse multpart", e);
		} finally {
			closeStream(is);
		}

		return res;
	}

	/**
	 * 
	 * Gets the attachment with the specified id of the received message.
	 * 
	 * @param registrationId the registration id (short number) that receives the messages
	 * @param messageId the message id (obtained in getAllMessages function)
	 * @param attachmentId the attachment id (obtained in getAllMessages function)
	 * @return the attachment of the received MMS.
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public MimeContent getAttachment(String registrationId, String messageId, String attachmentId) throws BlueviaException, IOException {

		// Check params
		checkRegistrationId(registrationId);

		if (Utils.isEmpty(messageId))
			throw new BlueviaException("Bad request: Message identifier is either null or empty", BlueviaException.BAD_REQUEST_EXCEPTION);

		if (Utils.isEmpty(attachmentId))
			throw new BlueviaException("Bad request: Attachment identifier is either null or empty", BlueviaException.BAD_REQUEST_EXCEPTION);

		// Build feed uri for the request
		String feedUri = "/" + registrationId + 
				RECEIVED_MESSAGES + "/" + messageId +
				ATTACHMENTS_FEED_PATH + "/" + attachmentId;


		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);   

		Entity res = null;
		InputStream is = null;

		try {
			
			String uri = mBaseUri + feedUri;

			GenericResponse response = mConnector.retrieve(uri, parameters);

			is = response.getAdditionalData().getBody();
			HashMap<String, String> responseHeaders = response.getAdditionalData().getHeaders();

			String contentType = responseHeaders.get("Content-Type");
			String contentDisp = responseHeaders.get("Content-Disposition");
			
			if (contentType.contains("xml") || contentType.contains("smil") || contentType.contains("text"))
				res = mMultipartParser.buildMimeContent(contentType, contentDisp, Utils.convertStreamToString(is), true);
			else res = mMultipartParser.buildMimeContent(contentType, contentDisp, is, true);
		
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

		if (res == null || !(res instanceof MimeContent))
			throw new BlueviaException("Error during request. Response received does not correspond to a "
					+ MimeContent.class.getName(), BlueviaException.INTERNAL_CLIENT_ERROR);

		return (MimeContent) res;

	}

}
