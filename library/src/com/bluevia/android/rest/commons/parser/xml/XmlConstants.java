///@cond private
package com.bluevia.android.rest.commons.parser.xml;

/**
 * Constants (like XML tags, etc) common to all XML SMS parser
 * @author Telefonica R&D
 * 
 */
public class XmlConstants {
	
	/*
	 * Query parameters
	 */
	public static final String PARAM_VERSION_KEY = "version";
	public static final String VERSION_1 = "v1";
	
    /*
     * XML Generic
     */
    public static final String TNS = "tns";
    public static final String TNS1 = "tns1";
    public static final String XSI = "xsi";
    public static final String UCTRPC = "uctrpc";
    public static final String RPC = "rpc";
    public static final String XMLNS = "xmlns";
    public static final String XSD_TEXTYPE_SCHEMALOCATION = "schemaLocation";
    public static final String NS_REST_COMMON_API_URI = "http://www.telefonica.com/schemas/UNICA/REST/common/v1";
    public static final String NS_RPC_COMMON_API_URI = "http://www.telefonica.com/schemas/UNICA/RPC/common/v1";
    public static final String NS_RPC_DEFINITION_URI = "http://www.telefonica.com/schemas/UNICA/RPC/definition/v1";
    public static final String NS_XML_SCHEMA_INSTANCE = "http://www.w3.org/2001/XMLSchema-instance";

    public static final String XSD_CLIENT_EXCEPTION = "ClientException";
    public static final String XSD_XSD_CLIENT_EXCEPTION_ATTR_TEXT = "text";
    

    public static final String XSD_RECEIPTREQUEST = "receiptRequest";
    public static final String XSD_RECEIPTREQUEST_ENDPOINT = "endpoint";
    public static final String XSD_RECEIPTREQUEST_CORRELATOR = "correlator";
    
    /*
     * Messasing generic tags 
     */
    public static final String XSD_MESSAGE_TYPE_ADDRESS = "address";
    public static final String XSD_MESSAGE_TYPE_DESCRIPTION = "description";
    public static final String XSD_MESSAGE_TYPE_ORIGINADDRESS = "originAddress";
    public static final String XSD_MESSAGE_TYPE_DESTINATIONADDRESS = "destinationAddress";
    public static final String XSD_MESSAGE_TYPE_DATETIME = "dateTime";
    
    /*
     * SMS XML tags
     */
    public static final String XSD_SMSTEXTTYPE_SMSTEXT = "smsText";
    public static final String XSD_SMSTEXTTYPE_ENCODE = "encode";
    public static final String XSD_SMSTEXTTYPE_SOURCEPORT = "sourceport";
    public static final String XSD_SMSTEXTTYPE_DESTINATIONPORT = "destinationport";
    public static final String XSD_SMSTEXTTYPE_ESM_CLASS = "esm_class";
    public static final String XSD_SMSTEXTTYPE_DATA_CODING = "data_coding";
    
    public static final String XSD_SMSDELIVERYSTATUS_STATUS = "smsDeliveryStatus";
    public static final String XSD_RECEIVEDSMS = "receivedSMS";
    public static final String XSD_DELIVERYSTATUS_STATUS = "deliveryStatus";
    public static final String XSD_SMSTEXTTYPE_MESSAGE = "message";
    public static final String XSD_SMSTEXTTYPE_IMAGE = "image";
    public static final String XSD_SMSTEXTTYPE_RINGTONE = "ringtone";
    public static final String XSD_SMSTEXTTYPE_SMSFORMAT = "smsFormat";
    public static final String XSD_SMSFORMATTYPE_VALUE_EMS= "Ems";
    public static final String XSD_SMSFORMATTYPE_VALUE_SMART_MESSAGING = "SmartMessaging";
    
    
    public static final String NS_SMS_API_URI = "http://www.telefonica.com/schemas/UNICA/REST/sms/v1/";
    public static final String SMS_SCHEMALOCATION = "http://www.telefonica.com/schemas/UNICA/REST/sms/v1/ UNICA_API_REST_sms_types_v1_0.xsd";
    

