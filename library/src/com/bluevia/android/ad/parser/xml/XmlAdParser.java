package com.bluevia.android.ad.parser.xml;


import java.io.InputStream;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.parser.IParser;
import com.bluevia.android.commons.parser.ParseException;

/**
 * XML implementation of {@link com.bluevia.android.commons.parser.IParser IParser}
 * Class that represents the parser object for any Ad entity.
 * Using this class you will be able to parse XML documents containing
 * a representation of any Ad entity into an instance
 * object of this entity
 *
 * @author Telefonica R&D
 * 
 */
public class XmlAdParser implements IParser {

    public Entity parse(InputStream is) throws ParseException {
        return new XmlAdResponseParser().parse(is);
    }
}

