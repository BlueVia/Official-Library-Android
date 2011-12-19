///@cond private
/**
 * \package com.bluevia.android.rest.location.parser This package contains all the factories to parse and serialize Location entities to any format (XML, JSON, etc.) and vice-versa
 * \package com.bluevia.android.rest.location.parser.xml This package contains the factory to parse and serialize Location entities to XML and vice-versa
 * in order to be sent using the SMS client.
 */
package com.bluevia.android.rest.location.parser;

import com.bluevia.android.rest.commons.parser.BlueviaEntityParser;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.location.parser.xml.XmlLocationParser;

/**
 * General implementation of {@link com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory BlueviaEntityParserFactory}
 * Creates parsers and serializers for Bluevia Location entities
 *
 * @author Telefonica R&D
 * 
 */
public class BlueviaLocationParserFactory implements BlueviaEntityParserFactory {

	/**
     * @return An xml or json parser to process streams providing data on Bluevia entities
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory#createParser()
     */
	public BlueviaEntityParser createParser() {
		return new XmlLocationParser();
	}
	
	
	/**
	 * Serializer not implemented for Bluevia Location entities
     */
	public BlueviaEntitySerializer createSerializer() {
		return null;	
	}

}
///@endcond
