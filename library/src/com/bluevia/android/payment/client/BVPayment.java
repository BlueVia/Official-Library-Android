/**
 * \package com.bluevia.android.payment.client This package contains the clients of Bluevia Payment Service
 */
package com.bluevia.android.payment.client;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import oauth.signpost.OAuthProviderListener;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.http.HttpResponse;

import com.bluevia.android.commons.Encoding;
import com.bluevia.android.commons.Entity;
import com.bluevia.android.commons.GsdpConstants;
import com.bluevia.android.commons.connector.http.oauth.BVAuthorizationHeaderSigningStrategy;
import com.bluevia.android.commons.connector.http.oauth.IOAuth;
import com.bluevia.android.commons.connector.http.oauth.OAuthToken;
import com.bluevia.android.commons.connector.http.oauth.RequestToken;
import com.bluevia.android.commons.data.xmlrpc.MethodCall;
import com.bluevia.android.commons.data.xmlrpc.MethodResponse;
import com.bluevia.android.commons.exception.BlueviaException;
import com.bluevia.android.commons.parser.xml.XmlConstants;
import com.bluevia.android.oauth.client.BVOauthClient;
import com.bluevia.android.payment.data.GetPaymentStatusParams;
import com.bluevia.android.payment.data.MakePaymentParams;
import com.bluevia.android.payment.data.PaymentInfo;
import com.bluevia.android.payment.data.PaymentRequestTokenParams;
import com.bluevia.android.payment.data.PaymentResult;
import com.bluevia.android.payment.data.PaymentStatus;
import com.bluevia.android.payment.data.ServiceInfo;
import com.bluevia.android.payment.parser.PaymentSerializer;
import com.bluevia.android.payment.parser.xml.XmlPaymentParser;

/**
 *  This class provides access to the set of functions which any user could use to access
 *  the Payment service functionality
 *
 * @author Telefonica R&D
 *
 */
public class BVPayment extends BVOauthClient {

	private static final String FEED_PAYMENT_BASE_URI = "/RPC/Payment";

	protected static final String FEED_PAYMENT = "/payment";
	protected static final String FEED_CANCEL_AUTHORIZATION = "/cancelAuthorization";
	protected static final String FEED_GET_PAYMENT_STATUS = "/getPaymentStatus";

	protected static final String METHOD_PAYMENT = "PAYMENT";
	protected static final String METHOD_CANCEL_AUTHORIZATION = "CANCEL_AUTHORIZATION";
	protected static final String METHOD_GET_PAYMENT_STATUS = "GET_PAYMENT_STATUS";

	protected static final String OAUTH_API_NAME = "Payment";
	protected static final String OAUTH_API_NAME_SANDBOX = "Payment_Sandbox";

	protected static final String HEADER_TIMESTAMP = "oauth_timestamp";

	/**
	 * Constructor 
	 * 
	 * @param mode
	 * @param consumerToken
	 * @param consumerSecret
	 * @throws BlueviaException
	 */
	public BVPayment(Mode mode, String consumerToken, String consumerSecret) throws BlueviaException {
		super(mode, consumerToken, consumerSecret);	//Calling super to initialize OauthClient

		if (mode == Mode.TEST)
			throw new BlueviaException("Payment API only available in LIVE and SANDBOX mode", BlueviaException.INVALID_MODE_EXCEPTION);
		
		mEncoding = Encoding.APPLICATION_XML;
		mParser = new XmlPaymentParser();
		mSerializer = new PaymentSerializer();

		mBaseUri += buildUri(mMode, FEED_PAYMENT_BASE_URI);	//Building base uri
	}

