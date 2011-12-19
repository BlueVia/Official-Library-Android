///@cond private
/**
 * \package com.bluevia.android.rest.mms This package contains the classes in order to send MMS using Bluevia API.
 * \package com.bluevia.android.rest.mms.parser This package contains all the factories to parse and serialize MMS entities to any format (XML, JSON, etc.) and vice-versa
 * \package com.bluevia.android.rest.mms.parser.xml This package contains the factory to parse and serialize MMS entities to XML and vice-versa
 * in order to be sent using the MMS client.
 */
package com.bluevia.android.rest.messaging.mms.parser;

import com.bluevia.android.rest.commons.parser.BlueviaEntityParser;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.messaging.mms.parser.xml.XmlMmsParser;
import com.bluevia.android.rest.messaging.mms.parser.xml.XmlMmsSerializer;

/**
 * XML implementation of {@link com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory BlueviaEntityParserFactory}
 * Creates Xml parsers and serializers for Bluevia MMS entities
 * @author Telefonica R&D
 * 
 */
public class BlueviaMmsParserFactory implements BlueviaEntityParserFactory {

	/**
     * @return An xml parser to process streams providing data on Bluevia MMS entities
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory#createParser()
     */
    public BlueviaEntityParser createParser() {
		return new XmlMmsParser();
    }

    /**
     * @return An xml serializer to generate data streams encoding the data on Bluevia MMS entities
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory#createSerializer()
     */
    public BlueviaEntitySerializer createSerializer() {
		return new XmlMmsSerializer();
    }

}
///@endcond