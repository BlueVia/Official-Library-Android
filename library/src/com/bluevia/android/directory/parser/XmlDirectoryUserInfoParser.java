package com.bluevia.android.directory.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.bluevia.android.commons.parser.ParseException;
import com.bluevia.android.directory.data.AccessInfo;
import com.bluevia.android.directory.data.PersonalInfo;
import com.bluevia.android.directory.data.Profile;
import com.bluevia.android.directory.data.TerminalInfo;
import com.bluevia.android.directory.data.UserInfo;

/**
 * Xml parser for XmlDirectoryUserInfo entities.
 *
 * @author Telefonica I+D
 * 
 */
class XmlDirectoryUserInfoParser implements DirectoryEntityParser {

	private static final String TAG = "XmlDirectoryTerminalInfoParser";

	public UserInfo parse(XmlPullParser parser) throws ParseException {

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

				if (XmlConstants.USERINFO.equals(nameSpace)) {
					Log.e(TAG, "Entro en USERINFO tag name is " + nameSpace);
					return userInfo;
				}
				break;

			case XmlPullParser.START_TAG:
				if(nameSpace.equals(XmlConstants.ACCESSINFO)){
					AccessInfo userAccessInfo = 
						new XmlDirectoryAccessInfoParser().parse(parser);
					if (userAccessInfo != null)
						userInfo.setAccessInfo(userAccessInfo);
				}
				if(nameSpace.equals(XmlConstants.PROFILE)){
					Profile userProfile = 
						new XmlDirectoryProfileParser().parse(parser);
					if (userProfile != null)
						userInfo.setProfile(userProfile);
				}
				if(nameSpace.equals(XmlConstants.TERMINALINFO)){
					TerminalInfo userTerminalInfo = 
						new XmlDirectoryTerminalInfoParser().parse(parser);
					if (userTerminalInfo != null)
						userInfo.setTerminalInfo(userTerminalInfo);
				}
				if(nameSpace.equals(XmlConstants.PERSONALINFO)){
					PersonalInfo personalInfo = 
						new XmlDirectoryPersonalInfoParser().parse(parser);
					if (personalInfo != null)
						userInfo.setPersonalInfo(personalInfo);
				}
			}
            try{
                eventType = parser.next();
            } catch (XmlPullParserException e) {
                throw new ParseException("Could not read next event.", e);
            } catch (IOException e) {
                throw new ParseException("Could not read next event.", e);
			}
		}
		return null;
	}

}
