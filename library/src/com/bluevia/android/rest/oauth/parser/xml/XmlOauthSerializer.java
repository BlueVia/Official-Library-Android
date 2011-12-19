package com.bluevia.android.rest.oauth.parser.xml;

import java.io.IOException;
import java.io.OutputStream;

import com.bluevia.android.rest.commons.Encoding;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.payment.data.PaymentInfo;
import com.bluevia.android.rest.payment.data.PaymentRequestTokenParams;
import com.bluevia.android.rest.payment.parser.xml.XmlPaymentRequestTokenSerializer;

public class XmlOauthSerializer implements BlueviaEntitySerializer {

	@Override
	public void serialize(Entity entity, OutputStream out) throws IOException, ParseException {
		if (entity == null)
            throw new ParseException("Can not serialize null entity ");
        else if (out == null)
            throw new ParseException("Can not serialize to null OutputStream ");
        
		if (entity instanceof PaymentRequestTokenParams)
			new XmlPaymentRequestTokenSerializer().serialize(entity, out);
    	else throw new ParseException("Entity class does not support serializing");
	}

	@Override
	public Encoding getEncoding() {
		return Encoding.APPLICATION_URL_ENCODED;
	}

}
