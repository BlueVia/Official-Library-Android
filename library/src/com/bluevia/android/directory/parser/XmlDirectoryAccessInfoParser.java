package com.bluevia.android.directory.parser;



import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.bluevia.android.commons.parser.ParseException;
import com.bluevia.android.commons.parser.xml.XmlParserSerializerUtils;
import com.bluevia.android.directory.data.AccessInfo;

/**
 * Xml parser for XmlDirectoryUserAccess entities.
 *
 * @author Telefonica I+D
 * 
*/
class XmlDirectoryAccessInfoParser implements DirectoryEntityParser {

    private static final String TAG = "XmlDirectoryUserAccessParser";

    /**
     * @see com.bluevia.android.commons.parser.IParser#parse(java.io.InputStream)
     */
    public AccessInfo parse(XmlPullParser parser) throws ParseException {

    	AccessInfo userAccessInfo= new AccessInfo();

        int eventType;

        try {
            eventType = parser.next();
            Log.e(TAG, "Expected event START_TAG: Actual event: " + XmlPullParser.TYPES[eventType]);
        } catch (XmlPullParserException xppe) {
            throw new ParseException("Could not read next event.", xppe);
        } catch (IOException ioe) {
            throw new ParseException("Could not read next event.", ioe);
        }

        while (eventType != XmlPullParser.END_DOCUMENT){
            String nameSpace = parser.getName();
            switch (eventType){
            case XmlPullParser.END_TAG:

                if (XmlConstants.ACCESSINFO.equals(nameSpace)) {
                    return userAccessInfo;
                }
                break;

            case XmlPullParser.START_TAG:
            	if(nameSpace.equals(XmlConstants.ACCESSINFO_ACCESSTYPE)){
                    try{
                        userAccessInfo.setAccessType(XmlParserSerializerUtils.getChildText(parser));
                    } catch(XmlPullParserException e){
                        throw new ParseException("Problem with getChild", e);
                    }
                }
                if(nameSpace.equals(XmlConstants.ACCESSINFO_APN)){
                    try{
                        userAccessInfo.setAPN(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException e){
                        throw new ParseException("Problem with getChild", e);
                    }
                }
                if(nameSpace.equals(XmlConstants.ACCESSINFO_ROAMING)){
                    try{
                        String roam = XmlParserSerializerUtils.getChildText(parser);
                        if(roam.equals("yes"))
                            userAccessInfo.setRoaming(true);
                        else
                            userAccessInfo.setRoaming(false);
                    }catch(XmlPullParserException e){
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
