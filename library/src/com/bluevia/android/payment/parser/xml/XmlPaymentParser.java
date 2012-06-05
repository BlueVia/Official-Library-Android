/**
 * @package com.bluevia.android.payment.parser.xml This package contains the classes in order to serialize and parse XML Payment data.
 */
package com.bluevia.android.payment.parser.xml;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.parser.ParseException;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.commons.parser.xml.XmlParserSerializerUtils;
import com.bluevia.android.commons.parser.xmlrpc.XmlMethodResponseParser;
import com.bluevia.android.payment.data.PaymentResult;
import com.bluevia.android.payment.data.PaymentStatus;
import com.bluevia.android.payment.data.PaymentStatus.TransactionStatusType;

/**
 * @class XmlPaymentParser
 * XML implementation of {@link com.bluevia.android.commons.parser.IParser IParser}
 * Class that represents the parser object for any Payment entity.
 * Using this class you will be able to parse XML documents containing
 * a representation of any Payment entity into an instance
 * object of this entity
 * @author Telefonica R&D
 */
public class XmlPaymentParser extends XmlMethodResponseParser {

    private static final String TAG = "XmlPaymentParser";

	/**
	 * Overrides XmlMethodResponseParser::handleResult
	 * Parses inside the 'result' element looking for a 'paymentResult' element with information about the result
	 * of the payment process.
	 * @param eventType Last tag gathered with XmlPullParser from the XML. 
	 * @return the PaymentResult entity data type
     * @throws ParseException
     * @throws XmlPullParserException
     * @throws IOException
	 */
    @Override
    protected Entity handleResult(int eventType) throws ParseException, XmlPullParserException, IOException {
    	Entity result = null;
        if (eventType != XmlPullParser.START_TAG) {
            throw new ParseException("Expected event START_TAG: Actual event: "
                    + XmlPullParser.TYPES[eventType]);
        }
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = mParser.getName();

            switch (eventType) {
                case XmlPullParser.END_TAG:
                    if (name.equals(XmlConstants.XSD_RPC_RESULT)) {
                    	if (result == null)
                    		throw new ParseException("Result element is missing");
                        return result;
                    }
                    break;
                case XmlPullParser.START_TAG:
                    if (name.equals(XmlConstants.XSD_RPC_RESULT)) {
                    	Log.d(TAG, "Entering result");
                    	//Do nothing, continue to "PaymentResult" tag
                    } else if (name.equals(XmlConstants.XSD_PAYMENT_PAYMENT_RESULT)) {
                        result = handlePaymentResult(eventType);
                    } else if (name.equals(XmlConstants.XSD_PAYMENT_GET_PAYMENT_STATUS)) {
                        result = handlePaymentStatus(eventType);
                    }                    
                default:
                    break;
            }
            eventType = mParser.next();
        }
        return null;
    }
    
    /**
	 * Parses inside the 'paymentResult' element gathering information about the result
	 * of the payment process.
     * 
     * @param eventType Last tag gathered with XmlPullParser from the XML.
     * @return the PaymentResult entity data type
     * @throws ParseException
     * @throws XmlPullParserException
     * @throws IOException
     */
	private PaymentResult handlePaymentResult(int eventType) throws ParseException, XmlPullParserException, IOException {
		PaymentResult paymentResult = new PaymentResult();

        if (eventType != XmlPullParser.START_TAG) {
            throw new ParseException("Expected event START_TAG: Actual event: "
                    + XmlPullParser.TYPES[eventType]);
        }

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = mParser.getName();

            switch (eventType) {
                case XmlPullParser.END_TAG:
                    if (name.equals(XmlConstants.XSD_PAYMENT_PAYMENT_RESULT)) {
                        return paymentResult;
                    }
                    break;

                case XmlPullParser.START_TAG:
                    if (name.equals(XmlConstants.XSD_PAYMENT_TRANSACTION_ID)) {
                    	try{
                    		paymentResult.setTransactionId(XmlParserSerializerUtils.getChildText(mParser));
                    	}catch(XmlPullParserException xppe){
                    		throw new ParseException("Problem with getChild. Element : transaction id");
                    	}
                    }
                    if (name.equals(XmlConstants.XSD_PAYMENT_TRANSACTION_STATUS)) {
                    	try{
                    		String stringStatus = XmlParserSerializerUtils.getChildText(mParser);
                    		TransactionStatusType status = stringToTransactionStatusType(stringStatus);
                    		paymentResult.setTransactionStatus(status);
                    	}catch(XmlPullParserException xppe){
                    		throw new ParseException("Problem with getChild. Element : transaction status");
                    	}
                    }
                    if (name.equals(XmlConstants.XSD_PAYMENT_TRANSACTION_STATUS_DESCRIPTION)) {
                    	try{
                    		paymentResult.setTransactionStatusDescription(XmlParserSerializerUtils.getChildText(mParser));
                    	}catch(XmlPullParserException xppe){
                    		throw new ParseException("Problem with getChild. Element : transaction status description");
                    	}
                    }
                  default:
                    break;
            }
            eventType = mParser.next();
        }
        return null;
	}    
	
	/**
	 * Parses inside the 'getPaymentStatusResult' element gathering information about the status
	 * of the payment process.
     * 
     * @param eventType Last tag gathered with XmlPullParser from the XML.
     * @return the PaymentResult entity data type
     * @throws ParseException
     * @throws XmlPullParserException
     * @throws IOException
     */
	private PaymentStatus handlePaymentStatus(int eventType) throws ParseException, XmlPullParserException, IOException {
		PaymentStatus paymentStatus = new PaymentStatus();

        if (eventType != XmlPullParser.START_TAG) {
            throw new ParseException("Expected event START_TAG: Actual event: "
                    + XmlPullParser.TYPES[eventType]);
        }

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = mParser.getName();

            switch (eventType) {
                case XmlPullParser.END_TAG:
                    if (name.equals(XmlConstants.XSD_PAYMENT_GET_PAYMENT_STATUS)) {
                        return paymentStatus;
                    }
                    break;

                case XmlPullParser.START_TAG:
                    if (name.equals(XmlConstants.XSD_PAYMENT_TRANSACTION_STATUS)) {
                    	try{
                    		String stringStatus = XmlParserSerializerUtils.getChildText(mParser);
                    		TransactionStatusType status = stringToTransactionStatusType(stringStatus);
                    		paymentStatus.setTransactionStatus(status);
                    	}catch(XmlPullParserException xppe){
                    		throw new ParseException("Problem with getChild. Element : transaction status");
                    	}
                    }
                    if (name.equals(XmlConstants.XSD_PAYMENT_TRANSACTION_STATUS_DESCRIPTION)) {
                    	try{
                    		paymentStatus.setTransactionStatusDescription(XmlParserSerializerUtils.getChildText(mParser));
                    	}catch(XmlPullParserException xppe){
                    		throw new ParseException("Problem with getChild. Element : transaction status description");
                    	}
                    }
                  default:
                    break;
            }
            eventType = mParser.next();
        }
        return null;
	}    
	
	private TransactionStatusType stringToTransactionStatusType(String input) throws ParseException{
		if (input.equals(XmlConstants.XSD_PAYMENT_ATTR_TRANSACTION_STATUS_SUCCESS_VALUE))
			return  TransactionStatusType.SUCCESS;
		else if (input.equals(XmlConstants.XSD_PAYMENT_ATTR_TRANSACTION_STATUS_FAILURE_VALUE))
			return TransactionStatusType.FAILURE;
		else if (input.equals(XmlConstants.XSD_PAYMENT_ATTR_TRANSACTION_STATUS_PENDING_VALUE))
			return TransactionStatusType.PENDING;
		else throw new ParseException("Unexpected value : transaction status");
	}
}

