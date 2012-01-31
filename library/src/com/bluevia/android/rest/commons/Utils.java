///@cond private
package com.bluevia.android.rest.commons;

/**
 * Utils class
 */
public class Utils {

	public static boolean isEmpty(String value) {
		return (value == null || value.trim().length() == 0);
	}
	
	public static boolean isNumber(String value){
		try {
            Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
	}
	
}
///@endcond