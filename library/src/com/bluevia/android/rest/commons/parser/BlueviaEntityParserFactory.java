///@cond private
/**
 * \package com.bluevia.android.rest.commons.parser This package contains basic classes to create factories to parse and serialize entities to any format (XML, JSON, etc.) and vice-versa for Bluevia APIs.
 */
package com.bluevia.android.rest.commons.parser;

/**
 * Interface that represents the factory to create a parser and a serializer
 * for an entity.
 *
 * @author Telefonica R&D
 * 
 */
public interface BlueviaEntityParserFactory {
    /**
     * Creates a parser for the entity
     * @return BlueviaEntityParser
     */
    public BlueviaEntityParser createParser();

    /**
     * Creates a serializer for the entity
     * @return BlueviaEntitySerializer
     */
    public BlueviaEntitySerializer createSerializer();
}
///@endcond