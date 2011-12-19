///@cond private
package com.bluevia.android.rest.ad.parser.xml;


import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import com.bluevia.android.rest.ad.data.AdRequest;
import com.bluevia.android.rest.commons.Encoding;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * XML implementation of {@link com.bluevia.android.rest.commons.parser.BlueviaEntityParser BlueviaEntityParser}
 * Class that represents the parser object for any Ad entity.
 * Using this class you will be able to parse XML documents containing
 * a representation of any received Ad entity into an instance
 * object of this entity
 *
 * @author Telefonica R&D
 * 
 */
public class XmlAdSerializer implements BlueviaEntitySerializer {

    private static final String AD_REQUEST_ID_HEADER_NAME="ad_request_id";	
    private static final String AD_SPACE_HEADER_NAME="ad_space";
    private static final String USER_AGENT_HEADER_NAME ="user_agent";
    private static final String AD_PRESENTATION_HEADER_NAME ="ad_presentation";
    private static final String AD_PRESENTATION_SIZE_HEADER_NAME="ad_presentation_size";
    private static final String AD_KEYWORDS_HEADER_NAME="keywords";
    private static final String AD_PROTECTION_POLICY_HEADER_NAME="protection_policy";
    private static final String AD_COUNTRY_HEADER_NAME="country";
    private static final String AD_TARGET_USER_ID_HEADER_NAME="target_user_id";

    private static final String AD_PRESENTATION_BANNER_VALUE = "0101";	
    private static final String AD_PRESENTATION_TEXT_VALUE = "0104";

    private static final String AD_PROTECTION_POLICY_LOW_VALUE="1";
    private static final String AD_PROTECTION_POLICY_SAFE_VALUE ="2";
    private static final String AD_PROTECTION_POLICY_HIGH_VALUE = "3";

    private static final String AD_PRESENTATION_SIZE_MMA_4_1_108_27 = "MMA_4_1:108.27";
    private static final String AD_PRESENTATION_SIZE_MMA_4_1_168_42 = "MMA_4_1:168.42";
    private static final String AD_PRESENTATION_SIZE_MMA_4_1_216_54 = "MMA_4_1:216.54";
    private static final String AD_PRESENTATION_SIZE_MMA_4_1_300_75 = "MMA_4_1:300.75";
    private static final String AD_PRESENTATION_SIZE_MMA_4_1_420_105 = "MMA_4_1:420.105";
    private static final String AD_PRESENTATION_SIZE_MMA_6_1_108_18 = "MMA_6_1:108.18";
    private static final String AD_PRESENTATION_SIZE_MMA_6_1_168_28 = "MMA_6_1:168.28";
    private static final String AD_PRESENTATION_SIZE_MMA_6_1_216_36 = "MMA_6_1:216.36";
    private static final String AD_PRESENTATION_SIZE_MMA_6_1_300_50 = "MMA_6_1:300.50";
    private static final String AD_PRESENTATION_SIZE_MMA_6_1_420_70 = "MMA_6_1:420.70";

	/**
	 * This function serializes an entity into an OutputStream.
	 * 
	 * @param entity the entity to serialize
	 * @param out the outputstream to write the serialization result to
     * @throws IOException thrown in the event of an I/O error during serialization
     * @throws ParseException thrown if the entity or output stream are unfit for serialization
     * 
	 */
    public void serialize(Entity entity, OutputStream out) throws IOException, ParseException {
        if (entity == null)
            throw new ParseException("Can not serialize null entity ");

        if (! (entity instanceof AdRequest))
            throw new ParseException("Entity class does not support serializing this entity class");

        AdRequest adRequest = (AdRequest) entity;
        List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(6);
        //Mandatory
        nameValuePairs.add(new BasicNameValuePair(AD_REQUEST_ID_HEADER_NAME, adRequest.getAdRequestId()));
        nameValuePairs.add(new BasicNameValuePair(AD_SPACE_HEADER_NAME, adRequest.getAdSpace()));
        nameValuePairs.add(new BasicNameValuePair(USER_AGENT_HEADER_NAME, "none"));
        nameValuePairs.add(new BasicNameValuePair(AD_PROTECTION_POLICY_HEADER_NAME, getProtectionPolicyCode(adRequest.getProtectionPolicy())));
        
        //Optional
        if (adRequest.getAdPresentation() != null)
            nameValuePairs.add(new BasicNameValuePair(AD_PRESENTATION_HEADER_NAME, getAdPresentationCode(adRequest.getAdPresentation())));

        if (adRequest.getAdPresentationSize() != null)
            nameValuePairs.add(new BasicNameValuePair(AD_PRESENTATION_SIZE_HEADER_NAME, getAdPresentationSizeCode(adRequest.getAdPresentationSize())));

        String composedKeywords = getComposedKeywords (adRequest.getKeywords());
        if (composedKeywords != null)
            nameValuePairs.add(new BasicNameValuePair(AD_KEYWORDS_HEADER_NAME, composedKeywords));

        if (adRequest.getCountry() != null)
        	nameValuePairs.add((new BasicNameValuePair(AD_COUNTRY_HEADER_NAME, adRequest.getCountry())));

        if (adRequest.getTargetUserId() != null)
           	nameValuePairs.add((new BasicNameValuePair(AD_TARGET_USER_ID_HEADER_NAME, adRequest.getTargetUserId())));
        
        //Finally it is not required by the gSDP
        //nameValuePairs.add(new BasicNameValuePair(AD_PROTOCOL_VERSION, "2"));

        UrlEncodedFormEntity formEncodedEntity = new UrlEncodedFormEntity(nameValuePairs);
        formEncodedEntity.writeTo(out);
    }

