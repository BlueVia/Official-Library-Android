/**
 * \package com.bluevia.android.messagery.mt.mms This package contains the classes in order to semd MMS using Bluevia API.
 * \package com.bluevia.android.messagery.mt.mms.client This package contains REST client to send MMS using Bluevia API.
 */
package com.bluevia.android.messagery.mt.mms.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

import com.bluevia.android.commons.Encoding;
import com.bluevia.android.commons.Utils;
import com.bluevia.android.commons.connector.GenericResponse;
import com.bluevia.android.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.android.commons.connector.http.multipart.FilePart;
import com.bluevia.android.commons.connector.http.multipart.PartContainer;
import com.bluevia.android.commons.connector.http.multipart.StringPart;
import com.bluevia.android.commons.connector.http.oauth.IOAuth;
import com.bluevia.android.commons.data.UserId;
import com.bluevia.android.commons.data.UserId.Type;
import com.bluevia.android.commons.exception.BlueviaException;
import com.bluevia.android.commons.exception.ConnectorException;
import com.bluevia.android.commons.parser.xml.BVRestXmlErrorParser;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.messagery.commons.parser.XmlMmsParser;
import com.bluevia.android.messagery.mt.client.BVMtClient;
import com.bluevia.android.messagery.mt.mms.data.Attachment;
import com.bluevia.android.messagery.mt.mms.data.MmsMessageReq;
import com.bluevia.android.messagery.mt.mms.parser.XmlMmsSerializer;

/**
 * 
 * Abstract client interface for the REST binding of the Bluevia MMS MT Service.
 * 
 * @author Telefonica R&D
 *
 */
public abstract class BVMtMmsClient extends BVMtClient {

	private static final String TAG = "BVMtMmsClient";

	/**
	 * Initializer for common attributes
	 */
	protected void init(){
		mEncoding = Encoding.APPLICATION_XML;
		mParser = new XmlMmsParser();
		mSerializer = new XmlMmsSerializer();
		mErrorParser = new BVRestXmlErrorParser();
	}

	/**
	 *
	 * Allows to know the delivery status of a previous sent MMS using Bluevia API
	 * @param mmsId the id of the mms previously sent using this API
	 * @return the delivery status of the MMS message id
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected String sendMms(MmsMessageReq mmsMessage) throws BlueviaException, IOException {
		String result = null;

		//Set the token for origin address if no exists
		if (mmsMessage != null && mmsMessage.getOriginAddress() == null){
			UserId originAddress = new UserId(Type.ALIAS, ((IOAuth)mConnector).getOauthToken().getToken());
			mmsMessage.setOriginAddress(originAddress);
		}

		checkMessage(mmsMessage);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		String uri = mBaseUri;

		GenericResponse response = null;
		InputStream is = null;

		try {

			BlueviaPartBase[] parts = buildMultipart(mmsMessage);

			response = mConnector.create(uri, params, parts, null);

			is = response.getAdditionalData().getBody();

			result = getMesageIdFromResponse(response.getAdditionalData().getHeaders(), uri);

		} catch (ConnectorException e){
			if (e.getAdditionalData().getBody() == null)
				throw e;
			else {
				is = e.getAdditionalData().getBody();
				String error = parseError(is);
				throw new ConnectorException(error, e.getCode());
			}
		} finally {
			//Close the stream
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					Log.i(TAG, e.getMessage(), e);
				}      	
		}

		return result;

	}

	/**
	 * TODO do this in a MultipartSerializer
	 * 
	 * @param message
	 * @return
	 * @throws BlueviaException
	 * @throws IOException
	 */
	private BlueviaPartBase[] buildMultipart(MmsMessageReq message) throws BlueviaException, IOException {

		byte[] bytes = serializeEntity(message);

		String entryString = new String(bytes, "UTF-8");

		//Build root-field
		StringPart rootField = new StringPart("root-fields", entryString);

		switch (mEncoding) {
		case APPLICATION_XML:
			rootField.setContentType(BlueviaPartBase.APPLICATION_XML);
			break;
		case APPLICATION_JSON:
			rootField.setContentType(BlueviaPartBase.APPLICATION_JSON);
			break;
		case APPLICATION_URL_ENCODED:
			rootField.setContentType(BlueviaPartBase.APPLICATION_URL_ENCODED);
		}

		BlueviaPartBase[] multipart = new BlueviaPartBase[2];
		multipart[0] = rootField;

		ArrayList<BlueviaPartBase> attachmentList = message.getAttachementList();

		BlueviaPartBase[] parts = attachmentList.toArray(new BlueviaPartBase[attachmentList.size()]);
		for (int i=0; i<parts.length; i++) {
			if (parts[i] instanceof StringPart)
				((StringPart) parts[i]).setFilename(parts[i].getName());
			parts[i].setName("attachment");
			parts[i].setContentDisposition("form-data");
		}

		PartContainer part_container = new PartContainer("attachments", parts);
		multipart[1] = part_container;

		return multipart;
	}

	/**
	 * Converts a list of Attachment objects to BlueviaPartBase list
	 * 
	 * @param attachments the list of Attachments to convert
	 * @return the list of Attachment as BlueviaPartBase list
	 * @throws BlueviaException 
	 */
	protected ArrayList<BlueviaPartBase> attachmentToParts(ArrayList<Attachment> attachments) throws BlueviaException {

		if (attachments == null)
			return null;

		ArrayList<BlueviaPartBase> parts = new ArrayList<BlueviaPartBase>(attachments.size());

		for (Attachment attachment : attachments){

			try {
				File f = new File(attachment.getFilePath());
				parts.add(new FilePart(f.getName(), f, attachment.getStringContentType()));

			} catch (FileNotFoundException e){
				Log.e(TAG, e.getMessage(), e);
				throw new BlueviaException("The path given " + attachment.getFilePath() + " is not correct.", e, BlueviaException.INVALID_PATH_EXCEPTION);
			}

		}

		return parts;

	}

	/**
	 * Adds the message parameter as a string attachment
	 * 
	 * @param message
	 * @param attachmentList
	 * @throws IOException
	 */
	protected void addMessageToAttachments(String message, ArrayList<BlueviaPartBase> attachmentList) throws IOException{
		if (!Utils.isEmpty(message)){
			if (attachmentList == null)
				attachmentList = new ArrayList<BlueviaPartBase>();
			attachmentList.add(new StringPart("message.txt", message));
		}
	}

}
