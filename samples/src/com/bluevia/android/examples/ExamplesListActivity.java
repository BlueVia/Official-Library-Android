package com.bluevia.android.examples;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bluevia.android.examples.ad.AdExampleActivity;
import com.bluevia.android.examples.directory.DirectoryExampleActivity;
import com.bluevia.android.examples.location.LocationExampleActivity;
import com.bluevia.android.examples.mms.MmsMOExampleActivity;
import com.bluevia.android.examples.mms.MmsMTExampleActivity;
import com.bluevia.android.examples.oauth.OauthExampleActivity;
import com.bluevia.android.examples.payment.PaymentExampleActivity;
import com.bluevia.android.examples.sms.SmsMOExampleActivity;
import com.bluevia.android.examples.sms.SmsMTExampleActivity;

public class ExamplesListActivity extends ListActivity {

	private static final int OAUTH_EXAMPLE = 0;
	private static final int SMS_MT_EXAMPLE = 1;
	private static final int SMS_MO_EXAMPLE = 2;
	private static final int MMS_MT_EXAMPLE = 3;
	private static final int MMS_MO_EXAMPLE = 4;
	private static final int USER_CONTEXT_EXAMPLE = 5;
	private static final int ADVERTISING_EXAMPLE = 6;
	private static final int LOCATION_EXAMPLE = 7;
	private static final int PAYMENT_EXAMPLE = 8;
	
	ArrayAdapter<String> mAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getListView().setBackgroundResource(R.drawable.background);
		getListView().setCacheColorHint(0);
		
		String[] examples = getResources().getStringArray(R.array.examples);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, examples);
		setListAdapter(mAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Intent i = new Intent(Intent.ACTION_MAIN);
		
		switch (position){
		case OAUTH_EXAMPLE:
			i.setClass(this, OauthExampleActivity.class);
			break;
		case SMS_MT_EXAMPLE:
			i.setClass(this, SmsMTExampleActivity.class);
			break;
		case SMS_MO_EXAMPLE:
			i.setClass(this, SmsMOExampleActivity.class);
			break;
		case MMS_MT_EXAMPLE:
			i.setClass(this, MmsMTExampleActivity.class);
			break;
		case MMS_MO_EXAMPLE:
			i.setClass(this, MmsMOExampleActivity.class);
			break;
		case USER_CONTEXT_EXAMPLE:
			i.setClass(this, DirectoryExampleActivity.class);
			break;
		case ADVERTISING_EXAMPLE:
			i.setClass(this, AdExampleActivity.class);
			break;
		case LOCATION_EXAMPLE:
			i.setClass(this, LocationExampleActivity.class);
			break;
		case PAYMENT_EXAMPLE:
			i.setClass(this, PaymentExampleActivity.class);
			break;
		}
		startActivity(i);
	}
	
	
	
}
