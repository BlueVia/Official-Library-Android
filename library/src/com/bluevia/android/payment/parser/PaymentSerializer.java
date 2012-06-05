/**
 * @package com.bluevia.android.payment.parser This package contains the classes in order to parse and serialize data for Payment API.
 */
package com.bluevia.android.payment.parser;

import java.io.ByteArrayOutputStream;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.parser.ISerializer;
import com.bluevia.android.commons.parser.SerializeException;
import com.bluevia.android.payment.data.PaymentRequestTokenParams;
import com.bluevia.android.payment.parser.url.UrlEncodedPaymentReqTokenSerializer;
import com.bluevia.android.payment.parser.xml.XmlPaymentSerializer;

public class PaymentSerializer implements ISerializer {

	@Override
	public ByteArrayOutputStream serialize(Entity entity) throws SerializeException {
		
		if (entity instanceof PaymentRequestTokenParams){
			return new UrlEncodedPaymentReqTokenSerializer().serialize(entity);
		} else return new XmlPaymentSerializer().serialize(entity);
	}

}
