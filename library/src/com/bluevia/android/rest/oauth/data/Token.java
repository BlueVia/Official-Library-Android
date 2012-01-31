/**
 * \package com.bluevia.android.rest.oauth.data This package contains entities to hold data related to oauth token request methods
 */
package com.bluevia.android.rest.oauth.data;

import com.bluevia.android.rest.commons.Entity;

/**
 * Object that represents an token with the information yielded by the gSDP in response to a "request token" http request.
 * It includes the following attributes:
 * <ul>
 * <li>Oauth token: The token identifier. It is returned by the service provider </li>
 * <li>Oauth token Secret: The token shared-secret.</li>
 * <li>Oauth Callback Confirmed: It must be present and set to true. The Consumer may use this to confirm 
 * that the Service Provider received the callback value.</li>
 * </ul>
 *
 * @author Telefonica R&D
 * 
 */
public class Token implements Entity{
	
	///@cond private
	protected String mOauthToken;
   
	protected String mOauthTokenSecret;
	
	protected Boolean mOauthCallbackConfirmed;
	
	///@endcond
	
	/**
	 * Instantiates the Token with its parameters
	 * 
	 * @param oauthToken the oauth_token identifier
	 * @param oauthTokenSecret the oauth shared-secret
	 */
	public Token(String oauthToken, String oauthTokenSecret){
		this.mOauthToken = oauthToken;
		this.mOauthTokenSecret = oauthTokenSecret;
	}
	
	/**
	 * Get Oauth Token
	 * 
	 * @return the oauth_token property
	 */
	public String getToken() {
		return mOauthToken;
	}

	/** 
	 * Get Oauth Token Secret
	 * 
	 * @return the Oauth Token Secret property
	 */
	public String getSecret() {
		return mOauthTokenSecret;
	}

	/** 
	 * Set Oauth Callback Confirmed
	 * 
	 * @param oauthCallbackConfirmed the oauth_callback_confirmed property value to set
	 */
	public void setOauthCallbackConfirmed(Boolean oauthCallbackConfirmed) {
		mOauthCallbackConfirmed = oauthCallbackConfirmed;
	}
	
	/**
	 * Gets the Oauth Callback Confirmed
	 * 
	 * @return Oauth Callback Confirmed
	 */
	public Boolean getOauthCallbackConfirmed(){
		return mOauthCallbackConfirmed;
	}

	/** 
	 * 	Checks that the token is valid. 
	 * 
	 * 	@return true if the Token seems to be valid, false otherwise
	 */
	public boolean isValid(){
		return true;
	}
	
}
