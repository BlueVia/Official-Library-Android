package com.bluevia.android.samples.sms;

import java.io.IOException;
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

import com.bluevia.android.commons.exception.BlueviaException;
import com.bluevia.android.messagery.mo.sms.client.BVMoSms;
import com.bluevia.android.messagery.mo.sms.data.SmsMessage;
import com.bluevia.android.samples.ApplicationInfo;
import com.bluevia.android.samples.BlueviaAsyncTask;
import com.bluevia.android.samples.R;

public class SmsMOActivity extends Activity {

	public static final String TAG = "SmsMOActivity";

	private EditText mShortNumber;
	private Button mGetMessages;
	private TextView mResult;

	private BVMoSms mClient;

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
			mClient = new BVMoSms(ApplicationInfo.MODE,
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

	private class GetSmsAsyncTask extends BlueviaAsyncTask<String, Void, ArrayList<SmsMessage>>{

		public GetSmsAsyncTask(Context context) {
			super(context);
		}

		@Override
		protected ArrayList<SmsMessage> doInBackground(String... params) {
			
			ArrayList<SmsMessage> messages = null;
			
			String registrationId = params[0];
			if (registrationId == null){
				Toast.makeText(SmsMOActivity.this, "Short number is mandatory", Toast.LENGTH_SHORT).show();
			} else {
				try {
					messages = mClient.getAllMessages(registrationId);
				} catch (BlueviaException e){
					Log.e(TAG, e.getMessage());
					errorMessage = e.getMessage();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					errorMessage = e.getMessage();
				}

			}
			return messages;
		}

		@Override
		protected void onPostExecute(ArrayList<SmsMessage> result) {
			super.onPostExecute(result);
			if (result != null) {
				if (!result.isEmpty()){
					StringBuffer composedStatus = new StringBuffer();
					for (SmsMessage message : result) {
						Log.i(TAG, message.getMessage());
						composedStatus.append("> " + message.getDate() + " : " + 
								message.getDestinationList().get(0) + " : " + message.getMessage() + ";\n");
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
