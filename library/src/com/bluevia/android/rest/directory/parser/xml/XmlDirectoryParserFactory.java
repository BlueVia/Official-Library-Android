///@cond private
/**
 * \package com.bluevia.android.rest.directory.parser This package contains all the factories to parse and serialize Directory entities to any format (XML, JSON, etc.) and vice-versa
 * \package com.bluevia.android.rest.directory.parser.xml This package contains the factory to parse and serialize Directory entities to XML and vice-versa
 * in order to be sent using the SMS client.
 */
package com.bluevia.android.rest.directory.parser.xml;

import com.bluevia.android.rest.commons.parser.BlueviaEntityParser;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;

/**
 * XML implementation of {@link com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory BlueviaEntityParserFactory}
 * Creates Xml parsers and serializers for Bluevia Directory entities
 *
 * @author Telefonica R&D
 * 
 */
public class XmlDirectoryParserFactory implements BlueviaEntityParserFactory {

	/**
     * @return An xml parser to process streams providing data on Bluevia entities
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory#createParser()
     */
    public BlueviaEntityParser createParser() {
        return new XmlDirectoryParser();

    }

    /**
     * @return An xml serializer to generate data streams encoding the data on Bluevia entities
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory#createSerializer()
     */
    public BlueviaEntitySerializer createSerializer() {
        return null;
    }

}
///@endcond
