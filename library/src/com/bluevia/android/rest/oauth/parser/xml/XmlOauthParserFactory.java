///@cond private
/**
 * \package com.bluevia.android.rest.oauth.parser.xml This package contains classes to parse
 * responses from the authentication server
 */
package com.bluevia.android.rest.oauth.parser.xml;

import com.bluevia.android.rest.commons.parser.BlueviaEntityParser;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;

/**
* XML implementation of {@link com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory BlueviaEntityParserFactory}
* Creates Xml parsers and serializers for Bluevia Oauth entities
*
* @author Telefonica R&D
* 
*/
public class XmlOauthParserFactory implements BlueviaEntityParserFactory {

	/**
     * @return An xml parser to process streams providing data on oauth tokens
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory#createParser()
     */
    public BlueviaEntityParser createParser() {
        return new XmlOauthParser();
    }

    /**
     * 
     * @return 
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory#createSerializer()
     */
    public BlueviaEntitySerializer createSerializer() {
        return new XmlOauthSerializer();
    }
	
}
///@endcond