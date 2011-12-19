///@cond private
package com.bluevia.android.rest.commons.connector.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.mail.util.ByteArrayDataSource;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.connector.IConnector;
import com.bluevia.android.rest.commons.connector.http.authentication.AuthenticationInfo;
import com.bluevia.android.rest.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.android.rest.commons.connector.http.multipart.MultipartEntity;
import com.bluevia.android.rest.commons.connector.http.multipart.PartContainer;
import com.bluevia.android.rest.commons.connector.http.multipart.StringPart;
import com.bluevia.android.rest.commons.parser.BlueviaEntitySerializer;
import com.bluevia.android.rest.commons.parser.ParseException;
import com.bluevia.android.rest.commons.parser.xml.XmlParserSerializerUtils;

/**
 * HttpConnector that allows to send requests to gSDP via HTTP REST
 *
 * @author Telefonica R&D
 * 
 */
public class HttpConnector implements IConnector {

	private static final String TAG = "HttpConnector";

	//ENDPOINT GSDP
	private static final String GSDP_BASE_URL = "https://api.bluevia.com/services/";

	private static final int MAX_REDIRECTS_COUNT = 5;

	/** 
	 * Type to set the restHttpMethod 
	 */
	private enum RestHttpMethod {POST, GET};

	/** 
	 * Internal HTTP client. 
	 */
	private DefaultHttpClient mHttpClient;

	/** 
	 * Enable certificate validation 
	 */
	private boolean enableCertificateValidation = true;

	/** 
	 * The Constant APPLICATION_XML.
	 */
	private static final String APPLICATION_XML_CONTENT_TYPE = "application/xml;charset=utf-8";

	/** 
	 * The Constant APPLICATION_JSON. 
	 */
	private static final String APPLICATION_JSON_CONTENT_TYPE = "application/json;charset=utf-8";

	/** 
	 * The Constant APPLICATION_DEFAULT_CONTENT_TYPE. 
	 */
	private static final String APPLICATION_DEFAULT_CONTENT_TYPE = "text/plain";

	/** 
	 *  The Constant APPLICATION_URL_ENCODED. 
	 */
	private static final String APPLICATION_URL_ENCODED_CONTENT_TYPE = "application/x-www-form-urlencoded";

	/** 
	 * Close the HTTP connections
	 */
	public void close() {
		if (mHttpClient != null)
			mHttpClient.getConnectionManager().shutdown();
	}

	public InputStream createEntity(String feedUri, Entity entity,
			BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo) throws IOException,
			HttpException, ParseException {
		return createEntity(feedUri, entity, serializer, authenticationInfo, null, new HashMap<String, String>());
	}

	public InputStream createEntity(String feedUri, Entity entity,
			BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo,
			HttpQueryParams parameters, HashMap<String, String> responseHeaders) throws IOException,
			HttpException, ParseException {
		return createEntity(feedUri, entity, serializer, authenticationInfo, parameters, null, responseHeaders);
	}

	public InputStream createEntity(String feedUri, Entity entity, BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo,
			HttpQueryParams parameters,
			HashMap<String, String> requestHeaders,
			HashMap<String, String> responseHeaders
	) throws IOException,
	HttpException, ParseException {

		HttpEntity res = null;
		String feedUrl = composeUrl(feedUri);

		Log.e(TAG, "feedUri: "+feedUri);
		Log.e(TAG, "FeedUrl: "+feedUrl);

		HttpEntity httpEntity = null;
		if (entity != null && serializer != null){
			httpEntity = createHttpEntityFromEntity (entity, serializer);
		}
		res = createAndExecuteRequest(feedUrl, RestHttpMethod.POST, httpEntity, authenticationInfo, parameters, requestHeaders, responseHeaders);

		if (res != null) {
			return res.getContent();
		}
		return null;
	}

	public InputStream createEntity(String feedUri, Entity entity, BlueviaEntitySerializer serializer, AuthenticationInfo authenticationInfo,
			HttpQueryParams parameters, HashMap<String, String> responseHeaders, ArrayList<BlueviaPartBase> partList) throws IOException,
			HttpException, ParseException {
		HttpEntity res = null;
		String feedUrl = composeUrl(feedUri);

		//Build the gSDP Entity part
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			serializer.serialize(entity, out);
		} catch (IOException iox) {
			throw iox;
		} catch (ParseException pex) {
			throw pex;
		}