	/**
	 * Gets a RequestToken for a Payment operation
	 * 
	 * @param amount  the cost of the digital good being sold, expressed in the minimum fractional monetary unit of the currency reflected in the next parameter (to avoid decimal digits). 
	 * @param currency the currency of the payment, following ISO 4217 (EUR, GBP, MXN, etc.). 
	 * @param name the name of the service for the payment
	 * @param serviceId the id of the service for the payment
	 * @return the request token
	 * @throws BlueviaException
	 */
	public RequestToken getPaymentRequestToken(int amount, String currency, String name, String serviceId) throws BlueviaException {

		//Reset token 
		((IOAuth)mConnector).setOauthToken(null);

		ServiceInfo serviceInfo = new ServiceInfo(serviceId, name);
		PaymentInfo info = new PaymentInfo(amount, currency);

		PaymentRequestTokenParams request = new PaymentRequestTokenParams(serviceInfo, info);

		if (!request.isValid())
			throw new BlueviaException("Bad request: Please, check function parameters", BlueviaException.BAD_REQUEST_EXCEPTION);

		byte[] data = serializeEntity(request);

		OAuthProviderListener listener = new BVPayentOauthProviderListener();

		return super.getRequestToken(null, data, listener);
	}

	/**
	 * OAuthProviderListener to manage the body data in the getPaymentRequestToken operation.
	 *
	 */
	private class BVPayentOauthProviderListener implements OAuthProviderListener {

		@Override
		public void prepareSubmission(HttpRequest request) throws Exception {}

		@Override
		public void prepareRequest(HttpRequest request) throws Exception {

			//Include apiName header
			String apiName = "";
			switch (mMode){
			case LIVE:
			case TEST:
				apiName = OAUTH_API_NAME;
				break;
			case SANDBOX:
				apiName = OAUTH_API_NAME_SANDBOX;
				break;
			}

			request.setHeader("Authorization", "OAuth " + 
					BVAuthorizationHeaderSigningStrategy.OAUTH_API_NAME + "=\"" + 
					apiName + "\"");
		}

		@Override
		public boolean onResponseReceived(HttpRequest request, HttpResponse response) throws Exception {
			return false;
		}
	}

	/**
	 * Gets an access token for a Payment operation
	 * 
	 * @param oauthVerifier the OAuth verifier for the token
	 * @param token the request token previously obtained
	 * @param secret request token secret
	 * @return the access token
	 * @throws BlueviaException
	 */
	public OAuthToken getPaymentAccessToken(String oauthVerifier, String token, String secret) throws BlueviaException {
		OAuthToken accessToken = super.getAccessToken(oauthVerifier, token, secret);
		((IOAuth)mConnector).setOauthToken(accessToken);
		return accessToken;
	}


	/**
	 * 
	 * Orders an economic charge on the user's operator account.
	 * 
	 * @param amount Amount to be charged, it may be an economic amount or a number of 'virtual units' (points, tickets, etc) (mandatory).
	 * @param currency Type of currency which corresponds with the amount above, following ISO 4217 (mandatory).
	 * @return Result of the payment operation.
	 * @throws BlueviaException An exception informing about errors encountered in the request/response.
	 * @throws IOException 
	 */
	public PaymentResult payment(int amount, String currency) throws BlueviaException, IOException {
		return payment(amount, currency, null, null);
	}

	/**
	 * 
	 * Orders an economic charge on the user's operator account.
	 * @param amount Amount to be charged, it may be an economic amount or a number of 'virtual units' (points, tickets, etc) (mandatory).
	 * @param currency Type of currency which corresponds with the amount above, following ISO 4217 (mandatory).
	 * @param endpoint the endpoint to receive notifications of the payment operation
	 * @param correlator the correlator
	 * @return Result of the payment operation.
	 * @throws BlueviaException An exception informing about errors encountered in the request/response.
	 * @throws IOException 
	 */
	public PaymentResult payment(int amount, String currency, String endpoint, String correlator) throws BlueviaException, IOException {

		Entity response;
		Entity responseEntity = null;

		PaymentInfo paymentInfo = new PaymentInfo(amount, currency);

		//Timestamp
		Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = new Date();
		String timeStamp = formatter.format(date);
		String millis = String.valueOf(date.getTime()/1000);

		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put(HEADER_TIMESTAMP, millis);
		((IOAuth)mConnector).setAdditionalParameters(additionalParams);

		MakePaymentParams params = new MakePaymentParams(timeStamp, paymentInfo, endpoint, correlator);

		MethodCall methodCall = new MethodCall();
		methodCall.setMethod(METHOD_PAYMENT).setParamsEntity(params);

		if (!methodCall.isValid())
			throw new BlueviaException("Bad request:  Please, check function parameters", BlueviaException.BAD_REQUEST_EXCEPTION);

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put(GsdpConstants.HEADER_CONTENT_TYPE, getEncoding(mEncoding));

		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		response = this.create(FEED_PAYMENT, methodCall, queryParams, headers);

		if ((response == null) || (!(response instanceof MethodResponse)))
			throw new BlueviaException( "Error during request. Response received does not correspond to a " + 
					MethodResponse.class.getName(),	BlueviaException.INTERNAL_CLIENT_ERROR);

		responseEntity = ((MethodResponse) response).getResponseEntity();

		if ((responseEntity == null) || (!(responseEntity instanceof PaymentResult)))
			throw new BlueviaException("Error during request. Response received does not correspond to a " + 
					PaymentResult.class.getName(), BlueviaException.INTERNAL_CLIENT_ERROR);

		PaymentResult paymentResult = (PaymentResult) responseEntity;

		return paymentResult;
	}

