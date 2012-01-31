package com.bluevia.android.rest.ad.data;


import java.util.ArrayList;

import com.bluevia.android.rest.commons.Entity;

/**
 * 
 * Object that represents ad resource information associated It includes the
 * following attributes:
 * <ul>
 * <li>CreativeElement: An ad object describes the different elements that shape
 * the adsource. For instance an small banner link in line, could have two
 * objects (image & text).</li>
 * <li>AdRepresentation: Tag used by clients to get ad sources. The kind of an
 * ad sources such as banner, sponsored links, interstitials, image&link, small
 * banner link inline, top banner link, below,... Represented as in numeric form
 * (same as on the request AD_PRESENTATION).</li>
 * </ul>
 *
 * @author Telefonica R&D
 * 
 *
 */
public class Resource implements Entity {
	
	/// @cond private
	
    private ArrayList<CreativeElement> mCreativeElement;

    private String mAdRepresentation;

    /// @endcond
    
	/**
	 * Instantiates a new empty Resource
	 */
    public Resource() {
        super();
        mCreativeElement = new ArrayList<CreativeElement>();
    }

    /**
     * Gets the ad representation
     *
     * @return the ad representation
     */
    public String getAdRepresentation() {
        return mAdRepresentation;
    }

    /**
     * Sets the ad representation
     *
     * @param adRepresentation the ad representation
     */
    public void setAdRepresentation(String adRepresentation) {
        mAdRepresentation = adRepresentation;
    }

    /**
     * Gets the creative elements associated the the ad
     *
     * @return the creative elements list
     */
    public ArrayList<CreativeElement> getCreativeElement() {
        return (ArrayList<CreativeElement>) mCreativeElement;
    }

    /**
     * Adds a CreativeElement for the ad representation
     *
     * @param c the creative element
     * @return
     */
    public boolean addCreativeElement(CreativeElement c) {
        return mCreativeElement.add(c);
    }

	/**
	 * check if the Resource entity is valid	
	 * 
	 * @return boolean result of the check
	 */	
    public boolean isValid() {
        if (mAdRepresentation == null)
            return false;

        if ((mCreativeElement == null) || (mCreativeElement.size() == 0))
            return false;

        for (CreativeElement c : mCreativeElement) {
            if (!c.isValid()) {
                return false;
            }
        }

        return true;
    }

}
