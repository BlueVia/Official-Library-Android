package com.bluevia.android.examples.oauth;

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

import com.bluevia.android.examples.ApplicationInfo;
import com.bluevia.android.examples.BlueviaAsyncTask;
import com.bluevia.android.examples.R;
import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.oauth.client.BlueviaOauthClient;
import com.bluevia.android.rest.oauth.data.RequestToken;
import com.bluevia.android.rest.oauth.data.Token;


public class OauthExampleActivity extends Activity {

	public static final String TAG = "OauthExampleActivity";

	private RequestToken mRequestToken;
	private Token mAccessToken;
	private BlueviaOauthClient mClient;

	private EditText mConsumerKey;
	private EditText mConsumerSecret;
	private Button mGetRequestTokenButton;

	private TextView mVerifier;
	private Button mGetAccessTokenButton;
	private EditText mAccess;
	private EditText mAccessSecret;

	private Button mResetButton;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.oauth_activity);

		mConsumerKey = (EditText)findViewById(R.id.key);
		mConsumerSecret = (EditText)findViewById(R.id.secret);

		mGetRequestTokenButton = (Button) findViewById(R.id.getRequestToken);
		mVerifier = (TextView)findViewById(R.id.verifier_code);
		mGetAccessTokenButton = (Button) findViewById(R.id.get_access);
		mAccess = (EditText)findViewById(R.id.text_access);
		mAccessSecret = (EditText)findViewById(R.id.text_access_secret);
		mResetButton = (Button) findViewById(R.id.reset);

		//Default values
		mConsumerKey.setText(ApplicationInfo.CONSUMER_KEY);
		mConsumerSecret.setText(ApplicationInfo.CONSUMER_SECRET);

		mGetRequestTokenButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				//Do not create client until consumer key and consumer secret has been typed
				createClient();

				if (mConsumerKey.getText().toString() == null || mConsumerKey.getText().toString().equals("") ||
						mConsumerSecret.getText().toString() == null || mConsumerSecret.getText().toString().equals(""))
					throw new IllegalArgumentException("Consumer Secret and Consumer Key cannot be null nor empty");

				getRequestToken();
			}
		});

		mGetAccessTokenButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getAccessToken();
			}
		});

		mResetButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				reset();
			}
		});

	}

	private void createClient(){

		closeClient();

		try {
			mClient = new BlueviaOauthClient(this,
					ApplicationInfo.MODE,
					mConsumerKey.getText().toString(), 
					mConsumerSecret.getText().toString());
		} catch (BlueviaClientException e){
			Log.e(TAG, e.getMessage());
		}
	}

	private void closeClient(){
		if (mClient != null)
			mClient.close();
		mClient = null;
	}

	private void getRequestToken(){

		new GetRequestTokenAsyncTask(this).execute();
	}

	private class GetRequestTokenAsyncTask extends BlueviaAsyncTask<Void, Void, RequestToken>{

		public GetRequestTokenAsyncTask(Context context) {
			super(context);
		}

		@Override
		protected RequestToken doInBackground(Void... params) {
			RequestToken response = null;
			try {
				response = mClient.getRequestToken();
			} catch (BlueviaClientException e){
				Log.e(TAG, e.getMessage(), e);
				errorMessage = e.getMessage();
			}

			return response;
		}

		@Override
		protected void onPostExecute(RequestToken result) {
			super.onPostExecute(result);
			if (result != null) {
				onRequestTokenReceived(result);
			} else {
				requestTokenError(errorMessage);
			}
		}
	}

	private void onRequestTokenReceived(RequestToken token){
		mRequestToken = token;

		if (mRequestToken.getVerificationUrl() != null){
			Uri url = Uri.parse(mRequestToken.getVerificationUrl());
			Intent i = new Intent(Intent.ACTION_VIEW, url);
			startActivity(i);
		}
	}

	private void requestTokenError(String error){
		mAccess.setText("Exception " + error);
	}

	private void getAccessToken(){

		if (mRequestToken == null){
			Toast.makeText(this, R.string.error_request_token_null, Toast.LENGTH_LONG).show();
		} else if (mVerifier.getText() == null || mVerifier.getText().toString().equals("")){
			Toast.makeText(this, R.string.error_pincode_null, Toast.LENGTH_LONG).show();
		} else {
			String param1 = mRequestToken.getToken();
			String param2 = mRequestToken.getSecret();
			String param3 = mVerifier.getText().toString();
			String[] params = {param1, param2, param3};
			new GetAccessTokenAsyncTask(this).execute(params);
		}
	}

	private class GetAccessTokenAsyncTask extends BlueviaAsyncTask<String, Void, Token>{

		public GetAccessTokenAsyncTask(Context context) {
			super(context);
		}

		@Override
		protected Token doInBackground(String... params) {
			String param1 = params[0];
			String param2 = params[1];
			String param3 = params[2];
			Token response = null;
			try {
				response = mClient.getAccessToken(param1,param2, param3);
			} catch (BlueviaClientException e){
				Log.e(TAG, e.getMessage(), e);
				errorMessage = e.getMessage();
			}

			return response;
		}

		@Override
		protected void onPostExecute(Token result) {
			super.onPostExecute(result);
			if (result != null) {
				onAccessTokenReceived(result);
			} else {
				accessTokenError(errorMessage);
			}
		}
	}

	private void onAccessTokenReceived(Token token){
		mAccessToken = token;

		mAccess.setText(mAccessToken.getToken());
		mAccessSecret.setText(mAccessToken.getSecret());
		Log.i(TAG, "Token received: " + mAccessToken.getToken() + " " + mAccessToken.getSecret());
	}

	private void accessTokenError(String error){
		mAccess.setText("Exception " + error);
	}

	private void reset(){

		mConsumerKey.setText("");
		mConsumerSecret.setText("");

		mVerifier.setText("");

		mAccess.setText("");
		mAccessSecret.setText("");

		mRequestToken = null;
		mAccessToken = null;

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		//Close client
		closeClient();
	}


}