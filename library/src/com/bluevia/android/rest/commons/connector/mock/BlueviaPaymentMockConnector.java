/// @cond private
package com.bluevia.android.rest.commons.connector.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.util.Log;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.connector.http.HttpException;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.http.authentication.AuthenticationInfo;
import com.bluevia.android.rest.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.android.rest.commons.data.xmlrpc.MethodCall;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;


/**
 * @class MockPaymentConnector
 * Class that represents the mock connector object for Payment.
 * Using this class you will be able to test API without a real network connection
 * 
 * @author Telefonica R&D 
 */
public class BlueviaPaymentMockConnector extends MockConnector {

    private static final String TAG = "MockPaymentConnector";
    
    protected static final String FEED_PAYMENT_BASE_URI = "RPC/Payment_Sandbox";
	protected static final String MAKE_PAYMENT_METHOD = "PAYMENT";
	protected static final String MAKE_CANCEL_AUTHORIZATION = "CANCEL_AUTHORIZATION";
	protected static final String GET_PAYMENT_STATUS = "GET_PAYMENT_STATUS";
    
    private static final String XML_METHOD_RESPONSE_INIT = 
    	"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
    	"<tns:methodResponse " +
    	"xsi:schemaLocation=\"http://www.telefonica.com/schemas/UNICA/RPC/payment/v1 " +
    	"UNICA_API_RPC_payment_types_v1_0.xsd\" " +
    	"xmlns:rpc=\"http://www.telefonica.com/schemas/UNICA/RPC/definition/v1\" " +
    	"xmlns:tns=\"http://www.telefonica.com/schemas/UNICA/RPC/payment/v1\" " +
    	"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
    
    private static final String XML_PAYMENT_RESULT_INIT = 
    	"<tns:result>" +
    		"<tns:paymentResult>";
    
    private static final String XML_PAYMENT_RESULT_END = 
			"</tns:paymentResult>" +
		"</tns:result>";
    
    private static final String XML_PAYMENT_STATUS_INIT = 
    	"<tns:result>" +
    		"<tns:getPaymentStatusResult>";
    
    private static final String XML_PAYMENT_STATUS_END = 
			"</tns:getPaymentStatusResult>" +
		"</tns:result>";

    private static final String XML_METHOD_RESPONSE_END = 
       	"</tns:methodResponse>";    	

    private BlueviaOauthMockConnector mOauthConnector;
    
    /**
     * Instantiates the MockConnector
     *
     * @param testType the code to indicate the MockConnector the behavior to emulate
     */
    public BlueviaPaymentMockConnector(int testType) {
        super(testType);
        mOauthConnector = new BlueviaOauthMockConnector(testType);
    }

    @Override
	/**
	 * 
	 */
	public void close() {
    	Log.d(TAG, "MockPaymentConnector closed");
	}

	/**
	 * 
	 */
	@Override
	protected InputStream createMockEntity(String feedUri, Entity entity,
			BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo,
			HttpQueryParams parameters, HashMap<String, String> requestHeaders,
			HashMap<String, String> responseHeaders,
			ArrayList<BlueviaPartBase> parts) throws IOException, HttpException,
			ParseException {
		InputStream res = null;
		
		Log.d(TAG, "Starting MockEntity - mTestType: " + mTestType);

		//Oauth process
    	if (feedUri.contains("getRequestToken") || feedUri.contains("getAccessToken") )
        	return mOauthConnector.createMockEntity(feedUri, entity, serializer, 
        			authenticationInfo, parameters, requestHeaders, responseHeaders, parts);
		
        switch (mTestType) {
            case TEST_OK_ORDINAL:
            	String result = buildXmlResponse(entity);
            	res = new ByteArrayInputStream(result.getBytes());
            break;
            
            case TEST_ERROR_IOEXCEPTION_ORDINAL:
                throw new IOException("Mock IO Exception launched");
            case TEST_ERROR_HTTPEXCEPTION_ORDINAL:
                throw new HttpException("Mock Http Exception launched", HttpException.INTERNAL_SERVER_ERROR, res);
            case TEST_UNAUTHORIZED_ORDINAL:
            	throw new HttpException("Mock Unauthorized Exception launched", HttpException.UNAUTHORIZED, null);
 
            default:
            	throw new IllegalArgumentException("Mode not supported");
        }
            	
		return res;
	}

	/**
	 * 
	 */
	@Override
	protected InputStream retrieveMockEntity(String feedUri, AuthenticationInfo authenticationInfo, 
			HttpQueryParams parameters, HashMap<String, String> responseHeaders) throws IOException,
			HttpException {
    	Log.i(TAG, "Retrieve Entity method not implemented for MockPaymentConnector");
		return null;
	}
	/**
	 * 
	 * @param entity
	 * @return
	 */
	protected String buildXmlResponse(Entity entity){
		
		StringBuffer sb = new StringBuffer(XML_METHOD_RESPONSE_INIT);
		MethodCall me = (MethodCall)entity;
		
		//Id and version parameters are not supported by the XSD.
    	sb.append("<rpc:id>");
    	sb.append(me.getId());
    	sb.append("</rpc:id>");
    	sb.append("<rpc:version>");
    	sb.append(me.getVersion());
    	sb.append("</rpc:version>");	
		
    	if (me.getMethod().equals(MAKE_PAYMENT_METHOD)) {
        	sb.append(XML_PAYMENT_RESULT_INIT);
			sb.append("<tns:transactionId>");
	    	String transactionId = Integer.toString((new Random()).nextInt(9999999));
	        sb.append("transactionId_" + transactionId);
			sb.append("</tns:transactionId>");
			sb.append("<tns:transactionStatus>");
			sb.append("PENDING");
			sb.append("</tns:transactionStatus>");
			sb.append("<tns:transactionStatusDescription>");
			sb.append("transactionStatusDescription_"+ transactionId);
			sb.append("</tns:transactionStatusDescription>");
	    	sb.append(XML_PAYMENT_RESULT_END);
    	} else if (me.getMethod().equals(MAKE_CANCEL_AUTHORIZATION)){
    		//Do nothing
    	} else if (me.getMethod().equals(GET_PAYMENT_STATUS)){
    		sb.append(XML_PAYMENT_STATUS_INIT);
    		sb.append("<tns:transactionStatus>");
			sb.append("SUCCESS");
			sb.append("</tns:transactionStatus>");
			sb.append("<tns:transactionStatusDescription>");
			sb.append("Successful Transaction");
			sb.append("</tns:transactionStatusDescription>");
	    	sb.append(XML_PAYMENT_STATUS_END);
    	}
    	
    	sb.append(XML_METHOD_RESPONSE_END);
    	
    	return sb.toString();
	}

}
