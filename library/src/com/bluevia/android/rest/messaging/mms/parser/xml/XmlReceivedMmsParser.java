///@cond private
package com.bluevia.android.rest.messaging.mms.parser.xml;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.messaging.AbstractReceivedMessage;
import com.bluevia.android.rest.messaging.mms.data.ReceivedMmsInfo;
import com.bluevia.android.rest.messaging.parser.XmlReceivedMessageParserHelper;

public class XmlReceivedMmsParser {

    private static final String TAG = "XmlReceivedMmsParser";
    
    
    /**
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParser#parseEntry(java.io.InputStream)
     */
	public ReceivedMmsInfo parseEntry(XmlPullParser parser) throws ParseException, IOException {
    	AbstractReceivedMessage res = null;
		try {
			res = XmlReceivedMessageParserHelper.parseSingleReceivedMessageEntry(parser, XmlPullParser.START_TAG, XmlConstants.XSD_RECEIVEDMMS);
		} catch (XmlPullParserException e) {
			throw new ParseException("Unable to parse ReceivedMmsInfo", e);
		}
		
    	if (res == null || !(res instanceof ReceivedMmsInfo))
    		throw new ParseException("Unable to parse ReceivedMmsInfo");
    	
    	return (ReceivedMmsInfo) res;
	}
}
///@endcond