    /*
     * USERID XML tags
     */
    public static final String XSD_USERIDTYPE_PHONENUMBER = "phoneNumber";
    public static final String XSD_USERIDTYPE_ANYURI = "anyUri";
    public static final String XSD_USERIDTYPE_ALIAS = "alias";
    public static final String XSD_USERIDTYPE_IPADDRESS = "ipAddress";
    public static final String XSD_USERIDTYPE_IPADDRESS_IPV4 = "ipv4";
    public static final String XSD_USERIDTYPE_IPADDRESS_IPV6 = "ipv6";
    public static final String XSD_USERIDTYPE_OTHERTYPE = "otherId";
    public static final String XSD_USERIDTYPE_OTHERTYPE_TYPE = "type";
    public static final String XSD_USERIDTYPE_OTHERTYPE_VALUE = "value";


    /*
     * MMS XML tags
     */
    public static final String PARAM_USE_ATTACHMENT_URLS = "useAttachmentURLs";
    public static final String XSD_MMSTEXTTYPE_MMSMESSAGE = "message";
    public static final String XSD_MMSDELIVERYSTATUS_STATUS = "messageDeliveryStatus";
    public static final String XSD_MMSTEXTTYPE_SUBJECT = "subject";
    public static final String XSD_RECEIVEDMMS = "receivedMessages";
    public static final String XSD_RECEIVEDMMS_IDENTIFIER = "messageIdentifier";
    public static final String XSD_RECEIVEDMMS_ATTACHMENT_URL = "attachmentURL";
    public static final String XSD_RECEIVEDMMS_HREF = "href";
    public static final String XSD_RECEIVEDMMS_CONTENT_TYPE = "contentType";

    public static final String NS_MMS_API_URI = "http://www.telefonica.com/schemas/UNICA/REST/mms/v1/";

    
    /*
     * DeliveryStatusType Values
     */
    public static final String XSD_DELIVERYSTATUSTYPE_VALUE_DELIVERED_TO_NETWORK = "DeliveredToNetwork";
    public static final String XSD_DELIVERYSTATUSTYPE_VALUE_DELIVERED_UNCERTAIN = "DeliveryUncertain";
    public static final String XSD_DELIVERYSTATUSTYPE_VALUE_DELIVERED_IMPOSSIBLE="DeliveryImpossible";
    public static final String XSD_DELIVERYSTATUSTYPE_VALUE_DELIVERED_MESSAGE_WAITING="MessageWaiting";
    public static final String XSD_DELIVERYSTATUSTYPE_VALUE_DELIVERED_TO_TERMINAL="DeliveredToTerminal";
    public static final String XSD_DELIVERYSTATUSTYPE_VALUE_DELIVERED_DELIVERY_NOTIF_NOTSUPP ="DeliveryNotificationNotSupported";

   /* Advertisement XML Tags */
    public static final String XSD_AD_ADRESPONSE = "adResponse";
    public static final String XSD_AD_AD = "ad";
    public static final String XSD_AD_RESOURCE = "resource";
    public static final String XSD_AD_CREATIVE_ELEMENT = "creative_element";
    public static final String XSD_AD_ATTRIBUTE = "attribute";
    public static final String XSD_AD_INTERACTION = "interaction";

    public static final String XSD_AD_AD_RESPONSE_ATTR_ID="id";
    public static final String XSD_AD_AD_RESPONSE_ATTR_VERSION="version";

    public static final String XSD_AD_AD_ATTR_AD_PLACEMENT = "ad_placement";
    public static final String XSD_AD_AD_ATTR_CAMPAIGN = "campaign";
    public static final String XSD_AD_AD_ATTR_FLIGHT = "flight";
    public static final String XSD_AD_AD_ATTR_ID = "id";

    public static final String XSD_AD_RESOURCE_ATTR_AD_PRESENTATION = "ad_presentation";

    public static final String XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE ="type";

    public static final String XSD_AD_ATTRIBUTE_ATTR_TYPE = "type";

