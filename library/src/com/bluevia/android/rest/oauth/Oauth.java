package com.bluevia.android.rest.oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.RequestLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.http.authentication.AuthenticationInfo;
import com.bluevia.android.rest.commons.connector.http.codec.Base64;
import com.bluevia.android.rest.oauth.data.Token;

/**
 * Class responsible for the Oauth signature method.
 * This class is used for the user to enter the strings required for authentication.
 * There are 3 parameters to create the headers in clients:
  * <ul>
 * <li> ConsumerKey. 
 * <li> ConsumerSecret. 
 * <li> AccessToken. 
 * </ul>
 *
 *
 * @author Telefonica R&D
 * 
 *
 */
public class Oauth implements AuthenticationInfo {

	///@cond private
	private static final String TAG = "OauthInfo";

	private static final String HEADER_AUTHORIZATION = "Authorization";
	
	private static final String HEADER_OAUTH_TOKEN = "oauth_token";
	private static final String HEADER_OAUTH_TOKEN_SECRET = "oauth_token_secret";
	private static final String HEADER_CONSUMER_KEY = "oauth_consumer_key";
	private static final String HEADER_CONSUMER_SECRET = "oauth_consumer_secret";
	private static final String HEADER_SIGNATURE = "oauth_signature";
	private static final String HEADER_SIGNATURE_METHOD = "oauth_signature_method";
	private static final String HEADER_NONCE = "oauth_nonce";
	public static final String HEADER_TIMESTAMP = "oauth_timestamp";
	public static final String HEADER_OAUTH_VERSION = "oauth_version";
	
	public static final String HEADER_OAUTH_CALLBACK = "oauth_callback";
	public static final String HEADER_OAUTH_VERIFIER = "oauth_verifier";
	public static final String HEADER_OAUTH_API_NAME = "xoauth_apiName";
	
	private static final String SIGNATURE_METHOD = "HMAC-SHA1";
	private static final String VERSION = "1.0";
	
	public static final String CALLBACK = "oob";

	private static final String ENCODING = "UTF-8";
	private static final String MAC_NAME = "HmacSHA1";

	private static final String FORM_ENCODED = "application/x-www-form-urlencoded";

	private String mConsumerKey;
	private String mConsumerSecret;
	private String mOauthToken;
	private String mOauthTokenSecret;
	
	private HttpQueryParams mAdditionalParameters;
	///@endcond
	
	/**
	 * Creates an Oauth info object.
	 * 
	 * @param consumerKey the consumer key of the application.
	 * @param consumerSecret the consumer secret of the application.
	 * @param accessToken the access token received after the getAccessToken request made by the user.
	 * @throws BlueviaClientException 
	 */
	public Oauth(String consumerKey, String consumerSecret, Token accessToken) throws BlueviaClientException {
		if (consumerKey == null || consumerSecret == null)
			throw new BlueviaClientException("Consumer key and consumer secret cannot be null", BlueviaClientException.BAD_REQUEST_EXCEPTION);
		mConsumerKey = consumerKey;
		mConsumerSecret = consumerSecret;
		setOauthToken(accessToken);
	}
	
	/**
	 * Creates an Oauth info object without access token.
	 * 
	 * @param consumerKey the consumer key of the application.
	 * @param consumerSecret the consumer secret of the application.
	 * @throws BlueviaClientException 
	 */
	public Oauth(String consumerKey, String consumerSecret) throws BlueviaClientException {
		this(consumerKey, consumerSecret, null);
	}
	
	///@cond private
	/**
	 * Gets the Oauth token
	 * 
	 * @return the Oauth token
	 */
	public String getOauthToken() {
		return mOauthToken;
	}

	/**
	 * Sets the Oauth token
	 * 
	 * @param oauthToken
	 */
	public void setOauthToken(Token oauthToken) {
		if (oauthToken != null){
			mOauthToken = oauthToken.getToken();
			mOauthTokenSecret = oauthToken.getSecret();
		} else {
			mOauthToken = null;
			mOauthTokenSecret = null;
		}
	}
	
	public void setAdditionalParameters(HttpQueryParams params){
		mAdditionalParameters = params;
	}
	
	/**
	 * Sets the credentials contained in the class to the request
	 * 
	 * @param httpClient The HttpClient that will send the request
	 * @param request The http request to be sent
	 */
	public void setCredentials(DefaultHttpClient httpClient, HttpRequest request){
		signRequest(request);
		//HttpClient is not used. It must be there to have a common interface
	}
	
