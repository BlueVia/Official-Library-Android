///@cond private
package com.bluevia.android.rest.oauth.parser.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParser;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.oauth.data.Token;


/**
 * XML implementation of {@link com.bluevia.android.rest.commons.parser.BlueviaEntityParser BlueviaEntityParser}
 * Class that represents the parser object for any Oauth entity.
 * Using this class you will be able to parse XML documents containing
 * a representation of any Oauth entity into an instance
 * object of this entity
 *
 * @author Telefonica R&D
 * 
 */
public class XmlOauthParser implements BlueviaEntityParser {

	/**
     * @see {@link com.bluevia.android.rest.commons.parser.BlueviaEntityParser com.bluevia.android.rest.parser.BlueviaEntityParser#parseEntry()}
     */
	public Entity parseEntry(InputStream is) throws ParseException, IOException {
		String responseString = convertStreamToString(is);
		
		// This implementation of the parser is very handcrafted and ad-hoc for the 
		// format we think the server will return based on the examples provided
		// by the documentation. In should be revised whenever a working server is 
		// available to check actual responses.
		
		//Split the string by the & symbol
		StringTokenizer responseParts = new StringTokenizer(responseString,"&");
		HashMap<String,String> responseTokens = new HashMap<String,String>();
		
		while(responseParts.hasMoreTokens()){
			String part = responseParts.nextToken();
			//Split each parameter in key/value pairs
			String [] subParts = part.split("=");
			if (subParts.length != 2){
				throw new ParseException("Unexpected format in authentication server response");
			}
			responseTokens.put(subParts[0], subParts[1]);
		}
		
		//Check whether the expected parameters were returned
		if (!responseTokens.containsKey("oauth_token")||
				!responseTokens.containsKey("oauth_token_secret")){
			throw new ParseException("Unexpected format in authentication server response");
		}
		
		//Fill the token
		Token token = new Token(responseTokens.get("oauth_token"), responseTokens.get("oauth_token_secret"));
		
		if (responseTokens.containsKey("oauth_callback_confirmed")){
			token.setOauthCallbackConfirmed(responseTokens.get("oauth_callback_confirmed").equals("true"));
		}
		
		return token;
	}
	
	
	private String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
		     StringBuilder sb = new StringBuilder();
		     String line;
		     try {
		    	 BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		    	 while ((line = reader.readLine()) != null) {
		    		 //sb.append(line).append("\n");
		    		 sb.append(line);
		    	 }
		     } finally {
		    	 is.close();
			}
			return sb.toString();
		} else {       
			return "";
		}
	}

}
///@endcond