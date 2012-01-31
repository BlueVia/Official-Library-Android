package com.bluevia.android.rest.payment.client;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.Log;

import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.commons.Entity;
import com.bluevia.android.rest.commons.client.AbstractRestClient;
import com.bluevia.android.rest.commons.client.AbstractRestClient.Mode;
import com.bluevia.android.rest.commons.connector.IConnector;
import com.bluevia.android.rest.commons.connector.http.HttpConnector;
import com.bluevia.android.rest.commons.connector.http.HttpQueryParams;
import com.bluevia.android.rest.commons.connector.mock.BlueviaPaymentMockConnector;
import com.bluevia.android.rest.commons.data.xmlrpc.MethodCall;
import com.bluevia.android.rest.commons.data.xmlrpc.MethodResponse;
import com.bluevia.android.rest.oauth.Oauth;
import com.bluevia.android.rest.oauth.client.BlueviaOauthClient;
import com.bluevia.android.rest.oauth.data.RequestToken;
import com.bluevia.android.rest.oauth.data.Token;
import com.bluevia.android.rest.payment.data.GetPaymentStatusParams;
import com.bluevia.android.rest.payment.data.MakePaymentParams;
import com.bluevia.android.rest.payment.data.PaymentInfo;
import com.bluevia.android.rest.payment.data.PaymentRequestTokenParams;
import com.bluevia.android.rest.payment.data.PaymentResult;
import com.bluevia.android.rest.payment.data.PaymentStatus;
import com.bluevia.android.rest.payment.data.ServiceInfo;
import com.bluevia.android.rest.payment.parser.BlueviaPaymentParserFactory;

/**
 * @class BlueviaPaymentClient
 * This class provides access to the set of functions which any user could use to access
 * the Bluevia Payment Service functionality
 *
 * @author Telefonica R&D
 * 
 */ 
public class BlueviaPaymentClient extends AbstractRestClient {

	private static final String TAG = "BlueviaPaymentClient";

	private static final String FEED_PAYMENT_BASE_URI_BLUEVIA = "RPC/Payment";
	private static final String FEED_PAYMENT_SANDBOX_BASE_URI = "RPC/Payment_Sandbox";

	private static final String FEED_PAYMENT = "/payment";
	private static final String FEED_CANCEL_AUTHORIZATION = "/cancelAuthorization";
	private static final String FEED_GET_PAYMENT_STATUS = "/getPaymentStatus";

	private static final String METHOD_PAYMENT = "PAYMENT";
	private static final String METHOD_CANCEL_AUTHORIZATION = "CANCEL_AUTHORIZATION";
	private static final String METHOD_GET_PAYMENT_STATUS = "GET_PAYMENT_STATUS";

	private static final String OAUTH_API_NAME = "Payment";
	private static final String OAUTH_API_NAME_SANDBOX = "Payment_Sandbox";

	private BlueviaPaymentOauthClient mOauthClient;
	
	private String mApiName;

	/**
	 * Creates a PaymentClient object to be able to do payments through the gSDP
	 * 
	 * @param context the Android context of the application.
	 * @param mode the communication mechanism to communicate with the gSDP
	 * @param consumerKey The oauth consumer key supplied by the developer.
	 * @param consumerSecret The oauth consumer secret supplied by the developer.
	 * @throws BlueviaClientException if an exception occurs during the request.
	 * 
	 */
	public BlueviaPaymentClient(Context context, AbstractRestClient.Mode mode, String consumerKey, String consumerSecret) throws BlueviaClientException {
		super(context, (mode != Mode.SANDBOX) ? FEED_PAYMENT_BASE_URI_BLUEVIA : FEED_PAYMENT_SANDBOX_BASE_URI, 
				isHttpMode(mode)? new HttpConnector() : new BlueviaPaymentMockConnector(mode.ordinal()), 
						new Oauth(consumerKey, consumerSecret), new BlueviaPaymentParserFactory());

		mOauthClient = new BlueviaPaymentOauthClient(context, mode, mConnector, consumerKey, consumerSecret);
	
		if (mode == Mode.SANDBOX)
			mApiName = OAUTH_API_NAME_SANDBOX;
		else mApiName = OAUTH_API_NAME;
	}
	
	/**
	 * Creates a PaymentClient object to be able to do payments through the gSDP
	 * 
	 * @param context the Android context of the application.
	 * @param mode the communication mechanism to communicate with the gSDP
	 * @param consumerKey The oauth consumer key supplied by the developer.
	 * @param consumerSecret The oauth consumer secret supplied by the developer.
	 * @throws BlueviaClientException if an exception occurs during the request.
	 * @param accessToken The oauth access token returned by the getAccessToken call.
	 * 
	 */
	public BlueviaPaymentClient(Context context, AbstractRestClient.Mode mode, String consumerKey, String consumerSecret, Token accessToken) throws BlueviaClientException {
		super(context, (mode != Mode.SANDBOX) ? FEED_PAYMENT_BASE_URI_BLUEVIA : FEED_PAYMENT_SANDBOX_BASE_URI, 
				isHttpMode(mode) ? new HttpConnector() : new BlueviaPaymentMockConnector(mode.ordinal()), 
						new Oauth(consumerKey, consumerSecret, accessToken), new BlueviaPaymentParserFactory());

		mOauthClient = new BlueviaPaymentOauthClient(context, mode, mConnector, consumerKey, consumerSecret);
	
		if (mode == Mode.SANDBOX)
			mApiName = OAUTH_API_NAME_SANDBOX;
		else mApiName = OAUTH_API_NAME;
	}

