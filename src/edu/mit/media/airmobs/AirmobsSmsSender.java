package edu.mit.media.airmobs;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


public class AirmobsSmsSender extends Activity {
	
	private void sendToProvider(String message){
		Log.i(LOG_TAG,"should be sending to Airmobs network provider peer message ==> "+message);
		AddressManager adrm = AddressManager.instance();
		try {
			Socket s = new Socket(adrm.getProviderAddress(),adrm.getProviderPort());
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			out.writeUTF(message);
			out.flush();
			s.close();
		} catch (UnknownHostException e) {
			Log.e(LOG_TAG,"error creating socket - airmobs provider host not found ");
		} catch (IOException e) {
			Log.e(LOG_TAG,"error using provider socket to send sms message ");
		}
	}
	
	@Override
	public void onCreate(Bundle saved) {
		super.onCreate(saved);
		Intent intent;
		Uri uri;
		// this case intent was created by click on a sms:// or smsto://  
		if ((intent = getIntent()) != null
			&& (uri = intent.getData()) != null) {
				mSms = uri.toString();
		}
		// this is explicit activation probably by the monitor service
		else {
			showDialog(DIALOG_ID);
		}
	}

	protected void showSmsEditor(){
		Log.i(LOG_TAG, "should be opening sms editor ... ");
		mSms = "<sms><msgto>8579281302</msgto><msgfrom>8579281302</msgfrom><msgtext> &lt; 8579281302 &gt;  hello world</msgtext></sms>";
		AirmobsSmsSender.this.sendToProvider(mSms);
        AirmobsSmsSender.this.finish();
		
	}
	
	protected Dialog onCreateDialog(int id) {
	    
		AlertDialog.Builder builder = new AlertDialog.Builder(this);		
		builder.setMessage("Send message using Airmobs network?")
	       .setCancelable(false)
	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	    // start activity MSG ui and send the msg and finish
	        	   showSmsEditor();
	        	   // kill messaging ui
	           }
	       })
	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                dialog.cancel();
	        	   // kill messaging ui
	           }
	       });
		AlertDialog alert = builder.create();
		return alert;
	}
	
	private String mSms;
	private static final int DIALOG_ID = 1;
	private static final String LOG_TAG = "Airmobs::AirmobsSmsSender";
}
