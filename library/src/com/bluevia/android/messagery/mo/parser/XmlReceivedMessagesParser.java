package com.bluevia.android.messagery.mo.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.bluevia.android.commons.data.UserId;
import com.bluevia.android.commons.parser.ParseException;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.commons.parser.xml.XmlParserSerializerUtils;
import com.bluevia.android.messagery.mo.data.AbstractReceivedMessage;
import com.bluevia.android.messagery.mo.data.ReceivedMessageList;
import com.bluevia.android.messagery.mo.mms.data.MmsMessageInfo;
import com.bluevia.android.messagery.mo.mms.data.MmsMessageInfo.AttachmentInfo;
import com.bluevia.android.messagery.mo.sms.data.SmsMessage;

/**
 * 
 *
 */
public class XmlReceivedMessagesParser {

	private static final String TAG = "XmlSmsReceivedSmsParser";

	private String mMessageType;

	public XmlReceivedMessagesParser(String api){
		mMessageType = api;
	}

	/**
	 * @see com.bluevia.android.commons.parser.IParser
	 */
	public ReceivedMessageList parseEntry(XmlPullParser parser) throws ParseException, IOException {
		ReceivedMessageList list = new ReceivedMessageList();

		int eventType;

		try {
			eventType = parser.getEventType();
		} catch (XmlPullParserException e) {
			throw new ParseException("Could not parse Receved Message entity.", e);
		}

		if (eventType == XmlPullParser.START_TAG && mMessageType.equals(parser.getName())) {
			while (eventType != XmlPullParser.END_DOCUMENT) {
				// Look for a new event
				try {
					eventType = parser.next();
				} catch (XmlPullParserException xppe) {
					throw new ParseException("Could not read next event.", xppe);
				}
				Log.d(TAG, "Siguiente evento: " + XmlPullParser.TYPES[eventType]);
				if (eventType == XmlPullParser.START_TAG) {
					try {
						AbstractReceivedMessage single = parseSingleReceivedMessageEntry(parser, eventType, mMessageType);
						if (!single.isValid()) {
							throw new ParseException("Recived Message is not valid");
						}
						list.add(single);
					} catch (XmlPullParserException xppe) {
						throw new ParseException("Unable to parse Received Message.", xppe);
					} catch (IOException ioe) {
						throw new ParseException("I/O exception while parsing Received SMS.", ioe);
					}
				}
			}
		} else {
			throw new ParseException("", new XmlPullParserException(
					"Start document root has not been found or its tag name is wrong"));
		}

		return list;
	}

