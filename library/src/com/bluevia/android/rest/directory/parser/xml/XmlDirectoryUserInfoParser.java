///@cond private
package com.bluevia.android.rest.directory.parser.xml;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.directory.data.UserAccessInfo;
import com.bluevia.android.rest.directory.data.UserInfo;
import com.bluevia.android.rest.directory.data.UserProfile;
import com.bluevia.android.rest.directory.data.UserTerminalInfo;

/**
 * Xml parser for XmlDirectoryUserInfo entities.
 *
 * @author Telefonica I+D
 * 
 */
class XmlDirectoryUserInfoParser implements DirectoryEntityParser {

	private static final String TAG = "XmlDirectoryUserTerminalInfoParser";

	/**
	 * @see com.bluevia.android.rest.commons.parser.BlueviaEntityParser#parseEntry(java.io.InputStream)
	 */
	public UserInfo parseEntry(XmlPullParser parser) throws ParseException, IOException {

		UserInfo userInfo = new UserInfo();
		int eventType;

		try {
			eventType = parser.next();
			Log.e(TAG, "Expected event START_TAG: Actual event: " + XmlPullParser.TYPES[eventType]);
		} catch (XmlPullParserException xppe) {
			throw new ParseException("Could not read next event.", xppe);
		} catch (IOException ioe) {
			throw new ParseException("Could not read next event.", ioe);
		}

		while (eventType != XmlPullParser.END_DOCUMENT){
			String nameSpace = parser.getName();

			switch (eventType){
			case XmlPullParser.END_TAG:

				if (XmlConstants.XSD_USERINFO.equals(nameSpace)) {
					Log.e(TAG, "Entro en XSD_USERINFO tag name is " + nameSpace);
					return userInfo;
				}
				break;

			case XmlPullParser.START_TAG:
				if(nameSpace.equals(XmlConstants.XSD_ACCESSINFO)){
					UserAccessInfo userAccessInfo = 
						new XmlDirectoryUserAccessInfoParser().parseEntry(parser);
					if (userAccessInfo != null)
						userInfo.setAccessInfo(userAccessInfo);
				}
				if(nameSpace.equals(XmlConstants.XSD_PROFILE)){
					UserProfile userProfile = 
						new XmlDirectoryUserProfileParser().parseEntry(parser);
					if (userProfile != null)
						userInfo.setUserProfile(userProfile);
				}
				if(nameSpace.equals(XmlConstants.XSD_TERMINALINFO)){
					UserTerminalInfo userTerminalInfo = 
						new XmlDirectoryUserTerminalInfoParser().parseEntry(parser);
					if (userTerminalInfo != null)
						userInfo.setUserTerminalInfo(userTerminalInfo);
				}
			}
			try{
				eventType = parser.next();
			}catch (XmlPullParserException xppe) {
				throw new ParseException("Could not read next event.", xppe);
			}
		}
		return null;
	}

}
///@endcond