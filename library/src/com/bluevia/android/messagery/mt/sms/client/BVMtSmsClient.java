/**
 * \package com.bluevia.android.messagery.mt.sms This package contains the classes in order to semd SMS using Bluevia API.
 * \package com.bluevia.android.messagery.mt.sms.client This package contains REST client to send SMS using Bluevia API.
 */
package com.bluevia.android.messagery.mt.sms.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import android.util.Log;

import com.bluevia.android.commons.Encoding;
import com.bluevia.android.commons.GsdpConstants;
import com.bluevia.android.commons.connector.GenericResponse;
import com.bluevia.android.commons.connector.http.oauth.IOAuth;
import com.bluevia.android.commons.data.UserId;
import com.bluevia.android.commons.data.UserId.Type;
import com.bluevia.android.commons.exception.BlueviaException;
import com.bluevia.android.commons.exception.ConnectorException;
import com.bluevia.android.commons.parser.xml.BVRestXmlErrorParser;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.messagery.commons.parser.XmlSmsParser;
import com.bluevia.android.messagery.mt.client.BVMtClient;
import com.bluevia.android.messagery.mt.sms.data.Sms;
import com.bluevia.android.messagery.mt.sms.parser.xml.XmlSmsSerializer;

/**
 * 
 * Abstract client interface for the REST binding of the Bluevia SMS MT Service.
 * 
 * @author Telefonica R&D
 *
 */
public abstract class BVMtSmsClient extends BVMtClient {

	private static final String TAG = "BVMtSmsClient";

	/**
	 * Initializer for common attributes
	 */
	protected void init(){
		mEncoding = Encoding.APPLICATION_XML;
		mParser = new XmlSmsParser();
		mSerializer = new XmlSmsSerializer();
		mErrorParser = new BVRestXmlErrorParser();
	}

	/**
	 * Allows to send and SMS to the gSDP.  It returns a String containing the SMSID of the sent SMS,
	 * in order to ask later for the status of the message.
	 * The max length of the message must be less than 160 characters.
	 *
	 * @param message the message to send via SMS. Message includes both message and message properties like senders, etc
	 * @return the sent SMS ID
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected String sendSmsMessage(Sms message) throws BlueviaException, IOException {
		String result = null;

		//Set the token for origin address if no exists
		if (message != null && message.getOriginAddress() == null){
			UserId originAddress = new UserId(Type.ALIAS, ((IOAuth)mConnector).getOauthToken().getToken());
			message.setOriginAddress(originAddress);
		}

		checkMessage(message);

		GenericResponse response = null;
		InputStream is = null;

		try {

			HashMap<String, String> params = new HashMap<String, String>();
			params.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

			byte[] bytes = serializeEntity(message);

			String uri = mBaseUri;
			
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put(GsdpConstants.HEADER_CONTENT_TYPE, getEncoding(mEncoding));

			response = mConnector.create(uri, params, bytes, headers);

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

}
