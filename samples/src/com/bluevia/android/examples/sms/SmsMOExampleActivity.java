package com.bluevia.android.examples.sms;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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
import com.bluevia.android.rest.messaging.sms.client.BlueviaSmsMOClient;
import com.bluevia.android.rest.messaging.sms.data.ReceivedSms;

public class SmsMOExampleActivity extends Activity {

	public static final String TAG = "SmsMOExampleActivity";

	private EditText mShortNumber;
	private Button mGetMessages;
	private TextView mResult;

	private BlueviaSmsMOClient mClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sms_mo_activity);

		mShortNumber = (EditText) findViewById(R.id.short_number);
		mGetMessages = (Button) findViewById(R.id.get_messages);
		mResult = (TextView) findViewById(R.id.result);


		mGetMessages.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getMessages();
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
			mClient = new BlueviaSmsMOClient(this,
					ApplicationInfo.MODE,
					ApplicationInfo.CONSUMER_KEY,
					ApplicationInfo.CONSUMER_SECRET);
		} catch (BlueviaClientException e){
			Log.e(TAG, e.getMessage());
		}
	}

	private void closeClient(){
		if (mClient != null)
			mClient.close();
		mClient = null;
	}

	private class GetSmsAsyncTask extends BlueviaAsyncTask<String, Void, ArrayList<ReceivedSms>>{

		public GetSmsAsyncTask(Context context) {
			super(context);
		}

		@Override
		protected ArrayList<ReceivedSms> doInBackground(String... params) {
			
			ArrayList<ReceivedSms> messages = null;
			
			String registrationId = params[0];
			if (registrationId == null){
				Toast.makeText(SmsMOExampleActivity.this, "Short number is mandatory", Toast.LENGTH_SHORT).show();
			} else {
				try {
					messages = mClient.getMessages(registrationId);
				} catch (BlueviaClientException e){
					Log.e(TAG, e.getMessage());
					errorMessage = e.getMessage();
				}

			}
			return messages;
		}

		@Override
		protected void onPostExecute(ArrayList<ReceivedSms> result) {
			super.onPostExecute(result);
			if (result != null) {
				if (!result.isEmpty()){
					StringBuffer composedStatus = new StringBuffer();
					for (ReceivedSms message : result) {
						Log.i(TAG, message.getMessage());
						composedStatus.append("> " + message.getDateTime() + " : " + 
								message.getAddressList().get(0).getUserIdValue() + " : " + message.getMessage() + ";\n");
					}
					mResult.setText(composedStatus);
				} else mResult.setText("No messages");
			} else {
				mResult.setText(errorMessage);
			}
		}
	}


	private void getMessages(){
		String[] params = new String[1];
		params[0] = mShortNumber.getText().toString();
		new GetSmsAsyncTask(this).execute(params);
	}

}
