package com.bluevia.android.messagery.mt.sms.parser.xml;

import java.io.ByteArrayOutputStream;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.parser.ISerializer;
import com.bluevia.android.commons.parser.SerializeException;
import com.bluevia.android.messagery.mt.sms.data.SmsMessageReq;

/**
 * XML implementation of {@link com.bluevia.android.commons.parser.ISerializer ISerializer}
 * Creates Xml serializer for Bluevia Sms entities
 * @author Telefonica R&D
 * 
 */
public class XmlSmsSerializer implements ISerializer {

	/**
	 * This class serializes to XML SMS Entities
	 * The entities that can be serialized are
	 * {@link com.bluevia.android.SmsMessageReq SmsMessageReq}
	 * @see com.bluevia.android.commons.parser.ISerializer#serialize(com.bluevia.android.commons.Entity, java.io.OutputStream)
	 */
	public ByteArrayOutputStream serialize(Entity entity) throws SerializeException {
		if (entity == null)
			throw new SerializeException("Can not serialize null entity ");

		if (entity instanceof SmsMessageReq) {
			return new XmlSmsMessageSerializer().serialize(entity);
		} else throw new SerializeException("Entity class does not support serializing");
	}

}