	/**
	 * Signs the request with the HMAC-SHA1 Oauth method, using the information contained
	 * in this class.
	 * 
	 * @param request the httprequest to be signed
	 */
    public void signRequest(HttpRequest request) {
    	
    	try {
    		
    		HttpQueryParams parameters = new HttpQueryParams();
    		
            collectQueryParameters(request, parameters);
            collectBodyParameters(request, parameters);
    		
    		prepareOauthParameters(request, parameters);
    		
        	String signature = buildSignature(request, parameters);

        	//Write signature
        	StringBuilder header = new StringBuilder();
        	header.append("OAuth ");
        	
        	//Oauth parameters
        	if (mOauthToken != null)
        		header.append(HEADER_OAUTH_TOKEN + "=\"" + mOauthToken + "\", ");
        	
        	if (parameters.keySet().contains(HEADER_OAUTH_VERIFIER)){
        		header.append(HEADER_OAUTH_VERIFIER + "=\"" + parameters.getParameterValue(HEADER_OAUTH_VERIFIER) + "\", ");
        	}
        	
        	if (parameters.keySet().contains(HEADER_OAUTH_CALLBACK)){
        		header.append(HEADER_OAUTH_CALLBACK + "=\"" + parameters.getParameterValue(HEADER_OAUTH_CALLBACK) + "\", ");
        	}
        	
        	header.append(HEADER_CONSUMER_KEY + "=\"" + mConsumerKey + "\", ");
        	header.append(HEADER_SIGNATURE + "=\"" + percentEncode(signature) + "\", ");
        	header.append(HEADER_SIGNATURE_METHOD + "=\"" + getSignatureMethod() + "\", ");
        	header.append(HEADER_TIMESTAMP + "=\"" + parameters.getParameterValue(HEADER_TIMESTAMP) + "\", ");
        	header.append(HEADER_NONCE + "=\"" + parameters.getParameterValue(HEADER_NONCE) + "\", ");
        	header.append(HEADER_OAUTH_VERSION +  "=\"" + VERSION + "\" ");
        	
        	//Additional parameters
        	if (mAdditionalParameters != null){
	        	for (String name : mAdditionalParameters.keySet()){
	        		if (header.lastIndexOf(name) == -1)
	        			header.append(", " + name + "=\"" + mAdditionalParameters.getParameterValue(name) + "\"");
	        	}
        	}
        	request.addHeader(HEADER_AUTHORIZATION, header.toString());
    	} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage());
		}
    	
    }
    
    private void collectQueryParameters(HttpRequest request, HttpQueryParams params){
        String url = request.getRequestLine().getUri();
        int q = url.indexOf('?');
        if (q >= 0) {
            // Combine the URL query string with the other parameters:
        	params.putAll(decodeForm(url.substring(q + 1)));
        }
    }
    
    protected void collectBodyParameters(HttpRequest request, HttpQueryParams params) {

		// collect x-www-form-urlencoded body params
    	if (request.getRequestLine().getMethod().equals("POST")){
    		try {
	    		HttpEntity httpEntity = ((HttpPost) request).getEntity();
	    		if (httpEntity != null){
		    		String contentType = httpEntity.getContentType().getValue();
		    		if (contentType != null && contentType.startsWith(FORM_ENCODED)) {
		    		    InputStream payload = httpEntity.getContent();
		    		    params.putAll(decodeForm(payload));
		    		}
	    		}
	    	} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
			}
    	}
		
	}
    
    private void prepareOauthParameters(HttpRequest request, HttpQueryParams params){
    	
    	if (mAdditionalParameters != null){
    		params.putAll(mAdditionalParameters);
    	}
    	
    	if (mOauthToken != null)
        	params.addParameter(HEADER_OAUTH_TOKEN, mOauthToken);
    	
    	if(!params.keySet().contains(HEADER_TIMESTAMP))
    		params.addParameter(HEADER_TIMESTAMP, String.valueOf(System.currentTimeMillis()/1000));
    	
    	params.addParameter(HEADER_CONSUMER_KEY, mConsumerKey);
    	params.addParameter(HEADER_SIGNATURE_METHOD, getSignatureMethod());
    	params.addParameter(HEADER_NONCE, String.valueOf(new Random().nextLong()));
    	params.addParameter(HEADER_OAUTH_VERSION, VERSION);
    }
    
	private String buildSignature(HttpRequest request, HttpQueryParams parameters) {
		
		String signature = "";
		try {
			String keyString = buildKey(mConsumerSecret, mOauthTokenSecret);
			SecretKey key = new SecretKeySpec(keyString.getBytes(ENCODING), MAC_NAME);
			Mac mac = Mac.getInstance(MAC_NAME);
			mac.init(key);
			
			String sbs = buildSignatureBaseString(request, parameters);
			byte[] text = sbs.getBytes(ENCODING);
			signature = new String(Base64.encodeBase64(mac.doFinal(text))).trim();

	    	Log.d(TAG, signature);
			
		} catch (GeneralSecurityException e) {
			Log.e(TAG, e.getMessage());
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage());
		}
		
		return signature;
	}
	
	/**
	 * Builds the key
	 * 
	 * @param consumerSecret
	 * @param tokenSecret
	 * @return
	 * @throws BlueviaClientException
	 */
	private String buildKey(String consumerSecret, String tokenSecret) {
		StringBuilder sb = new StringBuilder();
		String out = "";
		try {
			sb.append(percentEncode(consumerSecret));
			sb.append("&");
			if (tokenSecret != null)
				sb.append(percentEncode(tokenSecret));
			out = sb.toString();
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getLocalizedMessage());
		}
		return out;
	}
	
	/**
	 * TODO
	 * 
	 * @param request
	 * @return
	 * @throws BlueviaClientException
	 */
	private String buildSignatureBaseString(HttpRequest request, HttpQueryParams parameters) {
		String sbs = "";
		try {
	    	RequestLine requestLine = request.getRequestLine();
	    	
	    	StringBuilder sb = new StringBuilder();
	    	
	    	//Add method
	    	sb.append(requestLine.getMethod().toUpperCase());
	    	sb.append("&");
	    	
	    	//Add normalized uri
	    	String url = request.getRequestLine().getUri();
	        int q = url.indexOf('?');
	        if (q >= 0) {
	            // Combine the URL query string with the other parameters:
	        	sb.append(percentEncode(url.substring(0, q)));
	        } else sb.append(percentEncode(url));
	    	sb.append("&");
	    	
	    	//Add normalized parameters
	    	sb.append(percentEncode(getNormalizedParameters(parameters)));
	    	
	    	
	    	sbs = sb.toString();
	    	
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage());
		}
		return sbs;
	}
	
	private String getNormalizedParameters(HttpQueryParams parameters) throws UnsupportedEncodingException{
		StringBuilder sb = new StringBuilder();
		
		String[] set = new String[parameters.keySet().size()];
		set = (String[]) parameters.keySet().toArray(set);
		Arrays.sort(set);
		for (int i = 0; i<set.length; i++) {
	        String param = set[i];
	        if (HEADER_SIGNATURE.equals(param) || "realm".equals(param)) {
	            continue;
	        }
	        if (i > 0) {
	            sb.append("&");
	        }
	        sb.append(param);
	        sb.append("=");
	        sb.append(percentEncode(parameters.getParameterValue(param)));
	    }
		return sb.toString();
	}
	
	/**
	 * Gets the signature method
	 * 
	 * @return the signature method
	 */
	public static String getSignatureMethod() {
		return SIGNATURE_METHOD;
	}
	
	public static String percentEncode(String input) throws UnsupportedEncodingException{
		
		// RFC3986 compilant
		
		StringBuilder sb = new StringBuilder();
		
		for (int i=0; i<input.length(); i++) {
			if ((48 <= input.charAt(i) && input.charAt(i) <= 57) ||  //0-9
					(65 <= input.charAt(i) && input.charAt(i) <= 90) ||  //abc...xyz
					(97 <= input.charAt(i) && input.charAt(i) <= 122) || //ABC...XYZ
					(input.charAt(i) == '-' || input.charAt(i) == '.' || 
					input.charAt(i) == '_' || input.charAt(i) == '~'))  {

				sb.append(input.charAt(i));

			} else {
            	sb.append( "%" );
            	String hex = Integer.toHexString((int) input.charAt(i)).toUpperCase();
            	sb.append(hex); //converts char 255 to string "FF" (in uppercase)
            }
		}
		
		return sb.toString();
	}
	
	/** Parse a form-urlencoded document. */
    public static HttpQueryParams decodeForm(String form) {
    	HttpQueryParams params = new HttpQueryParams();
        if (form == null || form.length() == 0) {
            return params;
        }
        for (String nvp : form.split("\\&")) {
            int equals = nvp.indexOf('=');
            String name;
            String value;
            if (equals < 0) {
                name = percentDecode(nvp);
                value = null;
            } else {
                name = percentDecode(nvp.substring(0, equals));
                value = percentDecode(nvp.substring(equals + 1));
            }

            params.addParameter(name, value);
        }
        return params;
    }
    
    public static HttpQueryParams decodeForm(InputStream content) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		StringBuilder sb = new StringBuilder();
		String line = reader.readLine();
		while (line != null) {
		    sb.append(line);
		    line = reader.readLine();
		}
	
	return decodeForm(sb.toString());
	}
    
    public static String percentDecode(String s) {
        try {
            if (s == null) {
                return "";
            }
            return URLDecoder.decode(s, ENCODING);
            // This implements http://oauth.pbwiki.com/FlexibleDecoding
        } catch (java.io.UnsupportedEncodingException wow) {
            throw new RuntimeException(wow.getMessage(), wow);
        }
    }
    
    ///@endcond
	
}