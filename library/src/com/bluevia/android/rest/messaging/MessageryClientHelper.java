///@cond private
package com.bluevia.android.rest.messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.messaging.sms.data.ReceivedSms;

/**
 * Class to help SMS and MMS Client to build their methods.
 * It have helper methods common to SMS and MMS to build the requests and process the responses
 *
 * @author Telefonica R&D
 * 
 */
public class MessageryClientHelper {

    private static final String TAG = "MessageryClientHelper";

    /**
     * URI path separator
     */
    public static final String URI_PATH_SEPARATOR = "/";
    /**
     * Delivery status URI path
     */
    public static final String DELIVERY_STATUS_FEED_PATH_BLUEVIA = "/deliverystatus";

    /**
     * Meesage Id Location Header Name
     */
    public static final String MESSAGEID_LOCATION_HEADER = "Location";

    /**
     * Check if the message passed as parameter is valid and throws an BlueviaClientException if not.
     *
     * @param message message to check.
     * @throws BlueviaClientException if the message parameter is not valid.
     */
    public static void checkMessage(AbstractMessage message) throws BlueviaClientException{
        if ((message == null) || (!message.isValid()))
            throw new BlueviaClientException("message parameters are either null or not valid", BlueviaClientException.BAD_REQUEST_EXCEPTION);
    }

    /**
     * Gets the messageId from the Entity response.
     *
     * @param response Entity response which contents the result.
     * @param feed field of the uri to distinguish SMS and MMS
     * @return the messageId from the response.
     * @throws BlueviaClientException if the response or the result are not valid.
     */
    public static String getMesageIdFromResponse(Entity response, String feed) throws BlueviaClientException{

        //Check response is instance of SendSmsResult
        if ((response == null) || (!(response instanceof SendMessageResult)))
            throw new BlueviaClientException("Error during request. Response received does not correspond to an SendMessageResult",
                    BlueviaClientException.INTERNAL_CLIENT_ERROR);

        //Set the response
        String locationUrl = ((SendMessageResult) response).getResult();
        
        // Extract the SMSID from the delivery http URL using Java patterns
        // Location Url is like  "https://telefonica.com/gSDP/REST/{SMS|MMS}/outbound/requests/MessageId/deliverystatus"
        String regex = "^(https?)://(.*)/" + feed + "/(.*)" + DELIVERY_STATUS_FEED_PATH_BLUEVIA +"/?$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(locationUrl);
        if (matcher.matches()) {
            //MessageId value corresponds to the 3rd group on the pattern
        	Log.d(TAG,"matcher.group(1):"+matcher.group(1));
        	Log.d(TAG,"matcher.group(2):"+matcher.group(2));
        	Log.d(TAG,"matcher.group(3):"+matcher.group(3));
        	
       		return matcher.group(3);
        } else throw new BlueviaClientException("Error during request. Location received does not correspond to the pattern of an MessageID URI" +
            		"LocationUri: "+locationUrl+" regex: "+regex,
                    BlueviaClientException.INTERNAL_CLIENT_ERROR);
    }

    /**
     * Check if the messageId passed as parameter is valid.
     *
     * @param id messageId to check.
     * @throws BlueviaClientException if the id is not valid.
     */
    public static void checkMessageId(String id) throws BlueviaClientException{
        if ((id == null) || (id.trim().length() == 0))
            throw new BlueviaClientException("messageId is either null or empty", BlueviaClientException.BAD_REQUEST_EXCEPTION);
    }
    
    /**
     * Gets the MessageDeliveryStatus from the Entity response.
     *
     * @param response Entity response which contents the result.
     * @return an arrayList containing the MessageDeliveryStatus for each destination address from the sent message.
     * @throws BlueviaClientException if the response or the status are not valid.
     */
    public static ArrayList<MessageDeliveryStatus> getStatusFromResponse(Entity response) throws BlueviaClientException{
        ArrayList<MessageDeliveryStatus> status = null;

        //Check if response is instance of MessageDeliveryStatus
        if ((response == null) || (! (response instanceof MessageDeliveryStatusList)))
            throw new BlueviaClientException("Error during request. Response received does not correspond to an MessageDeliveryStatus",
                    BlueviaClientException.INTERNAL_CLIENT_ERROR);

        //Set the response
        status = ((MessageDeliveryStatusList) response).getDeliveryStatusList();

        if (status.isEmpty())
            throw new BlueviaClientException("The MessageDeliveryStatus received is empty",
                    BlueviaClientException.INTERNAL_CLIENT_ERROR);

        return status;
    }

    /**
     * Creates the SendMessageResult response with the messageId contained in the response headers.
     *
     * @param responseHeaders headers containing the messageId.
     * @return the response including its messageId.
     */
    public static SendMessageResult getResponseWithMessageId(HashMap<String, String> responseHeaders){
        SendMessageResult response = null;

        // In this case, when sending a Message, the response is not returned using an XML (is is null)
        // but using header location
        String messageId = responseHeaders.get (MESSAGEID_LOCATION_HEADER);
        if ((messageId != null) && (messageId.trim().length() > 0)) {
            response = new SendMessageResult();
            response.setResult (messageId);
        }
        return response;
    }
    
    
    /**
     * Check if the registrationId passed as parameter is valid.
     *
     * @param id registrationId to check.
     * @throws BlueviaClientException if the id is not valid.
     */
    public static void checkRegistrationId(String id) throws BlueviaClientException{
        if ((id == null) || (id.trim().length() == 0))
            throw new BlueviaClientException("RegistrationId is either null or empty", BlueviaClientException.BAD_REQUEST_EXCEPTION);
    }
    
}
///@endcond