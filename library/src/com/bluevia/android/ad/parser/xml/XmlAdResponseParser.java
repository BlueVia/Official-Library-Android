package com.bluevia.android.ad.parser.xml;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.bluevia.android.ad.data.Ad;
import com.bluevia.android.ad.data.AdResponse;
import com.bluevia.android.ad.data.AdsAttribute;
import com.bluevia.android.ad.data.FullCreativeElement;
import com.bluevia.android.ad.data.Interaction;
import com.bluevia.android.ad.data.Resource;
import com.bluevia.android.commons.parser.IParser;
import com.bluevia.android.commons.parser.ParseException;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.commons.parser.xml.XmlParserSerializerUtils;

/**
 * Xml parser for XmlAdResponse entities.
 *
 * @author Telefonica I+D
 * 
 */
class XmlAdResponseParser implements IParser {

	private static final String TAG = "XmlAdResponseParser";

	private XmlPullParser mParser = null;

	public AdResponse parse(InputStream is) throws ParseException {

		if (is == null) {
			return null;
		}

		// init the parser
		this.mParser = Xml.newPullParser();

		try {
			this.mParser.setInput(is, null);
		} catch (XmlPullParserException e) {
			Log.e(TAG, "Exception occurred while reading the XML Document", e);
			throw new ParseException("Could not create XmlAdResponse Parser", e);
		}

		// Start the parse process
		int eventType;

		try {
			eventType = mParser.getEventType();
		} catch (XmlPullParserException e) {
			throw new ParseException("Could not parse adResponse entity.", e);
		}

		if (eventType != XmlPullParser.START_DOCUMENT) {
			throw new ParseException("", new XmlPullParserException(
					"Attempting to initialize parsing beyond " + "the start of the document."));
		}

		try {
			// Gets the first eventType
			eventType = mParser.next();

			// Gets the adResponse
			AdResponse response = handleAdResponse(eventType);

			if ((response == null) || (! (response.isValid())))
				throw new ParseException("The xml received for the adResponse is not valid.");
			return response;
		} catch (XmlPullParserException xppe) {
			throw new ParseException("Could not read next event.", xppe);
		} catch (IOException ioe) {
			throw new ParseException("Could not read next event.", ioe);
		}
	}

