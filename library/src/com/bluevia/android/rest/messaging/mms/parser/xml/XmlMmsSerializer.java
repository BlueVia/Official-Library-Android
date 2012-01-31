///@cond private
package com.bluevia.android.rest.messaging.mms.parser.xml;


import java.io.IOException;
import java.io.OutputStream;

import com.bluevia.android.rest.commons.Encoding;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.messaging.mms.data.MmsMessage;

/**
 * XML implementation of {@link com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer BlueviaEntitySerializer}
 * Creates Xml serializer for Bluevia MMS entities
 * @author Telefonica R&D
 * 
 */
public class XmlMmsSerializer implements BlueviaEntitySerializer {

	/**
     * This class serializes to XML MMS Entities
     * The entities that can be serialized are
     * {@link com.bluevia.android.rest.messaging.mms.data.MmsMessage MmsMessage}
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer#serialize(com.bluevia.android.rest.commons.Entity, java.io.OutputStream)
     */
    public void serialize(Entity entity, OutputStream out) throws IOException, ParseException {;
        if (entity == null)
            throw new ParseException("Can not serialize null entity ");
        else if (out == null)
            throw new ParseException("Can not serialize to null OutputStream ");

        if (entity instanceof MmsMessage) {
            new XmlMmsMessageSerializer().serialize(entity, out);
        } else
            throw new ParseException("Entity class does not support serializing");

    }

    public Encoding getEncoding() {
        return Encoding.APPLICATION_XML;
    }

}
///@endcond
