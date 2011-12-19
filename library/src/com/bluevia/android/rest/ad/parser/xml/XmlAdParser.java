///@cond private
package com.bluevia.android.rest.ad.parser.xml;


import java.io.IOException;
import java.io.InputStream;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParser;
import com.bluevia.android.rest.commons.parser.ParseException;

/**
 * XML implementation of {@link com.bluevia.android.rest.commons.parser.BlueviaEntityParser BlueviaEntityParser}
 * Class that represents the parser object for any Ad entity.
 * Using this class you will be able to parse XML documents containing
 * a representation of any Ad entity into an instance
 * object of this entity
 *
 * @author Telefonica R&D
 * 
 */
public class XmlAdParser implements BlueviaEntityParser {

    /* (non-Javadoc)
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParser#parseEntry(java.io.InputStream)
     */
    public Entity parseEntry(InputStream is) throws ParseException, IOException {
        return new XmlAdResponseParser().parseEntry(is);
    }
}

///@endcond
