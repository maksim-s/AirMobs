package edu.mit.media.airmobs;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

public class SampleMonitorListener extends MonitorListener.Stub {
	
	public SampleMonitorListener(Context app) {
		mApp = app;
	}
	
	@Override
	public void monitorRoleProvider() throws RemoteException {
		Log.i(LOG_TAG,"runnung as Provider");
	}

	@Override
	public void monitorRoleCustomer() throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG,"runnung as Customer");
	}

	@Override
	public void monitorRoleIdle() throws RemoteException {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG,"runnung as Idle");
	}

	private static final String LOG_TAG = "SampleMonitorListener";
	private Context mApp;
}