	protected AbstractReceivedMessage parseSingleReceivedMessageEntry(XmlPullParser parser, int eventType, String messageType)
			throws XmlPullParserException, IOException, ParseException {

		AbstractReceivedMessage single;

		if (messageType.equals(XmlConstants.XSD_RECEIVEDSMS))
			single = new SmsMessage();
		else if (messageType.equals(XmlConstants.XSD_RECEIVEDMMS))
			single = new MmsMessageInfo();
		else throw new ParseException("Incorrect message type");

		if (eventType != XmlPullParser.START_TAG) {
			throw new ParseException("Expected event START_TAG: Actual event: "
					+ XmlPullParser.TYPES[eventType]);
		}

		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.END_TAG:
				String name = parser.getName();
				Log.d(TAG, "parseSingleReceivedMessageEntry end tag name is " + name);
				if (messageType.equals(name)) {
					Log.d(TAG, "parseSingleReceivedMessageEntry return : " + single);
					// stop parsing here.
					return single;
				}
				break;
			case XmlPullParser.START_TAG:
				name = parser.getName();
				Log.d(TAG, "parseSingleReceivedMessageEntry start tag name is " + name);
				if (XmlConstants.XSD_MESSAGE_TYPE_ORIGINADDRESS.equals(name)) {
					UserId originAddress = XmlParserSerializerUtils.parseUserId(parser, eventType, XmlConstants.XSD_MESSAGE_TYPE_ORIGINADDRESS);
					if (originAddress == null){
						throw new ParseException("Origin Address UserId is null");
					} else if  (!originAddress.isValid()){						
						throw new ParseException("Origin Address UserId is not valid");
					}
					single.setUserIdOriginAddress(originAddress);
				} else if (XmlConstants.XSD_MESSAGE_TYPE_DESTINATIONADDRESS.equals(name) ||
						XmlConstants.XSD_MESSAGE_TYPE_ADDRESS.equals(name)) {
					UserId destinationAddress = XmlParserSerializerUtils.parseUserId(parser, eventType, name);
					if (destinationAddress == null){
						throw new ParseException("Origin Address UserId is null");
					} else if  (!destinationAddress.isValid()){						
						throw new ParseException("Origin Address UserId is not valid");
					}
					single.addAddress(destinationAddress);
				} else if (XmlConstants.XSD_SMSTEXTTYPE_MESSAGE.equals(name)
						&& XmlConstants.XSD_RECEIVEDSMS.equals(messageType)) { //Same tag for SMS & MMS but differente meaning
					int[] textEventType = new int[1];
					String message = XmlParserSerializerUtils.getChildText(parser, textEventType);
					if ((message == null) && (textEventType[0] == XmlPullParser.END_TAG)){
						eventType = textEventType[0];
						continue;
					}
					//Only for SMS by now...
					if (single instanceof SmsMessage)
						((SmsMessage) single).setMessage(message);
				} else if (XmlConstants.XSD_MESSAGE_TYPE_DATETIME.equals(name)) {
					int[] textEventType = new int[1];
					String dateTime = XmlParserSerializerUtils.getChildText(parser, textEventType);
					if ((dateTime == null) && (textEventType[0] == XmlPullParser.END_TAG)){
						eventType = textEventType[0];
						continue;
					}
					single.setDate(dateTime);
				} else if (XmlConstants.XSD_RECEIVEDMMS_IDENTIFIER.equals(name)){ // Only MMS
					int[] textEventType = new int[1];
					String identifier = XmlParserSerializerUtils.getChildText(parser);
					if ((identifier == null) && (textEventType[0] == XmlPullParser.END_TAG)){
						eventType = textEventType[0];
						continue;
					}
					((MmsMessageInfo)single).setMessageIdentifier(identifier);
				} else if (XmlConstants.XSD_MMSTEXTTYPE_SUBJECT.equals(name)){ // Only MMS
					int[] textEventType = new int[1];
					String subject = XmlParserSerializerUtils.getChildText(parser);
					if ((subject == null) && (textEventType[0] == XmlPullParser.END_TAG)){
						eventType = textEventType[0];
						continue;
					}
					((MmsMessageInfo)single).setSubject(subject);
				}  else if (XmlConstants.XSD_RECEIVEDMMS_ATTACHMENT_URL.equals(name)){ // Only MMS
					AttachmentInfo attachmentUrl = parseAttachmentURL(parser, eventType, (MmsMessageInfo) single);
					((MmsMessageInfo)single).addAttachmentInfo(attachmentUrl);
				} 
				break;
			default:
				break;
			}
			eventType = parser.next();
		}		
		if (!single.isValid())
			throw new ParseException("Received message is not valid");
		return single;
	}

	protected AttachmentInfo parseAttachmentURL(XmlPullParser parser, int eventType, MmsMessageInfo mms)
			throws XmlPullParserException, IOException, ParseException {

		AttachmentInfo attach = mms.new AttachmentInfo();

		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.END_TAG:
				String name = parser.getName();
				if (XmlConstants.XSD_RECEIVEDMMS_ATTACHMENT_URL.equals(name)) {
					// stop parsing here.
					return attach;
				}
				break;
			case XmlPullParser.START_TAG:
				name = parser.getName();
				if (XmlConstants.XSD_RECEIVEDMMS_HREF.equals(name)) {
					int[] textEventType = new int[1];
					String value = XmlParserSerializerUtils.getChildText(parser, textEventType);
					if ((value == null) && (textEventType[0] == XmlPullParser.END_TAG)) {
						eventType = textEventType[0];
						continue;
					}
					attach.setUrl(value);
				} else if (XmlConstants.XSD_RECEIVEDMMS_CONTENT_TYPE.equals(name)) {
					int[] textEventType = new int[1];
					String value = XmlParserSerializerUtils.getChildText(parser, textEventType);
					if ((value == null) && (textEventType[0] == XmlPullParser.END_TAG)) {
						eventType = textEventType[0];
						continue;
					}
					attach.setContentType(value);			
				}
				break;
			default:
				break;
			}
			eventType = parser.next();
		}		
		if (!attach.isValid())
			throw new ParseException("Received message is not valid");
		return attach;
	}

}
