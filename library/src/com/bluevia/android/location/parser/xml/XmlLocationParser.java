package com.bluevia.android.location.parser.xml;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.parser.IParser;
import com.bluevia.android.commons.parser.ParseException;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.commons.parser.xml.XmlParserSerializerUtils;
import com.bluevia.android.location.data.CoordinatesType;
import com.bluevia.android.location.data.LocationDataType;
import com.bluevia.android.location.data.LocationDataType.ReportStatus;
import com.bluevia.android.location.data.LocationInfoType;

/**
 * XML implementation of {@link com.bluevia.android.commons.parser.IParser IParser}
 * Class that represents the parser object for any Location entity.
 * Using this class you will be able to parse XML documents containing
 * a representation of any Location entity into an instance
 * object of this entity
 *
 * @author Telefonica R&D
 *  
 */
public class XmlLocationParser implements IParser {

	private static final String TAG = "XmlLocationParser";

	private XmlPullParser mParser;

	public Entity parse(InputStream is) throws ParseException {

		if (is == null)
			throw new ParseException("Input stream is null");

		LocationDataType locationData = null;

		mParser = Xml.newPullParser();

		try { 
			mParser.setInput(is,null);
		} catch(XmlPullParserException e){
			Log.e(TAG, "Exception occureed while reading the XML Document", e);
			throw new ParseException("Could not create XmlLocationParser", e);
		}

		int eventType;

		try {
			eventType = mParser.getEventType();
			Log.d(TAG, "EventType: " + eventType);
		} catch (XmlPullParserException e) {
			throw new ParseException("Could not parse LocationDataType entity.", e);
		}
		if (eventType != XmlPullParser.START_DOCUMENT) {
			Log.e(TAG, "Attempting to initialize parsing beyond "
					+ "the start of the document.");
			throw new ParseException("",new XmlPullParserException("Attempting to initialize parsing beyond "
					+ "the start of the document."));
		}

		String text = "";
		String tagName = "";
		while (eventType != XmlPullParser.END_DOCUMENT){ 
			try {
				eventType = mParser.next();
				Log.d(TAG, "Event type:" + eventType);
			} catch (XmlPullParserException e) {
				throw new ParseException("Ill-formed xml");
			} catch (IOException e) {
				throw new ParseException(e);
			}			
			tagName = mParser.getName();
			if (tagName!=null){
				Log.d(TAG, "name = " + tagName);
			}
			text = mParser.getText();
			if (text!=null){
				Log.d(TAG, "text = " + text);
			}
			if (eventType == XmlPullParser.START_TAG){
				if (tagName.equals(XmlConstants.XSD_TERMINALLOCATION_LOCATION)){
					
					Entity entity;
					try {
						entity = parseLocationEntity(eventType);
					} catch (IOException e) {
						throw new ParseException(e);
					}
					
					if (entity instanceof LocationDataType){
						locationData = (LocationDataType) entity;
					} else throw new ParseException("Invalid received entity");
					
				} else throw new ParseException("Unrecognized tag: "+tagName);
			}
		}
		return locationData;
	}

	private LocationDataType parseLocationEntity(int eventType) throws ParseException, IOException {
		LocationDataType locationDataType = new LocationDataType();

		while (eventType != XmlPullParser.END_DOCUMENT){ 
			try {
				eventType = mParser.next();
				Log.d(TAG, "Event type:" + eventType);
			} catch (XmlPullParserException e) {
				throw new ParseException("Ill-formed xml");
			}			
			String tagName = mParser.getName();
			if (tagName!=null){
				Log.d(TAG, "name = " + tagName);
			}
			String text = mParser.getText();
			if (text!=null){
				Log.d(TAG, "text = " + text);
			}
			switch (eventType) {
			case XmlPullParser.END_TAG:
				if (tagName.equals(XmlConstants.XSD_TERMINALLOCATION_LOCATION)) {
					return locationDataType;
				}
				break;

			case XmlPullParser.START_TAG:
				if (tagName.equals(XmlConstants.XSD_TERMINALLOCATION_LOCATEDPARTY)){
					try {
						locationDataType.setLocationParty(
								XmlParserSerializerUtils.parseUserId(mParser, eventType, XmlConstants.XSD_TERMINALLOCATION_LOCATEDPARTY));
					} catch (XmlPullParserException e) {
						Log.e(TAG, "Error in XmlLocationParser " + e.getMessage(), e);
						throw new ParseException("Ill-formed xml");
					}
				} else if (tagName.equals(XmlConstants.XSD_TERMINALLOCATION_CURRENTLOCATION)){        	

					try {
						locationDataType.setCurrentLocation(parseCurrentLocation(eventType));
					} catch (NumberFormatException e) {
						Log.e(TAG, "Error in XmlLocationParser " + e.getMessage(), e);
						throw new ParseException("Altitude number not valid");
					} catch (XmlPullParserException e) {
						Log.e(TAG, "Error in XmlLocationParser " + e.getMessage(), e);
						throw new ParseException("Acurracy number not valid");
					} catch (java.text.ParseException e) {
						Log.e(TAG, "Error in XmlLocationParser " + e.getMessage(), e);
						throw new ParseException("Timestamp not valid");
					}
					Log.d(TAG, XmlConstants.XSD_TERMINALLOCATION_CURRENTLOCATION);

				} else if (tagName.equals(XmlConstants.XSD_TERMINALLOCATION_REPORTSTATUS)){
					try{
						ReportStatus status = stringToRetrievalStatus(XmlParserSerializerUtils.getChildText(mParser));
						locationDataType.setReportStatus(status);
					} catch (XmlPullParserException e) {
						throw new ParseException("Ill-formed xml");
					}	
				} else if (tagName.equals(XmlConstants.XSD_TERMINALLOCATION_ERRORINFORMATION)){
					Log.d(TAG, XmlConstants.XSD_TERMINALLOCATION_ERRORINFORMATION);
					//TODO parseError
				}
			default:
				break;
			}
		}
		return null;
	}

