package com.bluevia.android.messagery.mt.mms.parser;


import java.io.ByteArrayOutputStream;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.parser.ISerializer;
import com.bluevia.android.commons.parser.SerializeException;
import com.bluevia.android.messagery.mt.mms.data.MmsMessageReq;

/**
 * XML implementation of {@link com.bluevia.android.commons.parser.IParser IParser}
 * Creates Xml serializer for Bluevia MMS entities
 * @author Telefonica R&D
 * 
 */
public class XmlMmsSerializer implements ISerializer {

	/**
     * This class serializes to XML MMS Entities
     * The entities that can be serialized are
     * {@link com.bluevia.android.MmsMessageReq.mms.data.MmsMessage MmsMessageReq}
     * @see com.bluevia.android.commons.parser.ISerializer#serialize(com.bluevia.android.commons.Entity, java.io.OutputStream)
     */
	public ByteArrayOutputStream serialize(Entity entity) throws SerializeException {
	    if (entity == null)
            throw new SerializeException("Can not serialize null entity ");

        if (entity instanceof MmsMessageReq) {
            return new XmlMmsMessageSerializer().serialize(entity);
        } else
            throw new SerializeException("Entity class does not support serializing");

    }

}
