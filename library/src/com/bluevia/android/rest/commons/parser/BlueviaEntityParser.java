///@cond private
package com.bluevia.android.rest.commons.parser;

import java.io.IOException;
import java.io.InputStream;

import com.bluevia.android.rest.commons.Entity;



/**
 * Interface that represents the parser object for an entity. Object implementing this interface
 * will be able to parse XML documents containing an representation of an entity into an instance
 * object of this entity
 *
 * @author Telefonica R&D
 * 
 */
public interface BlueviaEntityParser {
    /**
     * @param is the inputstream containing the stream with the entity information
     * @return the {@link com.bluevia.android.rest.commons.Entity Entity} object
     * @throws ParseException when an error occurs converting the stream into an object
     * @throws IOException when an error reading the stream occurs
     */
    public Entity parseEntry(InputStream is) throws ParseException, IOException;

}
///@endcond

