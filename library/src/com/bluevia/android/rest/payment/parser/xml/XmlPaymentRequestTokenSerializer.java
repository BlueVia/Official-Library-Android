///@cond private
package com.bluevia.android.rest.payment.parser.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import com.bluevia.android.rest.commons.Encoding;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.payment.data.PaymentInfo;
import com.bluevia.android.rest.payment.data.PaymentRequestTokenParams;

public class XmlPaymentRequestTokenSerializer implements BlueviaEntitySerializer {

	private static final String PAYMENT_INFO = "paymentInfo.";
	private static final String PINFO_AMOUNT = PAYMENT_INFO + "amount";
	private static final String PINFO_CURRENCY = PAYMENT_INFO + "currency";

	private static final String SERVICE_INFO = "serviceInfo.";
	private static final String SINFO_SERVICE_ID = SERVICE_INFO + "serviceID";
	private static final String SINFO_NAME = SERVICE_INFO + "name";
	private static final String SINFO_DESCRIPTION = SERVICE_INFO + "description";


	@Override
	public void serialize(Entity entity, OutputStream out) throws IOException, ParseException {
		if (entity == null)
			throw new ParseException("Can not serialize null entity ");

		if (! (entity instanceof PaymentRequestTokenParams))
			throw new ParseException("Entity class does not support serializing this entity class");

		PaymentRequestTokenParams params = (PaymentRequestTokenParams) entity;
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(6);

		//ServiceInfo
		nameValuePairs.add(new BasicNameValuePair(SINFO_SERVICE_ID, params.getServiceInfo().getServiceId()));
		nameValuePairs.add(new BasicNameValuePair(SINFO_NAME, params.getServiceInfo().getServiceName()));
		if (params.getServiceInfo().getServiceDescription() != null)
			nameValuePairs.add(new BasicNameValuePair(SINFO_DESCRIPTION, 
					params.getServiceInfo().getServiceDescription()));
		
		//PaymentInfo
		PaymentInfo pInfo = params.getPaymentInfo();

		//Mandatory params
		nameValuePairs.add(new BasicNameValuePair(PINFO_AMOUNT, String.valueOf(pInfo.getAmount())));
		nameValuePairs.add(new BasicNameValuePair(PINFO_CURRENCY, pInfo.getCurrency()));

		UrlEncodedFormEntity formEncodedEntity = new UrlEncodedFormEntity(nameValuePairs);
		formEncodedEntity.writeTo(out);
	}

	@Override
	public Encoding getEncoding() {
		return Encoding.APPLICATION_URL_ENCODED;
	}

}
///@endcond