package com.bluevia.android.messagery.mt.mms.data;

import java.util.HashMap;
import java.util.Map.Entry;

import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.Utils;
import com.bluevia.android.commons.exception.BlueviaException;

/**
 * Class representing a mms attachment information. It includes the path of the file
 * and the Content-type.
 * 
 * The value of the Content-type must be chosen from the valid content-type enum list
 *
 */
public class Attachment implements Entity {

	/**
	 * Availables Content-type values.
	 *
	 */
	public enum ContentType {TEXT_PLAIN, 
		IMAGE_JPEG, IMAGE_BMP, IMAGE_GIF, IMAGE_PNG,
		AUDIO_AMR, AUDIO_MIDI, AUDIO_MP3, AUDIO_MPEG, AUDIO_WAV,
		VIDEO_MP4, VIDEO_AVI, VIDEO_3GPP};

	private static final String TEXT_PLAIN_ST = "text/plain";
	private static final String IMAGE_JPEG_ST = "image/jpeg";
	private static final String IMAGE_BMP_ST = "image/bmp";
	private static final String IMAGE_GIF_ST = "image/gif";
	private static final String IMAGE_PNG_ST = "image/png";
	private static final String AUDIO_AMR_ST = "audio/amr";
	private static final String AUDIO_MIDI_ST = "audio/midi";
	private static final String AUDIO_MP3_ST = "audio/mp3";
	private static final String AUDIO_MPEG_ST = "audio/mpeg";
	private static final String AUDIO_WAV_ST = "audio/wav";
	private static final String VIDEO_MP4_ST = "video/mp4";
	private static final String VIDEO_AVI_ST = "video/avi";
	private static final String VIDEO_3GPP_ST = "video/3gpp";

	private static final HashMap<ContentType, String> sContentTypesTable;
	private static final HashMap<String, ContentType> sInverseContentTypesTable;
	
	public static <T, E> HashMap<E, T> reverseMap(HashMap<T, E> map) {
		HashMap<E, T> res = new HashMap<E, T>(map.size());
	     for (Entry<T, E> entry : map.entrySet()) {
	    	 res.put(entry.getValue(), entry.getKey());
	     }
	     return res;
	}
	
	static {
		sContentTypesTable = new HashMap<Attachment.ContentType, String>();
		sContentTypesTable.put(ContentType.TEXT_PLAIN, TEXT_PLAIN_ST);
		sContentTypesTable.put(ContentType.IMAGE_JPEG, IMAGE_JPEG_ST);
		sContentTypesTable.put(ContentType.IMAGE_BMP, IMAGE_BMP_ST);
		sContentTypesTable.put(ContentType.IMAGE_GIF, IMAGE_GIF_ST);
		sContentTypesTable.put(ContentType.IMAGE_PNG, IMAGE_PNG_ST);
		sContentTypesTable.put(ContentType.AUDIO_AMR, AUDIO_AMR_ST);
		sContentTypesTable.put(ContentType.AUDIO_MIDI, AUDIO_MIDI_ST);
		sContentTypesTable.put(ContentType.AUDIO_MP3, AUDIO_MP3_ST);
		sContentTypesTable.put(ContentType.AUDIO_MPEG, AUDIO_MPEG_ST);
		sContentTypesTable.put(ContentType.AUDIO_WAV, AUDIO_WAV_ST);
		sContentTypesTable.put(ContentType.VIDEO_MP4, VIDEO_MP4_ST);
		sContentTypesTable.put(ContentType.VIDEO_AVI, VIDEO_AVI_ST);
		sContentTypesTable.put(ContentType.VIDEO_3GPP, VIDEO_3GPP_ST);
		
		sInverseContentTypesTable = reverseMap(sContentTypesTable);
	}
	
	private ContentType mContentType;
	private String mFilePath;
	
	/**
	 * 
	 * @param path the path of the file
	 * @param mContentType the content-type of the file
	 * @throws BlueviaException 
	 */
	public Attachment(String path, ContentType contentType) throws BlueviaException {
		this.mFilePath = path;
		
		if (contentType == null)
			throw new BlueviaException("Bad Request: The parameter 'content type' is not valid.", BlueviaException.BAD_REQUEST_EXCEPTION);
		this.mContentType = contentType;
	}
	
	/**
	 * 
	 * @param path the path of the file
	 * @param mContentType the content-type of the file
	 * @throws BlueviaException 
	 */
	public Attachment(String path, String contentType) throws BlueviaException{
		this.mFilePath = path;
		
		if (contentType == null || !sInverseContentTypesTable.containsKey(contentType))
			throw new BlueviaException("Bad Request: The parameter 'content type' is not valid.", BlueviaException.BAD_REQUEST_EXCEPTION);
		this.mContentType = sInverseContentTypesTable.get(contentType);
	}
	
	/**
	 * @return the mContentType
	 */
	public ContentType getContentType() {
		return mContentType;
	}
	
	/**
	 * @param mContentType the mContentType to set
	 */
	public void setContentType(ContentType contentType) {
		this.mContentType = contentType;
	}
	
	/**
	 * @return the mFilePath
	 */
	public String getFilePath() {
		return mFilePath;
	}
	
	/**
	 * @param mFilePath the mFilePath to set
	 */
	public void setFilePath(String filePath) {
		this.mFilePath = filePath;
	}
	
	/**
	 * 
	 * @return returns the content-type of the attachment as a String
	 */
	public String getStringContentType(){
		return sContentTypesTable.get(mContentType);
	}

	@Override
	public boolean isValid() {
		return !Utils.isEmpty(mFilePath) && mContentType != null;
	}
	
	
	
}