    public static final String XSD_AD_INTERACTION_ATTR_TYPE = "type";

    public static final String XSD_AD_ATTRIBUTE_ATTR_TYPE_URL_VALUE = "URL";
    public static final String XSD_AD_ATTRIBUTE_ATTR_TYPE_ADTEXT_VALUE = "adtext";
    public static final String XSD_AD_ATTRIBUTE_ATTR_TYPE_LOCATOR_VALUE = "locator";
    public static final String XSD_AD_ATTRIBUTE_ATTR_TYPE_CODEC_VALUE = "codec";

    public static final String XSD_AD_INTERACTION_ATTR_TYPE_CLICK2WAP_VALUE = "click2wap";

    public static final String XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_IMAGE_VALUE="image";
    public static final String XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_TEXT_VALUE="text";
    public static final String XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_SOUND_VALUE="sound";
    public static final String XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_ZIP_VALUE="zip";
    public static final String XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_PAGE_VALUE="page";
    
    /* Location xml tags */
    public static final String XSD_TERMINALLOCATION_LOCATION = "terminalLocation";
    public static final String XSD_TERMINALLOCATION_LOCATEDPARTY = "locatedParty";
    public static final String XSD_TERMINALLOCATION_REPORTSTATUS = "reportStatus";
    public static final String XSD_TERMINALLOCATION_CURRENTLOCATION = "currentLocation";
    public static final String XSD_TERMINALLOCATION_COORDINATES = "coordinates";
    public static final String XSD_TERMINALLOCATION_ERRORINFORMATION = "errorInformation";
	public static final String XSD_TERMINALLOCATION_LATITUDE = "latitude";
	public static final String XSD_TERMINALLOCATION_LONGITUDE = "longitude";
	public static final String XSD_TERMINALLOCATION_ACCURACY = "accuracy";
	public static final String XSD_TERMINALLOCATION_TIMESTAMP = "timestamp";
	public static final String XSD_TERMINALLOCATION_PHONENUMBER = "phoneNumber";
	public static final String XSD_TERMINALLOCATION_ALTITUDE = "altitude";
	
	public static final String XSD_TERMINALLOCATION_REPORTSTATUS_ATTR_RETRIEVED_VALUE = "Retrieved";
	public static final String XSD_TERMINALLOCATION_REPORTSTATUS_ATTR_NONRETRIEVED_VALUE = "Non-Retrieved";
	public static final String XSD_TERMINALLOCATION_REPORTSTATUS_ATTR_ERROR_VALUE = "Error";
	
    
	/* XML-RPC common tags */
	public static final String XSD_RPC_METHOD_CALL = "methodCall";
	public static final String XSD_RPC_METHOD_RESPONSE = "methodResponse";
	public static final String XSD_RPC_METHOD_ATTR = "method";
	public static final String XSD_RPC_ID_ATTR = "id";
	public static final String XSD_RPC_VERSION_ATTR = "version";
	public static final String XSD_RPC_PARAMS = "params";
	public static final String XSD_RPC_RESULT = "result";
	public static final String XSD_RPC_ERROR = "error";
	public static final String XSD_RPC_ERROR_CODE = "code";
	public static final String XSD_RPC_ERROR_MESSAGE = "message";
	