    /**
     * Gets the type of Encoding.
     *
     * @return the type of Encoding (APPLICATION_JSON, APPLICATION_XML, APPLICATION_URL_ENCODED)
     */
    public Encoding getEncoding() {
        return Encoding.APPLICATION_URL_ENCODED;
    }

    /**
     * Gets the Ad Presentation Code. 
     *
     * @param type Ad Presentation type. Values included: BANNER, TEXT
     * @return the Ad presentation Code
     * @throws ParseException
     */
    private String getAdPresentationCode (AdRequest.AdPresentationType type) throws ParseException {
        switch (type) {
            case BANNER:
                return AD_PRESENTATION_BANNER_VALUE;
            case TEXT:
                return AD_PRESENTATION_TEXT_VALUE;
            default:
                throw new ParseException("AdPresentation Type not supported");
        }
    }
    
    /**
     * Gets the Ad Presentation Size Code. 
     *
     * @param type Ad Presentation Size type.
     * @return the Ad presentation Size Code
     * @throws ParseException
     */
    private String getAdPresentationSizeCode(AdRequest.AdPresentationSizeType type) throws ParseException {
        switch (type) {
        case MMA_4_1_108_27:
            return AD_PRESENTATION_SIZE_MMA_4_1_108_27;
        case MMA_4_1_168_42:
        	return AD_PRESENTATION_SIZE_MMA_4_1_168_42;
        case MMA_4_1_216_54:
        	return AD_PRESENTATION_SIZE_MMA_4_1_216_54;
        case MMA_4_1_300_75:
        	return AD_PRESENTATION_SIZE_MMA_4_1_300_75;
        case MMA_4_1_420_105:
        	return AD_PRESENTATION_SIZE_MMA_4_1_420_105;
        case MMA_6_1_108_18:
        	return AD_PRESENTATION_SIZE_MMA_6_1_108_18;
        case MMA_6_1_168_28:
        	return AD_PRESENTATION_SIZE_MMA_6_1_168_28;
        case MMA_6_1_216_36:
        	return AD_PRESENTATION_SIZE_MMA_6_1_216_36;
        case MMA_6_1_300_50:
        	return AD_PRESENTATION_SIZE_MMA_6_1_300_50;
        case MMA_6_1_420_70:
        	return AD_PRESENTATION_SIZE_MMA_6_1_420_70;
        default:
            throw new ParseException("AdPresentationSize Type not supported");
        }
    }

    /**
     * Gets the Protection Policy Code. 
     *
     * @param type Protection Policy type. Values included: LOW, SAFE, HIGH
     * @return the Protection Policy Code
     * @throws ParseException
     */
    private String getProtectionPolicyCode (AdRequest.ProtectionPolicyType type) throws ParseException {
        switch (type) {
            case LOW:
                return AD_PROTECTION_POLICY_LOW_VALUE;
            case SAFE:
                return AD_PROTECTION_POLICY_SAFE_VALUE;
            case HIGH:
                return AD_PROTECTION_POLICY_HIGH_VALUE;
            default:
                throw new ParseException("PortectionPolicy Type not supported");
        }
    }

    /**
     * Gets the Composed Keywords. 
     *
     * @param keywords keyword string
     * @return the Composed Keywords
     */
    private String getComposedKeywords(String[] keywords) {
        String composedKeywords = null;
        if ((keywords != null) && (keywords.length > 0)) {
            StringBuffer aux = new StringBuffer(keywords[0]);

            for (int i = 1; i < keywords.length; i++) {
                aux.append("|").append(keywords[i]);
            }

            composedKeywords = aux.toString();
        }
        return composedKeywords;
    }
}

///@endcond