package com.bluevia.android.examples.ad;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.bluevia.android.examples.ApplicationInfo;
import com.bluevia.android.examples.BlueviaAsyncTask;
import com.bluevia.android.examples.R;
import com.bluevia.android.rest.ad.client.BlueviaAdClient;
import com.bluevia.android.rest.ad.data.AdRequest.AdPresentationType;
import com.bluevia.android.rest.ad.data.AdRequest.ProtectionPolicyType;
import com.bluevia.android.rest.ad.data.AdResponse;
import com.bluevia.android.rest.ad.data.AdsAttribute;
import com.bluevia.android.rest.ad.data.CreativeElement;
import com.bluevia.android.rest.ad.data.Interaction;
import com.bluevia.android.rest.commons.BlueviaClientException;

public class AdExampleActivity extends Activity {

	public static final String TAG = "AdExampleActivity";

	private Spinner mAdPresentationSpinner;
	private Button mGetSimpleAdButton;
	private BlueviaAdClient mClient;
	private ImageView mAdImage;
	private TextView mText;
	private String mImageInteractionUrl;
	private String mTextInteractionUrl;

	//README
	//Fill this values
	private static AdPresentationType mAdPresentationType = AdPresentationType.BANNER;
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
					mAdPresentationType = AdPresentationType.BANNER;
					break;
				case 1:
					mAdPresentationType = AdPresentationType.TEXT;
					break;
				case 2:
					mAdPresentationType = null;
					break;
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
				mAdPresentationType = AdPresentationType.BANNER;
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
			mClient = new BlueviaAdClient(this, 
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


	private class GetSimpleAdTask extends BlueviaAsyncTask<String, Void, AdResponse>{

		public GetSimpleAdTask(Context context) {
			super(context);
		}

		@Override
		protected AdResponse doInBackground(String... params) {
			AdResponse response = null;
			try {
				response = mClient.simpleAd(
						ApplicationInfo.AD_SPACE,
						null,
						mAdPresentationType,
						null, 
						null, 
						mProtectionPolicy,
						null,
						null);
			} catch (BlueviaClientException e) {
				errorMessage = e.getMessage();
			}

			return response;
		}

		@Override
		protected void onPostExecute(AdResponse result) {
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

	private void showResult(AdResponse result){
		if (result != null) {
			ArrayList<CreativeElement> creativeElement = result.getAd().getResource().getCreativeElement();
			try {
				for (int i = 0; i < creativeElement.size(); i++) {
					CreativeElement ce = creativeElement.get(i);
					CreativeElement.Type type = ce.getType();

					switch (type) {
					case IMAGE:
						mImageInteractionUrl = null;
						ArrayList<AdsAttribute> imageAttrs = ce.getAdsAttribute();
						for (int j = 0; j < imageAttrs.size(); j++) {
							AdsAttribute imageAttr = imageAttrs.get(j);
							AdsAttribute.Type imageAttrType = imageAttr.getType();
							if ((imageAttrType == AdsAttribute.Type.LOCATOR) || (imageAttrType == AdsAttribute.Type.URL)) {
								InputStream is = mClient.getMediaContent(imageAttr.getValue());
								mAdImage.setImageBitmap(BitmapFactory.decodeStream(is));
								is.close();
								mAdImage.setVisibility(View.VISIBLE);
								//There should be only just one attribute for the location of the image
								break;
							}
						}
						ArrayList<Interaction> imageInteractions = ce.getInteraction();
						for (int j = 0; j < imageInteractions.size(); j++) {
							Interaction imageInteraction = imageInteractions.get(j);
							if (imageInteraction.getType() == Interaction.Type.CLICK_2_WAP) {
								ArrayList<AdsAttribute> imageInteractionAttrs = imageInteraction.getAttribute();
								for (int k = 0; k < imageInteractionAttrs.size(); k++) {
									AdsAttribute imageInteractionAttr = imageInteractionAttrs.get(k);
									if (imageInteractionAttr.getType() == AdsAttribute.Type.URL) {
										mImageInteractionUrl = imageInteractionAttr.getValue();
										mAdImage.setOnClickListener(new OnClickListener() {
											public void onClick(View v) {
												Intent i = new Intent(Intent.ACTION_VIEW);
												if (mImageInteractionUrl != null) {
													i.setData(Uri.parse(mImageInteractionUrl));
													startActivity(i);
												}
											}
										});
										break;
									}
								}
							}
						}
						break;
					case TEXT:
						mTextInteractionUrl = null;
						ArrayList<AdsAttribute> textAttrs = ce.getAdsAttribute();
						for (int j = 0; j < textAttrs.size(); j++) {
							AdsAttribute textAttr = textAttrs.get(j);
							if (textAttr.getType() == AdsAttribute.Type.ADTEXT) {
								mText.setText(textAttr.getValue());
							}
						}
						ArrayList<Interaction> textInteractions = ce.getInteraction();
						for (int j = 0; j < textInteractions.size(); j++) {
							Interaction textInteraction = textInteractions.get(j);
							if (textInteraction.getType() == Interaction.Type.CLICK_2_WAP) {
								ArrayList<AdsAttribute> textInteractionAttrs = textInteraction.getAttribute();
								for (int k = 0; k < textInteractionAttrs.size(); k++) {
									AdsAttribute textInteractionAttr = textInteractionAttrs.get(k);
									if (textInteractionAttr.getType() == AdsAttribute.Type.URL) {
										mTextInteractionUrl = textInteractionAttr.getValue();
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
									}
								}
							}
						}
						break;
					default:
						showError("Creative Element Type not supported");
						return;
					}
				}
			} catch (BlueviaClientException e) {
				showError(e.getMessage());
			} catch (IOException e) {
				showError(e.getMessage());
			}
		}
	}

}
