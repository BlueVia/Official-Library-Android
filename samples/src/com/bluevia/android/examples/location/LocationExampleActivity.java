package com.bluevia.android.examples.location;

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

import com.bluevia.android.examples.ApplicationInfo;
import com.bluevia.android.examples.BlueviaAsyncTask;
import com.bluevia.android.examples.R;
import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.location.client.BlueviaLocationClient;
import com.bluevia.android.rest.location.data.CoordinatesType;
import com.bluevia.android.rest.location.data.LocationDataType;

public class LocationExampleActivity extends Activity {

	public static final String TAG = "LocationExampleActivity";

	private EditText mAcceptableAccuracy;
	private Button mGetLocationButton;
	private TextView mText;

	private BlueviaLocationClient mClient;

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
			mClient = new BlueviaLocationClient(this, 
					ApplicationInfo.MODE, 
					ApplicationInfo.CONSUMER_KEY, 
					ApplicationInfo.CONSUMER_SECRET, 
					ApplicationInfo.OAUTH_TOKEN);

		} catch (BlueviaClientException e){
			Log.e(TAG, e.getMessage());
		}
	}

	private void getLocation(){
		new GetLocationTask(this).execute();
		mText.setText("");
	}

	private class GetLocationTask extends BlueviaAsyncTask<Void, Void, LocationDataType>{

		public GetLocationTask(Context context) {
			super(context);
		}

		@Override
		protected LocationDataType doInBackground(Void... params) {
			LocationDataType response = null;
			try {

				Integer accuracy = null;
				if (mAcceptableAccuracy.getText() != null && 
						mAcceptableAccuracy.getText().toString().trim().length() != 0)
					accuracy = Integer.parseInt(mAcceptableAccuracy.getText().toString());

				response = mClient.getLocation(accuracy);

			} catch (BlueviaClientException e) {
				Log.e(TAG, "Error retrieving location: " + e.getMessage());
				errorMessage = e.getMessage();
			}

			return response;
		}

		@Override
		protected void onPostExecute(LocationDataType result) {
			super.onPostExecute(result);
			if (result != null) {
				showResult(result);
			} else {
				showError(errorMessage);
			}
		}
	};

	private void showResult(LocationDataType result){
		CoordinatesType c = result.getCurrentLocation().getCoordinates();
		if (c != null){															
			Log.d(TAG, "Latitude "+ c.getLatitude() +" Longitude: "+c.getLongitude());

			Intent i = new Intent(Intent.ACTION_VIEW);
			Uri data = Uri.parse("geo:"+c.getLatitude()+","+c.getLongitude()+"?z=15");
			i.setData(data);
			startActivity(i);
		}
	}

	private void showError(String message){
		mText.setText(message);
	}
}
