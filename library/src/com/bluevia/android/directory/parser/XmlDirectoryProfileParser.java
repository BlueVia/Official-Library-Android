package com.bluevia.android.directory.parser;


import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bluevia.android.commons.parser.ParseException;
import com.bluevia.android.commons.parser.xml.XmlParserSerializerUtils;
import com.bluevia.android.directory.data.Profile;


/**
 * Xml parser for XmlDirectoryUserProfile entities.
 *
 * @author Telefonica I+D
 * 
*/
class XmlDirectoryProfileParser implements DirectoryEntityParser {

    public Profile parse(XmlPullParser parser) throws ParseException
    {

        Profile userProfile = new Profile();

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
                if (nameSpace.equals(XmlConstants.PROFILE)) {
                    return userProfile;
                }
                break;

            case XmlPullParser.START_TAG:
                if(nameSpace.equals(XmlConstants.PROFILE_USERTYPE)){
                    try{
                        userProfile.setUserType(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.PROFILE_ICB)){
                    try{
                        userProfile.setICB(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.PROFILE_OCB)){
                    try{
                        userProfile.setOCB(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.PROFILE_LANGUAGE)){
                    try{
                        userProfile.setLang(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.PROFILE_PARENTALCONTROL)){
                    try{
                        userProfile.setParentalControl(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.PROFILE_OPERATORID)){
                    try{
                        userProfile.setOperatorId(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.PROFILE_MMSSTATUS)){
                    try{
                        String mms = XmlParserSerializerUtils.getChildText(parser);
                        if (mms.equals("activated"))
                            userProfile.setMmsStatus(true);
                        else
                            userProfile.setMmsStatus(false);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.PROFILE_SEGMENT)){
                    try{
                        userProfile.setSegment(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
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
