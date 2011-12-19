package com.bluevia.android.rest.commons.connector.http.authentication;

import org.apache.http.HttpRequest;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Abstract class to be extended by the different authentication information classes
 * 
 * @author Telefonica R&D
 * 
 */
public interface AuthenticationInfo {
	
	/**
	 * Sets the credentials contained in the class to the request
	 * 
	 * @param httpClient The HttpClient that will send the request
	 * @param request The http request to be sent
	 */
	public void setCredentials(DefaultHttpClient httpClient, HttpRequest request);
	
}
