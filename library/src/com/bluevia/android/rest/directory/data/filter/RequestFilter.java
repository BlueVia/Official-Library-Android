///@cond private
package com.bluevia.android.rest.directory.data.filter;

import java.util.Enumeration;
import java.util.Hashtable;

import com.bluevia.android.rest.commons.Entity;

/**
 * Abstract class which all request filter classes should extend
 * @author Telefonica R&D
 * 
*/
public abstract class RequestFilter implements Entity {

    protected Hashtable<String, Boolean> filters = new Hashtable<String,Boolean>();

    /**
     * Add a new filter to the filter record
     * @param key the filter key as required by the gSDP
     * @param value true to activate the filter, false to deactivate
     */
    protected final void setFilter(String key, boolean value){
        filters.put(key, value);
    }

    /**
     * Get the value of the filter for a property
     * @param key
     */
    protected final boolean getFilter (String key){
        if (filters.get(key) != null){
            return filters.get(key);
        } else {
            return false;
        }
    }

    /**
     * Get a string with the keys of all activated filters
     * @return A comma-separated list of all the activated filters
     */
    public final String getActivatedFilters(){
        Enumeration<String> keys = filters.keys();
        Enumeration<Boolean> values = filters.elements();
        StringBuffer sb = new StringBuffer(); 
        String activatedFilters = null;
        while (keys.hasMoreElements() && values.hasMoreElements()){
            String key = keys.nextElement();
            Boolean value = values.nextElement();
            if (value){
                sb.append(key);
                sb.append(",");
            }
        }
        activatedFilters = sb.toString();
        if (activatedFilters.endsWith(",")){
            activatedFilters = activatedFilters.substring(0,activatedFilters.length()-1);
        }
        return activatedFilters;
    }

	/* (non-Javadoc)
	 * @see com.bluevia.android.rest.commons.Entity#isValid()
	 */
	public boolean isValid() {
		return !filters.isEmpty();
	}
    
}

///@endcond
