///@cond private
package com.bluevia.android.rest.messaging.mms.parser.xml;

import org.xmlpull.v1.XmlSerializer;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.messaging.AbstractMessage;
import com.bluevia.android.rest.messaging.mms.data.MmsMessage;
import com.bluevia.android.rest.messaging.parser.AbstractXmlMessageSerializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Xml serializer for MmsMessage entities.
 * 
 * @author Telefonica I+D
 * 
 */
class XmlMmsMessageSerializer extends AbstractXmlMessageSerializer implements BlueviaEntitySerializer {

    private static final String TAG = "XmlMmsMessageSerializer";

    public void serialize(Entity entity, OutputStream out) throws IOException, ParseException {

        if (!(entity instanceof MmsMessage)) {
            throw new ParseException("Attempted to parse unsupported entity type");
        }
        super.serialize(entity, out);
    }

    @Override
    protected void serializeEntityContents(AbstractMessage message, XmlSerializer serializer) throws ParseException, IOException {

        if (!message.isValid()) {
            throw new ParseException("Invalid MMS entity");
        }

        // Root tag
        serializer.setPrefix(XmlConstants.TNS, XmlConstants.NS_MMS_API_URI);
        serializer.setPrefix(XmlConstants.TNS1, XmlConstants.NS_REST_COMMON_API_URI);
        serializer.startTag(XmlConstants.NS_MMS_API_URI, XmlConstants.XSD_MMSTEXTTYPE_MMSMESSAGE);
        
        // Addresses
        serializeAddressContents(message, serializer, XmlConstants.NS_MMS_API_URI);
        
    	// Recepit Rype
    	serializeReceiptTypeContents(message, serializer, XmlConstants.NS_MMS_API_URI);

        // Origin Adresses
        serializeOriginAddressContents(message, serializer, XmlConstants.NS_MMS_API_URI);

        // Message Contents
        serializeSubjectContents((MmsMessage) message, serializer);

        // End root tag
        serializer.endTag(XmlConstants.NS_MMS_API_URI, XmlConstants.XSD_MMSTEXTTYPE_MMSMESSAGE);

    }

    /**
     * This function serializes into the XML the Subject paramter
     * 
     * @param message the AbstractMessage message
     * @param serializer the serializer
     * 
     * @throws ParseException
     * @throws IOException
     */
    private void serializeSubjectContents(MmsMessage message, XmlSerializer serializer)
            throws ParseException, IOException {

        if ((message.getSubject() != null) && (!(message.getSubject().equals(""))) ) {
            serializer.startTag(XmlConstants.NS_MMS_API_URI, XmlConstants.XSD_MMSTEXTTYPE_SUBJECT);
            serializer.text(message.getSubject());
            serializer.endTag(XmlConstants.NS_MMS_API_URI, XmlConstants.XSD_MMSTEXTTYPE_SUBJECT);
        }
    }

}
///@endcond