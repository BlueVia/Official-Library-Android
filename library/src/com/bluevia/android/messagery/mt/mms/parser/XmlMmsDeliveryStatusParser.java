package com.bluevia.android.messagery.mt.mms.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;

import com.bluevia.android.commons.parser.ParseException;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.messagery.mt.data.DeliveryInfoList;
import com.bluevia.android.messagery.mt.parser.XmlDeliveryStatusParserHelper;

/**
 * Xml parser for XmlMmsDeliveryStatus entities.
 * 
 * @author Telefonica I+D
 * 
 */
public class XmlMmsDeliveryStatusParser {

	/**
	 * @see com.bluevia.android.commons.parser.IParser#parse(java.io.InputStream)
	 */
	public DeliveryInfoList parseEntry(XmlPullParser parser) throws ParseException, IOException {

        return XmlDeliveryStatusParserHelper.parseEntry(parser, XmlConstants.XSD_MMSDELIVERYSTATUS_STATUS);
	}

}
