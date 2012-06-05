/**
 * \package com.bluevia.android.ad.client This package contains the clients of Bluevia Advertisement Service
 */
package com.bluevia.android.ad.client;

import java.io.IOException;
import java.util.HashMap;

import com.bluevia.android.ad.data.AdRequest;
import com.bluevia.android.ad.data.AdRequest.Type;
import com.bluevia.android.ad.data.AdResponse;
import com.bluevia.android.ad.data.AdsAttribute;
import com.bluevia.android.ad.data.FullCreativeElement;
import com.bluevia.android.ad.data.Resource;
import com.bluevia.android.ad.data.simple.CreativeElement;
import com.bluevia.android.ad.data.simple.SimpleAdResponse;
import com.bluevia.android.ad.parser.url.UrlEncodedAdSerializer;
import com.bluevia.android.ad.parser.xml.XmlAdParser;
import com.bluevia.android.commons.Encoding;
import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.Utils;
import com.bluevia.android.commons.client.BVBaseClient;
import com.bluevia.android.commons.connector.http.oauth.IOAuth;
import com.bluevia.android.commons.exception.BlueviaException;
import com.bluevia.android.commons.parser.xml.BVRestXmlErrorParser;
import com.bluevia.android.commons.parser.xml.XmlConstants;

/**
 *  This class provides access to the set of functions which any user could use to access
 *  the Advertising service functionality
 *
 * @author Telefonica R&D
 *
 */
public abstract class BVAdvertisingClient extends BVBaseClient {

	protected static final String FEED_AD_REQUESTS_URI = "/simple/requests";

	protected void init(){
		mEncoding = Encoding.APPLICATION_URL_ENCODED;
		mParser = new XmlAdParser();
		mSerializer = new UrlEncodedAdSerializer();
		mErrorParser = new BVRestXmlErrorParser();
	}

	/**
	 * Requests the retrieving of an advertisement.
	 * 
	 * @param adSpace the adSpace of the Bluevia application (mandatory)
	 * @param country country where the target user is located. Must follow ISO-3166 (see http://www.iso.org/iso/country_codes.htm).
	 * (Optional: the parameter should not be included if the target user can be deduced by other means, for example, Oauth 3-legged or Oauth 2-legged with requestorID).
	 * @param targetUserId Identifier of the Target User. 
	 * (Optional: the parameter should not be included if the target user can be deduced by other means, for example, Oauth 3-legged or Oauth 2-legged with requestorID).
	 * @param adRequestId an unique id for the request. (optional: if it is not set, the SDK will generate it automatically)
	 * @param adPresentation the value is a code that represents the ad format type (optional)
	 * @param keywords the keywords the ads are related to (optional)
	 * @param protectionPolicy the adult control policy. It will be safe, low, high. It should be checked with the application SLA in the gSDP (optional)
	 * @param userAgent the user agent of the client (optional)
	 * @return the result returned by the server that contains the ad meta-data
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected AdResponse getSimpleAd(String adSpace, String country, String targetUserId, String adRequestId, AdRequest.Type adPresentation, String[] keywords, AdRequest.ProtectionPolicyType protectionPolicy, String userAgent)
			throws BlueviaException, IOException {
		
		AdResponse adResponse = null;

		// Mandatory parameter validation
		if (Utils.isEmpty(adSpace))
			throw new BlueviaException("Bad request: adSpace cannot be null nor empty", BlueviaException.BAD_REQUEST_EXCEPTION);

		String token = null;
		if (((IOAuth)mConnector).getOauthToken() != null)
			token = ((IOAuth)mConnector).getOauthToken().getToken();
		
		AdRequest request = new AdRequest(adSpace, adRequestId, token, adPresentation,
				userAgent, keywords, protectionPolicy, country, targetUserId);

		if (!request.isValid())
			throw new BlueviaException("Bad request. Please, check function parameters",
					BlueviaException.BAD_REQUEST_EXCEPTION);

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		Entity response = this.create("", request, parameters, null);

		if ((response == null) || (!(response instanceof AdResponse)))
			throw new BlueviaException("Error during request. Response received does not correspond to an AdResponse", BlueviaException.INTERNAL_CLIENT_ERROR);

		adResponse = (AdResponse) response;

		return adResponse;
	}

	/**
	 * Returns a simple ad response with the usefull information
	 * 
	 * @param response
	 * @return
	 */
	protected SimpleAdResponse simplifyResponse(AdResponse response){

		if (response == null)
			return null;

		SimpleAdResponse simpleResponse = new SimpleAdResponse();

		//Ad request id
		simpleResponse.setRequestId(response.getId());

		//Elements
		Resource resource = response.getAd().getResource();
		if (resource == null)
			return simpleResponse;

		for (FullCreativeElement e : resource.getCreativeElement()){
			
			if (e.getType() != null && 
					e.getAdsAttribute() != null && !e.getAdsAttribute().isEmpty() && 
					e.getInteraction() != null && !e.getInteraction().isEmpty() &&
					e.getInteraction().get(0).getAttribute() != null && !e.getInteraction().get(0).getAttribute().isEmpty()) {

				//Set type
				Type type = translateType(e.getType());

				//Set value
				String value = null;
				for (AdsAttribute attr : e.getAdsAttribute()){
					if (attr.getType() == AdsAttribute.Type.ADTEXT || attr.getType() == AdsAttribute.Type.LOCATOR)
						value = attr.getValue();
				}
				
				String interaction = null;
				for (AdsAttribute attr : e.getInteraction().get(0).getAttribute()){
					if (attr.getType() == AdsAttribute.Type.URL){
						interaction = attr.getValue();
					}
				}

				simpleResponse.addAdvertising(new CreativeElement(type, value, interaction));
			}
					
		}

		return simpleResponse;
	}

	/**
	 * Translates from FullCreativeElement.Type to AdRequest.Type
	 * 
	 * @param type the FullCreativeElement.Type representation
	 * @return the AdRequest.Type representation of the type received
	 */
	private AdRequest.Type translateType(FullCreativeElement.Type type){
		switch (type) {
		case IMAGE:
			return Type.IMAGE;
		case TEXT:
			return Type.TEXT;
		default:
			return null;
		}
	}
	
}
