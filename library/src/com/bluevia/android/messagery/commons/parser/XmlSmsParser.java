package com.bluevia.android.messagery.commons.parser;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.parser.IParser;
import com.bluevia.android.commons.parser.ParseException;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.messagery.mo.parser.XmlReceivedMessagesParser;
import com.bluevia.android.messagery.mt.sms.parser.xml.XmlSmsDeliveryStatusParser;

/**
 * XML implementation of {@link com.bluevia.android.commons.parser.IParser IParser}
 * Class that represents the parser object for any SMS entity.
 * Using this class you will be able to parse XML documents containing
 * a representation of any SMS entity into an instance
 * object of this entity
 *
 * @author Telefonica R&D
 * 
 */
public class XmlSmsParser implements IParser {

	private static final int DELIVERY_STATUS = 0;
	private static final int RECEIVED_SMS = 1;

	private static final String TAG = "XmlSmsParser";

	private static final HashMap<String, Integer> sParserMatcher;

	//TODO
	static {
		sParserMatcher = new HashMap<String, Integer>();
		sParserMatcher.put(XmlConstants.XSD_SMSDELIVERYSTATUS_STATUS, DELIVERY_STATUS);
		sParserMatcher.put(XmlConstants.XSD_RECEIVEDSMS, RECEIVED_SMS);
	}

	/**
	 * @see {@link com.bluevia.android.commons.parser.IParser IParser#parse()}
	 */
	public Entity parse(InputStream is) throws ParseException {

		Entity retEntity = null;

		// Type of the parser to be detected
		int parserType = 0;

		// Pull parser to do xml processing
		XmlPullParser parser = null;
		if (is == null) {
			return null;
		}
		parser = Xml.newPullParser();
		try {
			parser.setInput( is, null );
		} catch (XmlPullParserException e){
			Log.e(TAG, "Exception occurred while reading the XML Document", e);
			throw new ParseException("Could not create XmlSmsParser", e);    		
		}

		// Xml pull parser event type detected
		int eventType;

		// Start parsing
		try {
			eventType = parser.getEventType();
			Log.d(TAG, "eventTypeA " + eventType);
		} catch (XmlPullParserException e){
			Log.e(TAG, "Could not parse Sms entity");
			throw new ParseException("Could not parse Sms entity.", e);    		
		}
		if (eventType != XmlPullParser.START_DOCUMENT) {
			Log.e(TAG, "Attempting to initialize parsing beyond " + "the start of the document.");
			throw new ParseException("", new XmlPullParserException("Attempting to initialize parsing beyond "
					+ "the start of the document."));
		}

		// Advance to next event
		try {
			eventType = parser.next();
			Log.d(TAG, "eventTypeB " + eventType);
		} catch (XmlPullParserException xppe) {
			Log.e(TAG,"XmlPullParserException getting next event");
			throw new ParseException("Could not parse entity.", xppe);
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

		try {
			switch (parserType){
			case DELIVERY_STATUS:
				Log.e(TAG, "Entering Delivery Status Parser");
				XmlSmsDeliveryStatusParser xsdsp = new XmlSmsDeliveryStatusParser();
				if (xsdsp != null) 
					retEntity = xsdsp.parseEntry(parser);
				break;
			case RECEIVED_SMS:
                Log.e(TAG, "Entering Received Sms Parser");
                XmlReceivedMessagesParser xsrsp = new XmlReceivedMessagesParser(XmlConstants.XSD_RECEIVEDSMS);
                if (xsrsp != null) 
                	retEntity = xsrsp.parseEntry(parser);
                break;
			default:
				throw new ParseException(new XmlPullParserException("Entity class does not support parsing"));
			}
		} catch (IOException e){
			throw new ParseException(e);
		}

		return retEntity;
	}
}
