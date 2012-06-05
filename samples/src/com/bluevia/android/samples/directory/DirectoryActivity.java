package com.bluevia.android.samples.directory;

import java.io.IOException;

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

import com.bluevia.android.commons.exception.BlueviaException;
import com.bluevia.android.directory.client.BVDirectory;
import com.bluevia.android.directory.data.AccessInfo;
import com.bluevia.android.directory.data.PersonalInfo;
import com.bluevia.android.directory.data.Profile;
import com.bluevia.android.directory.data.TerminalInfo;
import com.bluevia.android.samples.ApplicationInfo;
import com.bluevia.android.samples.BlueviaAsyncTask;
import com.bluevia.android.samples.R;

public class DirectoryActivity extends Activity {

	public static final String TAG = "DirectoryActivity";

	public static final String[] RESOURCE_NAMES = {
		AccessInfo.class.getName(),
		Profile.class.getName(),
		TerminalInfo.class.getName(),
		PersonalInfo.class.getName()
	};

	private Spinner mSpinnerResource;

	private Button mRetrieveButton;

	private ListView mParameterList;

	private int mDirectory = 0;

	private BVDirectory mClient;

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
			mClient = new BVDirectory(ApplicationInfo.MODE,
					ApplicationInfo.CONSUMER_KEY,
					ApplicationInfo.CONSUMER_SECRET,
					ApplicationInfo.OAUTH_TOKEN_KEY,
					ApplicationInfo.OAUTH_TOKEN_SECRET);
		} catch (BlueviaException e){
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
				if (resourceType.equals(AccessInfo.class.getName())){
					AccessInfo r =  mClient.getAccessInfo();
					result = r.toString().split(",");
				} else if (resourceType.equals(Profile.class.getName())){
					Profile r = mClient.getProfile();
					result = r.toString().split(",");
				} else if (resourceType.equals(TerminalInfo.class.getName())){
					TerminalInfo r = mClient.getTerminalInfo();
					result = r.toString().split(",");
				}  else if (resourceType.equals(PersonalInfo.class.getName())){
					PersonalInfo r = mClient.getPersonalInfo();
					result = r.toString().split(",");
				} else {
					throw new IllegalArgumentException("Unsopported directory resource type");
				}

			} catch (BlueviaException e){
				Log.e(TAG, e.getMessage());
				errorMessage = e.getMessage();
			} catch (IOException e) {
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
