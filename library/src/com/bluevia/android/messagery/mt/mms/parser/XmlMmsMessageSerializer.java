package com.bluevia.android.messagery.mt.mms.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.parser.ISerializer;
import com.bluevia.android.commons.parser.SerializeException;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.messagery.data.AbstractMessage;
import com.bluevia.android.messagery.mt.mms.data.MmsMessageReq;
import com.bluevia.android.messagery.mt.parser.AbstractXmlMessageSerializer;

/**
 * Xml serializer for MmsMessageReq entities.
 * 
 * @author Telefonica I+D
 * 
 */
class XmlMmsMessageSerializer extends AbstractXmlMessageSerializer implements ISerializer {

	public ByteArrayOutputStream serialize(Entity entity) throws SerializeException {
	    
        if (!(entity instanceof MmsMessageReq)) {
            throw new SerializeException("Attempted to parse unsupported entity type");
        }
        return super.serialize(entity);
    }

    @Override
    protected void serializeEntityContents(AbstractMessage message, XmlSerializer serializer) throws SerializeException, IOException {

        if (!message.isValid()) {
            throw new SerializeException("Invalid MMS entity");
        }

        // Root tag
        serializer.setPrefix(XmlConstants.TNS, XmlConstants.NS_MMS_API_URI);
        serializer.setPrefix(XmlConstants.TNS1, XmlConstants.NS_REST_COMMON_API_URI);
        serializer.startTag(XmlConstants.NS_MMS_API_URI, XmlConstants.XSD_MMSTEXTTYPE_MMSMESSAGE);
        
        // Addresses
        serializeAddressContents(message, serializer, XmlConstants.NS_MMS_API_URI);
        
    	// Recepit Type
    	serializeReceiptTypeContents(message, serializer, XmlConstants.NS_MMS_API_URI);

    	//Sender name
    	serializeSenderNameContents(message, serializer, XmlConstants.NS_MMS_API_URI);
    	
        // Origin Adresses
        serializeOriginAddressContents(message, serializer, XmlConstants.NS_MMS_API_URI);

        // Message Contents
        serializeSubjectContents((MmsMessageReq) message, serializer);

        // End root tag
        serializer.endTag(XmlConstants.NS_MMS_API_URI, XmlConstants.XSD_MMSTEXTTYPE_MMSMESSAGE);

    }

    /**
     * This function serializes into the XML the Subject paramter
     * 
     * @param message the AbstractMessage message
     * @param serializer the serializer
     * 
     * @throws SerializeException
     * @throws IOException
     */
    private void serializeSubjectContents(MmsMessageReq message, XmlSerializer serializer)
            throws SerializeException, IOException {

        if ((message.getSubject() != null) && (!(message.getSubject().equals(""))) ) {
            serializer.startTag(XmlConstants.NS_MMS_API_URI, XmlConstants.XSD_MMSTEXTTYPE_SUBJECT);
            serializer.text(message.getSubject());
            serializer.endTag(XmlConstants.NS_MMS_API_URI, XmlConstants.XSD_MMSTEXTTYPE_SUBJECT);
        }
    }

}