	/**
	 * 
	 * Gets the Request Token
	 * 
	 * @param amount
	 * @param currency
	 * @return
	 * @throws BlueviaClientException
	 */
	public RequestToken getRequestToken(Integer amount, String currency, ServiceInfo serviceInfo) throws BlueviaClientException {

		//Reset token 
		((Oauth)mAuthInfo).setOauthToken(null);
		
		PaymentInfo info = buildPaymentInfo(amount, currency);

		PaymentRequestTokenParams request = new PaymentRequestTokenParams(serviceInfo, info);

		if (!request.isValid())
			throw new BlueviaClientException("Invalid parameters. Please, check function parameters",
					BlueviaClientException.BAD_REQUEST_EXCEPTION);

		return mOauthClient.getRequestToken(null, mApiName, request);
	}

	/**
	 * 
	 * Gets the AccessToken
	 * Additionally the client stores the access token for previous payment operations.
	 *
	 * @param oauthToken the oauthToken
	 * @param oauthTokenSecret the oauthTokenSecret
	 * @param oauthVerifier the oauthVerifier
	 * @return the AccessToken
	 * @throws BlueviaClientException if an exception occurs during the request.
	 */
	public Token getAccessToken(String oauthToken, String oauthTokenSecret, String oauthVerifier) throws BlueviaClientException{

		Token token = mOauthClient.getAccessToken(oauthToken, oauthTokenSecret, oauthVerifier);

		//Store the token
		((Oauth)mAuthInfo).setOauthToken(token);

		return token;
	}

	/**
	 * 
	 * Orders an economic charge on the user's operator account.
	 * @param amount Amount to be charged, it may be an economic amount or a number of 'virtual units' (points, tickets, etc) (mandatory).
	 * @param currency Type of currency which corresponds with the amount above, following ISO 4217 (mandatory).
	 * @return Result of the payment operation.
	 * @throws BlueviaClientException An exception informing about errors encountered in the request/response.
	 */
	public PaymentResult payment(Integer amount, String currency) throws BlueviaClientException {
		return payment(amount, currency, null, null);
	}

	/**
	 * 
	 * Orders an economic charge on the user's operator account.
	 * @param amount Amount to be charged, it may be an economic amount or a number of 'virtual units' (points, tickets, etc) (mandatory).
	 * @param currency Type of currency which corresponds with the amount above, following ISO 4217 (mandatory).
	 * 
	 * TODO
	 * 
	 * @return Result of the payment operation.
	 * @throws BlueviaClientException An exception informing about errors encountered in the request/response.
	 */
	public PaymentResult payment(Integer amount, String currency, String endpoint, String correlator) throws BlueviaClientException {

		Entity response;
		Entity responseEntity = null;

		PaymentInfo paymentInfo = buildPaymentInfo(amount, currency);

		//Timestamp
		Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = new Date();
		String timeStamp = formatter.format(date);
		String millis = String.valueOf(date.getTime()/1000);

		HttpQueryParams additionalParams = new HttpQueryParams();
		additionalParams.addParameter(Oauth.HEADER_TIMESTAMP, millis);
		((Oauth)mAuthInfo).setAdditionalParameters(additionalParams);

		MakePaymentParams params = new MakePaymentParams(timeStamp, paymentInfo, endpoint, correlator);

		MethodCall methodCall = new MethodCall();
		methodCall.setMethod(METHOD_PAYMENT).setParamsEntity(params);

		if (!methodCall.isValid())
			throw new BlueviaClientException("Invalid parameters. Please, check function parameters",
					BlueviaClientException.BAD_REQUEST_EXCEPTION);

		String uri = mBaseUri + FEED_PAYMENT;

		try {
			response = this.createEntity(uri, methodCall);
		} catch (IOException iox) {
			Log.e(TAG, "Error during IO", iox);
			throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);
		}

		if ((response == null) || (!(response instanceof MethodResponse)))
			throw new BlueviaClientException(
					"Error during request. Response received does not correspond to a MethodResponse",
					BlueviaClientException.INTERNAL_CLIENT_ERROR);

		responseEntity = ((MethodResponse) response).getResponseEntity();

		if ((responseEntity == null) || (!(responseEntity instanceof PaymentResult)))
			throw new BlueviaClientException("Error during request. Response received does not correspond to a PaymentResult",
					BlueviaClientException.INTERNAL_CLIENT_ERROR);

		PaymentResult paymentResult = (PaymentResult) responseEntity;

