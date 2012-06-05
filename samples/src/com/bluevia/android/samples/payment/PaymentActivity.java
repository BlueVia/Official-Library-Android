package com.bluevia.android.samples.payment;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bluevia.android.commons.connector.http.oauth.OAuthToken;
import com.bluevia.android.commons.connector.http.oauth.RequestToken;
import com.bluevia.android.commons.exception.BlueviaException;
import com.bluevia.android.payment.client.BVPayment;
import com.bluevia.android.payment.data.PaymentResult;
import com.bluevia.android.payment.data.PaymentStatus;
import com.bluevia.android.payment.data.PaymentStatus.TransactionStatusType;
import com.bluevia.android.samples.ApplicationInfo;
import com.bluevia.android.samples.BlueviaAsyncTask;
import com.bluevia.android.samples.R;

public class PaymentActivity extends Activity {

	public static final String TAG = "PaymentActivity";

	//README: change this value for different currencies
	private static final String CURRENCY = "EUR";

	private EditText mAmount;
	private TextView mCurrency;
	private Button mRequestTokenButton;
	private EditText mVerifier;
	private Button mAccessTokenButton;
	private Button mPaymentButton;
	private Button mCancelButton;
	private EditText mTransactionId;
	private Button mGetStatusButton;
	private TextView mResult;
	private Button mClearButton;

	private BVPayment mClient;

