///@cond private
package com.bluevia.android.rest.messaging.sms.parser.xml;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.messaging.AbstractMessage;
import com.bluevia.android.rest.messaging.parser.AbstractXmlMessageSerializer;
import com.bluevia.android.rest.messaging.sms.data.Sms;


/**
 * Xml serializer for Sms entities.
 * 
 * @author Telefonica I+D
 * 
 */
public abstract class AbstractXmlSmsSerializer extends AbstractXmlMessageSerializer implements BlueviaEntitySerializer {

	@Override
    protected void serializeEntityContents(AbstractMessage message, XmlSerializer serializer)
    throws ParseException, IOException {
		
		if (!message.isValid()) {
		    throw new ParseException("Invalid SMS entity");
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
     * @throws ParseException
     * @throws IOException
     */
    protected abstract void serializeSmsContents(Sms message, XmlSerializer serializer) throws ParseException, IOException;

}
///@endcond