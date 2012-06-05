package com.bluevia.android.messagery.mt.sms.parser.xml;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.parser.ISerializer;
import com.bluevia.android.commons.parser.SerializeException;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.messagery.mt.sms.data.Sms;
import com.bluevia.android.messagery.mt.sms.data.SmsMessageReq;

/**
 * Xml serializer for SmsMessageReq entities.
 * 
 * @author Telefonica I+D
 * 
 */
public class XmlSmsMessageSerializer extends AbstractXmlSmsSerializer implements ISerializer {

    /**
     * @throws SerializeException thrown if entity is not an instance of {@link com.bluevia.android.SmsMessageReq SmsMessageReq}
     * @see
     * com.bluevia.android.commons.parser.ISerializer#serialize(
     * com.bluevia.android.commons.Entity, java.io.OutputStream)
     */
    @Override
    public ByteArrayOutputStream serialize(Entity entity) throws SerializeException {
    	if (!(entity instanceof SmsMessageReq)) {
            throw new SerializeException("Attempted to parse unsupported entity type");
        }

        return super.serialize(entity);
    }

    @Override
    protected void serializeSmsContents(Sms message, XmlSerializer serializer) throws SerializeException, IOException {
    	
    	serializeAddressContents(message, serializer, XmlConstants.NS_SMS_API_URI);
    	
    	serializeSmsMessageContents((SmsMessageReq) message, serializer);
    	
    	serializeReceiptTypeContents(message, serializer, XmlConstants.NS_SMS_API_URI);
    	
    	serializeSenderNameContents(message, serializer, XmlConstants.NS_SMS_API_URI);
    	
    	serializeOriginAddressContents(message, serializer, XmlConstants.NS_SMS_API_URI);
        
    }
    
    /**
     * This function Serializes into the XML the SmsMessageReq contents
     * 
     * @param message the SmsMessageReq message
     * @param serializer the serializer
     * @throws SerializeException
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws IOException
     */
    private void serializeSmsMessageContents(SmsMessageReq message, XmlSerializer serializer) 
    	throws SerializeException, IllegalArgumentException, IllegalStateException, IOException {
    	
        if (message.getMessage() == null) {
            throw new SerializeException("Null message body in SMS");
        }
        else if (message.getMessage().equals("")) {
            throw new SerializeException("Empty message body in SMS");
        }
        else {
            serializer.startTag(XmlConstants.NS_SMS_API_URI,XmlConstants.XSD_SMSTEXTTYPE_MESSAGE);
            serializer.text(message.getMessage());
            serializer.endTag(XmlConstants.NS_SMS_API_URI,XmlConstants.XSD_SMSTEXTTYPE_MESSAGE);
        }
    }

}
