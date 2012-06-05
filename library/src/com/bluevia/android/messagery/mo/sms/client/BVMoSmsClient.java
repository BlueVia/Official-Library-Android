/**
 * \package com.bluevia.android.messagery.mo.sms This package contains the classes in order to receive SMS using Bluevia API.
 * \package com.bluevia.android.messagery.mo.sms.client This package contains REST client to receive SMS using Bluevia API.
 */
package com.bluevia.android.messagery.mo.sms.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.bluevia.android.commons.Encoding;
import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.exception.BlueviaException;
import com.bluevia.android.commons.parser.xml.BVRestXmlErrorParser;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.messagery.commons.parser.XmlSmsParser;
import com.bluevia.android.messagery.mo.client.BVMoClient;
import com.bluevia.android.messagery.mo.data.ReceivedMessageList;
import com.bluevia.android.messagery.mo.sms.data.SmsMessage;

/**
 * 
 * Abstract client interface for the REST binding of the Bluevia SMS MO Service.
 * 
 * @author Telefonica R&D
 *
 */
public abstract class BVMoSmsClient extends BVMoClient {

	/**
	 * Initializer of common attributes
	 */
	protected void init(){
		mEncoding = Encoding.APPLICATION_XML;
		mParser = new XmlSmsParser();
		mSerializer = null;	//No serializer
		mErrorParser = new BVRestXmlErrorParser();
	}

	/**
	 * 
	 * Allow to request for the list of the received SMSs for the app provisioned and authorized
	 * 
	 * @param registrationId The Bluevia service number for your country, including the country code without the + symbol
	 * @return the list of the received messages
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public ArrayList<SmsMessage> getAllMessages(String registrationId) throws BlueviaException, IOException {
		ArrayList<SmsMessage> receivedSmsList = null;

		// Check params
		checkRegistrationId(registrationId);

		// Build feed uri for the request
		String feedUri = "/" + registrationId + RECEIVED_MESSAGES;

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);    		

		Entity response = retrieve(feedUri, parameters);

		receivedSmsList = getReceivedSmsListFromResponse(response);

		return receivedSmsList;
	}

	/**
	 * Gets the received SMS list from the response Entity
	 * 
	 * @param response response Entity that contains the result
	 * @return the received SMS list
	 * @throws BlueviaException if the response or the list are not valid.
	 */
	private ArrayList<SmsMessage> getReceivedSmsListFromResponse(Entity response) throws BlueviaException{
		ArrayList<SmsMessage> receivedSmsList = null;


		//If there are no messages, return empty list
		if (response == null)
			return new ArrayList<SmsMessage>();

		//Check if response is instance of ReceivedSmsList
		if ((response == null) || (! (response instanceof ReceivedMessageList)))
			throw new BlueviaException("Error during request. Response received does not correspond to a " + 
		ReceivedMessageList.class.getName(), BlueviaException.INTERNAL_CLIENT_ERROR);

		//Get received data
		receivedSmsList = ((ReceivedMessageList)response).getList();

		// Check list
		if (receivedSmsList == null)
			throw new BlueviaException(ReceivedMessageList.class.getName() + " object is null", BlueviaException.INTERNAL_CLIENT_ERROR);

		return receivedSmsList;
	}

}
