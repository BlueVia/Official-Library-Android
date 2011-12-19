/**
 * \package com.bluevia.android.rest.ad This package contains the classes in order to get Advertisements using Bluevia APIs.
 * \package com.bluevia.android.rest.ad.client This package contains the client of Bluevia Advertisement Service
 */
package com.bluevia.android.rest.ad.client;


import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.bluevia.android.rest.ad.data.AdRequest;
import com.bluevia.android.rest.ad.data.AdResponse;
import com.bluevia.android.rest.ad.data.AdRequest.AdPresentationSizeType;
import com.bluevia.android.rest.ad.parser.xml.XmlAdParserFactory;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.client.AbstractRestClient;
import com.bluevia.android.rest.commons.connector.IConnector;
import com.bluevia.android.rest.commons.connector.http.HttpConnector;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.mock.BlueviaAdMockConnector;
import com.bluevia.android.rest.commons.parser.xml.XmlConstants;
import com.bluevia.android.rest.oauth.Oauth;
import com.bluevia.android.rest.oauth.data.Token;

/**
 * @class BlueviaAdClient
 * 
 *  This class provides access to the set of functions which any user could use to access
 *  the Advertising service functionality
 *
 * @author Telefonica R&D
 * 
 */
public class BlueviaAdClient extends AbstractRestClient {

	/// @cond private
	
    private static final String FEED_AD_BASE_URI = "REST/Advertising/simple/requests";
    private static final String FEED_AD_SANDBOX_BASE_URI = "REST/Advertising_Sandbox/simple/requests";
    
    /// @endcond
    
    /**
     * Creates an AdClient object to be able to get Ads from the gSDP
     * @param context the Android context of the application
     * @param mode the communication mechanism to communicate with the gSDP
     * @param consumerKey The oauth consumer key supplied by the developer.
     * @param consumerSecret The oauth consumer secret supplied by the developer.
     * @param accessToken The oauth access token returned by the getAccessToken call.
     * 
     * @throws BlueviaClientException if an exception occurs during the request.
     */
    public BlueviaAdClient(Context context, AbstractRestClient.Mode mode, String consumerKey, String consumerSecret, Token accessToken) throws BlueviaClientException {
        super(context, (mode != Mode.HTTP_SANDBOX) ? FEED_AD_BASE_URI : FEED_AD_SANDBOX_BASE_URI, 
        		(mode == Mode.HTTP || mode == Mode.HTTP_SANDBOX) ? new HttpConnector() : new BlueviaAdMockConnector(mode.ordinal()), 
        				new Oauth(consumerKey, consumerSecret, accessToken), new XmlAdParserFactory());
    }
    
    /**
     * Creates an AdClient object to be able to get Ads from the gSDP.
     * Access token is not needed (oauth-2-legged, only for trusted applications).
     * 
     * @param context the Android context of the application
     * @param mode the communication mechanism to communicate with the gSDP
     * @param consumerKey The oauth consumer key supplied by the developer.
     * @param consumerSecret The oauth consumer secret supplied by the developer.
     * 
     * @throws BlueviaClientException if an exception occurs during the request.
     */
    public BlueviaAdClient(Context context, AbstractRestClient.Mode mode, String consumerKey, String consumerSecret) throws BlueviaClientException {
        super(context, (mode != Mode.HTTP_SANDBOX) ? FEED_AD_BASE_URI : FEED_AD_SANDBOX_BASE_URI, 
        		(mode == Mode.HTTP || mode == Mode.HTTP_SANDBOX) ? new HttpConnector() : new BlueviaAdMockConnector(mode.ordinal()), 
        				new Oauth(consumerKey, consumerSecret), new XmlAdParserFactory());
    }
    
    /**
     * Requests the retrieving of an advertisement. This method can only be used with Oauth 3-legged client.
     * 
     * @param adSpace the adSpace of the Bluevia application (mandatory)
     * @param adRequestId an unique id for the request. (optional: if it is not set, the SDK will generate it automatically)
     * @param protectionPolicy the adult control policy. It will be safe, low, high. It should be checked with the application SLA in the gSDP (mandatory)
     * @param adPresentation the value is a code that represents the ad format type (optional)
     * @param adPresentationSize he value is a code that represents the ad size (optional)
     * @param keywords the keywords the ads are related to (optional)
     * 
     * @return the result returned by the server that contains the ad meta-data
     * @throws BlueviaClientException
     */
    public AdResponse simpleAd(String adSpace, String adRequestId, AdRequest.AdPresentationType adPresentation,
            AdPresentationSizeType adPresentationSize, String[] keywords, AdRequest.ProtectionPolicyType protectionPolicy)
            throws BlueviaClientException {
    	return simpleAd(adSpace, null, adPresentation, adPresentationSize, keywords, protectionPolicy, null, null);
    }
    
