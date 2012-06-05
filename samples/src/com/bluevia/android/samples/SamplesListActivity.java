package com.bluevia.android.samples;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bluevia.android.samples.ad.AdActivity;
import com.bluevia.android.samples.directory.DirectoryActivity;
import com.bluevia.android.samples.location.LocationActivity;
import com.bluevia.android.samples.mms.MmsMOActivity;
import com.bluevia.android.samples.mms.MmsMTActivity;
import com.bluevia.android.samples.oauth.OauthActivity;
import com.bluevia.android.samples.payment.PaymentActivity;
import com.bluevia.android.samples.sms.SmsMOActivity;
import com.bluevia.android.samples.sms.SmsMTActivity;

public class SamplesListActivity extends ListActivity {

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
			i.setClass(this, OauthActivity.class);
			break;
		case SMS_MT_EXAMPLE:
			i.setClass(this, SmsMTActivity.class);
			break;
		case SMS_MO_EXAMPLE:
			i.setClass(this, SmsMOActivity.class);
			break;
		case MMS_MT_EXAMPLE:
			i.setClass(this, MmsMTActivity.class);
			break;
		case MMS_MO_EXAMPLE:
			i.setClass(this, MmsMOActivity.class);
			break;
		case USER_CONTEXT_EXAMPLE:
			i.setClass(this, DirectoryActivity.class);
			break;
		case ADVERTISING_EXAMPLE:
			i.setClass(this, AdActivity.class);
			break;
		case LOCATION_EXAMPLE:
			i.setClass(this, LocationActivity.class);
			break;
		case PAYMENT_EXAMPLE:
			i.setClass(this, PaymentActivity.class);
			break;
		}
		startActivity(i);
	}
	
	
	
}
