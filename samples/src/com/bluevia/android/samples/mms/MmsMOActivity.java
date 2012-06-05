package com.bluevia.android.samples.mms;


import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluevia.android.commons.exception.BlueviaException;
import com.bluevia.android.messagery.mo.mms.client.BVMoMms;
import com.bluevia.android.messagery.mo.mms.data.MimeContent;
import com.bluevia.android.messagery.mo.mms.data.MmsMessage;
import com.bluevia.android.messagery.mo.mms.data.MmsMessageInfo;
import com.bluevia.android.samples.ApplicationInfo;
import com.bluevia.android.samples.BlueviaAsyncTask;
import com.bluevia.android.samples.R;

public class MmsMOActivity extends Activity {

	public static final String TAG = "MmsMTActivity";

	private EditText mShortNumber;
	private Button mGetMessagesButton;
	private TextView mList;
	private EditText mMmsId;
	private Button mGetMessageButton;
	private TextView mResult;
	private LinearLayout mImages;


	private BVMoMms mClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mms_mo_activity);

		mShortNumber = (EditText) findViewById(R.id.short_number);
		mGetMessagesButton = (Button) findViewById(R.id.get_messages);
		mList = (TextView) findViewById(R.id.messages_list);
		mResult = (TextView) findViewById(R.id.message_result);
		mMmsId = (EditText) findViewById(R.id.mms_id);
		mGetMessageButton = (Button) findViewById(R.id.get_message);
		mImages = (LinearLayout) findViewById(R.id.image_list);
		
		
		mGetMessagesButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getMessages();
			}
		});
		
		mGetMessageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getMessage();
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

	private void closeClient(){
		if (mClient != null)
			mClient.close();
		mClient = null;
	}

	private void createClient(){

		closeClient();

		try {
			mClient = new BVMoMms(ApplicationInfo.MODE,
					ApplicationInfo.CONSUMER_KEY,
					ApplicationInfo.CONSUMER_SECRET);
			
		} catch (BlueviaException e){
			Log.e(TAG, e.getMessage());
		}
	}
	
	private class GetMessagesListAsyncTask extends BlueviaAsyncTask<String, Void, ArrayList<MmsMessageInfo>>{

		public GetMessagesListAsyncTask(Context context) {
			super(context);
		}

		@Override
		protected ArrayList<MmsMessageInfo> doInBackground(String... params) {
			ArrayList<MmsMessageInfo> messages = null;
			
			String registrationId = params[0];
			if (registrationId == null){
				Toast.makeText(MmsMOActivity.this, "Short number is mandatory", Toast.LENGTH_SHORT).show();
			} else {
				try {
					
					messages = mClient.getAllMessages(registrationId, false);
					
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
		protected void onPostExecute(ArrayList<MmsMessageInfo> result) {
			super.onPostExecute(result);
			if (result != null) {
				if (!result.isEmpty()){
					StringBuffer composedStatus = new StringBuffer();
					for (MmsMessageInfo message : result) {
						Log.i(TAG, message.getDate() + " " + message.getSubject());
						composedStatus.append("> " + "Message id: " + message.getMessageId()
								+ " : " + message.getDestinationList().get(0)
								+ " : " + message.getSubject() + ";\n");
					}
					showResult(composedStatus.toString());
				} else showResult("No messages");
			} else showResult(errorMessage);
		}
	}

	private void getMessages(){
		
		//Reset
		mList.setText("");
		mResult.setText("");
		mImages.removeAllViews();
		
		
		String number = mShortNumber.getText().toString();
		try {
			new GetMessagesListAsyncTask(this).execute(number);
		} catch (IllegalArgumentException e){
			showResult("Short number is mandatory");
			Log.e(TAG, e.getMessage(), e);
		}
	}
	
	private class GetMessageAsyncTask extends BlueviaAsyncTask<String, Void, MmsMessage>{

		public GetMessageAsyncTask(Context context) {
			super(context);
		}

		@Override
		protected MmsMessage doInBackground(String... params) {
			MmsMessage message = null;

			String registrationId = params[0];
			String messageId = params[1];
			if (registrationId == null){
				Toast.makeText(MmsMOActivity.this, "Short number is mandatory", Toast.LENGTH_SHORT).show();
			} if (messageId == null){
				Toast.makeText(MmsMOActivity.this, "Message id is mandatory", Toast.LENGTH_SHORT).show();
			} else {
				try {
					
					message = mClient.getMessage(registrationId, messageId);
					
				} catch (BlueviaException e){
					Log.e(TAG, e.getMessage());
					errorMessage = e.getMessage();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					errorMessage = e.getMessage();
				}
			}

			return message;
		}

		@Override
		protected void onPostExecute(MmsMessage result) {
			super.onPostExecute(result);
			if (result != null) {
				
				String s = "";
				for (int i = 0; i<result.getAttachments().size(); i++){
					MimeContent content = result.getAttachments().get(i);
					s += "Att #"+i+": "+  content.getName();
					if (content.getContentType().contains("text")){
						if (content.getContent() instanceof String){
							s += "\n	> " +  (String) content.getContent();
						} else {
							s += "\n	> " +  new String((byte[]) content.getContent());
						}
					}
					s += "\n";
				
					//Images
					if (content.getContentType().contains("image")){
						byte[] bytes = (byte[]) content.getContent();
						ImageView image = new ImageView(MmsMOActivity.this);
						Bitmap bmp=BitmapFactory.decodeByteArray(bytes,0,bytes.length);
						image.setImageBitmap(bmp);
						mImages.addView(image);
					}
				}
				showResult2(s);
				
			} else showResult2(errorMessage);
		}
	}
	
	private void getMessage(){
	
		//Delete images
		mImages.removeAllViews();
		
		String number = mShortNumber.getText().toString();
		String mmsId = mMmsId.getText().toString();

		try {
			new GetMessageAsyncTask(this).execute(number, mmsId);
		} catch (IllegalArgumentException e){
			showResult("Short number and mms id are mandatory");
			Log.e(TAG, e.getMessage(), e);
		}
	}
	
	private void showResult(String text){
		if (text != null){
			mList.setText(text);
		}
	}
	
	private void showResult2(String text){
		if (text != null){
			mResult.setText(text);
		}
	}

}
