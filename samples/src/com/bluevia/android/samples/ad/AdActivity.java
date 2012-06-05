package com.bluevia.android.samples.ad;


import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bluevia.android.ad.client.BVAdvertising;
import com.bluevia.android.ad.data.AdRequest;
import com.bluevia.android.ad.data.AdRequest.ProtectionPolicyType;
import com.bluevia.android.ad.data.simple.CreativeElement;
import com.bluevia.android.ad.data.simple.SimpleAdResponse;
import com.bluevia.android.commons.exception.BlueviaException;
import com.bluevia.android.samples.ApplicationInfo;
import com.bluevia.android.samples.BlueviaAsyncTask;
import com.bluevia.android.samples.R;

public class AdActivity extends Activity {

	public static final String TAG = "AdActivity";

	private Spinner mAdPresentationSpinner;
	private Button mGetSimpleAdButton;
	private BVAdvertising mClient;
	private ImageView mAdImage;
	private TextView mText;
	private String mImageInteractionUrl;
	private String mTextInteractionUrl;

	//README
	//Fill this values
	private static AdRequest.Type mAdPresentationType = AdRequest.Type.IMAGE;
	private static ProtectionPolicyType mProtectionPolicy = ProtectionPolicyType.SAFE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.ad_activity);

		mAdPresentationSpinner = (Spinner) findViewById(R.id.adPresentationType);
		mGetSimpleAdButton = (Button)findViewById(R.id.getSimpleAd);
		mAdImage = (ImageView) findViewById(R.id.adImage);
		mText = (TextView) findViewById(R.id.text);

		ArrayAdapter<CharSequence> directoryAdapter = ArrayAdapter.createFromResource(this, R.array.adPresentationType, android.R.layout.simple_spinner_item);
		directoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mAdPresentationSpinner.setAdapter(directoryAdapter);

		mAdPresentationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position){
				case 0:
					mAdPresentationType = AdRequest.Type.IMAGE;
					break;
				case 1:
					mAdPresentationType = AdRequest.Type.TEXT;
					break;
				default:
					mAdPresentationType = null;
					break;
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
				mAdPresentationType = AdRequest.Type.IMAGE;
			}

		});

		mGetSimpleAdButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getSimpleAd();
			}
		});

		createClient();

	}

	private void createClient(){

		closeClient();

		try {
			mClient = new BVAdvertising(ApplicationInfo.MODE,
					ApplicationInfo.CONSUMER_KEY,
					ApplicationInfo.CONSUMER_SECRET,
					ApplicationInfo.OAUTH_TOKEN_KEY,
					ApplicationInfo.OAUTH_TOKEN_SECRET);
		} catch (BlueviaException e){
			Log.e(TAG, e.getMessage());
		}

	}

	private void closeClient(){
		if (mClient != null)
			mClient.close();
		mClient = null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		//Close the client
		closeClient();
	}

	private void getSimpleAd() {

		try {
			new GetSimpleAdTask(this).execute();
		} catch (IllegalArgumentException e){
			Log.e(TAG, e.getMessage(), e);
		}
	}


	private class GetSimpleAdTask extends BlueviaAsyncTask<String, Void, SimpleAdResponse>{

		public GetSimpleAdTask(Context context) {
			super(context);
		}

		@Override
		protected SimpleAdResponse doInBackground(String... params) {
			SimpleAdResponse response = null;
			try {

				response = mClient.getAdvertising3l(ApplicationInfo.AD_SPACE, 
						null, null, mAdPresentationType, null, mProtectionPolicy, null);
			} catch (BlueviaException e) {
				errorMessage = e.getMessage();
			} catch (IOException e) {
				errorMessage = e.getMessage();
			}

			return response;
		}

		@Override
		protected void onPostExecute(SimpleAdResponse result) {
			super.onPostExecute(result);
			if (result != null) {
				showResult(result);
			} else {
				showError(errorMessage);
			}
		}
	};

	private void showError(String result){
		mAdImage.setVisibility(View.GONE);
		mText.setText(result);
		mText.setOnClickListener(null);
	}

	private void showResult(SimpleAdResponse result){
		if (result != null) {

			ArrayList<CreativeElement> list = result.getAdvertisingList();
			for (int i = 0; i < list.size(); i++) {

				CreativeElement ce = list.get(i);

				switch (ce.getType()) {
				case IMAGE:
					String imageUrl = ce.getValue();
					mImageInteractionUrl = ce.getInteraction();

					Toast.makeText(this, imageUrl, Toast.LENGTH_SHORT).show();
					Toast.makeText(this, mImageInteractionUrl, Toast.LENGTH_SHORT).show();

					//InputStream is = mClient.getMediaContent(imageAttr.getValue());
					//mAdImage.setImageBitmap(BitmapFactory.decodeStream(is));
					//is.close();
					//mAdImage.setVisibility(View.VISIBLE);

					/*
						mAdImage.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								Intent i = new Intent(Intent.ACTION_VIEW);
								if (mImageInteractionUrl != null) {
									i.setData(Uri.parse(mImageInteractionUrl));
									startActivity(i);
								}
							}
						});
					 */
					break;
				case TEXT:

					String text = ce.getValue();
					mImageInteractionUrl = ce.getInteraction();
					mText.setText(text);
					mText.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							Intent i = new Intent(Intent.ACTION_VIEW);
							if (mTextInteractionUrl != null) {
								i.setData(Uri.parse(mTextInteractionUrl));
								startActivity(i);
							}
						}
					});
					break;
				default:
					showError("Creative Element Type not supported");
					return;
				}
			}
		}
	}

}
