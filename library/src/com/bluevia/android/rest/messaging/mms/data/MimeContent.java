package com.bluevia.android.rest.messaging.mms.data;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.Utils;

/**
 * Class representing a Mime Content (an attachment) of a received MMS.
 * The content is an Object which can be a String or a byte[].
 *
 */
public class MimeContent implements Entity {

	private String contentType;
    private Object content;
    private String contentEncoding;
    private String fileName;

    /**
     * @return the content of the MimeContent. This can be a String or a byte[]
     */
    public Object getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(Object content) {
        this.content = content;
    }

    /**
     * @return the contentEncoding
     */
    public String getContentEncoding() {
        return contentEncoding;
    }

    /**
     * @param contentEncoding the contentEncoding to set
     */
    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

	@Override
	public boolean isValid() {
		return !Utils.isEmpty(contentType) && content != null;
	}

}
