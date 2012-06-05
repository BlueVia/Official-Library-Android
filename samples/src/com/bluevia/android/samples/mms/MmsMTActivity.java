package com.bluevia.android.samples.mms;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bluevia.android.commons.exception.BlueviaException;
import com.bluevia.android.messagery.mt.data.DeliveryInfo;
import com.bluevia.android.messagery.mt.mms.client.BVMtMms;
import com.bluevia.android.messagery.mt.mms.data.Attachment;
import com.bluevia.android.samples.ApplicationInfo;
import com.bluevia.android.samples.BlueviaAsyncTask;
import com.bluevia.android.samples.R;

public class MmsMTActivity extends Activity {

	public static final String TAG = "MmsMTActivity";

	public static final int REQUEST_CODE_BASE = 10;
	public static final int REQUEST_CODE_ATTACH_IMAGE = REQUEST_CODE_BASE + 0;
	public static final int REQUEST_CODE_ATTACH_VIDEO = REQUEST_CODE_BASE + 1;
	public static final int REQUEST_CODE_ATTACH_SOUND = REQUEST_CODE_BASE + 2;

	public static final String IMAGE_UNSPECIFIED = "image/*";
	public static final String VIDEO_UNSPECIFIED = "video/*";

	private EditText mAddress;
	private EditText mSubject;
	private EditText mText;
	private TextView mAttachmentName;
	private Button mAttachButton;
	private Button mSendButton;
	private EditText mRetrieveStatusId;
	private TextView mMmsId;
	private Button mStatusButton;
	private TextView mMmsStatus;
	private Button mClearButton;

	private File mTempFile;

	private ArrayList<MmsAttachmentData> mAttachmentList;

	//as it is a temporary file, it is not included in the array list
	private MmsAttachmentData mTextAttachment;

	private class MmsAttachmentData {
		String  mAttachmentName;
		String mAttachmentContentType;
		String mAttachmentPath;
	};

