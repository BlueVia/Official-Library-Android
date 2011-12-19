package com.bluevia.android.rest.messaging.mms.parser.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParser;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory;
import com.bluevia.android.rest.messaging.mms.data.MimeContent;
import com.bluevia.android.rest.messaging.mms.data.ReceivedMms;
import com.bluevia.android.rest.messaging.mms.data.ReceivedMmsInfo;

/**
 * Multipart MMS parser
 *
 */
public class MultipartMmsParser {
	
	private BlueviaEntityParser mParser;
	
	public MultipartMmsParser(BlueviaEntityParserFactory factory){
		mParser = factory.createParser();
	}

	public ReceivedMms parseMultipart(MimeMultipart multipart) throws BlueviaClientException {
		ReceivedMms result = null;
    	
    	try {
    		
    		if (multipart == null)
    			throw new BlueviaClientException("Error parsing multipart: null mimemultipart", BlueviaClientException.INTERNAL_CLIENT_ERROR);
        	
        	if (multipart.getCount() < 2)	//At least 2: "root-fields" & "multiparts"
    			throw new BlueviaClientException("Error parsing multipart: no body parts", BlueviaClientException.INTERNAL_CLIENT_ERROR);
        	
        	result = new ReceivedMms();
        	
        	String[] headers = null;
        	
        	//Parse root-fields
        	BodyPart rootFieldsPart = multipart.getBodyPart(0);
        	headers = rootFieldsPart.getHeader("Content-Disposition");
        	if (headers != null && headers.length > 0 && headers[0] != null && headers[0].contains("root-fields")){
        		if (rootFieldsPart.getContent() instanceof ByteArrayInputStream){
        			ByteArrayInputStream is = (ByteArrayInputStream) rootFieldsPart.getContent();
        			Entity entity = mParser.parseEntry(is);
        			is.close();
        			if (entity instanceof ReceivedMmsInfo){
        				result.setMessage((ReceivedMmsInfo) entity);
        			} else throw new BlueviaClientException("Error parsing multipart: no message info", BlueviaClientException.INTERNAL_CLIENT_ERROR);
        		}
        	}
        	
        	BodyPart attachmentsPart = multipart.getBodyPart(1);
        	headers = attachmentsPart.getHeader("Content-Disposition");
        	if (headers != null && headers.length > 0 && headers[0] != null && headers[0].contains("attachments")){
        		
        		ArrayList<MimeContent> attachList = new ArrayList<MimeContent>();
        		
        		headers = attachmentsPart.getHeader("Content-Type");
        		if (headers != null && headers.length > 0 && headers[0] != null){
        			
        			if (headers[0].contains("multipart")){
        				//Multipart/mixed or related
        				MimeMultipart attachments = null;
        				if (attachmentsPart.getContent() instanceof MimeMultipart){
        					attachments = (MimeMultipart) attachmentsPart.getContent();
        				} else if (attachmentsPart.getContent() instanceof InputStream){
        					ByteArrayDataSource ds = new ByteArrayDataSource((InputStream) attachmentsPart.getContent(), 
        							headers[0]);
        					attachments = new MimeMultipart(ds);
        				}
        				if (attachments != null){
            				for (int i=0; i<attachments.getCount(); i++){
        						BodyPart part = attachments.getBodyPart(i);
        						attachList.add(parseBodyPart(part));
        					}
        				}
        			}
        		}
        		
        		result.setAttachments(attachList);
        	}

    	} catch (MessagingException e){
			throw new BlueviaClientException("Error parsing multipart: " + e.getLocalizedMessage(), BlueviaClientException.INTERNAL_CLIENT_ERROR);
    	} catch (IOException e) {
			throw new BlueviaClientException("Error parsing multipart: " + e.getLocalizedMessage(), BlueviaClientException.INTERNAL_CLIENT_ERROR);
    	} 
    	
    	return result;
    }

    private MimeContent parseBodyPart(BodyPart part) throws MessagingException, IOException{
    	MimeContent content = new MimeContent();

    	String contentType = part.getContentType();
    	
    	//Content-Type
    	Pattern p = Pattern.compile("(.*);(.*)");
    	Matcher m = p.matcher(contentType);
    	if (m.matches()){
    		content.setContentType(m.group(1));
    	} else content.setContentType(contentType);
    	
    	//Content-Transfer-Encoding
    	String[] cte = part.getHeader("Content-Transfer-Encoding");
    	if (cte != null && cte.length > 0)
    		content.setContentEncoding(cte[0]);
    	
    	//Filename
    	Pattern pattern = Pattern.compile("(.*)ame=(.*)");
    	Matcher matcher = pattern.matcher(contentType);
    	if (matcher.matches()){
    		content.setFileName(matcher.group(2));
    	}
    	
    	//Content
    	if (part.getContent() instanceof String){
    		content.setContent(part.getContent());
    	} else if (part.getContent() instanceof InputStream){
    		
    		InputStream is = (InputStream) part.getContent();
    		ByteArrayOutputStream os = new ByteArrayOutputStream();
    		byte[] buf = new byte[1024];
    		int read = 0;
    		while ((read = is.read(buf)) != -1) {
    			os.write(buf, 0, read);
    		}
    		content.setContent(os.toByteArray());
    		is.close();
    	} 
    	return content;
    }
	
}
