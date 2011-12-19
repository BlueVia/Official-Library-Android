package com.bluevia.android.rest.commons.connector.http.authentication;

import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Class to hold basic html authentication information: user // password 
 *
 */
public class BasicAuthInfo implements AuthenticationInfo {
	
	private String mUser;
	private String mPassword;
	
	/**
	 * Creates a new basic authentication information object
	 * 
	 * @param user the user name
	 * @param password the password for user
	 */
	public BasicAuthInfo(String user, String password){
		mUser = user;
		mPassword = password;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.connector.http.authentication.AuthenticationInfo#setCredentials(org.apache.http.impl.client.DefaultHttpClient, org.apache.http.HttpRequest)
	 */
	public void setCredentials(DefaultHttpClient httpClient, HttpRequest request) {
		httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, getCredentials());
		//HttpRequest is not used. It must be there to have a common interface
		
	}
	
	///@cond private
	/**
	 * Returns the credentials associated to this user&password set
	 * 
	 * @return the credentials associated to this user&password set
	 */
	private UsernamePasswordCredentials getCredentials(){
		return new UsernamePasswordCredentials(mUser, mPassword);
	}
	///@endcond
}
