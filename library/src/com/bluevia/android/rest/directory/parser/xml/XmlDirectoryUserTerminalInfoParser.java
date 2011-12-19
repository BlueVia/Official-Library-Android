///@cond private
package com.bluevia.android.rest.directory.parser.xml;



import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlParserSerializerUtils;
import com.bluevia.android.rest.directory.data.UserTerminalInfo;
import com.bluevia.android.rest.directory.parser.xml.XmlDirectoryParserHelper.Extension;

import android.util.Log;

import java.io.IOException;

/**
 * Xml parser for XmlDirectoryUserTerminalInfo entities.
 *
 * @author Telefonica I+D
 * 
*/
class XmlDirectoryUserTerminalInfoParser implements DirectoryEntityParser {

    private static final String TAG = "XmlDirectoryUserTerminalInfoParser";

    /**
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParser#parseEntry(java.io.InputStream)
     */
    public UserTerminalInfo parseEntry(XmlPullParser parser) throws ParseException, IOException {

        UserTerminalInfo userTerminalInfo = new UserTerminalInfo();
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

                if (XmlConstants.XSD_TERMINALINFO.equals(nameSpace)) {
                    Log.e(TAG, "Entro en END_TERMINALINFO tag name is " + nameSpace);
                    Log.e(TAG, "Mostrar el brand " + userTerminalInfo.getBrand());
                    return userTerminalInfo;
                }
                break;

            case XmlPullParser.START_TAG:
                if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO_BRAND)){
                    try{
                        Log.e(TAG, "Entro en START_BRAND tag name is " + nameSpace);

                        String b = XmlParserSerializerUtils.getChildText(parser);
                        Log.e(TAG, "El brand nuevo vale: "+b);
                        userTerminalInfo.setBrand(b);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO_MODEL)){
                    try{
                        Log.e(TAG, "Entro en START_MODEL tag name is " + nameSpace);
                        userTerminalInfo.setModel(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO_VERSION)){
                    try{
                        Log.e(TAG, "Entro en START_VERSION tag name is " + nameSpace);
                        userTerminalInfo.setVersion(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO_SCREENRESOLUTION)){
                    try{
                        Log.e(TAG, "Entro en START_SCREENRESOLUTION tag name is " + nameSpace);
                        userTerminalInfo.setScreenResolution(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO_IMEI)){
                    try{
                        Log.e(TAG, "Entro en START_IMEI tag name is " + nameSpace);
                        userTerminalInfo.setIMEI(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO_MMS)){
                    try{
                        Log.e(TAG, "Entro en START_MMS tag name is " + nameSpace);
                        String m = XmlParserSerializerUtils.getChildText(parser);
                        if (m.equals("yes"))
                            userTerminalInfo.setMMS(true);
                        else
                            userTerminalInfo.setMMS(false);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO_EMS)){
                    try{
                        Log.e(TAG, "Entro en START_EMS tag name is " + nameSpace);
                        String e = XmlParserSerializerUtils.getChildText(parser);
                        if (e.equals("yes"))
                            userTerminalInfo.setEMS(true);
                        else
                            userTerminalInfo.setEMS(false);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO_SMARTMESSAGING)){
                    try{
                        Log.e(TAG, "Entro en START_SMARTMESSAGING tag name is " + nameSpace);
                        String s = XmlParserSerializerUtils.getChildText(parser);
                        if (s.equals("yes"))
                            userTerminalInfo.setSmartMessaging(true);
                        else
                            userTerminalInfo.setSmartMessaging(false);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO_WAP)){
                    try{
                        Log.e(TAG, "Entro en START_WAP tag name is " + nameSpace);
                        String w = XmlParserSerializerUtils.getChildText(parser);
                        if (w.equals("yes"))
                            userTerminalInfo.setWAP(true);
                        else
                            userTerminalInfo.setWAP(false);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO_USSDPHASE)){
                    try{
                        Log.e(TAG, "Entro en START_USSDPHASE tag name is " + nameSpace);

                        //int ussd = Integer.parseInt(XmlDirectoryParserHelper.getChildText(parser));
                        String ussd = XmlParserSerializerUtils.getChildText(parser);
                        Log.e(TAG, "USSDPHASE is " + ussd);
                        userTerminalInfo.setUSSDPhase(ussd);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO_SYNCML)){
                    try{
                        Log.e(TAG, "Entro en START_SYNCML tag name is " + nameSpace);
                        String s = XmlParserSerializerUtils.getChildText(parser);
                        if (s.equals("yes"))
                            userTerminalInfo.setSyncMl(true);
                        else if (s.equals("no"))
                            userTerminalInfo.setSyncMl(false);
                        else throw new ParseException("Error parsing " + XmlConstants.XSD_TERMINALINFO_SYNCML);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO_SYNCMLVERSION)){
                    try{
                        Log.e(TAG, "Entro en START_SYNCMLVERSION tag name is " + nameSpace);
                        userTerminalInfo.setSyncMlVersion(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO_EMSMAXNUMBER)){
                    try{
                        Log.e(TAG, "Entro en START_EMSMAXNUMBER tag name is " + nameSpace);

                        int number = Integer.parseInt(XmlParserSerializerUtils.getChildText(parser));
                        Log.e(TAG, "EMSMAXNUMBER is " + number);
                        userTerminalInfo.setEMSmaxNumber(number);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO_STATUS)){
                    try{
                        Log.e(TAG, "Entro en START_STATUS tag name is " + nameSpace);
                        String status = XmlParserSerializerUtils.getChildText(parser);
                        if (status.equals("approved"))
                        	userTerminalInfo.setStatus(true);
                        else if (status.equals("not approved"))
                        	userTerminalInfo.setStatus(false);
                        else throw new ParseException("Error parsing " + XmlConstants.XSD_TERMINALINFO_STATUS);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO_EMAIL)){
                    try{
                        Log.e(TAG, "Entro en START_EMAIL tag name is " + nameSpace);
                        String m = XmlParserSerializerUtils.getChildText(parser);
                        if (m.equals("yes"))
                            userTerminalInfo.setEmail(true);
                        else
                            userTerminalInfo.setEmail(false);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO_EMN)){
                    try{
                        Log.e(TAG, "Entro en START_EMN tag name is " + nameSpace);
                        String e = XmlParserSerializerUtils.getChildText(parser);
                        if (e.equals("yes"))
                            userTerminalInfo.setEmn(true);
                        else
                            userTerminalInfo.setEmn(false);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO_ADCOTA)){
                    try{
                        Log.e(TAG, "Entro en START_ADCOTA tag name is " + nameSpace);
                        String a = XmlParserSerializerUtils.getChildText(parser);
                        if (a.equals("yes"))

                            userTerminalInfo.setAdcOta(true);
                        else
                            userTerminalInfo.setAdcOta(false);
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_LASTUPDATED)){
                    try{
                        Log.e(TAG, "Entro en START_LASTUPDATED tag name is " + nameSpace);
                        userTerminalInfo.setLastUpdated(XmlParserSerializerUtils.getChildText(parser));
                    }catch(XmlPullParserException xppe){
                        throw new ParseException("Problem with getChild");
                    }
                }
                if(nameSpace.equals(XmlConstants.XSD_EXTENSION)){
                    try{
                        Log.e(TAG, "Entro en START_EXTENSION tag name is " + nameSpace);
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

}
///@endcond