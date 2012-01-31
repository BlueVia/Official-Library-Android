///@cond private
package com.bluevia.android.rest.messaging.sms.parser.xml;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;

import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.messaging.ReceivedMessageList;
import com.bluevia.android.rest.messaging.parser.XmlReceivedMessageParserHelper;

public class XmlReceivedSmsParser {

    private static final String TAG = "XmlSmsReceivedSmsParser";
    
    
    /**
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParser#parseEntry(java.io.InputStream)
     */
	public ReceivedMessageList parseEntry(XmlPullParser parser) throws ParseException, IOException {
		return XmlReceivedMessageParserHelper.parseReceivedMessageListEntry(parser, XmlConstants.XSD_RECEIVEDSMS);
	}

	
}
///@endcond
