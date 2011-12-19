///@cond private
package com.bluevia.android.rest.directory.parser.xml;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlParserSerializerUtils;
import com.bluevia.android.rest.directory.data.UserProfile;
import com.bluevia.android.rest.directory.parser.xml.XmlDirectoryParserHelper.Extension;

import android.util.Log;

import java.io.IOException;


/**
 * Xml parser for XmlDirectoryUserProfile entities.
 *
 * @author Telefonica I+D
 * 
*/
class XmlDirectoryUserProfileParser implements DirectoryEntityParser {

    private static final String TAG = "XmlDirectoryUserProfileParser";


    /**
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParser#parseEntry(java.io.InputStream)
     */
    public UserProfile parseEntry(XmlPullParser parser) throws ParseException, IOException {

        UserProfile userProfile = new UserProfile();
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
                if (nameSpace.equals(XmlConstants.XSD_PROFILE)) {
                    Log.e(TAG, "Voy a devolver el profile");
                    return userProfile;
                }
                break;

            case XmlPullParser.START_TAG:
                if(nameSpace.equals(XmlConstants.XSD_PROFILE_USERTYPE)){
                    try{
                        Log.e(TAG, "START_TAG de USERTYPE con; " + nameSpace);
                        userProfile.setUserType(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_PROFILE_IMSI)){
                    try{
                        Log.e(TAG, "START_TAG de IMSI con; " + nameSpace);
                        userProfile.setIMSI(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_PROFILE_ICB)){
                    try{
                        Log.e(TAG, "START_TAG de ICB con; " + nameSpace);
                        userProfile.setICB(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_PROFILE_OCB)){
                    try{
                        Log.e(TAG, "START_TAG de OCB con; " + nameSpace);
                        userProfile.setOCB(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_PROFILE_LANGUAGE)){
                    try{
                        Log.e(TAG, "START_TAG de LANGUAGE con; " + nameSpace);
                        userProfile.setLang(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_PROFILE_SIMTYPE)){
                    try{
                        Log.e(TAG, "START_TAG de SIMTYPE con; " + nameSpace);
                        userProfile.setSimType(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_PROFILE_PARENTALCONTROL)){
                    try{
                        Log.e(TAG, "START_TAG de PARENTALCONTROL con; " + nameSpace);
                        userProfile.setParentalControl(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_PROFILE_CREDITCONTROL)){
                    try{
                        Log.e(TAG, "START_TAG de CREDITCONTROL con; " + nameSpace);
                        String credit = XmlParserSerializerUtils.getChildText(parser);
                        if (credit.equals("yes"))
                            userProfile.setCreditControl(true);
                        else
                            userProfile.setCreditControl(false);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_PROFILE_DIVERSIONMSISDN)){
                    try{
                        Log.e(TAG, "START_TAG de DIVERSIONMSISDN con; " + nameSpace);
                        userProfile.setDiversionMSISDN(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_PROFILE_ENTERPRISENAME)){
                    try{
                        Log.e(TAG, "START_TAG de ENTERPRISENAME con; " + nameSpace);
                        userProfile.setEnterpriseName(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_PROFILE_ROAMING)){
                    try{
                        Log.e(TAG, "START_TAG de ROAMING con; " + nameSpace);
                        String roa = XmlParserSerializerUtils.getChildText(parser);
                        if (roa.equals("yes"))
                            userProfile.setRoaming(true);
                        else
                            userProfile.setRoaming(false);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_PROFILE_OPERATORID)){
                    try{
                        Log.e(TAG, "START_TAG de OPERATORID con; " + nameSpace);
                        userProfile.setOperatorId(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_PROFILE_MMSSTATUS)){
                    try{
                        Log.e(TAG, "START_TAG de MMSSTATUS con; " + nameSpace);
                        String mms = XmlParserSerializerUtils.getChildText(parser);
                        if (mms.equals("activated"))
                            userProfile.setMmsStatus(true);
                        else
                            userProfile.setMmsStatus(false);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_PROFILE_SEGMENT)){
                    try{
                        Log.e(TAG, "START_TAG de SEGMENT con; " + nameSpace);
                        userProfile.setSegment(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_PROFILE_SUBSEGMENT)){
                    try{
                        Log.e(TAG, "START_TAG de SUBSEGMENT con; " + nameSpace);
                        userProfile.setSubSegment(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_PROFILE_SUBSCRIBEDSERVICE)){
                    try{
                        userProfile.addSubscribedService(getAttributeName(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }

                }

                if(nameSpace.equals(XmlConstants.XSD_LASTUPDATED)){
                    try{
                        Log.e(TAG, "START_TAG de LASTUPDATED con; " + nameSpace);
                        userProfile.setLastUpdated(XmlParserSerializerUtils.getChildText(parser));
                        Log.e(TAG, "START_TAG de LASTUPDATED con " + userProfile.getLastUpdated());
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_EXTENSION)){
                    try{
                        Log.e(TAG, "START_TAG de EXTENSION con; " + nameSpace);
                        //userProfile.setExtension(XmlDirectoryParserHelper.getChildText(parser));
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
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private String getAttributeName(XmlPullParser parser)  throws XmlPullParserException, IOException {
        Log.e(TAG, "Entro en getAttribute");
        return parser.getAttributeValue(0);
    }
    
}
///@endcond
