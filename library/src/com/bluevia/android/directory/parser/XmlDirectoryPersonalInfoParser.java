package com.bluevia.android.directory.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bluevia.android.commons.parser.ParseException;
import com.bluevia.android.commons.parser.xml.XmlParserSerializerUtils;
import com.bluevia.android.directory.data.PersonalInfo;

/**
 * Xml parser for XmlDirectoryPersonalInfoParser entities.
 *
 * @author Telefonica I+D
 * 
*/
class XmlDirectoryPersonalInfoParser implements DirectoryEntityParser {

    public PersonalInfo parse(XmlPullParser parser) throws ParseException {

    	PersonalInfo personalInfo = new PersonalInfo();
        int eventType;

        try {
            eventType = parser.next();
        } catch (XmlPullParserException xppe) {
            throw new ParseException("Could not read next event.", xppe);
        } catch (IOException ioe) {
            throw new ParseException("Could not read next event.", ioe);
        }

        while (eventType != XmlPullParser.END_DOCUMENT){
            String nameSpace = parser.getName();

            switch (eventType){
            case XmlPullParser.END_TAG:

                if (XmlConstants.PERSONALINFO.equals(nameSpace)) {
                    return personalInfo;
                }
                break;

            case XmlPullParser.START_TAG:
                if(nameSpace.equals(XmlConstants.PERSONALINFO_GENDER)){
                    try{
                        String gender = XmlParserSerializerUtils.getChildText(parser);
                        personalInfo.setGender(gender);
                    } catch(XmlPullParserException e){
                        throw new ParseException("Problem with getChild", e);
                    }
                }
            }
            try{
                eventType = parser.next();
            }catch (XmlPullParserException e) {
                throw new ParseException("Could not read next event.", e);
            } catch (IOException e) {
                throw new ParseException("Could not read next event.", e);
			}
        }
        return null;
    }

}
