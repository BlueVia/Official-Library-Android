package com.bluevia.android.messagery.mt.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.Utils;
import com.bluevia.android.commons.data.UserId;
import com.bluevia.android.commons.parser.ISerializer;
import com.bluevia.android.commons.parser.SerializeException;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.commons.parser.xml.XmlParserSerializerUtils;
import com.bluevia.android.messagery.data.AbstractMessage;

/**
 * Xml serializer for AbstractMessage (SMS and MMS) entities.
 * 
 * @author Telefonica I+D
 * 
 */
public abstract class AbstractXmlMessageSerializer implements ISerializer {

    /**
     * @param entity must be an instance of {@link com.bluevia.android.messagery.sms.data.Sms AbstractSms}
     * @param out the outputstream to write the serialization result to
     * @throws SerializeException thrown if entity is not an instance of {@link com.bluevia.android.messagery.sms.data.Sms AbstractSms}
     * @see
     * com.bluevia.android.commons.parser.ISerializer#serialize(
     * com.bluevia.android.commons.Entity, java.io.OutputStream)
     */
	public ByteArrayOutputStream serialize(Entity entity) throws SerializeException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
        if (!(entity instanceof AbstractMessage)) {
            throw new SerializeException("Attempted to parse unsupported entity type");
        };
        XmlSerializer serializer = Xml.newSerializer();
        try {
			serializer.setOutput(out, "UTF-8");
	        serializer.startDocument(null, null);
	        serializeEntityContents((AbstractMessage) entity, serializer);
	        serializer.endDocument();;
	        serializer.flush();
		} catch (IOException e) {
			throw new SerializeException(e);
		}
        
		return out;
    }

    /**
     * Abstract function to serialize each kind of AbstractMessage
     * 
     * @param message the AbstractMessage message
     * @param serializer the serializer
     * @throws SerializeException 
     * @throws IOException 
     */
    protected abstract void serializeEntityContents(AbstractMessage message, XmlSerializer serializer) throws SerializeException, IOException;
    
    /**
     * This function Serializes into the XML the Address paramter
     * 
     * @param message the AbstractMessage message
     * @param serializer the serializer
     * @throws SerializeException
     * @throws IOException
     */
    protected void serializeAddressContents(AbstractMessage message, XmlSerializer serializer, String ns) throws SerializeException, IOException {
           //Address tags for each address in the sms message entity bean
        Iterator<UserId> addressess = message.getAddressList().iterator();
        if (!addressess.hasNext()) {
            throw new SerializeException("No destination address in message");
        }
        while (addressess.hasNext()) {
            UserId address = addressess.next();
            serializer.startTag(ns, XmlConstants.XSD_MESSAGE_TYPE_ADDRESS);
            XmlParserSerializerUtils.serializeUserIdContents(XmlConstants.NS_REST_COMMON_API_URI, address, serializer);
            serializer.endTag(ns, XmlConstants.XSD_MESSAGE_TYPE_ADDRESS);
        }
    }

    /**
     * This function Serializes into the XML the OriginAddress paramter
     * 
     * @param message the AbstractMessage message
     * @param serializer the serializer
     * @throws SerializeException
     * @throws IOException
     */
    protected void serializeOriginAddressContents(AbstractMessage message, XmlSerializer serializer, String ns) throws SerializeException, IOException {
        UserId originAddress = message.getOriginAddress();
    	if (originAddress != null && originAddress.getUserIdValue() != null && !originAddress.getUserIdValue().equals("")) {
            serializer.startTag(ns, XmlConstants.XSD_MESSAGE_TYPE_ORIGINADDRESS);
            XmlParserSerializerUtils.serializeUserIdContents(XmlConstants.NS_REST_COMMON_API_URI, originAddress, serializer);
            serializer.endTag(ns, XmlConstants.XSD_MESSAGE_TYPE_ORIGINADDRESS);
        }
    }
    
    /**
     * This function Serializes into the XML the endpoint and correlator parameters (optional)
     * 
     * @param message the AbstractMessage message
     * @param serializer the serializer
     * @throws SerializeException
     * @throws IOException
     */
    protected void serializeReceiptTypeContents(AbstractMessage message, XmlSerializer serializer, String ns) throws SerializeException, IOException {
        String endpoint = message.getEndpoint();
        String correlator = message.getCorrelator();
    	XmlParserSerializerUtils.serializeReceiptRequestContents(ns, XmlConstants.NS_REST_COMMON_API_URI, endpoint, correlator, serializer);
    }
    
    /**
     * This function Serializes into the XML the sender name parameter (optional)
     * 
     * @param message the AbstractMessage message
     * @param serializer the serializer
     * @throws SerializeException
     * @throws IOException
     */
    protected void serializeSenderNameContents(AbstractMessage message, XmlSerializer serializer, String ns) throws SerializeException, IOException {
        String senderName = message.getSenderName();
    	if (!Utils.isEmpty(senderName)) {
    		serializer.startTag(XmlConstants.NS_SMS_API_URI,XmlConstants.XSD_MESSAGE_TYPE_SENDERNAME);
            serializer.text(senderName);
            serializer.endTag(XmlConstants.NS_SMS_API_URI,XmlConstants.XSD_MESSAGE_TYPE_SENDERNAME);
        }
    	
    }

}
