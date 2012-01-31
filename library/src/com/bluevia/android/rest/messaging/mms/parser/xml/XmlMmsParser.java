///@cond private
/**
 * \package com.bluevia.android.rest.messaging.mms.parser.xml This package contains the classes in order to send SMS using Bluevia API.
 */
package com.bluevia.android.rest.messaging.mms.parser.xml;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParser;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;

/**
 * XML implementation of {@link com.bluevia.android.rest.commons.parser.BlueviaEntityParser BlueviaEntityParser}
 * Class that represents the parser object for any MMS entity.
 * Using this class you will be able to parse XML documents containing
 * a representation of any MMS entity into an instance
 * object of this entity
 *
 * @author Telefonica R&D
 * 
 */
public class XmlMmsParser implements BlueviaEntityParser {

	private static final int DELIVERY_STATUS = 0;
	private static final int RECEIVED_MESSAGES = 1;
	private static final int RECEIVED_MESSAGE = 2;

	private static final String TAG = "XmlSmsParser";

	private static final HashMap<String, Integer> sParserMatcher;

	static {
		sParserMatcher = new HashMap<String, Integer>();
		sParserMatcher.put(XmlConstants.XSD_MMSDELIVERYSTATUS_STATUS, DELIVERY_STATUS);
		sParserMatcher.put(XmlConstants.XSD_RECEIVEDMMS, RECEIVED_MESSAGES);
		sParserMatcher.put(XmlConstants.XSD_MMSTEXTTYPE_MMSMESSAGE, RECEIVED_MESSAGE);
	}

	/**
	 * @see {@link com.bluevia.android.rest.commons.parser.BlueviaEntityParser com.bluevia.android.rest.parser.BlueviaEntityParser#parseEntry()}
	 */
	public Entity parseEntry(InputStream is) throws ParseException, IOException {

		//Check first that is is not null
		if (is == null) {
			return null;
		}
		
		Entity retEntity = null;

		// Type of the parser to be detected
		int parserType = 0;

		XmlPullParser parser = Xml.newPullParser();

		try {
			parser.setInput(is, null);
		} catch (XmlPullParserException e){
			Log.e(TAG, "Exception occurred while reading the XML Document", e);
			throw new ParseException("Could not create XmlSmsParser", e);    		
		}

		int eventType;

		// Start parsing
		try {
			eventType = parser.getEventType();
		} catch (XmlPullParserException e){
			throw new ParseException("Could not parse Sms entity.", e);    		
		}
		if (eventType != XmlPullParser.START_DOCUMENT) {
			throw new ParseException("", new XmlPullParserException("Attempting to initialize parsing beyond "
					+ "the start of the document."));
		}

		// Advance to next event
		try {
			eventType = parser.next();
		} catch (XmlPullParserException xppe) {
			Log.e(TAG,"XmlPullParserException getting next event");
			throw new ParseException("Could not parse Sms entity.", xppe);
		} catch (IOException ioe) {
			Log.e(TAG,"IOException getting next event");
			throw new ParseException("Error during IO", ioe);
		}

		// Get name of first element found
		if (eventType == XmlPullParser.START_TAG){
			String nameSpace = parser.getName();
			Integer type = sParserMatcher.get(nameSpace);
			if (type == null)
				throw new ParseException("Could not parse entity.");
			parserType = type.intValue();
		}

		switch (parserType){
		case DELIVERY_STATUS:
			Log.e(TAG, "Entering Delivery Status Parser");
			XmlMmsDeliveryStatusParser xmdsp = new XmlMmsDeliveryStatusParser();
			if (xmdsp != null) 
				retEntity = xmdsp.parseEntry(parser);
			break;
		case RECEIVED_MESSAGES:
			Log.e(TAG, "Entering Received Mms list Parser");
			XmlReceivedMmsListParser xrmlp = new XmlReceivedMmsListParser();
			if (xrmlp != null) 
				retEntity = xrmlp.parseEntry(parser);
			break;
		case RECEIVED_MESSAGE:
			Log.e(TAG, "Entering Received Mms Parser");
			XmlReceivedMmsParser xrmp = new XmlReceivedMmsParser();
			if (xrmp != null) 
				retEntity = xrmp.parseEntry(parser);
			break;
		default:
			throw new ParseException(new XmlPullParserException("Entity class does not support parsing"));
		}

		return retEntity;
	}

}
///@endcond