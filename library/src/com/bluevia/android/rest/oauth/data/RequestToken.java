package com.bluevia.android.rest.oauth.data;


/**
 * This object extends the Token class to include the URL to verify the Request Token. 
 *
 * @author Telefonica R&D
 * 
 */
public class RequestToken extends Token {
	
	///@cond private
	private String mVerificationUrl;
	
	private static final String VERIFICATION_URL = "https://connect.bluevia.com/authorise?oauth_token=";
	///@endcond
	
	/**
	 * Instantiates the Token with its parameters
	 * 
	 * @param oauthToken the oauth_token identifier
	 * @param oauthTokenSecret the oauth shared-secret
	 */
	public RequestToken(String oauthToken, String oauthTokenSecret) {
		super(oauthToken, oauthTokenSecret);
		
		buildVerificationUrl();
	}
	
	/**
	 * Instantiates the Token with it parameter
	 * 
	 * @param token
	 */
	public RequestToken(Token token){
		super(token.getToken(), token.getSecret());
		setOauthCallbackConfirmed(token.getOauthCallbackConfirmed());
		buildVerificationUrl();
	}
	
	/**
	 * Gets the verification url
	 * 
	 * @return The verification url
	 */
	public String getVerificationUrl(){
		return mVerificationUrl;
	}
	
	/**
	 * Sets the verification url
	 * 
	 * @param url the verification url
	 */
	public void setVerificationUrl(String url){
		mVerificationUrl = url;
	}
	
	/** 
	 * 	Checks that the token is valid. 
	 * 	@return true if the Token seems to be valid, false otherwise
	 */
	@Override
	public boolean isValid(){
		return (mOauthCallbackConfirmed != null && mOauthCallbackConfirmed == true);
	}
	
	
	///@cond private
	private void buildVerificationUrl(){
		mVerificationUrl = VERIFICATION_URL + mOauthToken;
	}
	///@endcond

}
