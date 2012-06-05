package com.bluevia.android.directory.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bluevia.android.commons.parser.ParseException;
import com.bluevia.android.commons.parser.xml.XmlParserSerializerUtils;
import com.bluevia.android.directory.data.TerminalInfo;

/**
 * Xml parser for XmlDirectoryUserTerminalInfo entities.
 *
 * @author Telefonica I+D
 * 
*/
class XmlDirectoryTerminalInfoParser implements DirectoryEntityParser {

    public TerminalInfo parse(XmlPullParser parser) throws ParseException {

    	TerminalInfo userTerminalInfo = new TerminalInfo();
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

                if (XmlConstants.TERMINALINFO.equals(nameSpace)) {
                    return userTerminalInfo;
                }
                break;

            case XmlPullParser.START_TAG:
                if(nameSpace.equals(XmlConstants.TERMINALINFO_BRAND)){
                    try{
                        String b = XmlParserSerializerUtils.getChildText(parser);
                        userTerminalInfo.setBrand(b);
                    } catch(XmlPullParserException e){
                        throw new ParseException("Problem with getChild", e);
                    }
                }
                if(nameSpace.equals(XmlConstants.TERMINALINFO_MODEL)){
                    try{
                        userTerminalInfo.setModel(XmlParserSerializerUtils.getChildText(parser));
                    } catch(XmlPullParserException e){
                        throw new ParseException("Problem with getChild", e);
                    }
                }
                if(nameSpace.equals(XmlConstants.TERMINALINFO_VERSION)){
                    try{
                        userTerminalInfo.setVersion(XmlParserSerializerUtils.getChildText(parser));
                    } catch(XmlPullParserException e){
                        throw new ParseException("Problem with getChild", e);
                    }
                }
                if(nameSpace.equals(XmlConstants.TERMINALINFO_MMS)){
                    try{
                        String m = XmlParserSerializerUtils.getChildText(parser);
                        if (m.equals("yes"))
                            userTerminalInfo.setMMS(true);
                        else
                            userTerminalInfo.setMMS(false);
                    } catch(XmlPullParserException e){
                        throw new ParseException("Problem with getChild", e);
                    }
                }
                if(nameSpace.equals(XmlConstants.TERMINALINFO_EMS)){
                    try{
                        String e = XmlParserSerializerUtils.getChildText(parser);
                        if (e.equals("yes"))
                            userTerminalInfo.setEMS(true);
                        else
                            userTerminalInfo.setEMS(false);
                    } catch(XmlPullParserException e){
                        throw new ParseException("Problem with getChild", e);
                    }
                }
                if(nameSpace.equals(XmlConstants.TERMINALINFO_SMARTMESSAGING)){
                    try{
                        String s = XmlParserSerializerUtils.getChildText(parser);
                        if (s.equals("yes"))
                            userTerminalInfo.setSmartMessaging(true);
                        else
                            userTerminalInfo.setSmartMessaging(false);
                    } catch(XmlPullParserException e){
                        throw new ParseException("Problem with getChild", e);
                    }
                }
                if(nameSpace.equals(XmlConstants.TERMINALINFO_WAP)){
                    try{
                        String w = XmlParserSerializerUtils.getChildText(parser);
                        if (w.equals("yes"))
                            userTerminalInfo.setWAP(true);
                        else
                            userTerminalInfo.setWAP(false);
                    } catch(XmlPullParserException e){
                        throw new ParseException("Problem with getChild", e);
                    }
                }
                if(nameSpace.equals(XmlConstants.TERMINALINFO_USSDPHASE)){
                    try{
                        //int ussd = Integer.parseInt(XmlDirectoryParserHelper.getChildText(parser));
                        String ussd = XmlParserSerializerUtils.getChildText(parser);
                        userTerminalInfo.setUSSDPhase(ussd);
                    } catch(XmlPullParserException e){
                        throw new ParseException("Problem with getChild", e);
                    }
                }
                if(nameSpace.equals(XmlConstants.TERMINALINFO_EMSMAXNUMBER)){
                    try{
                        int number = Integer.parseInt(XmlParserSerializerUtils.getChildText(parser));
                        userTerminalInfo.setEMSmaxNumber(number);
                    } catch(XmlPullParserException e){
                        throw new ParseException("Problem with getChild", e);
                    }
                }
                if(nameSpace.equals(XmlConstants.TERMINALINFO_WAP_PUSH)){
                    try{
                        String w = XmlParserSerializerUtils.getChildText(parser);
                        if (w.equals("yes"))
                            userTerminalInfo.setWapPush(true);
                        else
                            userTerminalInfo.setWapPush(false);
                    } catch(XmlPullParserException e){
                        throw new ParseException("Problem with getChild", e);
                    }
                }
                if(nameSpace.equals(XmlConstants.TERMINALINFO_MMS_VIDEO)){
                    try{
                        String w = XmlParserSerializerUtils.getChildText(parser);
                        if (w.equals("yes"))
                            userTerminalInfo.setMmsVideo(true);
                        else
                            userTerminalInfo.setMmsVideo(false);
                    } catch(XmlPullParserException e){
                        throw new ParseException("Problem with getChild", e);
                    }
                }
                if(nameSpace.equals(XmlConstants.TERMINALINFO_VIDEO_STREAMING)){
                    try{
                        String w = XmlParserSerializerUtils.getChildText(parser);
                        if (w.equals("yes"))
                            userTerminalInfo.setVideoStreaming(true);
                        else
                            userTerminalInfo.setVideoStreaming(false);
                    } catch(XmlPullParserException e){
                        throw new ParseException("Problem with getChild", e);
                    }
                }
                if(nameSpace.equals(XmlConstants.TERMINALINFO_SCREENRESOLUTION)){
                    try{
                        userTerminalInfo.setScreenResolution(XmlParserSerializerUtils.getChildText(parser));
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
