///@cond private
/**
 * \package com.bluevia.android.rest.ad.parser This package contains all the factories to parse and serialize Ad entities to any format (XML, JSON, etc.) and vice-versa
 * \package com.bluevia.android.rest.ad.parser.xml This package contains the factory to parse and serialize Ad entities to XML and vice-versa
 */
package com.bluevia.android.rest.ad.parser.xml;

import com.bluevia.android.rest.commons.parser.BlueviaEntityParser;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;

/**
 * XML implementation of
 * {@link com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory
 * BlueviaEntityParserFactory} Creates Xml parsers and serializers for Bluevia Ad
 * entities
 *
 * @author Telefonica R&D
 * 
 */
public class XmlAdParserFactory implements BlueviaEntityParserFactory {

    /*
     * (non-Javadoc)
     * @see
     * com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory#createParser()
     */
    public BlueviaEntityParser createParser() {
        return new XmlAdParser();

    }

    /*
     * (non-Javadoc)
     * @see
     * com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory#createSerializer
     * ()
     */
    public BlueviaEntitySerializer createSerializer() {
        return new XmlAdSerializer();
    }

}

///@endcond
