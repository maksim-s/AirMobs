package edu.mit.media.airmobs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class OperatorNetworkManager {
	private NetworkParams networkParams;
	private NetworkInfo mobile;
	
	public OperatorNetworkManager(Context c) {
		ConnectivityManager connection = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		this.mobile = connection.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// fill in the network params 
	}
	
	/**
	 * 
	 * @return returns whether the device is connected to an Operator Network
	 */
	public boolean getStatus() {
		return this.mobile.isConnected();
	}
	
	public NetworkParams getParams() {
		return this.networkParams;
	}
	
}
