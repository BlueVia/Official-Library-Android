/**
 * \package com.bluevia.android.rest.directory.data.filter This package contains REST filters to notify the Directory library which fileds of the User Infromation to retrieve
 *  using Bluevia API.
 */
package com.bluevia.android.rest.directory.data.filter;

import com.bluevia.android.rest.directory.parser.xml.XmlConstants;

/**
 * This class works as a filter to let developers notify the library about which fields of the
 * UserInfo block they want to retrieve. Developers must set a true value in those
 * fields they whish the library to retrieve from the gSDP and pass the object to the
 * getUserInfor method of their DirectoryClient instance

 * @author Telefonica R&D
 * 
 */
public class UserInfoFilter extends RequestFilter {
	
    /**
     * Gets the AccessInfo filter
     * 
     * @return the AccessInfo filter
     */
    public boolean isAcessInfo() {
        return getFilter(XmlConstants.XSD_DATASET_ACCESSINFO);
    }
    
    /**
     * Sets the AccessInfo filter
     * 
     * @param value the AccessInfo filter to set
     */
    public void setAccessInfo(boolean value) {
        setFilter(XmlConstants.XSD_DATASET_ACCESSINFO, value);
    }    
    
    /**
     * Gets the Profile filter
     * 
     * @return the Profile filter
     */
    public boolean isProfile() {
        return getFilter(XmlConstants.XSD_DATASET_PROFILE);
    }
    
    /**
     * Sets the Profile filter
     * 
     * @param value the Profile filter to set
     */
    public void setProfile(boolean value) {
        setFilter(XmlConstants.XSD_DATASET_PROFILE, value);
    }
    
    /**
     * Gets the TerminalInfo filter
     * 
     * @return the TerminalInfo filter
     */
    public boolean isTerminalInfo() {
        return getFilter(XmlConstants.XSD_DATASET_TERMINALINFO);
    }
    
    /**
     * Sets the TerminalInfo filter
     * 
     * @param value the TerminalInfo filter to set
     */
    public void setTerminalInfo(boolean value) {
        setFilter(XmlConstants.XSD_DATASET_TERMINALINFO, value);
    }
    
	/* (non-Javadoc)
	 * @see com.bluevia.android.rest.directory.data.filter.RequestFilter#isValid()
	 */
	@Override
	public boolean isValid() {
		
		return filters.size() >= 2;
	}
    
}
