package com.bluevia.android.messagery.mt.sms.parser.xml;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;

import com.bluevia.android.commons.parser.ParseException;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.messagery.mt.data.DeliveryInfoList;
import com.bluevia.android.messagery.mt.parser.XmlDeliveryStatusParserHelper;



/**
 * Xml parser for XmlSmsDeliveryStatus entities.
 * 
 * @author Telefonica I+D
 * 
 */
public class XmlSmsDeliveryStatusParser {

    /**
     * @see com.bluevia.android.commons.parser.IParser
     */
    public DeliveryInfoList parseEntry(XmlPullParser parser) throws ParseException, IOException {
    	return XmlDeliveryStatusParserHelper.parseEntry(parser, XmlConstants.XSD_SMSDELIVERYSTATUS_STATUS);
    }

}