    /**
     * Requests the retrieving of an advertisement.
     * 
     * @param adSpace the adSpace of the Bluevia application (mandatory)
     * @param adRequestId an unique id for the request. (optional: if it is not set, the SDK will generate it automatically)
     * @param adPresentation the value is a code that represents the ad format type (optional)
     * @param adPresentationSize he value is a code that represents the ad size (optional)
     * @param keywords the keywords the ads are related to (optional)
     * @param protectionPolicy the adult control policy. It will be safe, low, high. It should be checked with the application SLA in the gSDP (mandatory)
     * @param country country where the target user is located. Must follow ISO-3166 (see http://www.iso.org/iso/country_codes.htm).
     * (Optional: the parameter should not be included if the target user can be deduced by other means, for example, Oauth 3-legged or Oauth 2-legged with requestorID).
     * @param targetUserId Identifier of the Target User. 
     * (Optional: the parameter should not be included if the target user can be deduced by other means, for example, Oauth 3-legged or Oauth 2-legged with requestorID).
     * @return the result returned by the server that contains the ad meta-data
     * @throws BlueviaClientException
     */
    public AdResponse simpleAd(String adSpace, String adRequestId, AdRequest.AdPresentationType adPresentation, 
    		AdPresentationSizeType adPresentationSize, String[] keywords, AdRequest.ProtectionPolicyType protectionPolicy, String country, String targetUserId)
            throws BlueviaClientException {
        AdResponse adResponse = null;

        // Mandatory parameter validation
        if ((adSpace == null) || (adSpace.trim().length() == 0)) /* any other specific format for requestid to validate?*/
            throw new BlueviaClientException(
                    "Remember adSpace is mandatory and cannot be nor null nor empty",
                    BlueviaClientException.BAD_REQUEST_EXCEPTION);

        try {
            AdRequest request = new AdRequest(adSpace, adRequestId, ((Oauth)mAuthInfo).getOauthToken(), adPresentation,
                    adPresentationSize, keywords, protectionPolicy, country, targetUserId);

            if (!request.isValid())
                throw new BlueviaClientException("Invalid parameters. Please, check function parameters",
                        BlueviaClientException.BAD_REQUEST_EXCEPTION);

            HttpQueryParams parameters = new HttpQueryParams();
            parameters.addParameter(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);
            
            Entity response = this.createEntity(mBaseUri, request, parameters);

            if ((response == null) || (!(response instanceof AdResponse)))
                throw new BlueviaClientException(
                        "Error during request. Response received does not correspond to an AdResponse",
                        BlueviaClientException.INTERNAL_CLIENT_ERROR);

            adResponse = (AdResponse) response;
            
        } catch (IOException iox) {
            throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);
        }

        return adResponse;
    }

    /**
     * Gets the media content associated to the media URl
     * @param uri the uri of the media to get
     * @return an InputStrem with the media bytes
     */
    public InputStream getMediaContent (String uri) throws BlueviaClientException {
        try {
            return mConnector.getMediaContentAsStream(uri);
        } catch (IOException iox) {
            throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);
        }
    }
    
    ///@cond private
    /**
     * Creates an AdClient with a specific IConnector object
     * 
     * @param context the Android context of the application
     * @param connector the connector for communication mechanism
     * @param consumerKey The oauth consumer key supplied by the developer.
     * @param consumerSecret The oauth consumer secret supplied by the developer.
     * @param accessToken The oauth access token returned by the getAccessToken call.
     * 
     * @throws BlueviaClientException if an exception occurs during the request.
     */
    protected BlueviaAdClient(Context context, IConnector connector, String consumerKey, String consumerSecret, Token accessToken) throws BlueviaClientException {
    	super(context, FEED_AD_BASE_URI, connector,new Oauth(consumerKey, consumerSecret, accessToken), new XmlAdParserFactory());
    }
    
    /**
     * Creates an AdClient with a specific IConnector object and no oauth token (oauth 2-legged)
     * 
     * @param context the Android context of the application
     * @param connector the connector for communication mechanism
     * @param consumerKey The oauth consumer key supplied by the developer.
     * @param consumerSecret The oauth consumer secret supplied by the developer.
     * 
     * @throws BlueviaClientException if an exception occurs during the request.
     */
    protected BlueviaAdClient(Context context, IConnector connector, String consumerKey, String consumerSecret) throws BlueviaClientException {
    	super(context, FEED_AD_BASE_URI, connector,new Oauth(consumerKey, consumerSecret), new XmlAdParserFactory());
    }
    ///@endcond
}
