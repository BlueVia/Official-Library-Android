/// @cond private
package com.bluevia.android.rest.commons.connector.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.bluevia.android.rest.ad.data.AdRequest;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.connector.http.HttpException;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.http.authentication.AuthenticationInfo;
import com.bluevia.android.rest.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;

import android.util.Log;

/**
 * Mock implementation of the communication with the gSDP server via HTTP for the Ad APIs
 * This class simulates the behavior of the gSDP server when using Ad API. It is called from MockConnector
 *
 * @see MockConnector
 * @author Telefonica R&D
 * 
*/
public class BlueviaAdMockConnector extends MockConnector {

    protected static final String FEED_AD_BASE_URI = "Advertising_Sandbox/simple/requests";

    private static final String XML_ADRESPONSE_INIT = 
    		  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<tns:adResponse id=\"%s\" version=\"3\"" /* adresponse id */
            + " xmlns:tns=\"http://www.telefonica.com/schemas/UNICA/REST/sgap/v1/\" "
            + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
            + " xsi:schemaLocation=\"http://www.telefonica.com/schemas/UNICA/REST/sgap/v1/ UNICA_API_SGAP_REST_Binding_types_v1_0.xsd \""
            + ">"
            + "<tns:ad ad_placement=\"0\" campaign=\"34\" flight=\"45\" id=\"5005\">"
            + "    <tns:resource ad_presentation=\"%s\">";
    
    private static final String XML_ADRESPONSE_BANNER_ELEMENT = 
    		  "      <tns:creative_element type=\"image\">"
            + "        <tns:attribute type=\"locator\">https://www.canalcliente.movistar.es/vgn/images/portal/navigation/site_259/s_259_311541878.gif</tns:attribute>"
            + "        <tns:interaction type=\"click2wap\">"
            + "          <tns:attribute type=\"URL\">http://www.movistar.es</tns:attribute>"
            + "        </tns:interaction>"
            + "      </tns:creative_element>";
            
    private static final String XML_ADRESPONSE_TEXT_ELEMENT = 
    	 	  "      <tns:creative_element type=\"text\"> "
            + "       <tns:attribute type=\"codec\">UTF-8</tns:attribute> "
            + "       <tns:attribute type=\"adtext\">Internet m\u00f3vil con tu adsl por solo 9,50 euros/mes.</tns:attribute> "
            + "       <tns:interaction type=\"click2wap\"> "
            + "           <tns:attribute type=\"URL\">http://www.tarifas.movistar.es/particulares/internet/tarifas</tns:attribute>"
            + "       </tns:interaction>"
            + "   </tns:creative_element>";
     
    private static final String XML_ADRESPONSE_END = 
    	 	  "    </tns:resource>"
            + "  </tns:ad>"
            + "</tns:adResponse>";

	private static final String TAG = "MockAdConnector";
     
    /**
     * Instantiates the MockConnector
     *
     * @param testType the code to indicate the MockConnector the behavior to emulate
     */
    public BlueviaAdMockConnector(int testType) {
        super(testType);
    }

    /*
     * (non-Javadoc)
     * @see com.bluevia.android.rest.commons.connector.mock.MockConnector#createMockEntity(java.lang.String, com.bluevia.android.rest.commons.Entity, com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer, com.bluevia.android.rest.oauth.Oauth, com.bluevia.android.rest.commons.connector.http.HttpQueryParams, java.util.HashMap, java.util.HashMap, java.util.ArrayList)
     */
    @Override
	protected InputStream createMockEntity(String feedUri, Entity entity,
			BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo,
			HttpQueryParams parameters, HashMap<String, String> requestHeaders,
			HashMap<String, String> responseHeaders,
			ArrayList<BlueviaPartBase> parts) throws IOException, HttpException,
			ParseException {

        // Emulate serialization
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            serializer.serialize(entity, out);
        } catch (IOException iox) {
            throw iox;
        } catch (ParseException pex) {
            throw pex;
        }

        if (!(entity instanceof AdRequest))
            return null; // Should never happen because serialization should
                         // detect it

        AdRequest adRequest = (AdRequest) entity;
        ByteArrayInputStream result = null;
        String xml = null;       
    	
        switch (mTestType) {
            case TEST_OK_ORDINAL:
            	xml = buildXmlResponse(adRequest.getAdPresentation());
                result = new ByteArrayInputStream(String.format(xml, adRequest.getAdRequestId(),
                        adRequest.getAdPresentation() != null ? adRequest.getAdPresentation() : 0).getBytes());
                break;
            case TEST_ERROR_IOEXCEPTION_ORDINAL:
            	throw new IOException("Mock IO Exception launched");
            case TEST_ERROR_HTTPEXCEPTION_ORDINAL:
            	throw new HttpException("Mock Http Exception launched",
                        HttpException.INTERNAL_SERVER_ERROR, null);
            case TEST_UNAUTHORIZED_ORDINAL:
            	throw new HttpException("Mock Unauthorized Exception launched", HttpException.UNAUTHORIZED, null);
            default:
            	throw new IllegalArgumentException("Mode not supported");
        }
        return result;
    }
    
    /**
     * Builds a mock an xml response to a request depending on the type of the ad presentation
     * type. 
     * @param type
     * @return
     */
    protected String buildXmlResponse(AdRequest.AdPresentationType type){
    	if (type == null)
    		return XML_ADRESPONSE_INIT + XML_ADRESPONSE_BANNER_ELEMENT + XML_ADRESPONSE_TEXT_ELEMENT + XML_ADRESPONSE_END;
    	
    	switch (type) {
		case BANNER:
			return XML_ADRESPONSE_INIT + XML_ADRESPONSE_BANNER_ELEMENT + XML_ADRESPONSE_END;
		case TEXT:
			return XML_ADRESPONSE_INIT + XML_ADRESPONSE_TEXT_ELEMENT + XML_ADRESPONSE_END;
		default:
			return null;
		}
    }

    /*
     * (non-Javadoc)
     * @see com.bluevia.android.rest.commons.connector.mock.MockConnector#retrieveMockEntity(java.lang.String, com.bluevia.android.rest.oauth.Oauth, com.bluevia.android.rest.commons.connector.http.HttpQueryParams)
     */
	@Override
	protected InputStream retrieveMockEntity(String feedUri,
			AuthenticationInfo authenticationInfo, HttpQueryParams parameters) throws IOException,
			HttpException {
    	Log.i(TAG, "Retrieve Entity method not implemented for MockAdConnector");
		return null;
	}
    
	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.connector.mock.MockConnector#close()
	 */
    public void close() {
    	Log.d(TAG, "MockAdConnector closed");
    }

}

///@endcond
