package com.bluevia.android.messagery.mt.sms.parser.xml;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.bluevia.android.commons.parser.ISerializer;
import com.bluevia.android.commons.parser.SerializeException;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.messagery.data.AbstractMessage;
import com.bluevia.android.messagery.mt.parser.AbstractXmlMessageSerializer;
import com.bluevia.android.messagery.mt.sms.data.Sms;


/**
 * Xml serializer for Sms entities.
 * 
 * @author Telefonica I+D
 * 
 */
public abstract class AbstractXmlSmsSerializer extends AbstractXmlMessageSerializer implements ISerializer {

	@Override
    protected void serializeEntityContents(AbstractMessage message, XmlSerializer serializer)
    throws SerializeException, IOException {
		
		if (!message.isValid()) {
		    throw new SerializeException("Invalid SMS entity");
		}
		
		// Root tag
		serializer.setPrefix(XmlConstants.TNS, XmlConstants.NS_SMS_API_URI);
		serializer.setPrefix(XmlConstants.TNS1, XmlConstants.NS_REST_COMMON_API_URI);
		serializer.setPrefix(XmlConstants.XSI, XmlConstants.NS_XML_SCHEMA_INSTANCE);
		serializer.startTag(XmlConstants.NS_SMS_API_URI, XmlConstants.XSD_SMSTEXTTYPE_SMSTEXT);
		serializer.attribute(XmlConstants.NS_XML_SCHEMA_INSTANCE, XmlConstants.XSD_TEXTYPE_SCHEMALOCATION, XmlConstants.SMS_SCHEMALOCATION);
		
		// Message Contents
		serializeSmsContents((Sms) message, serializer);
		
		// End root tag
		serializer.endTag(XmlConstants.NS_SMS_API_URI, XmlConstants.XSD_SMSTEXTTYPE_SMSTEXT);
		}

    /**
     * Abstract function to serialize each kind of Sms entities
     * 
     * @param message the SMS Message
     * @param serializer the serializer
     * @throws SerializeException
     * @throws IOException
     */
    protected abstract void serializeSmsContents(Sms message, XmlSerializer serializer) throws SerializeException, IOException;

}
