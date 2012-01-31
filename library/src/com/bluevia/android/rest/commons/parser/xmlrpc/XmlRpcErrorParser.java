///@cond private
package com.bluevia.android.rest.commons.parser.xmlrpc;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.parser.ParseException;

public class XmlRpcErrorParser extends XmlMethodResponseParser {
	
	@Override
	protected Entity handleResult(int eventType) throws ParseException,
			XmlPullParserException, IOException {
        return null;
	}

}
///@endcond