	/**
	 * 
	 * @param eventType Last tag gathered with XmlPullParser from the XML.
	 * @return the handleAdResponse
	 * @throws ParseException
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private AdResponse handleAdResponse(int eventType) throws XmlPullParserException, IOException,
	ParseException {
		AdResponse adResponse = null;

		if (eventType != XmlPullParser.START_TAG) {
			throw new ParseException("Expected event START_TAG: Actual event: "
					+ XmlPullParser.TYPES[eventType]);
		}

		while (eventType != XmlPullParser.END_DOCUMENT) {
			String name = mParser.getName();
			switch (eventType) {
			case XmlPullParser.END_TAG:
				if (XmlConstants.XSD_AD_ADRESPONSE.equals(name)) {
					return adResponse;
				}
				break;
			case XmlPullParser.START_TAG:
				if (name.equals(XmlConstants.XSD_AD_ADRESPONSE)) {
					try {
						adResponse = new AdResponse();
						String id = mParser.getAttributeValue(null/* ns */,
								XmlConstants.XSD_AD_AD_RESPONSE_ATTR_ID);
						String version = mParser.getAttributeValue(null/* ns */,
								XmlConstants.XSD_AD_AD_RESPONSE_ATTR_VERSION);
						adResponse.setId(id);
						if ((version == null) || (version.trim().length() == 0))
							throw new ParseException("The xml received for the adResponse is not valid.");

						adResponse.setVersion(Integer.parseInt(version));
					} catch (NumberFormatException e) {
						throw new ParseException("The xml received for the adResponse is not valid.");
					}
				} else if (name.equals(XmlConstants.XSD_AD_AD)) {
					if (adResponse == null)
						throw new ParseException("AdResponse Element is missing");

					//Check there is not already one Ad. (xml should contain just one ad element)
					if (adResponse.getAd() != null)
						throw new ParseException("AdResponse Element has several ad elements.  It should contain just one.");

					adResponse.setAd(handleAd(eventType));
				}
			}
			eventType = mParser.next();
		}
		return null;
	}

	/**
	 * 
	 * @param eventType Last tag gathered with XmlPullParser from the XML.
	 * @return the handleAd
	 * @throws ParseException
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private Ad handleAd(int eventType) throws XmlPullParserException, IOException, ParseException {

		Ad ad = null;

		if (eventType != XmlPullParser.START_TAG) {
			throw new ParseException("Expected event START_TAG: Actual event: "
					+ XmlPullParser.TYPES[eventType]);
		}

		while (eventType != XmlPullParser.END_DOCUMENT) {
			String name = mParser.getName();

			switch (eventType) {
			case XmlPullParser.END_TAG:
				if (name.equals(XmlConstants.XSD_AD_AD)) {
					return ad;
				}
				break;

			case XmlPullParser.START_TAG:
				if (name.equals(XmlConstants.XSD_AD_AD)) {
					ad = new Ad();
					try {
						String adPlacement = mParser.getAttributeValue(null /* ns */,
								XmlConstants.XSD_AD_AD_ATTR_AD_PLACEMENT);
						if (adPlacement != null)
							ad.setAdPlacement(Integer.parseInt(adPlacement));
					} catch (NumberFormatException e) {
						throw new ParseException("The xml received for the adResponse is not valid.");
					}
					ad.setCampaign(mParser.getAttributeValue(null/* ns */,
							XmlConstants.XSD_AD_AD_ATTR_CAMPAIGN));
					ad.setFlight(mParser.getAttributeValue(null/* ns */,
							XmlConstants.XSD_AD_AD_ATTR_FLIGHT));
					ad.setId(mParser.getAttributeValue(null/* ns */,
							XmlConstants.XSD_AD_AD_ATTR_ID));
				} else if (name.equals(XmlConstants.XSD_AD_RESOURCE)) {
					if (ad == null)
						throw new ParseException("Ad Element is missing");
					if(ad.getResource() != null)
						throw new ParseException("Ad Element has several Resource elements. It should contain just one.");
					ad.setResource(handleResource(eventType));
				}

			default:
				break;
			}
			eventType = mParser.next();
		}
		return null;
	}

	/**
	 * 
	 * @param eventType Last tag gathered with XmlPullParser from the XML.
	 * @return the handleResource
	 * @throws ParseException
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private Resource handleResource(int eventType) throws XmlPullParserException, IOException,
	ParseException {

		Resource resource = null;

		if (eventType != XmlPullParser.START_TAG) {
			throw new ParseException("Expected event START_TAG: Actual event: "
					+ XmlPullParser.TYPES[eventType]);
		}

		while (eventType != XmlPullParser.END_DOCUMENT) {
			String name = mParser.getName();

			switch (eventType) {
			case XmlPullParser.END_TAG:
				if (name.equals(XmlConstants.XSD_AD_RESOURCE)) {
					return resource;
				}
				break;
			case XmlPullParser.START_TAG:
				if (name.equals(XmlConstants.XSD_AD_RESOURCE)) {
					resource = new Resource();
					resource.setAdRepresentation(mParser.getAttributeValue(null/* ns */,
							XmlConstants.XSD_AD_RESOURCE_ATTR_AD_PRESENTATION));
				} else if (name.equals(XmlConstants.XSD_AD_CREATIVE_ELEMENT)) {
					if (resource == null)
						throw new ParseException("Resource Element is missing");
					resource.addCreativeElement(handleCreativeElement(eventType));
				}
			default:
				break;
			}
			eventType = mParser.next();
		}

		return null;
	}

	/**
	 * 
	 * @param eventType Last tag gathered with XmlPullParser from the XML.
	 * @return the handleCreativeElement
	 * @throws ParseException
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private FullCreativeElement handleCreativeElement(int eventType) throws XmlPullParserException,
	IOException, ParseException {

		FullCreativeElement creativeElement = null;

		if (eventType != XmlPullParser.START_TAG) {
			throw new ParseException("Expected event START_TAG: Actual event: "
					+ XmlPullParser.TYPES[eventType]);
		}

		while (eventType != XmlPullParser.END_DOCUMENT) {
			String name = mParser.getName();

			switch (eventType) {
			case XmlPullParser.END_TAG:
				if (name.equals(XmlConstants.XSD_AD_CREATIVE_ELEMENT)) {
					return creativeElement;
				}
				break;
			case XmlPullParser.START_TAG:
				if (name.equals(XmlConstants.XSD_AD_CREATIVE_ELEMENT)) {
					creativeElement = new FullCreativeElement();
					creativeElement.setType(stringToCreativeElementType(mParser.getAttributeValue(null/* ns */,
							XmlConstants.XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE)));
				} else if (name.equals(XmlConstants.XSD_AD_ATTRIBUTE)) {
					if (creativeElement == null)
						throw new ParseException("Resource Element is missing");
					creativeElement.addAdsAttribute(handleAttribute(eventType));
				} else if (name.equals(XmlConstants.XSD_AD_INTERACTION)) {
					if (creativeElement == null)
						throw new ParseException("Resource Element is missing");
					creativeElement.addInteraction(handleInteraction(eventType));
				}
			default:
				break;
			}
			eventType = mParser.next();
		}

		return null;
	}

	/**
	 * 
	 * @param eventType Last tag gathered with XmlPullParser from the XML.
	 * @return the handleInteraction
	 * @throws ParseException
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private Interaction handleInteraction(int eventType) throws XmlPullParserException,
	IOException, ParseException {

		Interaction interaction = null;

		if (eventType != XmlPullParser.START_TAG) {
			throw new ParseException("Expected event START_TAG: Actual event: "
					+ XmlPullParser.TYPES[eventType]);
		}

		while (eventType != XmlPullParser.END_DOCUMENT) {
			String name = mParser.getName();

			switch (eventType) {
			case XmlPullParser.END_TAG:
				if (name.equals(XmlConstants.XSD_AD_INTERACTION)) {
					return interaction;
				}
				break;

			case XmlPullParser.START_TAG:
				if (name.equals(XmlConstants.XSD_AD_INTERACTION)) {
					interaction = new Interaction();
					interaction.setType(stringToInteractionType(mParser.getAttributeValue(null /* ns */,
							XmlConstants.XSD_AD_INTERACTION_ATTR_TYPE)));
				} else if (name.equals(XmlConstants.XSD_AD_ATTRIBUTE)) {
					if (interaction == null)
						throw new ParseException("Interaction Element is missing");
					interaction.addAttribute(handleAttribute(eventType));
				}
			default:
				break;
			}
			eventType = mParser.next();
		}

		return null;
	}

	/**
	 * 
	 * @param eventType Last tag gathered with XmlPullParser from the XML.
	 * @return the handleAttribute
	 * @throws ParseException
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private AdsAttribute handleAttribute(int eventType) throws XmlPullParserException, IOException,
	ParseException {

		AdsAttribute attribute = null;

		if (eventType != XmlPullParser.START_TAG) {
			throw new ParseException("Expected event START_TAG: Actual event: "
					+ XmlPullParser.TYPES[eventType]);
		}

		while (eventType != XmlPullParser.END_DOCUMENT) {
			String name = mParser.getName();

			switch (eventType) {
			case XmlPullParser.END_TAG:
				if (name.equals(XmlConstants.XSD_AD_ATTRIBUTE)) {
					return attribute;
				}
				break;

			case XmlPullParser.START_TAG:
				if (name.equals(XmlConstants.XSD_AD_ATTRIBUTE)) {
					attribute = new AdsAttribute();
					attribute.setType(stringToAdsAttributeType(mParser.getAttributeValue(null/* ns */,
							XmlConstants.XSD_AD_ATTRIBUTE_ATTR_TYPE)));
					int[] textEventType = new int[1];
					String value = XmlParserSerializerUtils.getChildText(mParser, textEventType);
					if ((value == null) && (textEventType[0] == XmlPullParser.END_TAG)) {
						eventType = textEventType[0];
						continue;
					}
					attribute.setValue(value);
				}
			default:
				break;
			}
			eventType = mParser.next();
		}

		return null;
	}

	/**
	 * Return the creative element type from a string
	 * 
	 * @param typeStr String that represents the creative element type
	 * @return FullCreativeElement.Type. Values included: IMAGE, TEXT, SOUND, ZIP, PAGE
	 */
	private FullCreativeElement.Type stringToCreativeElementType(String typeStr) {
		if (typeStr == null)
			return null;

		typeStr = typeStr.trim();

		if (typeStr.equals(XmlConstants.XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_IMAGE_VALUE)) {
			return FullCreativeElement.Type.IMAGE;
		} else if (typeStr.equals(XmlConstants.XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_TEXT_VALUE)) {
			return FullCreativeElement.Type.TEXT;
		} else if (typeStr.equals(XmlConstants.XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_SOUND_VALUE)) {
			return FullCreativeElement.Type.SOUND;
		} else if (typeStr.equals(XmlConstants.XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_ZIP_VALUE)) {
			return FullCreativeElement.Type.ZIP;
		} else if (typeStr.equals(XmlConstants.XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_PAGE_VALUE)) {
			return FullCreativeElement.Type.PAGE;
		} else
			return null;
	}

	/**
	 * Return the ads attribute type from a string
	 * 
	 * @param typeStr String that represents the ads attribute type
	 * @return AdsAttribute.Type. Values included: ADTEXT, LOCATOR, URL, CODEC
	 */
	private AdsAttribute.Type stringToAdsAttributeType(String typeStr) {
		if (typeStr == null)
			return null;

		typeStr = typeStr.trim();

		if (typeStr.equals(XmlConstants.XSD_AD_ATTRIBUTE_ATTR_TYPE_ADTEXT_VALUE)) {
			return AdsAttribute.Type.ADTEXT;
		} else if (typeStr.equals(XmlConstants.XSD_AD_ATTRIBUTE_ATTR_TYPE_LOCATOR_VALUE)) {
			return AdsAttribute.Type.LOCATOR;
		} else if (typeStr.equals(XmlConstants.XSD_AD_ATTRIBUTE_ATTR_TYPE_URL_VALUE)) {
			return AdsAttribute.Type.URL;
		} else if (typeStr.equals(XmlConstants.XSD_AD_ATTRIBUTE_ATTR_TYPE_CODEC_VALUE)) {
			return AdsAttribute.Type.CODEC;
		} else
			return null;
	}

	/**
	 * Return the interaction type from a string
	 * 
	 * @param typeStr String that represents the interaction type
	 * @return Interaction.Type. Values included: CLICK_2_WAP
	 */
	private Interaction.Type stringToInteractionType(String typeStr) {
		if (typeStr == null)
			return null;

		typeStr = typeStr.trim();

		if (typeStr.equals(XmlConstants.XSD_AD_INTERACTION_ATTR_TYPE_CLICK2WAP_VALUE)) {
			return Interaction.Type.CLICK_2_WAP;
		} else
			return null;
	}

}
