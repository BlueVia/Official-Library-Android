///@cond private
package com.bluevia.android.rest.commons.parser;


import java.io.IOException;
import java.io.OutputStream;

import com.bluevia.android.rest.commons.Encoding;
import com.bluevia.android.rest.commons.Entity;

/**
 * Interface that represents the serializer object for an entity. Object implementing this interface
 * will be able to serialize an entity object into an XML document containing a representation of this
 * entity
 * object of this entity
 *
 * @author Telefonica R&D
 * 
 */
public interface BlueviaEntitySerializer {
    /**
     * Class to serialize an entity into an OutputStream
     * @param entity Entity to parse.
     * @param out OutputStream to inject the serialized content into
     * @throws IOException thrown in the event of an I/O error during serialization
     * @throws ParseException thrown if the entity or output stream are unfit for serialization
     *  or in the event of any other serialization error
     */
    void serialize(Entity entity, OutputStream out) throws IOException, ParseException;

    /**
     * @return the encoding used to create the OutputStream (result of the serialization)
     */
    Encoding getEncoding();
}
///@endcond