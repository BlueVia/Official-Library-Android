package com.bluevia.android.examples.sms;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.bluevia.android.rest.commons.data.UserId;
import com.bluevia.android.rest.commons.data.UserId.Type;
import com.bluevia.android.rest.messaging.MessageDeliveryStatus;
import com.bluevia.android.rest.messaging.sms.client.BlueviaSmsMTClient;

public class SmsMTExampleActivity extends Activity {

	public static final String TAG = "SmsMTExampleActivity";

	private EditText mAddress;
	private EditText mText;
	private TextView mCount;
	private Button mSendButton;
	private EditText mRetrieveStatusId;
	private TextView mSmsId;
	private Button mStatusButton;
	private TextView mSmsStatus;
	private Button mClearButton;
	private BlueviaSmsMTClient mClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sms_mt_activity);

		mAddress = (EditText)findViewById(R.id.address);
		mText = (EditText)findViewById(R.id.text);
		mCount = (TextView)findViewById(R.id.wordcount);
		mSendButton = (Button)findViewById(R.id.send);
		mSmsId = (TextView)findViewById(R.id.smsId);
		mRetrieveStatusId = (EditText)findViewById(R.id.retrieveSmsStatusId);
		mStatusButton = (Button)findViewById(R.id.status);
		mSmsStatus = (TextView)findViewById(R.id.smsStatus);
		mClearButton = (Button)findViewById(R.id.clear);

		mText.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				setCount();
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {}
		});

		mSendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendSms();
			}
		});

		mStatusButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				retrieveSmsStatus();
			}
		});

		mClearButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				clear();
			}
		});

		createClient();
		clear();
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
			mClient = new BlueviaSmsMTClient(this,
					ApplicationInfo.MODE,
					ApplicationInfo.CONSUMER_KEY,
					ApplicationInfo.CONSUMER_SECRET, 
					ApplicationInfo.OAUTH_TOKEN);
		} catch (BlueviaClientException e){
			Log.e(TAG, e.getMessage());
		}
	}

	private void closeClient(){
		if (mClient != null)
			mClient.close();
		mClient = null;
	}

	/**
	 * 
	 * Params: addresses (comma separated) / text
	 * Result: smsIds
	 *
	 */
	private class SendSmsTask extends BlueviaAsyncTask<String, Void, String>{

		public SendSmsTask(Context context) {
			super(context);
		}

		@Override
		protected String doInBackground(String... params) {
			String[] phoneNumbers = mAddress.getText().toString().split(",");
			ArrayList<UserId> addresses = new ArrayList<UserId>(phoneNumbers.length);
			for (int i = 0; i<phoneNumbers.length; i++) {
				if (phoneNumbers[i].trim().length() == 0)
					continue;
				addresses.add (new UserId(Type.PHONE_NUMBER, phoneNumbers[i].trim()));
			}

			String text = params[1];
			String smsId = null;

			try {
				smsId = mClient.sendSms(text, 
						addresses.toArray(new UserId[addresses.size()]));
			} catch (BlueviaClientException e) {
				Log.e(TAG, e.getMessage());
				errorMessage = e.getMessage();
			}

			return smsId;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null){
				Toast.makeText(mContext, "SMS sent. SmsId: " + result, Toast.LENGTH_LONG).show();
				showResult(result);
			} else showError(errorMessage);
		}
	}

	private void sendSms(){
		String addresses = mAddress.getText().toString();

		String text = mText.getText().toString();
		clearMessages();

		try {
			new SendSmsTask(this).execute(addresses, text);
		} catch (IllegalArgumentException e){
			showError("You must specify an address to send the sms.");
			Log.e(TAG, e.getMessage(), e);
		}
	}

	private void showError(String result){
		mSmsId.setText(result);
		mSmsStatus.setClickable(false);
	}

	private void showResult(String result){
		if (result != null){
			mSmsId.setText(result);
			mSmsStatus.setClickable(true);
			mRetrieveStatusId.setText(result);
		}
	}

	/**
	 * 
	 * Params: smsId
	 * Result: ArrayList<MessageDeliveryStatus>
	 *
	 */
	private class RetrieveSmsStatus extends BlueviaAsyncTask<String, Void, ArrayList<MessageDeliveryStatus>>{

		public RetrieveSmsStatus(Context context) {
			super(context);
		}

		@Override
		protected ArrayList<MessageDeliveryStatus> doInBackground(String... params) {
			String smsId = params[0];
			if (smsId == null)
				throw new IllegalArgumentException("No sms id");

			ArrayList<MessageDeliveryStatus> status = null;

			try {
				status = mClient.getSmsDeliveryStatus(smsId);
			} catch (BlueviaClientException e){
				Log.e(TAG, e.getMessage());
				errorMessage = e.getMessage();
			}

			return status;
		}

		@Override
		protected void onPostExecute(ArrayList<MessageDeliveryStatus> result) {
			super.onPostExecute(result);
			if (result != null) {
				StringBuffer composedStatus = new StringBuffer();
				for (MessageDeliveryStatus single : result) {
					composedStatus.append(single.getStatus().toString() + ",");
				}
				mSmsStatus.setText(composedStatus);
			} else showError(errorMessage);
		}
	}

	private void retrieveSmsStatus(){
		String smsId = mRetrieveStatusId.getText().toString();
		clearMessages();

		try {
			new RetrieveSmsStatus(this).execute(smsId);
		} catch (IllegalArgumentException e){
			showError("You must specify a sms id.");
			Log.e(TAG, e.getMessage(), e);
		}
	}

	private void setCount(){
		mCount.setText(String.valueOf(mText.getText().length()));
	}

	private void clear(){
		mAddress.setText("");
		mText.setText("");
		mSmsId.setText("");
		mSmsStatus.setText("");
		mRetrieveStatusId.setText("");

		mSmsStatus.setClickable(false);
	}

	private void clearMessages(){
		mSmsId.setText("");
		mSmsStatus.setText("");
	}

}