	/* Payment xml tags */
	public static final String NS_PAYMENT_API_URI = "http://www.telefonica.com/schemas/UNICA/RPC/payment/v1";
	public static final String NS_PAYMENT_SCHEMALOCATION = "http://www.telefonica.com/schemas/UNICA/RPC/payment/v1 UNICA_API_RPC_payment_types_v1_0.xsd";
	public static final String XSD_PAYMENT_MAKE_PAYMENT_PARAMS = "paymentParams";
	public static final String XSD_PAYMENT_GET_PAYMENT_STATUS_PARAMS = "getPaymentStatusParams";	
	public static final String XSD_PAYMENT_MAKE_REFUND_PARAMS = "refundParams";
	public static final String XSD_PAYMENT_PAYMENT_RESULT = "paymentResult";
	public static final String XSD_PAYMENT_GET_PAYMENT_STATUS = "getPaymentStatusResult";
	public static final String XSD_PAYMENT_TIME_STAMP = "timestamp";
	public static final String XSD_PAYMENT_PAYMENT_INFO = "paymentInfo";
	public static final String XSD_PAYMENT_PAYMENT_INFO_END_USER_IDENTIFIER = "endUserIdentifier";
	public static final String XSD_PAYMENT_PAYMENT_INFO_AMOUNT = "amount";
	public static final String XSD_PAYMENT_PAYMENT_INFO_CURRENCY = "currency";
	public static final String XSD_PAYMENT_PAYMENT_INFO_TAXES = "taxes";
	public static final String XSD_PAYMENT_PAYMENT_INFO_CODE = "code";
	public static final String XSD_PAYMENT_PAYMENT_INFO_REFERENCE_CODE = "referencecode";
	public static final String XSD_PAYMENT_PAYMENT_INFO_DESCRIPTION = "description";
	public static final String XSD_PAYMENT_TRANSACTION_ID = "transactionId"; 	
	public static final String XSD_PAYMENT_TRANSACTION_STATUS = "transactionStatus"; 	
	public static final String XSD_PAYMENT_TRANSACTION_STATUS_DESCRIPTION = "transactionStatusDescription"; 	
	public static final String XSD_PAYMENT_ATTR_TRANSACTION_STATUS_SUCCESS_VALUE = "SUCCESS";
	public static final String XSD_PAYMENT_ATTR_TRANSACTION_STATUS_FAILURE_VALUE = "FAILURE";
	public static final String XSD_PAYMENT_ATTR_TRANSACTION_STATUS_PENDING_VALUE = "PENDING";
//	public static final String XSD_PAYMENT_METHOD_CALL = "methodCall"; See xmlconstants xml-rpc common section  	
//	public static final String XSD_PAYMENT_METHOD_RESPONSE = "methodResponse"; See xmlconstants xml-rpc common section	
//	public static final String XSD_PAYMENT_METHOD_PARAMS = "params"; See xmlconstants xml-rpc common section	
//	public static final String XSD_PAYMENT_RESULT = "result"; See xmlconstants xml-rpc common section	
	
	/* Call management xml tags */
	public static final String NS_CALLMANAGEMENT_API_URI = "http://www.telefonica.com/schemas/UNICA/RPC/third_party_call/v1";
	public static final String NS_CALLMANAGEMENT_SCHEMALOCATION = "http://www.telefonica.com/schemas/UNICA/RPC/third_party_call/v1 UNICA_API_RPC_thirdpartycall_types_v1_0.xsd";
	public static final String XSD_CALLMANAGEMENT_MAKE_CALL_SESSION_PARAMS = "makeCallSessionParams";
	public static final String XSD_CALLMANAGEMENT_CALL_PARTICIPANT = "callParticipant";
	public static final String XSD_CALLMANAGEMENT_FIRST_PARTICIPANT_DISPLAYED_ADDRESS = "firstParticipantDisplayedAddress";
	public static final String XSD_CALLMANAGEMENT_OTHER_PARTICIPANTS_DISPLAYED_ADDRESS = "otherParticipantsDisplayedAddress";
	public static final String XSD_CALLMANAGEMENT_MEDIA_INFO = "mediaInfo";
	public static final String XSD_CALLMANAGEMENT_MEDIA_INFO_TYPE = "media";
	public static final String XSD_CALLMANAGEMENT_MEDIA_DIRECTION_TYPE = "mediaDirection";
	public static final String XSD_CALLMANAGEMENT_CHANGE_MEDIA_NOT_ALLOWED = "changeMediaNotAllowed";
	public static final String XSD_CALLMANAGEMENT_MAKE_CALL_SESSION_RESULT = "makeCallSessionResult";
	public static final String XSD_CALLMANAGEMENT_CALL_SESSION_IDENTIFIER = "callSessionIdentifier";
	
}
///@endcond
