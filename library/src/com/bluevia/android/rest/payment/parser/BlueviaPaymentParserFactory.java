///@cond private
/**
 * @package com.bluevia.android.rest.payment.parser This package contains all the factories to parse and serialize Payment entities to any format (XML, JSON, etc.) and vice-versa
 * @package com.bluevia.android.rest.payment.parser.xml This package contains the factory to parse and serialize Payment entities to XML and vice-versa
 */
package com.bluevia.android.rest.payment.parser;

import com.bluevia.android.rest.commons.parser.BlueviaEntityParser;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.payment.parser.xml.XmlPaymentParser;
import com.bluevia.android.rest.payment.parser.xml.XmlPaymentSerializer;

/**
 * @class PaymentParserFactory
 * General implementation of {@link com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory  BlueviaEntityParserFactory}
 * Creates parsers and serializers for Bluevia Payment entities
 * @author Telefonica R&D
 */
public class BlueviaPaymentParserFactory implements BlueviaEntityParserFactory {

	/* (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory#createParser()
	 */
	public BlueviaEntityParser createParser() {
		return new XmlPaymentParser();
	}

	/* (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory#createSerializer()
	 */
	public BlueviaEntitySerializer createSerializer() {
		return new XmlPaymentSerializer();
	}
}

///@endcond