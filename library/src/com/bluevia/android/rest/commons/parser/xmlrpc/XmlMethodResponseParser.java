///@cond private
package com.bluevia.android.rest.commons.parser.xmlrpc;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.data.xmlrpc.MethodResponse;
import com.bluevia.android.rest.commons.data.xmlrpc.RpcError;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParser;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.commons.parser.xml.XmlParserSerializerUtils;

import android.util.Log;
import android.util.Xml;


/**
 * Abstract Xml parser for MethodResponse entities.
 * 
 * @author Telefonica I+D
 * 
 */
public abstract class XmlMethodResponseParser implements BlueviaEntityParser {

    private static final String TAG = "XmlMethodResponseParser";
    
    protected XmlPullParser mParser = null;

    /*
     * (non-Javadoc)
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParser#parseEntry(java.io.InputStream)
     */
	public MethodResponse parseEntry(InputStream is) throws ParseException, IOException {
		
		Log.d(TAG, "Starting parseEntry");
		
		//Check first that is is not null
        if (is == null) {
            return null;
        }
        
    	mParser = Xml.newPullParser();
    	
    	try{
    		mParser.setInput(is,null);
		} catch(XmlPullParserException e){
			Log.e(TAG, "Exception occureed while reading the XML Document", e);
			throw new ParseException("Could not create the CallSessionResult", e);
		}

		int eventType;
		
		try {
            eventType = mParser.getEventType();
        } catch (XmlPullParserException e) {
            throw new ParseException("Could not parse adResponse entity.", e);
        }

        if (eventType != XmlPullParser.START_DOCUMENT) {
            throw new ParseException("", new XmlPullParserException(
                    "Attempting to initialize parsing beyond the start of the document."));
        }
        
        try {
            // Gets the first eventType
            eventType = mParser.next();

            // Gets the MethodResponse
            MethodResponse response = handleMethodResponse(eventType);

            if ((response == null) || (! (response.isValid())))
                throw new ParseException("The xml received for the adResponse is not valid.");

            return response;
        } catch (XmlPullParserException xppe) {
            throw new ParseException("Could not read next event.", xppe);
        } catch (IOException ioe) {
            throw new ParseException("Could not read next event.", ioe);
        }
	}
	
	/**
	 * This function parse the common methodResponse tag
	 * 
	 * @param eventType
	 * @return
	 * @throws ParseException
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private MethodResponse handleMethodResponse(int eventType) throws ParseException, XmlPullParserException, IOException{
		MethodResponse response = null;
		
		Log.d(TAG, "Start handleMethodResponse");
		
		if (eventType != XmlPullParser.START_TAG) {
            throw new ParseException("Expected event START_TAG: Actual event: "
                    + XmlPullParser.TYPES[eventType]);
        }

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = mParser.getName();
            Log.d(TAG, "mParser: " + name);
            switch (eventType) {
                case XmlPullParser.END_TAG:
                    if (XmlConstants.XSD_RPC_METHOD_RESPONSE.equals(name)) {
                        return response;
                    }
                    break;
                case XmlPullParser.START_TAG:
                    if (name.equals(XmlConstants.XSD_RPC_METHOD_RESPONSE)) {
                        response = new MethodResponse();
                    } else if (name.equals(XmlConstants.XSD_RPC_ID_ATTR)) {
                    	response.setId(XmlParserSerializerUtils.getChildText(mParser));
                    } else if (name.equals(XmlConstants.XSD_RPC_VERSION_ATTR)) {
                    	String version = XmlParserSerializerUtils.getChildText(mParser);
                        if ((version == null) || (version.trim().length() == 0))
                            throw new ParseException("The xml received for the CallSessionResult is not valid.");
                        response.setVersion(version);
                    } else if (name.equals(XmlConstants.XSD_RPC_RESULT)) {
                        if (response == null)
                            throw new ParseException("MethodResponse Element is missing");

                        //Check there is not already one ResponseEntity. (xml should contain just one result element)
                        if (response.getResponseEntity() != null)
                            throw new ParseException("MethodResponse Element has several result elements.  It should contain just one.");

                        response.setResponseEntity(handleResult(eventType));
                    } else if (name.equals(XmlConstants.XSD_RPC_ERROR)) {
                        if (response == null)
                            throw new ParseException("MethodResponse Element is missing");

                        response.setResponseEntity(handleError(eventType));
                    }
            }
            eventType = mParser.next();
        }
        return null;
	}
	
	/**
	 * Method to be implemented by extended classes to parse the result of the method response.
	 * 
	 * @param eventType
	 * @return the result Entity of the MethodResponse
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws XmlPullParserException 
	 */
	protected abstract Entity handleResult(int eventType) throws ParseException, XmlPullParserException, IOException;

	/**
	 * TODO
	 * 
	 * @param eventType
	 * @return
	 * @throws ParseException
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private RpcError handleError(int eventType) throws ParseException, XmlPullParserException, IOException {
		return XmlParserSerializerUtils.handleError(mParser, eventType);
	}
}
///@endcond