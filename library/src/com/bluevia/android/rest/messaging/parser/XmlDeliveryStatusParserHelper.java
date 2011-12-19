///@cond private
package com.bluevia.android.rest.messaging.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bluevia.android.rest.commons.data.UserId;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.commons.parser.xml.XmlParserSerializerUtils;
import com.bluevia.android.rest.messaging.MessageDeliveryStatus;
import com.bluevia.android.rest.messaging.MessageDeliveryStatusList;
import com.bluevia.android.rest.messaging.MessageDeliveryStatus.Status;

import android.util.Log;


/**
 * Common functions for Sms and Mms DeliveryStatusParser.
 * 
 * @author Telefonica I+D
 * 
 */
public final class XmlDeliveryStatusParserHelper {

	private static final String TAG = "DeliveryStatusParserHelper";

	/**
	 * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParser#parseEntry(java.io.InputStream)
	 */
	public static MessageDeliveryStatusList parseEntry(XmlPullParser parser, String messageType) throws ParseException, IOException {

		MessageDeliveryStatusList deliveryStatus = new MessageDeliveryStatusList();

		int eventType;

		try {
			eventType = parser.getEventType();
		} catch (XmlPullParserException e) {
			throw new ParseException("Could not parse DeliveryStatus entity.", e);
		}

		if (eventType == XmlPullParser.START_TAG && messageType.equals(parser.getName())) {
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
						MessageDeliveryStatus singleStatus = parseDeliveryStatusSingle(parser, eventType, messageType);
						if (!singleStatus.isValid()) {
							throw new ParseException("StatusRecived is not valid");
						}
						deliveryStatus.add(singleStatus);
					} catch (XmlPullParserException xppe) {
						throw new ParseException("Unable to parse Single DeliveryStatus.", xppe);
					} catch (IOException ioe) {
						throw new ParseException("Unable to parse Single DeliveryStatus.", ioe);
					}
				}
			}
		} else {
			throw new ParseException("", new XmlPullParserException(
			"Start document root has not been found or its tag name is wrong"));
		}

		return deliveryStatus;
	}

	/**
	 * This method parsers the
	 * <tns:messageDeliveryStatus>" +
	 *    + <tns:address> (delegates this part to handleAddress)
	 *    <tns:deliveryStatus>DeliveredToTerminal</tns:deliveryStatus>"
	 * </tns:messageDeliveryStatus>"
	 * 
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static MessageDeliveryStatus parseDeliveryStatusSingle(XmlPullParser parser, int eventType, String messageType)
	throws XmlPullParserException, IOException, ParseException {

		MessageDeliveryStatus statusSingle=new MessageDeliveryStatus();

		if (eventType != XmlPullParser.START_TAG) {
			throw new ParseException("Expected event START_TAG: Actual event: "
					+ XmlPullParser.TYPES[eventType]);
		}

		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.END_TAG:
				String name = parser.getName();
				Log.d(TAG, "parseDeliveryStatusSingle end tag name is " + name);
				if (messageType.equals(name)) {
					Log.d(TAG, "parseDeliveryStatusSingle return statusSingle " + statusSingle);
					// stop parsing here.
					return statusSingle;
				}
				break;
			case XmlPullParser.START_TAG:
				name = parser.getName();
				Log.d(TAG, "parseDeliveryStatusSingle start tag name is " + name);
				if (XmlConstants.XSD_MESSAGE_TYPE_ADDRESS.equals(name)) {
					UserId address=XmlParserSerializerUtils.parseUserId(parser, eventType, XmlConstants.XSD_MESSAGE_TYPE_ADDRESS);
					if (address == null){
						throw new ParseException("Address UserId is null");
					} else if  (!address.isValid()){						
						throw new ParseException("Address UserId is not valid");
					}

					statusSingle.setAddress(address);
				} else if (XmlConstants.XSD_DELIVERYSTATUS_STATUS.equals(name)) {
					int[] textEventType = new int[1];
					String status = XmlParserSerializerUtils.getChildText(parser, textEventType);
					if ((status == null) && (textEventType[0] == XmlPullParser.END_TAG)){
						eventType = textEventType[0];
						continue;
					}
					statusSingle.setStatus(translateStatus(status));

				}else if(XmlConstants.XSD_MESSAGE_TYPE_DESCRIPTION.equals(name)){
					String description=handleDescription(parser, eventType);
					statusSingle.setDescription(description);
				}
				break;
			default:
				break;
			}
			eventType = parser.next();
		}
		return null;
	}

	public static String handleDescription(XmlPullParser parser, int eventType)
	throws XmlPullParserException, IOException, ParseException {

		String description=null;

		if (eventType != XmlPullParser.START_TAG) {
			throw new ParseException("Expected event START_TAG: Actual event: "
					+ XmlPullParser.TYPES[eventType]);
		}

		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.END_TAG:
				String name = parser.getName();
				Log.d(TAG, "handleDecription end tag name is " + name);
				if (XmlConstants.XSD_MESSAGE_TYPE_DESCRIPTION.equals(name)) {
					Log.d(TAG, "The description is " + description);
					// stop parsing here.
					return description;
				}
				break;
			case XmlPullParser.START_TAG:
				name = parser.getName();
				if (XmlConstants.XSD_MESSAGE_TYPE_DESCRIPTION.equals(name)) {
					int[] textEventType = new int[1];
					description = XmlParserSerializerUtils.getChildText(parser, textEventType);
					if ((description == null) && (textEventType[0] == XmlPullParser.END_TAG)){
						eventType = textEventType[0];
						continue;
					}
				}
				break;
			default:
				break;
			}
			eventType = parser.next();
		}
		return null;
	}

	public static MessageDeliveryStatus.Status translateStatus(String status) {
		MessageDeliveryStatus.Status ret = null;
		if (status.equals(XmlConstants.XSD_DELIVERYSTATUSTYPE_VALUE_DELIVERED_TO_NETWORK)){
			ret=Status.DELIVERED_TO_NETWORK;
		} else if (status.equals(XmlConstants.XSD_DELIVERYSTATUSTYPE_VALUE_DELIVERED_UNCERTAIN)){
			ret=Status.DELIVERY_UNCERTAIN;
		} else if (status.equals(XmlConstants.XSD_DELIVERYSTATUSTYPE_VALUE_DELIVERED_IMPOSSIBLE)){
			ret=Status.DELIVERY_IMPOSSIBLE;
		} else if (status.equals(XmlConstants.XSD_DELIVERYSTATUSTYPE_VALUE_DELIVERED_MESSAGE_WAITING)){
			ret=Status.MESSAGE_WAITING;
		} else if (status.equals(XmlConstants.XSD_DELIVERYSTATUSTYPE_VALUE_DELIVERED_TO_TERMINAL)){
			ret=Status.DELIVERED_TO_TERMINAL;
		} else if (status.equals(XmlConstants.XSD_DELIVERYSTATUSTYPE_VALUE_DELIVERED_DELIVERY_NOTIF_NOTSUPP)){
			ret=Status.DELIVERY_NOTIFICATION_NOT_SUPPORTED;
		}
		return ret;
	}

}
///@endcond