	private BVMtMms mMTClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mms_mt_activity);

		mAddress = (EditText)findViewById(R.id.address);
		mSubject = (EditText)findViewById(R.id.subject);
		mText = (EditText)findViewById(R.id.text);
		mAttachmentName = (TextView)findViewById(R.id.attachment);
		mAttachButton = (Button)findViewById(R.id.attach);
		mSendButton = (Button)findViewById(R.id.send);
		mRetrieveStatusId = (EditText)findViewById(R.id.retrieveMmsStatusId);
		mStatusButton = (Button)findViewById(R.id.status);
		mClearButton = (Button)findViewById(R.id.clear);
		mMmsId = (TextView)findViewById(R.id.mms_id);
		mMmsStatus = (TextView)findViewById(R.id.mmsStatus);

		mAttachmentList = new ArrayList<MmsAttachmentData>();

		mSendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendMms();
			}
		});

		mAttachButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showAttachmentsDialog();
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

	private void closeClient(){
		if (mMTClient != null)
			mMTClient.close();
		mMTClient = null;
	}

	private void createClient(){

		closeClient();

		try {
			mMTClient = new BVMtMms(ApplicationInfo.MODE,
					ApplicationInfo.CONSUMER_KEY,
					ApplicationInfo.CONSUMER_SECRET,
					ApplicationInfo.OAUTH_TOKEN_KEY,
					ApplicationInfo.OAUTH_TOKEN_SECRET);
		} catch (BlueviaException e){
			Log.e(TAG, e.getMessage());
		}
	}

	private void showAttachmentsDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		ArrayAdapter<CharSequence> adapter =
				ArrayAdapter.createFromResource(this, R.array.attachments, android.R.layout.select_dialog_item);
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				int option = REQUEST_CODE_ATTACH_IMAGE + which;
				switch (option){
				case REQUEST_CODE_ATTACH_IMAGE :
					selectImage();
					break;
				case REQUEST_CODE_ATTACH_VIDEO :
					selectVideo();
					break;
				case REQUEST_CODE_ATTACH_SOUND :
					selectAudio();
					break;
				default :
					Log.e(TAG, "Attachment option not valid");
				}
			}
		});

		builder.show();
	}

	private void selectImage() {
		selectMediaByType(this, REQUEST_CODE_ATTACH_IMAGE, IMAGE_UNSPECIFIED);
	}

	private void selectVideo() {
		selectMediaByType(this, REQUEST_CODE_ATTACH_VIDEO, VIDEO_UNSPECIFIED);
	}

	private void selectMediaByType(Context context, int requestCode, String contentType) {
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
		innerIntent.setType(contentType);
		Intent wrapperIntent = Intent.createChooser(innerIntent, null);
		startActivityForResult(wrapperIntent, requestCode);
	}

	private void selectAudio() {
		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_INCLUDE_DRM, false);
		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, R.string.selectAudio);
		startActivityForResult(intent, REQUEST_CODE_ATTACH_SOUND);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Uri uri = null;
			switch (requestCode){
			case REQUEST_CODE_ATTACH_IMAGE :
			case REQUEST_CODE_ATTACH_VIDEO :
				uri = data.getData();
				break;

			case REQUEST_CODE_ATTACH_SOUND : {
				uri = (Uri) data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
				if (android.provider.Settings.System.DEFAULT_RINGTONE_URI.equals(uri)) {
					break;
				}
				break;
			}
			default :
				throw new IllegalStateException("Invalid request code.");
			}
			attachFile(uri, requestCode);
		}
	}

	private void attachFile(Uri data, int type){
		Log.v(TAG, data.toString());
		String[] projection = null;

		switch (type){
		case REQUEST_CODE_ATTACH_IMAGE :
			projection = new String[3];
			projection[0] = MediaStore.Images.ImageColumns.DATA;
			projection[1] = MediaStore.Images.ImageColumns.DISPLAY_NAME;
			projection[2] = MediaStore.Images.ImageColumns.MIME_TYPE;
			break;
		case REQUEST_CODE_ATTACH_VIDEO :
			projection = new String[3];
			projection[0] = MediaStore.Video.VideoColumns.DATA;
			projection[1] = MediaStore.Video.VideoColumns.DISPLAY_NAME;
			projection[2] = MediaStore.Video.VideoColumns.MIME_TYPE;
			break;

		case REQUEST_CODE_ATTACH_SOUND :
			projection = new String[3];
			projection[0] = MediaStore.Audio.AudioColumns.DATA;
			projection[1] = MediaStore.Audio.AudioColumns.DISPLAY_NAME;
			projection[2] = MediaStore.Audio.AudioColumns.MIME_TYPE;
			break;
		default:
			Log.e(TAG, "Invalid request code");
		}

		if (projection != null){
			Cursor c = managedQuery(data, projection, null, null, null);
			if (c!=null && c.moveToFirst()) {
				MmsAttachmentData attachment = new MmsAttachmentData();
				attachment.mAttachmentPath = c.getString(0);
				attachment.mAttachmentName = c.getString(1);
				attachment.mAttachmentContentType = c.getString(2);

				addAttachmentToList(attachment);

				StringBuffer attachmentsText = new StringBuffer("Files attached: ");
				for (MmsAttachmentData attach : mAttachmentList) {
					attachmentsText.append(attach.mAttachmentName);
					attachmentsText.append(",");
				}
				attachmentsText.deleteCharAt(attachmentsText.length()-1);
				mAttachmentName.setText(attachmentsText.toString());
			}
		}
	}

	private void addAttachmentToList(MmsAttachmentData attachment) {
		if (mAttachmentList == null)
			mAttachmentList = new ArrayList<MmsAttachmentData>();

		boolean alreadyAttached = false;
		int i = 0;
		while (!alreadyAttached && i<mAttachmentList.size()){
			if (mAttachmentList.get(i).mAttachmentPath.equals(attachment.mAttachmentPath))
				alreadyAttached = true;
			i++;
		}
		if (!alreadyAttached)
			mAttachmentList.add (attachment);
		else
			Log.v(TAG, "File " + attachment.mAttachmentName + " is already attached.");
	}


	/**
	 * Params: MMS Object
	 * Result: MMS Id
	 *
	 */
	private class SendMmsTask extends BlueviaAsyncTask<MmsObj, Void, String>{

		public SendMmsTask(Context context) {
			super(context);
		}

		@Override
		protected String doInBackground(MmsObj... params) {
			MmsObj mms = params[0];
			ArrayList<String> addresses = mms.addresses;
			String subject = mms.subject;
			String message = mms.message;
			ArrayList<Attachment> parts = mms.parts;
			String mmsIds = null;

			try {
				mmsIds = mMTClient.send(addresses, subject, message, parts);
			} catch (BlueviaException e){
				Log.e(TAG, e.getMessage());
				errorMessage = e.getMessage();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
				errorMessage = e.getMessage();
			}

			return mmsIds;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null){
				Toast.makeText(mContext, "SMS sent. SmsId: " + result, Toast.LENGTH_LONG).show();
				showResult(result);
			} else showError(errorMessage);

			//Delete the temp file and the attachment
			if (mTempFile != null)
				mTempFile.delete();
			if (mAttachmentList != null)
				mAttachmentList.remove(mTextAttachment);
			mTextAttachment = null;
		}
	}

	//Private class to send the MMS params to the AsyncTask
	private class MmsObj {
		ArrayList<String> addresses;
		String subject;
		String message;
		ArrayList<Attachment> parts;

		public MmsObj(ArrayList<String> adr, String subject, String message, ArrayList<Attachment> parts){
			addresses = adr; this.subject = subject; this.message = message; this.parts = parts;
		}
	}

	private void sendMms(){
		String[] phoneNumbers = mAddress.getText().toString().split(",");
		ArrayList<String> addresses = new ArrayList<String>(phoneNumbers.length);
		for (int i = 0; i<phoneNumbers.length; i++) {
			if (phoneNumbers[i].trim().length() == 0)
				continue;
			addresses.add(phoneNumbers[i].trim());
		}

		String subject = mSubject.getText().toString();

		String text = mText.getText().toString();

		clearMessages();

		try {
			ArrayList<Attachment> attachments = new ArrayList<Attachment>();
			if ((mAttachmentList != null) && (mAttachmentList.size() > 0)) {
				for (MmsAttachmentData attach : mAttachmentList) {
					attachments.add(new Attachment(attach.mAttachmentPath, attach.mAttachmentContentType));
				}
			}

			new SendMmsTask(this).execute(new MmsObj(addresses, subject, text, attachments));

		} catch (IllegalArgumentException e){
			showError(e.getMessage());
			Log.e(TAG, e.getMessage(), e);
		} catch (BlueviaException e) {
			showError(e.getMessage());
			Log.e(TAG, e.getMessage(), e);
		}
	}

	private void showError(String result){
		mMmsId.setText(result);
		mMmsStatus.setClickable(false);
	}

	private void showResult(String result){
		if (result != null){
			mMmsId.setText(result);
			mMmsStatus.setClickable(true);
			mRetrieveStatusId.setText(result);
		}
	}

	private class RetrieveMmsStatus extends BlueviaAsyncTask<String, Void, ArrayList<DeliveryInfo>>{

		public RetrieveMmsStatus(Context context) {
			super(context);
		}

		@Override
		protected ArrayList<DeliveryInfo> doInBackground(String... params) {
			String mmsId = params[0];
			if (mmsId == null)
				throw new IllegalArgumentException("No mms id");

			ArrayList<DeliveryInfo> status = null;

			try {
				status = mMTClient.getDeliveryStatus(mmsId);
			} catch (BlueviaException e){
				Log.e(TAG, e.getMessage());
				errorMessage = e.getMessage();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
				errorMessage = e.getMessage();
			}

			return status;
		}

		@Override
		protected void onPostExecute(ArrayList<DeliveryInfo> result) {
			super.onPostExecute(result);
			if (result != null) {
				StringBuffer composedStatus = new StringBuffer();
				for (DeliveryInfo single : result) {
					composedStatus.append(single.getStatus().toString() + ",");
				}
				mMmsStatus.setText(composedStatus);
			} else showError(errorMessage);
		}
	}

	private void retrieveSmsStatus(){
		String smsId = mRetrieveStatusId.getText().toString();
		clearMessages();

		try {
			new RetrieveMmsStatus(this).execute(smsId);
		} catch (IllegalArgumentException e){
			showError("You must specify a mms id.");
			Log.e(TAG, e.getMessage(), e);
		}
	}

	private void clear(){
		mAddress.setText("");
		mSubject.setText("");
		mText.setText("");
		mMmsId.setText("");
		mMmsStatus.setText("");
		mRetrieveStatusId.setText("");

		if (mAttachmentList != null)
			mAttachmentList.clear();
		mAttachmentList = null;
		mAttachmentName.setText("");

		mMmsStatus.setClickable(false);
	}

	private void clearMessages(){
		mMmsId.setText("");
		mMmsStatus.setText("");
	}


}
