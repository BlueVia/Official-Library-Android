package com.bluevia.android.rest.messaging;

import com.bluevia.android.rest.commons.Entity;

/**
 * Class representing the SendSmsResult type
 *
 * This type is composed of the following fields:
 * <ul>
 * <li>result; mandatory: String
 * </ul>
 *
 * @author Telefonica R&D
 * 
 */

//TODO Think about doing this class immutable

public final class SendMessageResult implements Entity {

    private String mResult;

    /**
     * Instantiates a new SMS result type.
     */
    public SendMessageResult() {
        super();
    }

    /**
     * Gets the result.
     *
     * @return the result
     */
    public String getResult() {
        return mResult;
    }

    /**
     * Sets the result.
     *
     * @param result the new result
     */
    public void setResult(String result) {
        this.mResult = result;
    }

    /* (non-Javadoc)
     * @see com.bluevia.android.rest.Entity#isValid()
     */
    public boolean isValid() {
        return mResult != null;
    }

}
