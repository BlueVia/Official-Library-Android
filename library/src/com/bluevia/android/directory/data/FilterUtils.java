package com.bluevia.android.directory.data;

import java.util.HashMap;

import com.bluevia.android.directory.data.UserInfo.DataSet;
import com.bluevia.android.directory.parser.XmlConstants;

public class FilterUtils {

	private static final HashMap<UserInfo.DataSet, String> sUserInfoMatcher;
	private static final HashMap<PersonalInfo.Fields, String> sPersonalInfoMatcher;
	private static final HashMap<Profile.Fields, String> sProfileMatcher;
	private static final HashMap<AccessInfo.Fields, String> sAccessInfoMatcher;
	private static final HashMap<TerminalInfo.Fields, String> sTerminalInfoMatcher;

	static {

		//User Info
		sUserInfoMatcher = new HashMap<DataSet, String>();
		sUserInfoMatcher.put(DataSet.ACCESS_INFO, XmlConstants.DATASET_ACCESSINFO);
		sUserInfoMatcher.put(DataSet.PERSONAL_INFO, XmlConstants.DATASET_PERSONALINFO);
		sUserInfoMatcher.put(DataSet.PROFILE, XmlConstants.DATASET_PROFILE);
		sUserInfoMatcher.put(DataSet.TERMINAL_INFO, XmlConstants.DATASET_TERMINALINFO);

		//Personal Info
		sPersonalInfoMatcher = new HashMap<PersonalInfo.Fields, String>();
		sPersonalInfoMatcher.put(PersonalInfo.Fields.GENDER, XmlConstants.PERSONALINFO_GENDER);

		//User Profile
		sProfileMatcher = new HashMap<Profile.Fields, String>();
		sProfileMatcher.put(Profile.Fields.USER_TYPE, XmlConstants.PROFILE_USERTYPE);
		sProfileMatcher.put(Profile.Fields.ICB, XmlConstants.PROFILE_ICB);
		sProfileMatcher.put(Profile.Fields.OCB, XmlConstants.PROFILE_OCB);
		sProfileMatcher.put(Profile.Fields.LANGUAGE, XmlConstants.PROFILE_LANGUAGE);
		sProfileMatcher.put(Profile.Fields.PARENTAL_CONTROL, XmlConstants.PROFILE_PARENTALCONTROL);
		sProfileMatcher.put(Profile.Fields.OPERATOR_ID, XmlConstants.PROFILE_OPERATORID);
		sProfileMatcher.put(Profile.Fields.MMS_STATUS, XmlConstants.PROFILE_MMSSTATUS);
		sProfileMatcher.put(Profile.Fields.SEGMENT, XmlConstants.PROFILE_SEGMENT);

		//Access Info
		sAccessInfoMatcher = new HashMap<AccessInfo.Fields, String>();
		sAccessInfoMatcher.put(AccessInfo.Fields.ACCESS_TYPE, XmlConstants.ACCESSINFO_ACCESSTYPE);
		sAccessInfoMatcher.put(AccessInfo.Fields.APN, XmlConstants.ACCESSINFO_APN);
		sAccessInfoMatcher.put(AccessInfo.Fields.ROAMING, XmlConstants.ACCESSINFO_ROAMING);

		//Terminal Info
		sTerminalInfoMatcher = new HashMap<TerminalInfo.Fields, String>();
		sTerminalInfoMatcher.put(TerminalInfo.Fields.BRAND, XmlConstants.TERMINALINFO_BRAND);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.MODEL, XmlConstants.TERMINALINFO_MODEL);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.VERSION, XmlConstants.TERMINALINFO_VERSION);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.MMS, XmlConstants.TERMINALINFO_MMS);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.EMS, XmlConstants.TERMINALINFO_EMS);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.SMART_MESSAGING, XmlConstants.TERMINALINFO_SMARTMESSAGING);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.WAP, XmlConstants.TERMINALINFO_WAP);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.USSD_PHASE, XmlConstants.TERMINALINFO_USSDPHASE);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.EMS_MAX_NUMBER, XmlConstants.TERMINALINFO_EMSMAXNUMBER);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.WAP_PUSH, XmlConstants.TERMINALINFO_WAP_PUSH);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.MMS_VIDEO, XmlConstants.TERMINALINFO_MMS_VIDEO);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.VIDEO_STREAMING, XmlConstants.TERMINALINFO_VIDEO_STREAMING);
		sTerminalInfoMatcher.put(TerminalInfo.Fields.SCREEN_RESOLUTION, XmlConstants.TERMINALINFO_SCREENRESOLUTION);
	}

	public static String buildUserInfoFilter(UserInfo.DataSet[] dataSet){
		StringBuilder sb = new StringBuilder();

		for (DataSet data : dataSet){
			sb.append(sUserInfoMatcher.get(data));
			sb.append(",");
		}

		String res = sb.toString();

		if (res.endsWith(",")){
			res = res.substring(0,res.length()-1);
		}

		return res;
	}	

	public static String buildPersonalInfoFilter(PersonalInfo.Fields[] fields){
		StringBuilder sb = new StringBuilder();

		for (PersonalInfo.Fields field : fields){
			sb.append(sPersonalInfoMatcher.get(field));
			sb.append(",");
		}

		return deleteComma(sb.toString());
	}

	public static String buildProfileFilter(Profile.Fields[] fields){
		StringBuilder sb = new StringBuilder();

		for (Profile.Fields field : fields){
			sb.append(sProfileMatcher.get(field));
			sb.append(",");
		}

		return deleteComma(sb.toString());
	}

	public static String buildAccessInfoFilter(AccessInfo.Fields[] fields){

		StringBuilder sb = new StringBuilder();

		for (AccessInfo.Fields field : fields){
			sb.append(sAccessInfoMatcher.get(field));
			sb.append(",");
		}

		return deleteComma(sb.toString());
	}

	public static String buildTerminalInfoFilter(TerminalInfo.Fields[] fields){
		StringBuilder sb = new StringBuilder();

		for (TerminalInfo.Fields field : fields){
			sb.append(sTerminalInfoMatcher.get(field));
			sb.append(",");
		}

		return deleteComma(sb.toString());
	}

	protected static String deleteComma(String in){
		if (in.endsWith(",")){
			in = in.substring(0,in.length()-1);
		}

		return in;
	}
}