	/**
	 * Merchant can use this operation to invalidate the access token of the session.
	 * If the payment has been made before, it remains valid, but no more getPaymentStatus operations will be enabled.
	 * 
	 * @throws IOException 
	 * @throw BlueviaException An exception informing about errors encountered in the request/response.
	 */
	public void cancelAuthorization() throws BlueviaException, IOException {

		MethodCall methodCall = new MethodCall();
		methodCall.setMethod(METHOD_CANCEL_AUTHORIZATION);

		if (!methodCall.isValid())
			throw new BlueviaException("Bad request:  Please, check function parameters", BlueviaException.BAD_REQUEST_EXCEPTION);

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put(GsdpConstants.HEADER_CONTENT_TYPE, getEncoding(mEncoding));

		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		Entity response = this.create(FEED_CANCEL_AUTHORIZATION, methodCall, queryParams, headers);

		if ((response == null) || (!(response instanceof MethodResponse)))
			throw new BlueviaException( "Error during request. Response received does not correspond to an " + 
					MethodResponse.class.getName(), BlueviaException.INTERNAL_CLIENT_ERROR);
	}

	/**
	 * Retrieves the status of a previous payment operation
	 * 
	 * @param transactionId the id of the transaction
	 * @return the status of the payment
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public PaymentStatus getPaymentStatus(String transactionId) throws BlueviaException, IOException{

		GetPaymentStatusParams params = new GetPaymentStatusParams(transactionId);

		MethodCall methodCall = new MethodCall();
		methodCall.setMethod(METHOD_GET_PAYMENT_STATUS).setParamsEntity(params);

		if (!methodCall.isValid())
			throw new BlueviaException("Bad request:  Please, check function parameters", BlueviaException.BAD_REQUEST_EXCEPTION);

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put(GsdpConstants.HEADER_CONTENT_TYPE, getEncoding(mEncoding));

		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		Entity response = null;

		response = this.create(FEED_GET_PAYMENT_STATUS, methodCall, queryParams, headers);

		if ((response == null) || (!(response instanceof MethodResponse)))
			throw new BlueviaException( "Error during request. Response received does not correspond to a " + 
					MethodResponse.class.getName(), BlueviaException.INTERNAL_CLIENT_ERROR);

		Entity responseEntity = ((MethodResponse) response).getResponseEntity();

		if ((responseEntity == null) || (!(responseEntity instanceof PaymentStatus)))
			throw new BlueviaException("Error during request. Response received does not correspond to a " + 
					PaymentStatus.class.getName(), BlueviaException.INTERNAL_CLIENT_ERROR);

		PaymentStatus paymentStatus = (PaymentStatus) responseEntity;

		return paymentStatus;
	}

	/**
	 * Sets the access token of the session
	 * This functions is used to change the token of the session to be able to get the
	 * payment status of an old operation, or cancel the authorization of a token.
	 * 
	 * @param accessToken
	 */
	public void setAccessToken(OAuthToken accessToken){
		((IOAuth)mConnector).setOauthToken(accessToken);
	}

}
