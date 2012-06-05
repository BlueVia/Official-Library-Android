package com.bluevia.android.messagery.mo.mms.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bluevia.android.commons.parser.ParseException;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.messagery.mo.data.AbstractReceivedMessage;
import com.bluevia.android.messagery.mo.mms.data.MmsMessageInfo;
import com.bluevia.android.messagery.mo.parser.XmlReceivedMessagesParser;

/**
 * XML implementation of {@link com.bluevia.android.commons.parser.IParser IParser}
 * 
 * Class that represents the parser object for ReceivedMms entities.
 * Using this class you will be able to parse XML documents containing
 * a representation of ReceivedMms entities into an instance
 * object of this entity
 *
 * @author Telefonica R&D
 *  
 */
public class XmlReceivedMmsParser extends XmlReceivedMessagesParser {

    public XmlReceivedMmsParser() {
		super(XmlConstants.XSD_RECEIVEDMMS);
	}
    
	public MmsMessageInfo parseMmsInfo(XmlPullParser parser) throws ParseException, IOException {
    	AbstractReceivedMessage res = null;
		try {
			res = parseSingleReceivedMessageEntry(parser, XmlPullParser.START_TAG, XmlConstants.XSD_RECEIVEDMMS);
		} catch (XmlPullParserException e) {
			throw new ParseException("Unable to parse MmsMessageInfo", e);
		}
		
    	if (res == null || !(res instanceof MmsMessageInfo))
    		throw new ParseException("Unable to parse MmsMessageInfo");
    	
    	return (MmsMessageInfo) res;
	}
}
