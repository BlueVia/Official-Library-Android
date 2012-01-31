///@cond private
/**
 * \package com.bluevia.android.rest.messaging.mms This package contains the classes in order to send MMS using Bluevia API.
 * \package com.bluevia.android.rest.messaging.mms.client This package contains the classes in order to send MMS using Bluevia API.
 */
package com.bluevia.android.rest.messaging.mms.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import android.content.Context;
import android.util.Log;

import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.client.AbstractRestClient;
import com.bluevia.android.rest.commons.connector.IConnector;
import com.bluevia.android.rest.commons.connector.http.HttpException;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.http.authentication.AuthenticationInfo;
import com.bluevia.android.rest.commons.parser.BlueviaEntityParserFactory;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlParserSerializerUtils;
import com.bluevia.android.rest.messaging.MessageryClientHelper;
import com.bluevia.android.rest.messaging.SendMessageResult;
import com.bluevia.android.rest.messaging.mms.data.MmsMessage;
import com.bluevia.android.rest.messaging.mms.parser.xml.MultipartMmsParser;

/**
 * Client interface for the REST binding of the Bluevia MMS Service.
 * 
 * @author Telefonica R&D
 * 
 */
public abstract class MmsClient extends AbstractRestClient {

	private static final String TAG = "MmsClient";

	protected static final String FEED_MMS_BASE_URI = "REST/MMS";
	protected static final String FEED_MMS_SANDBOX_BASE_URI = "REST/MMS_Sandbox";

	/**
	 * Creates a MmsClient object, with a explicit IConnector, to send MMSs and retrieve its status using the gSDP.
	 * @param context the Android context of the application.
	 * @param uri the base uri of the client
	 * @param connector the connector for communication mechanism.
	 * @param auth the parameters with the security info supplied by the developer.
	 * @param factory the factory that parse and serialize entities to stream and vice-versa.
	 * @throws BlueviaClientException
	 */
	public MmsClient(Context context, String uri, IConnector connector, AuthenticationInfo auth, BlueviaEntityParserFactory factory) throws BlueviaClientException {
		super(context, uri, connector, auth, factory);
	}

}
///@endcond
