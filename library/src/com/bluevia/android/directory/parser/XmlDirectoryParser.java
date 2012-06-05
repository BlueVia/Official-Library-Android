package com.bluevia.android.directory.parser;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.parser.IParser;
import com.bluevia.android.commons.parser.ParseException;


/**
 * XML implementation of {@link com.bluevia.android.commons.parser.IParser IParser}
 * Class that represents the parser object for any Directory entity.
 * Using this class you will be able to parse XML documents containing
 * a representation of any Directory entity into an instance
 * object of this entity
 *
 * @author Telefonica R&D
 * 
 */
public class XmlDirectoryParser implements IParser {

    private static final int USER_INFO = 0;
    private static final int TERMINAL_INFO = 1;
    private static final int PROFILE = 2;
    private static final int ACCESS_INFO = 3;
    private static final int PERSONAL_INFO = 4;
    private static final String TAG = "XmlDirectoryParser";

    private static final HashMap<String, Integer> sParserMatcher;
    
    static {
        sParserMatcher = new HashMap<String, Integer>();
        sParserMatcher.put(XmlConstants.USERINFO, USER_INFO);
        sParserMatcher.put(XmlConstants.TERMINALINFO, TERMINAL_INFO);
        sParserMatcher.put(XmlConstants.PROFILE, PROFILE);
        sParserMatcher.put(XmlConstants.ACCESSINFO, ACCESS_INFO);	
        sParserMatcher.put(XmlConstants.PERSONALINFO, PERSONAL_INFO);	
    }

    public Entity parse(InputStream is) throws ParseException {

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
            throw new ParseException("Could not create XmlDirectoryProfileParser", e);
        }


        int eventType;

        try {
            eventType = parser.getEventType();
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

        switch (parserType) {
        	case USER_INFO:
        		Log.v(TAG, "Using XmlDirectoryUserInfoParser");
        		return new XmlDirectoryUserInfoParser().parse(parser);
            case TERMINAL_INFO:
                Log.v(TAG, "Using XmlDirectoryTerminalInfoParser");
                return new XmlDirectoryTerminalInfoParser().parse(parser);
            case PROFILE:
                Log.v(TAG, "Using XmlDirectoryProfileParser");
                return new XmlDirectoryProfileParser().parse(parser);
            case ACCESS_INFO:
                Log.v(TAG, "Using XmlDirectoryAccessInfoParser");
                return new XmlDirectoryAccessInfoParser().parse(parser);
            case PERSONAL_INFO:
                Log.v(TAG, "Using XmlDirectoryAccessInfoParser");
                return new XmlDirectoryPersonalInfoParser().parse(parser);

            default:
                throw new ParseException(
                	new XmlPullParserException("Entity class does not support parsing"));
        }
    }
}
