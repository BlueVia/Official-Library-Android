/**
 * \package com.bluevia.android.rest.commons This package contains classes with common functionallity to be used by all other APIs.
 */
package com.bluevia.android.rest.commons;


///@cond private

/**
 * Common interface for all entities that are sent or received via Bluevia
 * HTTP REST API
 *
 * @author Telefonica R&D
 * 
 */
public interface Entity {

	/**
     * Validates the entity data following Bluevia rules
     * 
	 * @return boolean result of the check
     */
    public boolean isValid();
}

///@endcond