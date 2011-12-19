///@cond private
/**
 * \package com.bluevia.android.rest.messaging.sms This package contains the classes in order to send SMS using Bluevia API.
 * \package com.bluevia.android.rest.messaging.sms.parser This package contains all the factories to parse and serialize SMS entities to any format (XML, JSON, etc.) and vice-versa
 * \package com.bluevia.android.rest.messaging.sms.parser.xml This package contains the factory to parse and serialize SMS entities to XML and vice-versa
 * in order to be sent using the SMS client.
 */
package com.bluevia.android.rest.messaging.sms.parser;

import com.bluevia.android.rest.commons.parser.BlueviaEntityParser;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.messaging.sms.parser.xml.XmlSmsParser;
import com.bluevia.android.rest.messaging.sms.parser.xml.XmlSmsSerializer;

/**
 * XML implementation of {@link com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory BlueviaEntityParserFactory}
 * Creates Xml parsers and serializers for Bluevia entities
 * @author Telefonica R&D
 * 
 */
public class BlueviaSmsParserFactory implements BlueviaEntityParserFactory {

	/**
     * @return An xml or json parser to process streams providing data on Bluevia entities
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory#createParser()
     */
    public BlueviaEntityParser createParser() {
		return new XmlSmsParser();
    }

    /**
     * @return An xml or json serializer to generate data streams encoding the data on Bluevia entities
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory#createSerializer()
     */
    public BlueviaEntitySerializer createSerializer() {
		return new XmlSmsSerializer();
    }

}
///@endcond
