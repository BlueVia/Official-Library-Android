package com.bluevia.android.commons.parser.xml;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.data.StringEntity;
import com.bluevia.android.commons.parser.IParser;
import com.bluevia.android.commons.parser.ParseException;

/**
 * Implementation of IParser to parse XML error responses from the Bluevia gSDP into a StringEntity
 * 
 * @author Telefonica R&D
 *
 */
public class BVRestXmlErrorParser implements IParser {

	private static final String TAG = "BVRestXmlErrorParser";

	@Override
	public Entity parse(InputStream is) throws ParseException {
		
		if (is == null)
			return null;

		String text = null;

		// init the parser
		XmlPullParser parser = Xml.newPullParser();

		try {
			parser.setInput(is, null);
		} catch (XmlPullParserException e) {
			Log.e(TAG, "Exception occurred while reading the XML Document", e);
			return null;
		}

		// Start the parse process
		int eventType;

		try {
			eventType = parser.getEventType();
		} catch (XmlPullParserException e) {
			return null;
		}

		if (eventType != XmlPullParser.START_DOCUMENT) {
			return null;
		}

		try {
			// Gets the first eventType
			eventType = parser.next();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
				if (eventType == XmlPullParser.START_TAG &&
						name.equals(XmlConstants.XSD_XSD_CLIENT_EXCEPTION_ATTR_TEXT))
					text = XmlParserSerializerUtils.getChildText(parser);
				eventType = parser.next();
			}
		} catch (XmlPullParserException xppe) {
			return null;
		} catch (IOException ioe) {
			return null;
		}

		StringEntity entity = new StringEntity(text);

		return entity;
	}

}