		return paymentResult;
	}

	/**
	 * Merchant can use this operation instead of payment, to cancel a previously authorized purchase.
	 * @param none
	 * @return none
	 * @throw BlueviaClientException An exception informing about errors encountered in the request/response.
	 */
	public void cancelAuthorization() throws BlueviaClientException {

		MethodCall methodCall = new MethodCall();
		methodCall.setMethod(METHOD_CANCEL_AUTHORIZATION);

		if (!methodCall.isValid())
			throw new BlueviaClientException("Invalid parameters. Please, check function parameters",
					BlueviaClientException.BAD_REQUEST_EXCEPTION);

		String uri = mBaseUri + FEED_CANCEL_AUTHORIZATION;

		try {
			Entity response = this.createEntity(uri, methodCall);

			if ((response == null) || (!(response instanceof MethodResponse)))
				throw new BlueviaClientException(
						"Error during request. Response received does not correspond to an MethodResponse",
						BlueviaClientException.INTERNAL_CLIENT_ERROR);

		} catch (IOException iox) {
			Log.e(TAG, "Error during IO", iox);
			throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);			
		}	
	}
	
	public PaymentStatus getPaymentStatus(String transactionId) throws BlueviaClientException{
		
		GetPaymentStatusParams params = new GetPaymentStatusParams(transactionId);
		
		MethodCall methodCall = new MethodCall();
		methodCall.setMethod(METHOD_GET_PAYMENT_STATUS).setParamsEntity(params);
		
		if (!methodCall.isValid())
			throw new BlueviaClientException("Invalid parameters. Please, check function parameters",
					BlueviaClientException.BAD_REQUEST_EXCEPTION);

		String uri = mBaseUri + FEED_GET_PAYMENT_STATUS;

		Entity response = null;
		
		try {
			response = this.createEntity(uri, methodCall);
		} catch (IOException iox) {
			Log.e(TAG, "Error during IO", iox);
			throw new BlueviaClientException("Error during IO", BlueviaClientException.INTERNAL_CLIENT_ERROR);
		}

		if ((response == null) || (!(response instanceof MethodResponse)))
			throw new BlueviaClientException(
					"Error during request. Response received does not correspond to a MethodResponse",
					BlueviaClientException.INTERNAL_CLIENT_ERROR);

		Entity responseEntity = ((MethodResponse) response).getResponseEntity();

		if ((responseEntity == null) || (!(responseEntity instanceof PaymentStatus)))
			throw new BlueviaClientException("Error during request. Response received does not correspond to a PaymentResult",
					BlueviaClientException.INTERNAL_CLIENT_ERROR);

		PaymentStatus paymentStatus = (PaymentStatus) responseEntity;
		
		return paymentStatus;
	}
	
	/**
	 * Sets the token of the session
	 * 
	 * @param accessToken
	 */
	public void setAccessToken(Token accessToken){
		((Oauth)mAuthInfo).setOauthToken(accessToken);
	}
	
	/**
	 * 
	 * @param amount
	 * @param currency
	 * @return
	 * @throws BlueviaClientException
	 */
	private PaymentInfo buildPaymentInfo(Integer amount, String currency) throws BlueviaClientException {
		//Check mandatory params
		if ((amount == null) || (currency == null) || ((currency != null) && (currency.trim().length() == 0)) )
			throw new BlueviaClientException("Remember amount and currency are mandatory and cannot be null or empty", 
					BlueviaClientException.BAD_REQUEST_EXCEPTION);

		return new PaymentInfo(amount, currency);
	}

	/**
	 * Creates an PaymentClient with a specific IConnector object
	 * 
	 * @param context the Android context of the application.
	 * @param mode TODO
	 * @param connector the connector for communication mechanism
	 * @param consumerKey The oauth consumer key supplied by the developer.
	 * @param consumerSecret The oauth consumer secret supplied by the developer.
	 * @throws BlueviaClientException if an exception occurs during the request.
	 */
	protected BlueviaPaymentClient(Context context, Mode mode, IConnector connector, String consumerKey, String consumerSecret) throws BlueviaClientException {
		super(context, FEED_PAYMENT_BASE_URI_BLUEVIA, connector, new Oauth(consumerKey, consumerSecret), new BlueviaPaymentParserFactory());

		mOauthClient = new BlueviaPaymentOauthClient(context, mode, connector, consumerKey, consumerSecret);
	}

	/**
	 * TODO
	 */
	private class BlueviaPaymentOauthClient extends BlueviaOauthClient {
		private BlueviaPaymentOauthClient(Context context, Mode mode, IConnector connector, String consumerKey, String consumerSecret) throws BlueviaClientException{
			super(context, mode, connector, consumerKey, consumerSecret);
		}

		public RequestToken getRequestToken(String callback, String apiName, Entity request) throws BlueviaClientException {
			//Reset token 
			((Oauth)mAuthInfo).setOauthToken(null);
			return super.getRequestToken(callback, apiName, request);
		}
	}

}
