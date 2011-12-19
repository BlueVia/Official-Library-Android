package com.bluevia.android.rest.directory.parser.xml;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlParserSerializerUtils;

/**
 * Utilities for XmlDirectoryParser
 *
 * @author Telefonica R&D
 * 
 */

public class XmlDirectoryParserHelper {
	
	/**
	 * 
	 * @param parser
	 * @param eventType
	 * @return 
	 * @throws XmlPullParserException when an error occurs parsing
	 * @throws IOException when an error reading the stream occurs
	 * @throws ParseException when an error occurs converting the stream into an object
	 */
	public static Extension handleExtension(XmlPullParser parser, int eventType) throws XmlPullParserException, IOException, ParseException {

		Extension extension = new Extension();

		if (eventType != XmlPullParser.START_TAG) {
			throw new ParseException("Expected event START_TAG: Actual event: "
					+ XmlPullParser.TYPES[eventType]);
		}

		while (eventType != XmlPullParser.END_DOCUMENT) {
			String nameSpace = parser.getName();
			switch (eventType) {
			case XmlPullParser.END_TAG:
				if (XmlConstants.XSD_EXTENSION.equals(nameSpace)) {
					return extension;
				}
				break;

			case XmlPullParser.START_TAG:
				if (XmlConstants.XSD_SERVEREXCEPTION.equals(nameSpace)) {
					extension.setServerException(handleServerException(parser, eventType));
				}

			default:
				break;
			}
			eventType = parser.next();
		}
		return null;
	}

	/**
	 * 
	 * @param parser
	 * @param eventType
	 * @return
	 * @throws XmlPullParserException when an error occurs parsing
	 * @throws IOException when an error reading the stream occurs
	 * @throws ParseException when an error occurs converting the stream into an object
	 */
	public static ServerException handleServerException(XmlPullParser parser, int eventType) throws XmlPullParserException, IOException, ParseException {

		ServerException serverException = new ServerException();

		if (eventType != XmlPullParser.START_TAG) {
			throw new ParseException("Expected event START_TAG: Actual event: "
					+ XmlPullParser.TYPES[eventType]);
		}

		while (eventType != XmlPullParser.END_DOCUMENT) {
			String nameSpace = parser.getName();
			switch (eventType) {
			case XmlPullParser.END_TAG:
				if (XmlConstants.XSD_SERVEREXCEPTION.equals(nameSpace)) {
					return serverException;
				}
				break;

			case XmlPullParser.START_TAG:

				if (XmlConstants.XSD_EXCEPTIONCATEGORY.equals(nameSpace)) {
					serverException.setExceptionCategory(XmlParserSerializerUtils.getChildText(parser));
				}
				if (XmlConstants.XSD_EXCEPTIONID.equals(nameSpace)) {
					serverException.setExceptionId(Integer.parseInt(XmlParserSerializerUtils.getChildText(parser)));
				}
				if (XmlConstants.XSD_TEXT.equals(nameSpace)) {
					serverException.setText(XmlParserSerializerUtils.getChildText(parser));
				}
				if (XmlConstants.XSD_VARIABLES.equals(nameSpace)) {
					serverException.setVariables(XmlParserSerializerUtils.getChildText(parser));
				}

			default:
				break;
			}
			eventType = parser.next();
		}
		return null;
	}

	/**
	 * Common object for all parser
	 * @author Telefonica I+D
	 *
	 */
	public static class Extension{
		ServerException serverException = null;

		void setServerException (ServerException e){
			serverException = e;
		}
		ServerException getServerException (){
			return serverException;
		}

	}

	/**
	 * Common object for all parsers
	 * @author Telefonica I+D
	 *
	 */
	public static class ServerException{
		String exceptionCategory = null;
		int exceptionId = 0;
		String text = null;
		String variables = null;

		void setExceptionCategory (String exceptionCategory){
			this.exceptionCategory = exceptionCategory;
		}
		void setExceptionId (int exceptionId){
			this.exceptionId = exceptionId;
		}
		void setText (String text){
			this.text = text;
		}
		void setVariables (String variables){
			this.variables = variables;
		}
		String getExceptionCategory (){
			return exceptionCategory;
		}
		int getExceptionId (){
			return exceptionId;
		}
		String getText (){
			return text;
		}
		String getVariables (){
			return variables;
		}

	}

}
