///@cond private
package com.bluevia.android.rest.messaging.sms.parser.xml;


import org.xmlpull.v1.XmlSerializer;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.messaging.sms.data.Sms;
import com.bluevia.android.rest.messaging.sms.data.SmsMessage;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Xml serializer for SmsMessage entities.
 * 
 * @author Telefonica I+D
 * 
 */
class XmlSmsMessageSerializer extends AbstractXmlSmsSerializer implements BlueviaEntitySerializer {


    private static final String TAG = "XmlSmsMessageSerializer";

    /**
     * @throws ParseException thrown if entity is not an instance of {@link com.bluevia.android.rest.messaging.sms.data.SmsMessage SmsMessage}
     * @see
     * com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer#serialize(
     * com.bluevia.android.rest.commons.Entity, java.io.OutputStream)
     */
    @Override
    public void serialize(Entity entity, OutputStream out) throws IOException, ParseException {
        if (!(entity instanceof SmsMessage)) {
            throw new ParseException("Attempted to parse unsupported entity type");
        }

        super.serialize (entity, out);
    }

    @Override
    protected void serializeSmsContents(Sms message, XmlSerializer serializer) throws ParseException, IOException {
    	
    	serializeAddressContents(message, serializer, XmlConstants.NS_SMS_API_URI);
    	
    	serializeSmsMessageContents((SmsMessage) message, serializer);
    	
    	serializeReceiptTypeContents(message, serializer, XmlConstants.NS_SMS_API_URI);
    	
    	serializeOriginAddressContents(message, serializer, XmlConstants.NS_SMS_API_URI);
        
    }
    
    /**
     * This function Serializes into the XML the SmsMessage contents
     * 
     * @param message the SmsMessage message
     * @param serializer the serializer
     * @throws ParseException
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws IOException
     */
    private void serializeSmsMessageContents(SmsMessage message, XmlSerializer serializer) 
    	throws ParseException, IllegalArgumentException, IllegalStateException, IOException {
    	
        if (message.getMessage() == null) {
            throw new ParseException("Null message body in SMS");
        }
        else if (message.getMessage().equals("")) {
            throw new ParseException("Empty message body in SMS");
        }
        else {
            serializer.startTag(XmlConstants.NS_SMS_API_URI,XmlConstants.XSD_SMSTEXTTYPE_MESSAGE);
            serializer.text(message.getMessage());
            serializer.endTag(XmlConstants.NS_SMS_API_URI,XmlConstants.XSD_SMSTEXTTYPE_MESSAGE);
        }
    }

}
///@endcond