///@cond private
package com.bluevia.android.rest.commons.parser.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.data.UserId;
import com.bluevia.android.rest.commons.data.UserId.Type;
import com.bluevia.android.rest.commons.data.xmlrpc.MethodResponse;
import com.bluevia.android.rest.commons.data.xmlrpc.RpcError;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xmlrpc.XmlRpcErrorParser;

/**
 * 
 * Internal class with xml parser and serializer util functions.
 * 
 * @author Telefonica I+D
 * 
 * 
 */
public class XmlParserSerializerUtils {

	private static final String TAG = "XmlParserSerializerUtils";

	public static String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {        
			return "";
		}
	}
	
	/**
	 * 
	 * @param parser
	 * @return
	 * @throws XmlPullParserException when an error occurs parsing
	 * @throws IOException when an error reading the stream occurs
	 */
	public static String getChildText(XmlPullParser parser)  throws XmlPullParserException, IOException {
		int eventType = parser.next();
		Log.e(TAG, "getChildText Siguiente evento: " + XmlPullParser.TYPES[eventType] + " nombre: " + parser.getName());
		if (eventType != XmlPullParser.TEXT) {
			return null;
		}
		Log.d(TAG, "Text1: " + parser.getText());
		return parser.getText();
	}
	
	/**
	 * 
	 * @param parser
	 * @param eventTypeRef
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static String getChildText(XmlPullParser parser, int[] eventTypeRef)  throws XmlPullParserException, IOException {
		int eventType = parser.next();
		eventTypeRef[0] = eventType;

		Log.d(TAG, "getChildText Siguiente evento: " + XmlPullParser.TYPES[eventType] + " nombre: " + parser.getName());
		if (eventType != XmlPullParser.TEXT) {
			return null;
		}
		Log.d(TAG, "Text2: " + parser.getText().trim());
		return parser.getText().trim();
	}
	
	/**
	 * This method parser an userid from the xml and with an specific [userid] tag (i.e. "address")
	 * If address is empty or null it returns null
	 *  <tns:[userid]>"
	 *      <uctr:phoneNumber>6698787878</uctr:phoneNumber>
	 *  </tns:[userid]>
	 * @return String with the phoneNumber
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static UserId parseUserId(XmlPullParser parser, int eventType, String userIdTag) throws XmlPullParserException,
	IOException, ParseException {

		UserId userId = null;

		if (eventType != XmlPullParser.START_TAG) {
			throw new ParseException("Expected event START_TAG: Actual event: "
					+ XmlPullParser.TYPES[eventType]);
		}

		while (eventType != XmlPullParser.END_DOCUMENT) {
			String name = parser.getName();
			switch (eventType) {
			case XmlPullParser.END_TAG:
				Log.d(TAG, "handleUserId end tag name is " + name);
				if (name.equals(userIdTag)) {
					return userId;
				}
				break;
			case XmlPullParser.START_TAG:
				if (XmlConstants.XSD_USERIDTYPE_PHONENUMBER.equals(name)) {
					int[] textEventType = new int[1];
					String value = XmlParserSerializerUtils.getChildText(parser, textEventType);
					Log.d(TAG, "handleUserId: " + XmlPullParser.TYPES[textEventType[0]]);
					if ((value == null) && (textEventType[0] == XmlPullParser.END_TAG)) {
						eventType = textEventType[0];
						continue;
					}
					userId = new UserId(Type.PHONE_NUMBER, value);
				} else if (XmlConstants.XSD_USERIDTYPE_ALIAS.equals(name)) {
					int[] textEventType = new int[1];
					String value = XmlParserSerializerUtils.getChildText(parser, textEventType);
					Log.d(TAG, "handleUserId: " + XmlPullParser.TYPES[textEventType[0]]);
					if ((value == null) && (textEventType[0] == XmlPullParser.END_TAG)) {
						eventType = textEventType[0];
						continue;
					}
					userId = new UserId(Type.ALIAS, value);
				} else if (XmlConstants.XSD_USERIDTYPE_ANYURI.equals(name)) {
					int[] textEventType = new int[1];
					String value = XmlParserSerializerUtils.getChildText(parser, textEventType);
					Log.d(TAG, "handleUserId: " + XmlPullParser.TYPES[textEventType[0]]);
					if ((value == null) && (textEventType[0] == XmlPullParser.END_TAG)) {
						eventType = textEventType[0];
						continue;
					}
					userId = new UserId(Type.ANY_URI, value);
				} else if (XmlConstants.XSD_USERIDTYPE_IPADDRESS.equals(name)) {
					userId = handleIpAddress(parser, eventType);
				} else if (XmlConstants.XSD_USERIDTYPE_OTHERTYPE.equals(name)) {
					userId = handleUserIdOtherType(parser, eventType);
				}
				break;
			default:
				break;
			}
			eventType = parser.next();
		}
		return null;
	}

	private static UserId handleIpAddress (XmlPullParser parser, int eventType) throws XmlPullParserException, IOException,
	ParseException {
		UserId userId = null;

		if (eventType != XmlPullParser.START_TAG) {
			throw new ParseException("Expected event START_TAG: Actual event: "
					+ XmlPullParser.TYPES[eventType]);
		}

		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.END_TAG:
				String name = parser.getName();
				Log.d(TAG, "handleIpAddress end tag name is " + name);
				if (XmlConstants.XSD_USERIDTYPE_IPADDRESS.equals(name)) {
					Log.d(TAG, "UserId is " + userId.getType() + "=" + userId.getUserIdValue());
					// stop parsing here.
					return userId;
				}
				break;
			case XmlPullParser.START_TAG:
				name = parser.getName();
				Log.d(TAG, "handleIpAddress start tag name is " + name);
				if (XmlConstants.XSD_USERIDTYPE_IPADDRESS_IPV4.equals(name)) {
					int[] textEventType = new int[1];
					String value  = XmlParserSerializerUtils.getChildText(parser, textEventType);
					Log.d(TAG, "handleUserId: " + XmlPullParser.TYPES[textEventType[0]]);
					if ((value == null) && (textEventType[0] == XmlPullParser.END_TAG)) {
						eventType = textEventType[0];
						continue;
					}
					userId = new UserId(Type.IPV4_ADDRESS, value);
				} else if (XmlConstants.XSD_USERIDTYPE_IPADDRESS_IPV6.equals(name)) {
					int[] textEventType = new int[1];
					String value  = XmlParserSerializerUtils.getChildText(parser, textEventType);
					Log.d(TAG, "handleIpAddress: " + XmlPullParser.TYPES[textEventType[0]]);
					if ((value == null) && (textEventType[0] == XmlPullParser.END_TAG)) {
						eventType = textEventType[0];
						continue;
					}
					userId = new UserId(Type.IPV6_ADDRESS, value);
				}
				break;
			default:
				break;
			}
			eventType = parser.next();
		}

		return userId;

	}

	private static UserId handleUserIdOtherType (XmlPullParser parser, int eventType) 
	throws XmlPullParserException, IOException, ParseException {
		UserId userId = null;

		if (eventType != XmlPullParser.START_TAG) {
			throw new ParseException("Expected event START_TAG: Actual event: "
					+ XmlPullParser.TYPES[eventType]);
		}

		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.END_TAG:
				String name = parser.getName();
				Log.d(TAG, "handleUserIdOtherType end tag name is " + name);
				if (XmlConstants.XSD_USERIDTYPE_OTHERTYPE.equals(name)) {
					Log.d(TAG, "UserId is " + userId.getType() + "=" + userId.getUserIdValue());
					// stop parsing here.
					return userId;
				}
				break;
			case XmlPullParser.START_TAG:
				name = parser.getName();
				if (XmlConstants.XSD_USERIDTYPE_OTHERTYPE_TYPE.equals(name)) {
					int[] textEventType = new int[1];
					String value  = XmlParserSerializerUtils.getChildText(parser, textEventType);
					Log.d(TAG, "handleUserId: " + XmlPullParser.TYPES[textEventType[0]]);
					if ((value == null) && (textEventType[0] == XmlPullParser.END_TAG)) {
						eventType = textEventType[0];
						continue;
					}
					if (userId == null) {
						userId = new UserId();
						userId.setType(Type.OTHER_ID);
					}
					userId.setOtherType(value);
				} else if (XmlConstants.XSD_USERIDTYPE_OTHERTYPE_VALUE.equals(name)) {
					int[] textEventType = new int[1];
					String value  = XmlParserSerializerUtils.getChildText(parser, textEventType);
					Log.d(TAG, "handleUserIdOtherType: " + XmlPullParser.TYPES[textEventType[0]]);
					if ((value == null) && (textEventType[0] == XmlPullParser.END_TAG)) {
						eventType = textEventType[0];
						continue;
					}
					if (userId == null) {
						userId = new UserId();
						userId.setType(Type.OTHER_ID);
					}
					Log.d(TAG, "handleUserIdOtherType: otherid value" + value);
					userId.setUserIdValue(value);
				}
				break;
			default:
				break;
			}
			eventType = parser.next();
		}

		return userId;
	}
	
	/**
	 * 
	 * @param is
	 * @return
	 */
	public static String parseRestError(InputStream is) {
		if (is == null) {
            return null;
        }

        // init the parser
		XmlPullParser parser = Xml.newPullParser();

        try {
        	parser.setInput(is, null);
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Exception occurred while reading the XML Document", e);
            return null;
        }

        // Start the parse process
        int eventType;

        try {
            eventType = parser.getEventType();
        } catch (XmlPullParserException e) {
        	return null;
        }

        if (eventType != XmlPullParser.START_DOCUMENT) {
        	return null;
        }

        try {
            // Gets the first eventType
            eventType = parser.next();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                if (eventType == XmlPullParser.START_TAG &&
                		name.equals(XmlConstants.XSD_XSD_CLIENT_EXCEPTION_ATTR_TEXT))
                	return getChildText(parser);
                eventType = parser.next();
            }
        } catch (XmlPullParserException xppe) {
            return null;
        } catch (IOException ioe) {
            return null;
        }
		return null;
	}
	
	/**
	 * 
	 * @param is
	 * @return
	 */
	public static String parseRpcError(InputStream is) {
		XmlRpcErrorParser parser = new XmlRpcErrorParser();
		
		try {
			Entity res = parser.parseEntry(is);
			if (res != null && res instanceof MethodResponse){
				Entity responseEntity = ((MethodResponse) res).getResponseEntity();
				if (responseEntity != null && responseEntity instanceof RpcError){
					return ((RpcError)responseEntity).getMessage();
				}
			}
		} catch (ParseException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return null;
	}
	
	public static RpcError handleError(XmlPullParser parser, int eventType) throws ParseException,
			XmlPullParserException, IOException {

		RpcError error = null;
		
		if (eventType != XmlPullParser.START_TAG) {
            throw new ParseException("Expected event START_TAG: Actual event: "
                    + XmlPullParser.TYPES[eventType]);
        }
		
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();

            switch (eventType) {
                case XmlPullParser.END_TAG:
                    if (name.equals(XmlConstants.XSD_RPC_ERROR)) {
                    	if (error == null)
                    		throw new ParseException("Error parsing response");
                        return error;
                    }
                    break;
                case XmlPullParser.START_TAG:
                    if (name.equals(XmlConstants.XSD_RPC_ERROR)) {
                    	error = new RpcError();
                    } else if (name.equals(XmlConstants.XSD_RPC_ERROR_CODE)) {
                    	String code = getChildText(parser);
                        error.setCode(code);
                    } else if (name.equals(XmlConstants.XSD_RPC_ERROR_MESSAGE)) {
                    	String message = getChildText(parser);
                        error.setMessage(message);
                    }                    
                default:
                    break;
            }
            eventType = parser.next();
        }
        return null;
	}
	
	
	/**
	 * 
	 * @param ns
	 * @param address
	 * @param serializer
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void serializeUserIdContents(String ns, UserId address, XmlSerializer serializer)
	throws ParseException, IOException {

		if (!address.isValid()) {
			throw new ParseException("address in not valid");
		}

		switch (address.getType()) {
		case PHONE_NUMBER:
			serializer.startTag(ns, XmlConstants.XSD_USERIDTYPE_PHONENUMBER);
			serializer.text(address.getUserIdValue());
			serializer.endTag(ns, XmlConstants.XSD_USERIDTYPE_PHONENUMBER);
			break;
		case ALIAS:
			serializer.startTag(ns, XmlConstants.XSD_USERIDTYPE_ALIAS);
			serializer.text(address.getUserIdValue());
			serializer.endTag(ns, XmlConstants.XSD_USERIDTYPE_ALIAS);
			break;
		case ANY_URI:
			serializer.startTag(ns, XmlConstants.XSD_USERIDTYPE_ANYURI);
			serializer.text(address.getUserIdValue());
			serializer.endTag(ns, XmlConstants.XSD_USERIDTYPE_ANYURI);
			break;
		case IPV4_ADDRESS:
			serializer.startTag(ns, XmlConstants.XSD_USERIDTYPE_IPADDRESS);
			serializer.startTag(ns, XmlConstants.XSD_USERIDTYPE_IPADDRESS_IPV4);
			serializer.text(address.getUserIdValue());
			serializer.endTag(ns, XmlConstants.XSD_USERIDTYPE_IPADDRESS_IPV4);
			serializer.endTag(ns, XmlConstants.XSD_USERIDTYPE_IPADDRESS);
			break;
		case IPV6_ADDRESS:
			serializer.startTag(ns, XmlConstants.XSD_USERIDTYPE_IPADDRESS);
			serializer.startTag(ns, XmlConstants.XSD_USERIDTYPE_IPADDRESS_IPV6);
			serializer.text(address.getUserIdValue());
			serializer.endTag(ns, XmlConstants.XSD_USERIDTYPE_IPADDRESS_IPV6);
			serializer.endTag(ns, XmlConstants.XSD_USERIDTYPE_IPADDRESS);
			break;
		case OTHER_ID:
			serializer.startTag(ns, XmlConstants.XSD_USERIDTYPE_OTHERTYPE);
			serializer.startTag(ns, XmlConstants.XSD_USERIDTYPE_OTHERTYPE_TYPE);
			serializer.text(address.getOtherType());
			serializer.endTag(ns, XmlConstants.XSD_USERIDTYPE_OTHERTYPE_TYPE);
			serializer.startTag(ns, XmlConstants.XSD_USERIDTYPE_OTHERTYPE_VALUE);
			serializer.text(address.getUserIdValue());
			serializer.endTag(ns, XmlConstants.XSD_USERIDTYPE_OTHERTYPE_VALUE);
			serializer.endTag(ns, XmlConstants.XSD_USERIDTYPE_OTHERTYPE);
			break;
		}
	}
	
	/**
	 * 
	 * 
	 * @param apiNamespace
	 * @param commonTypeNamespace
	 * @param endpoint
	 * @param correlator
	 * @param serializer
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void serializeReceiptRequestContents(String apiNamespace, String commonTypeNamespace, String endpoint, String correlator, XmlSerializer serializer)
	throws ParseException, IOException {
		if (endpoint != null && endpoint.trim().length() != 0 && correlator != null && correlator.trim().length() != 0) {
            serializer.startTag(apiNamespace, XmlConstants.XSD_RECEIPTREQUEST);

            //Endpoint
            serializer.startTag(commonTypeNamespace, XmlConstants.XSD_RECEIPTREQUEST_ENDPOINT);
            serializer.text(endpoint);
            serializer.endTag(commonTypeNamespace, XmlConstants.XSD_RECEIPTREQUEST_ENDPOINT);
            
            //Correlator
            serializer.startTag(commonTypeNamespace, XmlConstants.XSD_RECEIPTREQUEST_CORRELATOR);
            serializer.text(correlator);
            serializer.endTag(commonTypeNamespace, XmlConstants.XSD_RECEIPTREQUEST_CORRELATOR);
            
            serializer.endTag(apiNamespace, XmlConstants.XSD_RECEIPTREQUEST);
        }
	}


}
///@endcond