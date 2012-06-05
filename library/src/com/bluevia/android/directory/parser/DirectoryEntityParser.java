package com.bluevia.android.directory.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.parser.ParseException;

/**
 * Interface that should be implemented by all the classes that parser an specific directory entity
 * The XMLDirectoryParser guess first the entity related to the XML received, by processing the XML root element
 * Then it delegates the parsing of the XML to an specific DirectoryEntityParser, that will parse and extract the specific
 * entity object.
 *
 * @author Telefonica I+D
 * 
     */
public interface DirectoryEntityParser {

	/**
     * Parse the next entry on the parser
     * @param parser is the parser with the entity information
     * @return the {@link com.bluevia.android.commons.Entity Entity} object
     * @throws ParseException when an error occurs converting the stream into an object
     * @throws IOException when an error reading the stream occurs
     */
    public Entity parse(XmlPullParser parser) throws ParseException;
}