		byte[] entryBytes = out.toByteArray();
		out.close();

		String entryString = new String(entryBytes, "UTF-8");

		//Build root-field
		StringPart rootField = new StringPart("root-fields", entryString);

		switch (serializer.getEncoding()) {
		case APPLICATION_XML:
			rootField.setContentType(BlueviaPartBase.APPLICATION_XML);
			break;
		case APPLICATION_JSON:
			rootField.setContentType(BlueviaPartBase.APPLICATION_JSON);
			break;
		case APPLICATION_URL_ENCODED:
			rootField.setContentType(BlueviaPartBase.APPLICATION_URL_ENCODED);
		}

		BlueviaPartBase[] allParts = new BlueviaPartBase[2];
		allParts[0] = rootField;

		BlueviaPartBase[] parts = partList.toArray(new BlueviaPartBase [partList.size()]);
		for (int i=0; i<parts.length; i++) {
			if (parts[i] instanceof StringPart)
				((StringPart) parts[i]).setFilename(parts[i].getName());
			parts[i].setName("attachment");
			parts[i].setContentDisposition("form-data");
		}
		
		PartContainer part_container = new PartContainer("attachments", parts);
		allParts[1] = part_container;

		HttpEntity httpEntity = new MultipartEntity(allParts);
		res = createAndExecuteRequest(feedUrl, RestHttpMethod.POST, httpEntity, authenticationInfo, parameters, responseHeaders);

