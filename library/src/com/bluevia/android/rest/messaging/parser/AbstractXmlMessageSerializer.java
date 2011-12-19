///@cond private
package com.bluevia.android.rest.messaging.parser;

import org.xmlpull.v1.XmlSerializer;

import com.bluevia.android.rest.commons.Encoding;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.data.UserId;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.commons.parser.xml.XmlParserSerializerUtils;
import com.bluevia.android.rest.messaging.AbstractMessage;

import android.util.Xml;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * Xml serializer for AbstractMessage (SMS and MMS) entities.
 * 
 * @author Telefonica I+D
 * 
 */
public abstract class AbstractXmlMessageSerializer implements BlueviaEntitySerializer {

    private static final String TAG = "AbstractXmlSmsMessageSerializer";

    /**
     * @param entity must be an instance of {@link com.bluevia.android.rest.messaging.sms.data.Sms AbstractSms}
     * @param out the outputstream to write the serialization result to
     * @throws IOException thrown in the output stream could not be written
     * @throws ParseException thrown if entity is not an instance of {@link com.bluevia.android.rest.messaging.sms.data.Sms AbstractSms}
     * @see
     * com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer#serialize(
     * com.bluevia.android.rest.commons.Entity, java.io.OutputStream)
     */
    public void serialize(Entity entity, OutputStream out) throws IOException, ParseException {

        if (!(entity instanceof AbstractMessage)) {
            throw new ParseException("Attempted to parse unsupported entity type");
        };
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(out, "UTF-8");
        serializer.startDocument(null, null);
        serializeEntityContents((AbstractMessage) entity, serializer);
        serializer.endDocument();;
        serializer.flush();
    }

    /**
     * Abstract function to serialize each kind of AbstractMessage
     * 
     * @param message the AbstractMessage message
     * @param serializer the serializer
     * @throws ParseException 
     * @throws IOException 
     */
    protected abstract void serializeEntityContents(AbstractMessage message, XmlSerializer serializer) throws ParseException, IOException;
    
    /**
     * This function Serializes into the XML the Address paramter
     * 
     * @param message the AbstractMessage message
     * @param serializer the serializer
     * @throws ParseException
     * @throws IOException
     */
    protected void serializeAddressContents(AbstractMessage message, XmlSerializer serializer, String ns) throws ParseException, IOException {
           //Address tags for each address in the sms message entity bean
        Iterator<UserId> addressess = message.getAddressList().iterator();
        if (!addressess.hasNext()) {
            throw new ParseException("No destination address in message");
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
     * @throws ParseException
     * @throws IOException
     */
    protected void serializeOriginAddressContents(AbstractMessage message, XmlSerializer serializer, String ns) throws ParseException, IOException {
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
     * @throws ParseException
     * @throws IOException
     */
    protected void serializeReceiptTypeContents(AbstractMessage message, XmlSerializer serializer, String ns) throws ParseException, IOException {
        String endpoint = message.getEndpoint();
        String correlator = message.getCorrelator();
    	XmlParserSerializerUtils.serializeReceiptRequestContents(ns, XmlConstants.NS_REST_COMMON_API_URI, endpoint, correlator, serializer);
    }

    /**
     * @see com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer#getEncoding()
     */
    public Encoding getEncoding() {
        return Encoding.APPLICATION_XML;
    }

}
///@endcond