package com.bluevia.android.examples;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;


/**
 * Extended AsyncTask adding a Progress Dialog
 * This is used to avoid the user interface getting blocked by the HTTP request.
 * 
 * @author Telefonica R&D
 *
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
public abstract class BlueviaAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    protected String errorMessage;
    protected ProgressDialog mDialog;
    protected Context mContext;

    public BlueviaAsyncTask(Context context){
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mContext != null)
            mDialog = new ProgressDialog(mContext);
        	mDialog.setMessage(mContext.getString(R.string.operation_waiting));
        	mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, mContext.getString(R.string.cancel),
        			new DialogInterface.OnClickListener() {
						
        		public void onClick(DialogInterface dialog, int which) {
					cancel(true);
					Toast.makeText(mContext, mContext.getString(R.string.operation_canceled), Toast.LENGTH_LONG).show();
				}
			});
        	mDialog.show();
    }
    
    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        mDialog.cancel();
    }
}
