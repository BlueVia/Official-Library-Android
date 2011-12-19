/**
 * \package com.bluevia.android.rest.commons.parser.xmlrpc This package contains the classes in order to manage and parse/serialize xml-rpc calls and responses using Bluevia API.
 */
package com.bluevia.android.rest.commons.parser.xmlrpc;

import java.io.IOException;
import java.io.OutputStream;

import org.xmlpull.v1.XmlSerializer;

import com.bluevia.android.rest.commons.Encoding;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.data.xmlrpc.MethodCall;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;

import android.util.Log;
import android.util.Xml;


/**
 * Xml serializer for RPC MethodCall entities.
 */
public abstract class XmlMethodCallSerializer implements BlueviaEntitySerializer {
	
    private static final String TAG = "XmlMethodCallSerializer";

	public Encoding getEncoding() {
		return Encoding.APPLICATION_XML;
	}

	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer#serialize(com.bluevia.android.rest.commons.Entity, java.io.OutputStream)
	 */
	public abstract void serialize(Entity entity, OutputStream out) throws IOException, ParseException;

	/**
	 * This function serializes the methodCall tag.
	 * 
	 * @param entity the entity to serialize
	 * @param out out the outputstream to write the serialization result to
	 * @param apiUri the specific API Uri
	 * @param schemaLocation the specific Schema Location for the API
     * @throws IOException thrown in the output stream could not be written
     * @throws ParseException thrown if entity is not an instance of {@link com.bluevia.android.rest.messaging.sms.data.Sms AbstractSms}
	 */
	protected void serialize(Entity entity, OutputStream out, String apiUri,
			String schemaLocation) throws IOException, ParseException {
        if (!(entity instanceof MethodCall)) {
            throw new ParseException("Attempted to parse unsupported entity type");
        };
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(out, "UTF-8");
        serializer.startDocument(null, null);
        
        // Root tag
		serializer.setPrefix(XmlConstants.UCTRPC, XmlConstants.NS_RPC_COMMON_API_URI);
		serializer.setPrefix(XmlConstants.RPC, XmlConstants.NS_RPC_DEFINITION_URI);
		serializer.setPrefix(XmlConstants.TNS, apiUri);
		serializer.setPrefix(XmlConstants.XSI, XmlConstants.NS_XML_SCHEMA_INSTANCE);
		serializer.startTag(apiUri, XmlConstants.XSD_RPC_METHOD_CALL);
		serializer.attribute(XmlConstants.NS_XML_SCHEMA_INSTANCE, XmlConstants.XSD_TEXTYPE_SCHEMALOCATION, schemaLocation);
		
		//Serialize MethodCall Attributes
		serializeMethodCallAttributes((MethodCall) entity, serializer, apiUri);
		
		// Message Params
		if (((MethodCall) entity).getParamsEntity() != null)
		{
			serializer.startTag(apiUri, XmlConstants.XSD_RPC_PARAMS);
			serializeParams(((MethodCall) entity).getParamsEntity(), serializer);
			serializer.endTag(apiUri, XmlConstants.XSD_RPC_PARAMS);
		}
		
		// End root tag
		serializer.endTag(apiUri, XmlConstants.XSD_RPC_METHOD_CALL);
        
        serializer.endDocument();;
        serializer.flush();
	}
	
	/**
	 * Serializes the common attributes of the MethodCall
	 * 
	 * @param methodCall the MethodCall entity
	 * @param serializer the serializer
	 * @throws IOException
	 * @throws ParseException
	 */
	private void serializeMethodCallAttributes(MethodCall methodCall, XmlSerializer serializer, String apiUri) throws IOException, ParseException {
	
		//Id (optional)
		if (methodCall.getId() != null){
			serializer.startTag(XmlConstants.NS_RPC_DEFINITION_URI, XmlConstants.XSD_RPC_ID_ATTR);
	        serializer.text(methodCall.getId());
	        serializer.endTag(XmlConstants.NS_RPC_DEFINITION_URI, XmlConstants.XSD_RPC_ID_ATTR);
		}
		
		//Version (mandatory)
		if (methodCall.getVersion() == null)
			throw new ParseException("Version attribute cannot be null");
        serializer.startTag(XmlConstants.NS_RPC_DEFINITION_URI, XmlConstants.XSD_RPC_VERSION_ATTR);
        serializer.text(methodCall.getVersion());
        serializer.endTag(XmlConstants.NS_RPC_DEFINITION_URI, XmlConstants.XSD_RPC_VERSION_ATTR);

		//Method (mandatory)
        Log.d(TAG, "getting Method:" + methodCall.getMethod());
		if (methodCall.getMethod() == null)
			throw new ParseException("Method attribute cannot be null");
        serializer.startTag(apiUri, XmlConstants.XSD_RPC_METHOD_ATTR);
        serializer.text(methodCall.getMethod());
        serializer.endTag(apiUri, XmlConstants.XSD_RPC_METHOD_ATTR);
	}
	
	/**
	 * Abstract function to serialize the params of the Method Call
	 * 
	 * @param entity the specific entity to serialize
     * @param serializer the serializer
	 * @throws IOException
	 * @throws ParseException
	 */
	protected abstract void serializeParams(Entity entity, XmlSerializer serializer) throws IOException, ParseException;
	
}
