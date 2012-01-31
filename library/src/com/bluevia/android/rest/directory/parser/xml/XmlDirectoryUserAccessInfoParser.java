///@cond private
package com.bluevia.android.rest.directory.parser.xml;



import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlParserSerializerUtils;
import com.bluevia.android.rest.directory.data.UserAccessInfo;
import com.bluevia.android.rest.directory.parser.xml.XmlDirectoryParserHelper.Extension;

import android.util.Log;

import java.io.IOException;

/**
 * Xml parser for XmlDirectoryUserAccess entities.
 *
 * @author Telefonica I+D
 * 
*/
class XmlDirectoryUserAccessInfoParser implements DirectoryEntityParser {

    private static final String TAG = "XmlDirectoryUserAccessParser";

    /**
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParser#parseEntry(java.io.InputStream)
     */
    public UserAccessInfo parseEntry(XmlPullParser parser) throws ParseException, IOException {

        UserAccessInfo userAccessInfo= new UserAccessInfo();
        Extension extension = null;

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

                if (XmlConstants.XSD_ACCESSINFO.equals(nameSpace)) {
                    Log.e(TAG, "END_TAG ACCESSINFO " + nameSpace);

                    return userAccessInfo;
                }
                break;

            case XmlPullParser.START_TAG:
                if(nameSpace.equals(XmlConstants.XSD_ACCESSINFO_CONNECTED)){
                    try{
                        Log.e(TAG, "START_TAG CONNECTED " + nameSpace);
                        String connection = XmlParserSerializerUtils.getChildText(parser);
                        if(connection.equals("yes"))
                         userAccessInfo.setConnected(true);
                        else
                            userAccessInfo.setConnected(false);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_ACCESSINFO_IPADDRESS)){
                    try{
                        Log.e(TAG, "START_TAG IPADDRESS " + nameSpace);
                        userAccessInfo.setIP(handleIpAddress(eventType,parser));
                        Log.e(TAG, "GET_IP " + userAccessInfo.getIP());
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
            //cambiar el accessType
                if(nameSpace.equals(XmlConstants.XSD_ACCESSINFO_ACCESSTYPE)){
                    try{
                        Log.e(TAG, "START_TAG ACCESSTYPE " + nameSpace);
                        userAccessInfo.setAccessType(XmlParserSerializerUtils.getChildText(parser));
                        Log.e(TAG, "START_TAG ACCESSTYPE valor: " + userAccessInfo.getAccessType());
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_ACCESSINFO_CONNECTIONTIME)){
                    try{
                        Log.e(TAG, "START_TAG CONNECTIONTIME " + nameSpace);
                        userAccessInfo.setConnectionTime(Integer.parseInt(XmlParserSerializerUtils.getChildText(parser)));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_ACCESSINFO_APN)){
                    try{
                        Log.e(TAG, "START_TAG APN " + nameSpace);
                        userAccessInfo.setAPN(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_ACCESSINFO_ROAMING)){
                    try{
                        Log.e(TAG, "START_TAG ROAMING " + nameSpace);
                        String roam = XmlParserSerializerUtils.getChildText(parser);
                        if(roam.equals("yes"))
                            userAccessInfo.setRoaming(true);
                        else
                            userAccessInfo.setRoaming(false);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_EXTENSION)){
                    try{
                        Log.e(TAG, "START_TAG EXTENSION " + nameSpace);
                        extension = XmlDirectoryParserHelper.handleExtension(parser,eventType);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
            }
            try{
                eventType = parser.next();
            }catch (XmlPullParserException xppe) {
                throw new ParseException("Could not read next event.", xppe);
            }
        }
       return null;
    }

    /**
     *
     * @param eventType
     * @return String with the ip Address
     * @throws XmlPullParserException when an error occurs parsing
     * @throws IOException when an error reading the stream occurs
     * @throws ParseException when an error occurs converting the stream into an object
     */
    private String handleIpAddress(int eventType,XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {

        String ipAddress = null;

        if (eventType != XmlPullParser.START_TAG) {
            throw new ParseException("Expected event START_TAG: Actual event: "
                + XmlPullParser.TYPES[eventType]);
        }

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String nameSpace = parser.getName();
            switch (eventType) {
            case XmlPullParser.END_TAG:

                if (XmlConstants.XSD_IPV4.equals(nameSpace)) {
                    return ipAddress;
                }
                if (XmlConstants.XSD_IPV6.equals(nameSpace)) {
                    return ipAddress;
                }
                break;

            case XmlPullParser.START_TAG:
                if (XmlConstants.XSD_IPV4.equals(nameSpace)) {
                    ipAddress = XmlParserSerializerUtils.getChildText(parser);
                }
                if (XmlConstants.XSD_IPV6.equals(nameSpace)) {
                    ipAddress = XmlParserSerializerUtils.getChildText(parser);
                }

            default:
                break;
            }
            eventType = parser.next();
        }
        return null;
    }

}
///@endcond
