///@cond private
package com.bluevia.android.rest.messaging;

import java.util.ArrayList;

import com.bluevia.android.rest.commons.Entity;

/**
 * 
 * Class representing the ReceivedMessageList that will be received using the SMS/MMS Client API
 * This type is composed of the following fields:
 * <ul>
 * <li>mList
 * </ul>
 *
 * This implementation is not synchronized
 *
 * @author Telefonica R&D
 * 
 *
 */
public class ReceivedMessageList implements Entity {

	///@cond private
    private ArrayList<AbstractReceivedMessage> mList;
    ///@endcond

    /**
     *  Instantiates a new ReceivedSmsList
     */
    public ReceivedMessageList(){
    	super();
    	mList = new ArrayList<AbstractReceivedMessage>();
    }
    
    /**
     * @see com.bluevia.android.rest.commons.Entity#isValid(ApiMode)
     */
    public boolean isValid() {
		for (AbstractReceivedMessage message : mList){
			if (!message.isValid())
				return false;
		}
		return true;
	}
    /**
     * Adds a new single received Message
     * @param element
     * @return
     */
    public boolean add(AbstractReceivedMessage element){
        return mList.add(element);
    }

    /**
     * Return the received Message list for all recipient addresses
     * @param <E>
     * @return the list
     */
    @SuppressWarnings("unchecked")
	public <E> ArrayList<E> getList()  {
        return (ArrayList<E>)mList.clone();
    }

}
///@endcond