	private RequestToken mRequestToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.payment_activity);

		mAmount = (EditText) findViewById(R.id.amount);
		mCurrency = (TextView) findViewById(R.id.currency);
		mRequestTokenButton = (Button) findViewById(R.id.get_payment_request_token);
		mVerifier = (EditText) findViewById(R.id.verifier);
		mAccessTokenButton = (Button) findViewById(R.id.get_payment_access_token);
		mPaymentButton = (Button) findViewById(R.id.pay);
		mCancelButton = (Button) findViewById(R.id.cancel);
		mTransactionId = (EditText) findViewById(R.id.transactionId);
		mGetStatusButton = (Button) findViewById(R.id.get_payment_status);
		mResult = (TextView) findViewById(R.id.result);
		mClearButton = (Button) findViewById(R.id.clear);

		mCurrency.setText(CURRENCY);

		mRequestTokenButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getRequestToken();
			}
		});

		mAccessTokenButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getAccessToken();
			}
		});

		mPaymentButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pay();
			}
		});

		mCancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cancel();
			}
		});

		mGetStatusButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getStatus();
			}
		});

		mClearButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				clear();
			}
		});

		createClient();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		//Close the client
		closeClient();
	}

	private void createClient(){

		closeClient();

		try {

			mClient = new BVPayment(ApplicationInfo.MODE, 
					ApplicationInfo.CONSUMER_KEY, 
					ApplicationInfo.CONSUMER_SECRET);

		} catch (BlueviaException e){
			Log.e(TAG, e.getMessage());
		}
	}

	private void closeClient(){
		if (mClient != null)
			mClient.close();
		mClient = null;
	}

	private void clear(){
		mVerifier.setText("");
		mAmount.setText("");
		mTransactionId.setText("");
		mResult.setText("");

		mRequestToken = null;
	}

	private void showResult(String result){
		if (result != null){
			mResult.setText(result);
		}
	}

	private class GetPaymentRequestTokenAsyncTask extends BlueviaAsyncTask<String, Void, RequestToken>{

		public GetPaymentRequestTokenAsyncTask(Context context) {
			super(context);
		}

		@Override
		protected RequestToken doInBackground(String... params) {
			RequestToken response = null;
			try {
				Integer amount = Integer.valueOf(params[0]);

				response = mClient.getPaymentRequestToken(amount, CURRENCY, ApplicationInfo.SERVICEID, ApplicationInfo.SERVICENAME);
			} catch (BlueviaException e){
				Log.e(TAG, e.getMessage(), e);
				errorMessage = e.getMessage();
			} catch (NumberFormatException e){
				Log.e(TAG, e.getMessage(), e);
				errorMessage = "Amount must be a number: " + params[0];
			}

			return response;
		}

		@Override
		protected void onPostExecute(RequestToken result) {
			super.onPostExecute(result);
			if (result != null) {
				onRequestTokenReceived(result);
			} else {
				showResult(errorMessage);
			}
		}
	}

	private void onRequestTokenReceived(RequestToken token){
		mRequestToken = token;

		Uri url = Uri.parse(mRequestToken.getVerificationUrl());
		Intent i = new Intent(Intent.ACTION_VIEW, url);
		startActivity(i);
	}

	private void getRequestToken(){

		//Reset token
		mRequestToken = null;

		String[] params = new String[1];
		params[0] = mAmount.getText().toString();
		new GetPaymentRequestTokenAsyncTask(this).execute(params);
	}


	private void getAccessToken(){

		if (mRequestToken == null){
			Toast.makeText(this, R.string.error_request_token_null, Toast.LENGTH_LONG).show();
		} else if (mVerifier.getText() == null || mVerifier.getText().toString().equals("")){
			Toast.makeText(this, R.string.error_pincode_null, Toast.LENGTH_LONG).show();
		} else {
			String param1 = mVerifier.getText().toString();
			String param2 = mRequestToken.getToken();
			String param3 = mRequestToken.getSecret();
			String[] params = {param1, param2, param3};
			new GetAccessTokenAsyncTask(this).execute(params);
		}
	}

	private class GetAccessTokenAsyncTask extends BlueviaAsyncTask<String, Void, OAuthToken>{

		public GetAccessTokenAsyncTask(Context context) {
			super(context);
		}

		@Override
		protected OAuthToken doInBackground(String... params) {
			String verifier = params[0];
			String token = params[1];
			String secret = params[2];
			OAuthToken response = null;
			try {
				response = mClient.getPaymentAccessToken(verifier, token, secret);
			} catch (BlueviaException e){
				Log.e(TAG, e.getMessage(), e);
				errorMessage = e.getMessage();
			}

			return response;
		}

		@Override
		protected void onPostExecute(OAuthToken result) {
			super.onPostExecute(result);
			if (result != null) {
				String s = "Token received: " + result.getToken() + " " + result.getSecret();
				Log.i(TAG, s);
				Toast.makeText(PaymentActivity.this, s, Toast.LENGTH_SHORT).show();
			} else {
				showResult(errorMessage);
			}
		}
	}


	private class PaymentAsyncTask extends BlueviaAsyncTask<String, Void, String> {
		public PaymentAsyncTask(Context context) {
			super(context);
		}

		@Override
		protected String doInBackground(String... params) {
			String transactionId = null;
			try {
				Integer amount = Integer.valueOf(params[0]);

				PaymentResult res = mClient.payment(amount, CURRENCY);
				transactionId = res.getTransactionId();

			} catch (BlueviaException e) {
				Log.e(TAG, e.getMessage());
				errorMessage = e.getMessage();
			} catch (NumberFormatException e){
				Log.e(TAG, e.getMessage());
				errorMessage = "Amount must be a number: " + params[0];
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
				errorMessage = e.getMessage();
			}

			return transactionId;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null){
				Toast.makeText(mContext, "Payment operation completed. TransactionId: " + result, Toast.LENGTH_LONG).show();
				mTransactionId.setText(result);
			} else showResult(errorMessage);
		}
	}

	private void pay(){
		String[] params = new String[1];
		params[0] = mAmount.getText().toString();
		new PaymentAsyncTask(this).execute(params);
	}

	private class CancelAuthorizationAsyncTask extends BlueviaAsyncTask<Void, Void, Boolean> {

		public CancelAuthorizationAsyncTask(Context context) {
			super(context);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				mClient.cancelAuthorization();
				return true;
			} catch (BlueviaException e) {
				Log.e(TAG, e.getMessage());
				errorMessage = e.getMessage();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
				errorMessage = e.getMessage();
			}
			return false;
		}

		/* (non-Javadoc)
		 * @see com.bluevia.android.tests.BlueviaAsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result){
				Toast.makeText(mContext, "Cancel operation canceled", Toast.LENGTH_LONG).show();
				showResult("Cancel operation canceled");
			} else showResult(errorMessage);
		}

	}

	private void cancel(){
		new CancelAuthorizationAsyncTask(this).execute();
	}

	private class GetStatusAsyncTask extends BlueviaAsyncTask<String, Void, TransactionStatusType> {
		public GetStatusAsyncTask(Context context) {
			super(context);
		}

		@Override
		protected TransactionStatusType doInBackground(String... params) {
			TransactionStatusType transactionStatus = null;
			try {
				String transactionId = params[0];

				PaymentStatus res = mClient.getPaymentStatus(transactionId);
				transactionStatus = res.getTransactionStatus();

			} catch (BlueviaException e) {
				Log.e(TAG, e.getMessage());
				errorMessage = e.getMessage();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
				errorMessage = e.getMessage();
			}

			return transactionStatus;
		}

		@Override
		protected void onPostExecute(TransactionStatusType result) {
			super.onPostExecute(result);
			if (result != null){
				mResult.setText(result.toString());
			} else showResult(errorMessage);
		}
	}

	private void getStatus(){
		String id = mTransactionId.getText().toString();
		new GetStatusAsyncTask(this).execute(id);
	}

}
