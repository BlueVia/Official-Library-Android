package com.bluevia.android.commons.parser;

import java.io.IOException;
import java.io.InputStream;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.Utils;
import com.bluevia.android.commons.data.StringEntity;

/** 
 * Default parser that converts the InputStream into UTF-8 text
 *
 */
public class DefaultParser implements IParser {

	@Override
	public Entity parse(InputStream is) throws ParseException {
		
		if (is == null)
			return null;
		
		String text;
		try {
			text = Utils.convertStreamToString(is);
		} catch (IOException e) {
			throw new ParseException(e.getMessage(), e);
		}
		StringEntity entity = new StringEntity(text);
		
		return entity;
	}

}
