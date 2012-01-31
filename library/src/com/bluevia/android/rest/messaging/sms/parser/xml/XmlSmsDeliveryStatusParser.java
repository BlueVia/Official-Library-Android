///@cond private
package com.bluevia.android.rest.messaging.sms.parser.xml;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;

import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.messaging.MessageDeliveryStatusList;
import com.bluevia.android.rest.messaging.parser.XmlDeliveryStatusParserHelper;



/**
 * Xml parser for XmlSmsDeliveryStatus entities.
 * 
 * @author Telefonica I+D
 * 
 */
class XmlSmsDeliveryStatusParser {

    private static final String TAG = "XmlSmsDeliveryStatusParser";

    /**
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParser#parseEntry(java.io.InputStream)
     */
    public MessageDeliveryStatusList parseEntry(XmlPullParser parser) throws ParseException, IOException {
    	
    	return XmlDeliveryStatusParserHelper.parseEntry(parser, XmlConstants.XSD_SMSDELIVERYSTATUS_STATUS);
    }

}
///@endcond
