///@cond private
package com.bluevia.android.rest.commons.connector.http;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

/**
 * HttpQueryParams allows to create parameters in the HTTP URI request
 *
 * @author Telefonica R&D
 * 
     */
public class HttpQueryParams {

    private HashMap<String, String> mParameters;

    /**
     * Class Contructor
     */
    public HttpQueryParams() {
        this.mParameters = new HashMap<String, String>(10); //We do not expect much parameters
    }

    /**
     * Adds a parameter into the url query string
     * @param name the name of the paparmeter
     * @param value the value of the parameter
     */
    public void addParameter (String name, String value) {
        if (name != null && value != null) {
            if (!mParameters.containsKey(name)) {
                mParameters.put(name, value);
            } else {
                mParameters.remove(name);
                mParameters.put(name, value);
            }
        }
    }

    /**
     * Generates the url query string including the parameters
     * @param url the base url
     * @return the url including the query parameters
     * @throws UnsupportedEncodingException 
     */
    public String generateQueryUrl (String url) throws UnsupportedEncodingException {
        StringBuffer res = new StringBuffer(url);
        res.append(url.indexOf('?') >= 0 ? '&' : '?');

        Set<String> keys = mParameters.keySet();
        int i = 0;
        for (String name : keys) {
          if (i > 0) {
            res.append('&');
          }
          res.append(encodeUri(name)).append('=');
          res.append(encodeUri(getParameterValue(name)));
          i++;
        }
        return res.toString();
    }

    /**
     * Gets the parameter value
     * @param name the name of the parameter
     * @return the value of the parameter
     */
    public String getParameterValue (String name) {
        return mParameters.get(name);
    }
    
    /**
     * Returns a set of keys of the http params.
     * 
     * @return a set of keys of the http params
     */
    public Set<String> keySet(){
    	return mParameters.keySet();
    }

    /**
     * Clear all the parameters
     */
    public void clear() {
        mParameters.clear();
    }

    private String encodeUri(String uri) throws UnsupportedEncodingException {
        String encodedUri;
        try {
            encodedUri = URLEncoder.encode(uri, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            // should not happen.
            Log.e("HttpQueryParams",
                  "UTF-8 not supported -- should not happen.  "
                  + "Using default encoding.", uee);
            encodedUri = URLEncoder.encode(uri, "UTF-8");
        }
        return encodedUri;
    }
    
    public void putAll(HttpQueryParams params){
    	mParameters.putAll(params.mParameters);
    }


}

///@endcond