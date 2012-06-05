package com.bluevia.android.samples.location;

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

import com.bluevia.android.commons.exception.BlueviaException;
import com.bluevia.android.location.client.BVLocation;
import com.bluevia.android.location.data.simple.LocationInfo;
import com.bluevia.android.samples.ApplicationInfo;
import com.bluevia.android.samples.BlueviaAsyncTask;
import com.bluevia.android.samples.R;

public class LocationActivity extends Activity {

	public static final String TAG = "LocationActivity";

	private EditText mAcceptableAccuracy;
	private Button mGetLocationButton;
	private TextView mText;

	private BVLocation mClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.location_activity);

		mAcceptableAccuracy = (EditText)findViewById(R.id.accuracy);
		mGetLocationButton = (Button)findViewById(R.id.get_location);
		mText = (TextView)findViewById(R.id.text);

		mGetLocationButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getLocation();
			}
		});

		createClient();                
	}

	@Override 
	protected void onDestroy() {
		super.onDestroy();
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
			mClient = new BVLocation(ApplicationInfo.MODE,
					ApplicationInfo.CONSUMER_KEY,
					ApplicationInfo.CONSUMER_SECRET,
					ApplicationInfo.OAUTH_TOKEN_KEY,
					ApplicationInfo.OAUTH_TOKEN_SECRET);

		} catch (BlueviaException e){
			Log.e(TAG, e.getMessage());
		}
	}

	private void getLocation(){
		new GetLocationTask(this).execute();
		mText.setText("");
	}

	private class GetLocationTask extends BlueviaAsyncTask<Void, Void, LocationInfo>{

		public GetLocationTask(Context context) {
			super(context);
		}

		@Override
		protected LocationInfo doInBackground(Void... params) {
			LocationInfo response = null;
			try {

				Integer accuracy = null;
				if (mAcceptableAccuracy.getText() != null && 
						mAcceptableAccuracy.getText().toString().trim().length() != 0)
					accuracy = Integer.parseInt(mAcceptableAccuracy.getText().toString());

				response = mClient.getLocation(accuracy);

			} catch (BlueviaException e) {
				Log.e(TAG, "Error retrieving location: " + e.getMessage());
				errorMessage = e.getMessage();
			} catch (IOException e) {
				Log.e(TAG, "Error retrieving location: " + e.getMessage());
				errorMessage = e.getMessage();
			}

			return response;
		}

		@Override
		protected void onPostExecute(LocationInfo result) {
			super.onPostExecute(result);
			if (result != null) {
				showResult(result);
			} else {
				showError(errorMessage);
			}
		}
	};

	private void showResult(LocationInfo result){
		if (result != null){															
			Log.d(TAG, "Latitude "+ result.getLatitude() +" Longitude: "+result.getLongitude());

			Intent i = new Intent(Intent.ACTION_VIEW);
			Uri data = Uri.parse("geo:"+result.getLatitude()+","+result.getLongitude()+"?z=15");
			i.setData(data);
			startActivity(i);
		}
	}

	private void showError(String message){
		mText.setText(message);
	}
}