		if (res != null) {
			return res.getContent();
		}
		return null;
	}

	public InputStream retrieveEntity(String feedUri, AuthenticationInfo authenticationInfo) throws IOException, HttpException {
		return retrieveEntity (feedUri, authenticationInfo, null);
	}

	public InputStream retrieveEntity(String feedUri, AuthenticationInfo authenticationInfo, HttpQueryParams parameters) throws IOException, HttpException {
		HttpEntity res = null;
		String feedUrl = composeUrl(feedUri);

		res = createAndExecuteRequest(feedUrl, RestHttpMethod.GET, null, authenticationInfo, parameters, null);

		if (res != null) {
			return res.getContent();
		}
		return null;
	}

	/**
	 * Creates an HttpClient
	 *
	 * @return the created HTTP Client
	 */
	protected HttpClient lazyInitHttpClient() {
		if (mHttpClient == null) {
			HttpParams params = new BasicHttpParams();

			// Default connection and socket timeout of 20 seconds.
			HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
			HttpConnectionParams.setSoTimeout(params, 20 * 1000);
			HttpConnectionParams.setSocketBufferSize(params, 8192);

			//Default timeout used when retrieving a ManagedClientConnection from the ClientConnectionManager.
			ConnManagerParams.setTimeout(params, 20 * 1000);

			// Don't handle redirects -- return them to the caller.
			HttpClientParams.setRedirecting(params, false);

			// Set the specified user agent and register standard protocols.
			//HttpProtocolParams.setUserAgent(params, getCurrentUserAgent());

			if (enableCertificateValidation)
				mHttpClient = createHttpClientWithCertificateValidation (params);
			else
				mHttpClient = createHttpClientWithNoCertificateValidation (params);

			mHttpClient.removeRequestInterceptorByClass(
					org.apache.http.protocol.RequestExpectContinue.class);


			// TODO Poner la conexion a traves del proxy si existe en la
			// configuracion del APN si esta conectado por 3g. Sino no poner
			// proxy (si va por wireless)

			// if (proxyHost != null && proxyPort != -1) {
			//HttpHost httpproxy = new HttpHost("192.168.254.2", 8080);
			//mHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
			//       httpproxy);
			// }
		}
		return mHttpClient;
	}

	/**
	 * Creates an http entity using data from an Bluevia entity
	 * @param entity
	 * @param serializer
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	private HttpEntity createHttpEntityFromEntity (Entity entity, BlueviaEntitySerializer serializer) throws IOException, ParseException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			serializer.serialize(entity, out);
		} catch (IOException iox) {
			throw iox;
		} catch (ParseException pex) {
			throw pex;
		}

		byte[] entryBytes = out.toByteArray();
		out.close();

		if (entryBytes != null && Log.isLoggable(TAG, Log.DEBUG)) {
			try {
				Log.d(TAG, "Serialized entry: " + new String(entryBytes, "UTF-8"));
			} catch (UnsupportedEncodingException uee) {
				// should not happen
				throw new IllegalStateException("UTF-8 should be supported!",
						uee);
			}
		}
		//TODO Add gzip if server supports it
		AbstractHttpEntity httpEntity = new ByteArrayEntity(entryBytes);

		switch (serializer.getEncoding()) {
		case APPLICATION_XML:
			httpEntity.setContentType(APPLICATION_XML_CONTENT_TYPE);
			break;
		case APPLICATION_JSON:
			httpEntity.setContentType(APPLICATION_JSON_CONTENT_TYPE);
			break;
		case APPLICATION_URL_ENCODED:
			httpEntity.setContentType(APPLICATION_URL_ENCODED_CONTENT_TYPE);
			break;
		default:
			httpEntity.setContentType(APPLICATION_DEFAULT_CONTENT_TYPE);
			break;
		}
		return httpEntity;
	}

    /**
     * Create an httpRequest and execute it
     * @param clientUrl endpoint to send the request
     * @param httpMethod http method (POST|GET)
     * @param httpEntity http entity to send in the request
     * @param authenticationInfo authentication info to authorize the request
     * @param parameters http query parameters
     * @param responseHeaders headers obtained from the server response to this request
     * @return the body of the response to this request
     * @throws IOException
     * @throws HttpException
     */
	private HttpEntity createAndExecuteRequest (String clientUrl, RestHttpMethod httpMethod,
			HttpEntity httpEntity, AuthenticationInfo authenticationInfo, HttpQueryParams parameters, HashMap<String, String> responseHeaders) throws IOException, HttpException {
		return createAndExecuteRequest(clientUrl, httpMethod, httpEntity, authenticationInfo, parameters, null, responseHeaders);
	}

    /**
     * Create an httpRequest with headers and execute it
     * @param clientUrl endpoint to send the request
     * @param httpMethod http method (POST|GET)
     * @param httpEntity http entity to send in the request
     * @param authenticationInfo authentication info to authorize the request
     * @param parameters http query parameters
     * @param requestHeaders headers to include in the request
     * @param responseHeaders headers obtained from the server response to this request
     * @return the body of the response to this request
     * @throws IOException
     * @throws HttpException
     */
	private HttpEntity createAndExecuteRequest (String clientUrl, RestHttpMethod httpMethod,
			HttpEntity httpEntity, AuthenticationInfo authenticationInfo, HttpQueryParams parameters, HashMap<String, String> requestHeaders,
			HashMap<String, String> responseHeaders) throws IOException, HttpException {

		HttpResponse response = null;

		HttpClient client = lazyInitHttpClient();
		int redirectsLeft = MAX_REDIRECTS_COUNT;

		String url;
		if (parameters != null)
			url = parameters.generateQueryUrl(clientUrl);
		else url = clientUrl;
		
		//We must follow redirects ourselves, since we want to follow redirects even on POSTs, which
		// the HTTP library does not do.
		while (redirectsLeft > 0) {
			//Verify Uri
			URI uri = null;
			try {
				uri = new URI(url);
			} catch (URISyntaxException use) {
				Log.d(TAG, "Unable to parse " + url + " as URI.", use);
				throw new IOException("Unable to parse " + url
						+ " as URI.");
			}

			//Create the request
			HttpUriRequest request = null;
			switch (httpMethod) {
			case POST:
				request = new HttpPost(uri);
				//include the entity
				if (httpEntity != null) {
					((HttpPost)request).setEntity(httpEntity);
				}

				break;
			case GET:
				request = new HttpGet(uri);
			}

			//Add request headers
			if (requestHeaders != null){
				Iterator<Entry<String, String>> headerSet = requestHeaders.entrySet().iterator();
				while (headerSet.hasNext()){
					Entry<String,String> thisHeader = headerSet.next();
					request.addHeader(thisHeader.getKey(), thisHeader.getValue());
				}
			}

			//Secutiry credentials
			addAuthenticationInfo(authenticationInfo, request);

			//Execute the request
			response = null;
			try {
				response = client.execute(request);
			} catch (IOException ioe) {
				Log.w(TAG, "Unable to execute HTTP request to ." + uri + " Msg: "+ ioe, ioe);
				throw ioe;
			}

			//Analyze the response
			StatusLine statusLine = response.getStatusLine();
			if (statusLine == null) {
				Log.w(TAG, "StatusLine is null.");
				throw new NullPointerException("StatusLine is null -- should not happen.");
			}

			Log.d(TAG, response.getStatusLine().toString());
			for (Header h : response.getAllHeaders()) {
				Log.d(TAG, h.getName() + ": " + h.getValue());
				if (responseHeaders!= null && h != null && h.getName() != null && h.getValue() != null)
					responseHeaders.put(h.getName(), h.getValue());
			}

			int status = statusLine.getStatusCode();
			HttpEntity responseHttpEntity = response.getEntity();

			if ((status >= 200) && (status < 300)) {
				//Log.e(TAG, convertStreamToString(responseHttpEntity.getContent()));
				return responseHttpEntity;
			} else if (status == 302)  { //manage redirects
				if (responseHttpEntity != null) responseHttpEntity.consumeContent(); //Otherwise, the http lib cannot close connection
				Header location = response.getFirstHeader("Location");
				if (location == null) {
					Log.d(TAG, "Redirect requested but no Location "
							+ "specified.");
					throw new HttpException ("Http 302 Response Error: Redirect requested but no Location Header found", HttpException.BAD_REQUEST_EXCEPTION, null);
				}

				Log.d(TAG, "Following redirect to " + location.getValue());

				//Add default query parameters
				url = parameters.generateQueryUrl(location.getValue());
			} else {
				String errorMessage = null;
				String completeMessage = null;
				
				if (responseHttpEntity != null){
					InputStream is = responseHttpEntity.getContent();
					
					//TODO do this in a better way
					if (uri.toString().contains("REST"))
						errorMessage = XmlParserSerializerUtils.parseRestError(is);
					else if (uri.toString().contains("RPC"))
						errorMessage = XmlParserSerializerUtils.parseRpcError(is);
						
					Log.w(TAG, "ERROR: " + errorMessage);
					if (is != null)
						is.close();
				}
				
				switch (status) {
				case 400:
					completeMessage = "Http 400 Error: Bad Request: ";
					if (errorMessage != null)
						completeMessage += errorMessage;
					else completeMessage += "The request could not be understood by the server. " +
							"The client SHOULD NOT repeat the request without modifications.";
					throw new HttpException (completeMessage, HttpException.BAD_REQUEST, null);
				case 401:
					completeMessage = "Http 401 Error: Unauthorized: ";
					if (errorMessage != null)
						completeMessage += errorMessage;
					else completeMessage += "The request requires user authentication.";
					throw new HttpException (completeMessage, HttpException.UNAUTHORIZED, null);
				case 403:
					completeMessage = "Http 403: Forbidden: ";
					if (errorMessage != null)
						completeMessage += errorMessage;
					else completeMessage += "The server understood the request, but is refusing to fulfil it.";
					throw new HttpException (completeMessage, HttpException.FORBIDDEN, null);
				case 404:
					completeMessage = "Http 404: Not Found: ";
					if (errorMessage != null)
						completeMessage += errorMessage;
					else completeMessage += "The server has not found anything matching the Request.";
					throw new HttpException (completeMessage, HttpException.NOT_FOUND, null);
				case 408:
					completeMessage = "Http 408: Connection Timed out";
					if (errorMessage != null)
						completeMessage += ": " + errorMessage;
					else completeMessage += ".";
					throw new HttpException (completeMessage, HttpException.REQUEST_TIME_OUT, null);
				case 500:
					completeMessage = "Http 500: Internal Server Error";
					if (errorMessage != null)
						completeMessage += ": " + errorMessage;
					else completeMessage += ".";
					throw new HttpException (completeMessage, HttpException.INTERNAL_SERVER_ERROR, null);
				default:
					completeMessage = "Not Expected Http Error with code " + status;
					if (errorMessage != null)
						completeMessage += ": " + errorMessage;
					else completeMessage += ".";
					throw new HttpException (completeMessage, status, null);
				}
			}
			redirectsLeft --;
		}

		return null;
	}


	/**
	 * Looks at sLocale and mContext and returns current UserAgent String.
	 * This method is a copy of the one in WebSettings.java
	 * @return Current UserAgent String.
	 */
	/* FIXME: quitamos el metodo por ahora ya que no se usa, para converger
	 * en la interfaz publica con la parte de Linux.
	 * Descomentar si es necesario.
	 * 
    private synchronized String getCurrentUserAgent() {
        Locale locale = Locale.getDefault();

        StringBuffer buffer = new StringBuffer();
        // Add version
        final String version = Build.VERSION.RELEASE;
        if (version.length() > 0) {
            buffer.append(version);
        } else {
            // default to "1.0"
            buffer.append("1.0");
        }
        buffer.append("; ");
        final String language = locale.getLanguage();
        if (language != null) {
            buffer.append(language.toLowerCase());
            final String country = locale.getCountry();
            if (country != null) {
                buffer.append("-");
                buffer.append(country.toLowerCase());
            }
        } else {
            // default to "en"
            buffer.append("en");
        }
        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            final String model = Build.MODEL;
            if (model.length() > 0) {
                buffer.append("; ");
                buffer.append(model);
            }
        }
        final String id = Build.ID;
        if (id.length() > 0) {
            buffer.append(" Build/");
            buffer.append(id);
        }
        final String base = mContext.getResources().getText(
                com.android.internal.R.string.web_user_agent).toString();
        return String.format(base, buffer);
    }
	 */

	/**
	 * Disables the validation of certificates for SSL connections.
	 */
	private DefaultHttpClient createHttpClientWithNoCertificateValidation(HttpParams params) {
		// Create a specific protocol socket factory, for https, based on java default
		// socket factories.
		final SocketFactory sslSocketFactory = new SSLForNonValidCertsSocketFactory();

		// Install the all-trusting host verifier
		//sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		SchemeRegistry sr = new SchemeRegistry();
		sr.register(new Scheme("https", sslSocketFactory, 8443));
		sr.register(new Scheme("https", sslSocketFactory, 443));
		sr.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));
		sr.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, sr);
		return new DefaultHttpClient(manager, params);
	}

	/**
	 * Enables the validation of certificates for SSL connections.
	 */
	private DefaultHttpClient createHttpClientWithCertificateValidation(HttpParams params) {
		// Create a specific protocol socket factory, for https, based on java default
		// socket factories.
		SchemeRegistry sr = new SchemeRegistry();
		sr.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 8443));
		sr.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		sr.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));
		sr.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

		// Install the all-trusting host verifier
		//sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, sr);
		return new DefaultHttpClient(manager, params);
	}

    /**
     * Composes an endpoint URL appending the provided URL to the stored base url
     * @param feedUrl the url to append to the stored base url
     * @return the composed URL
     */
	private String composeUrl(String feedUrl){
		return GSDP_BASE_URL + feedUrl;
	}

    /**
     * Add authentication headers to the request
     * @param info the authentication headers
     * @param request the http request to add the headers to
     */
	private void addAuthenticationInfo(AuthenticationInfo info, HttpRequest request){
		if (info != null){
			info.setCredentials(mHttpClient, request);
		}
	}

    /**
     * Get media content from an uri as an input stream
     * @param uri the endpoint to get the media stream from
     * @return the input stream with the media contents 
     */
	public InputStream getMediaContentAsStream(String uri) throws IOException, HttpException {
		HttpEntity res = null;

		res = createAndExecuteRequest(uri, RestHttpMethod.GET, null, null, null, null);

		if (res != null) {
			return res.getContent();
		}
		return null;
	}
	
	/**
	 * 
     * Get media content from an uri as an byte array data source
     * 
     * @param uri the endpoint to get the media stream from
	 * @return the content from the uri as an byte array data source
	 * @throws IOException
	 * @throws HttpException
	 */
	public ByteArrayDataSource getMediaContentAsDataSource(String uri, AuthenticationInfo authenticationInfo, HttpQueryParams parameters) throws IOException, HttpException {
		HttpEntity res = null;

		String feedUrl = composeUrl(uri);
		res = createAndExecuteRequest(feedUrl, RestHttpMethod.GET, null, authenticationInfo, parameters, null);

		if (res != null) {
			return new ByteArrayDataSource(res.getContent(), res.getContentType().getValue());
		}
		return null;
	}

}

///@endcond