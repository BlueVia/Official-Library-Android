///@cond private
package com.bluevia.android.rest.messaging.sms.parser.xml;


import java.io.IOException;
import java.io.OutputStream;

import com.bluevia.android.rest.commons.Encoding;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.messaging.sms.data.SmsMessage;

/**
 * XML implementation of {@link com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer BlueviaEntitySerializer}
 * Creates Xml serializer for Bluevia Sms entities
 * @author Telefonica R&D
 * 
 */
public class XmlSmsSerializer implements BlueviaEntitySerializer {

	/**
     * This class serializes to XML SMS Entities
     * The entities that can be serialized are
     * {@link com.bluevia.android.rest.messaging.sms.data.SmsMessage SmsMessage}
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer#serialize(com.bluevia.android.rest.commons.Entity, java.io.OutputStream)
     */
    public void serialize(Entity entity, OutputStream out) throws IOException, ParseException {;
        if (entity == null)
            throw new ParseException("Can not serialize null entity ");
        else if (out == null)
            throw new ParseException("Can not serialize to null OutputStream ");

        if (entity instanceof SmsMessage) {
            new XmlSmsMessageSerializer().serialize(entity, out);
        } else
            throw new ParseException("Entity class does not support serializing");

    }

    public Encoding getEncoding() {
        return Encoding.APPLICATION_XML;
    }

}
///@endcond