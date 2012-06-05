package com.bluevia.android.samples;

import com.bluevia.android.commons.client.BVBaseClient.Mode;

public class ApplicationInfo {

	/*
	 * Change this constant to select other modes
	 */
	public static final Mode MODE = Mode.SANDBOX;

	// OAUTH INFO
	// The CONSUMER_KEY, CONSUMER_SECRET, OAUTH_TOKEN, OAUTH_TOKEN_SECRET static const parameters must 
	// contain real values.
	// That values could be obtained from bluevia portal using the oauth calls included in this library. 
	public static final String CONSUMER_KEY = "vw12012654505986";
	public static final String CONSUMER_SECRET = "WpOl66570544";

	public static final String OAUTH_TOKEN_KEY = "ad3f0f598ffbc660fbad9035122eae74";
	public static final String OAUTH_TOKEN_SECRET = "4340b28da39ec36acb4a205d3955a853";
	
	//SMS / MMS Keyword
	public static final String KEYWORD = "SANDBLUEDEMOS";
	
	//Advertising
	public static final String AD_SPACE = "BV15125";
	
	//Payment
	public static final String SERVICEID = "";
	public static final String SERVICENAME = "";
}
