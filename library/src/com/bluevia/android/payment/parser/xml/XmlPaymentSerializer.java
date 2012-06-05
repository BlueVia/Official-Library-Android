package com.bluevia.android.payment.parser.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.parser.SerializeException;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.commons.parser.xml.XmlParserSerializerUtils;
import com.bluevia.android.commons.parser.xmlrpc.XmlMethodCallSerializer;
import com.bluevia.android.payment.data.GetPaymentStatusParams;
import com.bluevia.android.payment.data.MakePaymentParams;
import com.bluevia.android.payment.data.PaymentInfo;

/**
 * @class XmlPaymentSerializer
 * XML implementation of {@link com.bluevia.android.commons.parser.IParser IParser}
 * Class that represents the serializer object for any payment entity.
 * Using this class you will be able to serialize XML documents with a representation of payment entities
 * from instantiated objects of these entities
 * @author Telefonica R&D
 */
public class XmlPaymentSerializer extends XmlMethodCallSerializer {

	
	/**
	 * Overrides XmlMethodCallSerializer::serialize
	 * This function serializes the methodCall tag.
	 * 
	 * @param entity the entity to serialize
	 * @param out the outputstream to write the serialization result to
     * @throws SerializeException
	 */
	@Override
	public ByteArrayOutputStream serialize(Entity entity) throws SerializeException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		super.serialize(entity, out, XmlConstants.NS_PAYMENT_API_URI, XmlConstants.NS_PAYMENT_SCHEMALOCATION);	
		return out;
 	}

	
	/**
	 * Overrides XmlMethodCallSerializer::serializeParams
	 * Abstract function to serialize the params of the Method Call
	 * 
	 * @param entity the specific entity to serialize
     * @param serializer the serializer
	 * @throws IOException
	 * @throws SerializeException
	 */
	@Override
	protected void serializeParams(Entity entity, XmlSerializer serializer)	throws IOException, SerializeException {
		if (entity instanceof MakePaymentParams) {
			MakePaymentParams paymentParams = (MakePaymentParams) entity;
			serializer.startTag(XmlConstants.NS_PAYMENT_API_URI, XmlConstants.XSD_PAYMENT_MAKE_PAYMENT_PARAMS);
			
			// Time stamp (mandatory)
			if (paymentParams.getTimeStamp() == null)
				throw new SerializeException("Time stamp cannot be null");
			serializer.startTag(XmlConstants.NS_PAYMENT_API_URI, XmlConstants.XSD_PAYMENT_TIME_STAMP);
			serializer.text(String.valueOf(paymentParams.getTimeStamp()));
			serializer.endTag(XmlConstants.NS_PAYMENT_API_URI, XmlConstants.XSD_PAYMENT_TIME_STAMP);
			
			// Payment info (mandatory)
			if (paymentParams.getPaymentInfo() == null)
				throw new SerializeException("Payment info cannot be null");
			serializer.startTag(XmlConstants.NS_PAYMENT_API_URI, XmlConstants.XSD_PAYMENT_PAYMENT_INFO);
			serializePaymentInfo(paymentParams.getPaymentInfo(), serializer);
			serializer.endTag(XmlConstants.NS_PAYMENT_API_URI, XmlConstants.XSD_PAYMENT_PAYMENT_INFO);
			
			// Receipt request (optional)
			String endpoint = paymentParams.getEndpoint();
			String correlator = paymentParams.getCorrelator();
			XmlParserSerializerUtils.serializeReceiptRequestContents(XmlConstants.NS_PAYMENT_API_URI, 
					XmlConstants.NS_RPC_COMMON_API_URI, endpoint, correlator, serializer);
			
			serializer.endTag(XmlConstants.NS_PAYMENT_API_URI, XmlConstants.XSD_PAYMENT_MAKE_PAYMENT_PARAMS);
		
		} else if (entity instanceof GetPaymentStatusParams){
			
			GetPaymentStatusParams params = (GetPaymentStatusParams) entity;
			serializer.startTag(XmlConstants.NS_PAYMENT_API_URI, XmlConstants.XSD_PAYMENT_GET_PAYMENT_STATUS_PARAMS);
			
			//Transaction id (mandatory)
			if (params.getTransactionId() == null)
				throw new SerializeException("Transaction id cannot be null");

			serializer.startTag(XmlConstants.NS_PAYMENT_API_URI, XmlConstants.XSD_PAYMENT_TRANSACTION_ID);
			serializer.text(params.getTransactionId());
			serializer.endTag(XmlConstants.NS_PAYMENT_API_URI, XmlConstants.XSD_PAYMENT_TRANSACTION_ID);
			
			serializer.endTag(XmlConstants.NS_PAYMENT_API_URI, XmlConstants.XSD_PAYMENT_GET_PAYMENT_STATUS_PARAMS);
			
		} else throw new SerializeException("Attempted to serialize an unsupported entity type");
		
	}
	
	/**
	 * Serializes the payment info content
	 * @param paymentInfo Entity with the information of the payment
	 * @param serializer
	 * @throws IOException
	 * @throws SerializeException
	 */
	private void serializePaymentInfo(PaymentInfo paymentInfo, XmlSerializer serializer) throws IOException, SerializeException {
		if (!paymentInfo.isValid())
			throw new SerializeException("Invalid Payment Info");
		
		// Amount (mandatory)
		serializer.startTag(XmlConstants.NS_PAYMENT_API_URI, XmlConstants.XSD_PAYMENT_PAYMENT_INFO_AMOUNT);
		serializer.text(String.valueOf(paymentInfo.getAmount()));
		serializer.endTag(XmlConstants.NS_PAYMENT_API_URI, XmlConstants.XSD_PAYMENT_PAYMENT_INFO_AMOUNT);			
		// Currency (mandatory)
		if (paymentInfo.getCurrency() == null)
			throw new SerializeException("Currency cannot be null");
		serializer.startTag(XmlConstants.NS_PAYMENT_API_URI, XmlConstants.XSD_PAYMENT_PAYMENT_INFO_CURRENCY);
		serializer.text(paymentInfo.getCurrency());
		serializer.endTag(XmlConstants.NS_PAYMENT_API_URI, XmlConstants.XSD_PAYMENT_PAYMENT_INFO_CURRENCY);			
	}
}

