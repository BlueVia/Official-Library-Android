///@cond private
package com.bluevia.android.rest.directory.parser.xml;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParser;
import com.bluevia.android.rest.commons.parser.ParseException;

import android.util.Log;
import android.util.Xml;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;


/**
 * XML implementation of {@link com.bluevia.android.rest.commons.parser.BlueviaEntityParser BlueviaEntityParser}
 * Class that represents the parser object for any Directory entity.
 * Using this class you will be able to parse XML documents containing
 * a representation of any Directory entity into an instance
 * object of this entity
 *
 * @author Telefonica R&D
 * 
 */
public class XmlDirectoryParser implements BlueviaEntityParser {

    private static final int USER_INFO = 0;
    private static final int TERMINAL_INFO = 1;
    private static final int PROFILE = 2;
    private static final int ACCESS_INFO = 3;
    private static final String TAG = "XmlDirectoryParser";

    private static final HashMap<String, Integer> sParserMatcher;
    
    static {
        sParserMatcher = new HashMap<String, Integer>();
        sParserMatcher.put(XmlConstants.XSD_USERINFO, USER_INFO);
        sParserMatcher.put(XmlConstants.XSD_TERMINALINFO, TERMINAL_INFO);
        sParserMatcher.put(XmlConstants.XSD_PROFILE, PROFILE);
        sParserMatcher.put(XmlConstants.XSD_ACCESSINFO, ACCESS_INFO);	
    }

    /**
     * @see {@link com.bluevia.android.rest.commons.parser.BlueviaEntityParser com.bluevia.android.rest.parser.BlueviaEntityParser#parseEntry()}
     */
    public Entity parseEntry(InputStream is) throws ParseException, IOException {

       int parserType = 0;
       XmlPullParser parser=null;
       if (is == null) {
           return null;
           }
        parser = Xml.newPullParser();

        try{
            parser.setInput(is,null);
        } catch(XmlPullParserException e){
            Log.e(TAG, "Exception occurred while reading the XML Document", e);
            throw new ParseException("Could not create XmlDirectoryUserProfileParser", e);
        }


        int eventType;

        try {
            eventType = parser.getEventType();
            Log.d(TAG, "eventTypeA "+eventType);
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Could not parse directoryEntity entity");
            throw new ParseException("Could not parse Directory Info entity.", e);
        }
        if (eventType != XmlPullParser.START_DOCUMENT) {
            Log.e(TAG, "Attempting to initialize parsing beyond "
                    + "the start of the document.");
            throw new ParseException("",new XmlPullParserException("Attempting to initialize parsing beyond "
                    + "the start of the document."));
        }
        
        try {
            eventType = parser.next();
            Log.d(TAG, "eventTypeB "+eventType);
        } catch (XmlPullParserException xppe) {
            Log.e(TAG,"XmlPullParserException getting next event");
            throw new ParseException("Could not parse Directory Info entity.", xppe);
        } catch (IOException ioe) {
            Log.e(TAG,"IOException getting next event");
            throw new ParseException("Error during IO", ioe);
        }

        if (eventType == XmlPullParser.START_TAG){
            String nameSpace = parser.getName();
            parserType = ((Integer)sParserMatcher.get(nameSpace)).intValue();
        }

        Log.e(TAG, "El isClass vale "+parserType);
        switch (parserType) {
        	case USER_INFO:
        		Log.e(TAG, "ENTRAMOS EN EL PARSER XMLUSERINFOPARSER");
        		return new XmlDirectoryUserInfoParser().parseEntry(parser);
            case TERMINAL_INFO:
                Log.e(TAG, "ENTRAMOS EN EL PARSER XMLDIRECTORYTEMINALINFOPARSER");
                return new XmlDirectoryUserTerminalInfoParser().parseEntry(parser);
            case PROFILE:
                Log.e(TAG, "ENTRAMOS EN EL PARSER XMLDIRECTORYPROFILEPARSER");
                return new XmlDirectoryUserProfileParser().parseEntry(parser);
            case ACCESS_INFO:
                Log.e(TAG, "ENTRAMOS EN EL PARSER XMLDIRECTORYACCESSINFOPARSER");
                return new XmlDirectoryUserAccessInfoParser().parseEntry(parser);

            default:
                throw new ParseException(
                	new XmlPullParserException("Entity class does not support parsing"));
        }
    }
}
///@endcond