	private LocationInfoType parseCurrentLocation(int eventType) throws ParseException, IOException, NumberFormatException, XmlPullParserException, java.text.ParseException {
		
		LocationInfoType locationInfo = new LocationInfoType();
		
		while (eventType != XmlPullParser.END_DOCUMENT){ 
			try {
				eventType = mParser.next();
				Log.d(TAG, "Event type:" + eventType);
			} catch (XmlPullParserException e) {
				throw new ParseException("Ill-formed xml");
			}			
			String tagName = mParser.getName();
			if (tagName!=null){
				Log.d(TAG, "name = " + tagName);
			}
			String text = mParser.getText();
			if (text!=null){
				Log.d(TAG, "text = " + text);
			}
			switch (eventType) {
			case XmlPullParser.END_TAG:
				if (tagName.equals(XmlConstants.XSD_TERMINALLOCATION_CURRENTLOCATION)) {
					return locationInfo;
				}
				break;

			case XmlPullParser.START_TAG:
				if (tagName.equals(XmlConstants.XSD_TERMINALLOCATION_COORDINATES)){
					locationInfo.setCoordinates(parseCoordinates(eventType));
				} else if (tagName.equals(XmlConstants.XSD_TERMINALLOCATION_ALTITUDE)){        	
					locationInfo.setAltitude(Float.valueOf(XmlParserSerializerUtils.getChildText(mParser)));
				} else if (tagName.equals(XmlConstants.XSD_TERMINALLOCATION_ACCURACY)){        	
					locationInfo.setAccuracy(Integer.valueOf(XmlParserSerializerUtils.getChildText(mParser)));
				} else if (tagName.equals(XmlConstants.XSD_TERMINALLOCATION_TIMESTAMP)){ 
					DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss"); 
					locationInfo.setTimestamp((Date)formatter.parse(XmlParserSerializerUtils.getChildText(mParser)));
				} else {
					throw new ParseException("Unrecognized tag: "+tagName);
				}
			default:
				break;
			}
		}
		return null;
	}

	
	private CoordinatesType parseCoordinates(int eventType) throws ParseException, IOException, NumberFormatException, XmlPullParserException {
		float latitude = 0, longitude = 0;
		
		while (eventType != XmlPullParser.END_DOCUMENT){ 
			try {
				eventType = mParser.next();
				Log.d(TAG, "Event type:" + eventType);
			} catch (XmlPullParserException e) {
				throw new ParseException("Ill-formed xml");
			}			
			String tagName = mParser.getName();
			if (tagName!=null){
				Log.d(TAG, "name = " + tagName);
			}
			String text = mParser.getText();
			if (text!=null){
				Log.d(TAG, "text = " + text);
			}
			switch (eventType) {
			case XmlPullParser.END_TAG:
				if (tagName.equals(XmlConstants.XSD_TERMINALLOCATION_COORDINATES)) {
					return new CoordinatesType(longitude, latitude);
				}
				break;

			case XmlPullParser.START_TAG:
				if (tagName.equals(XmlConstants.XSD_TERMINALLOCATION_LATITUDE)){
					latitude = Float.valueOf(XmlParserSerializerUtils.getChildText(mParser));
				} else if (tagName.equals(XmlConstants.XSD_TERMINALLOCATION_LONGITUDE)){        	
					longitude = Float.valueOf(XmlParserSerializerUtils.getChildText(mParser));
				} else {
					throw new ParseException("Unrecognized tag: "+tagName);
				}
			default:
				break;
			}
		}
		return null;
	}
	
	private ReportStatus stringToRetrievalStatus(String typeStr) {
		if (typeStr == null)
			return null;

		typeStr = typeStr.trim();

		if (typeStr.equals(XmlConstants.XSD_TERMINALLOCATION_REPORTSTATUS_ATTR_RETRIEVED_VALUE)) {
			return ReportStatus.RETRIEVED;
		} else if (typeStr.equals(XmlConstants.XSD_TERMINALLOCATION_REPORTSTATUS_ATTR_NONRETRIEVED_VALUE)) {
			return ReportStatus.NON_RETRIEVED;
		} else if (typeStr.equals(XmlConstants.XSD_TERMINALLOCATION_REPORTSTATUS_ATTR_ERROR_VALUE)) {
			return ReportStatus.ERROR;
		} else return null;
	}

	
}
