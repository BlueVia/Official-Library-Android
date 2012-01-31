package com.bluevia.android.examples.directory;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.bluevia.android.examples.ApplicationInfo;
import com.bluevia.android.examples.BlueviaAsyncTask;
import com.bluevia.android.examples.R;
import com.bluevia.android.rest.commons.BlueviaClientException;
import com.bluevia.android.rest.directory.client.BlueviaDirectoryClient;
import com.bluevia.android.rest.directory.data.UserAccessInfo;
import com.bluevia.android.rest.directory.data.UserProfile;
import com.bluevia.android.rest.directory.data.UserTerminalInfo;

public class DirectoryExampleActivity extends Activity {

	public static final String TAG = "DirectoryExampleActivity";

	public static final String[] RESOURCE_NAMES = {
		UserAccessInfo.class.getName(),
		UserProfile.class.getName(),
		UserTerminalInfo.class.getName()
	};

	private Spinner mSpinnerResource;

	private Button mRetrieveButton;

	private ListView mParameterList;

	private int mDirectory = 0;

	private BlueviaDirectoryClient mClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.directory_activity);

		mSpinnerResource = (Spinner)findViewById(R.id.resourceType);
		mRetrieveButton = (Button)findViewById(R.id.retrieveResource);
		mParameterList = (ListView) findViewById(R.id.elementsList);
		mParameterList.setCacheColorHint(0);

		ArrayAdapter<CharSequence> directoryAdapter = ArrayAdapter.createFromResource(this, R.array.directoryResources, android.R.layout.simple_spinner_item);
		directoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerResource.setAdapter(directoryAdapter);

		mSpinnerResource.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mDirectory = position;
			}

			public void onNothingSelected(AdapterView<?> parent) {
				mDirectory = 0;
			}

		});

		mRetrieveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getResource();
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
			mClient = new BlueviaDirectoryClient(this, 
					ApplicationInfo.MODE, 
					ApplicationInfo.CONSUMER_KEY, 
					ApplicationInfo.CONSUMER_SECRET, 
					ApplicationInfo.OAUTH_TOKEN);
		} catch (BlueviaClientException e){
			Log.e(TAG, e.getMessage());
		}

	}

	private class RetrieveDirectoryResource extends BlueviaAsyncTask<String, Void, String[]>{

		public RetrieveDirectoryResource(Context context) {
			super(context);
		}

		@Override
		protected String[] doInBackground(String... params) {
			String resourceType = params[0];

			String[] result = null;
			try {
				if (resourceType.equals(UserAccessInfo.class.getName())){
					UserAccessInfo r =  mClient.getUserAccessInformation();
					result = r.toStringArray();
				} else if (resourceType.equals(UserProfile.class.getName())){
					UserProfile r = mClient.getUserProfile();
					result = r.toStringArray();
				} else if (resourceType.equals(UserTerminalInfo.class.getName())){
					UserTerminalInfo r = mClient.getUserTerminalInformation();
					result = r.toStringArray();
				} else {
					throw new IllegalArgumentException("Unsopported directory resource type");
				}


			} catch (BlueviaClientException e){
				Log.e(TAG, e.getMessage());
				errorMessage = e.getMessage();
			}

			return result;
		}

		@Override
		protected void onPostExecute(String[] result) {
			super.onPostExecute(result);
			if (result != null)
				showResult(result);
			else showError(errorMessage);
		}

	}

	private void getResource(){

		try{
			new RetrieveDirectoryResource(this).execute(RESOURCE_NAMES[mDirectory]);
		} catch (IllegalArgumentException e){
			showError("Error retrieving directory information");
			Log.e(TAG, e.getMessage(), e);
		}

	}

	private void showResult(String[] result){

		ArrayAdapter<String> parameterAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
		for (int i=0; i<result.length; i++){
			parameterAdapter.add(result[i]);
		}
		mParameterList.setAdapter(parameterAdapter);

	}

	private void showError(String error){
		Log.e(TAG, error);
		ArrayAdapter<String> parameterAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
		parameterAdapter.add(error);
		mParameterList.setAdapter(parameterAdapter);
	}

}
