package edu.mit.media.airmobs;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.*;

/**
 * 
 * @author maksim
 *
 * a wrapper class for android WifiManager
 * 
 */

public class WifiNetworkManager {
	private boolean status;
	private boolean isConnected;
	private NetworkParams networkParams;
	private WifiManager wifiManager;

	public WifiNetworkManager(Context c) {
		this.wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
		this.status  = wifiManager.isWifiEnabled();
		if (status) {
			WifiInfo info = wifiManager.getConnectionInfo();
			SupplicantState state = info.getSupplicantState();
			if (state == SupplicantState.DORMANT || state == SupplicantState.INACTIVE) {
				this.isConnected = false;
			} else {
				this.isConnected = true;
			}
		}
		// fill in the network params	
	}
	
	
	
	// enable wifi
	public boolean turnOn() {
		if (this.status) {
			return true;
		} else {
			return this.wifiManager.setWifiEnabled(true);
		}
	}
	
	// disable wifi
	public boolean turnOff() {
		if (this.status) {
			return this.wifiManager.setWifiEnabled(false);
		} else {
			return true;
		}
	}
	
	// returns true if connected to any wifi network
	public boolean isConnected() {
		return this.isConnected;
	}
	
	// return the list of names of available wifi networks
	public ArrayList<String> search() {
		ArrayList<String> networkNames = new ArrayList<String>();
		if (this.wifiManager.startScan()) {
			List<ScanResult> networks = this.wifiManager.getScanResults();
			for (ScanResult n : networks) {
				networkNames.add(n.SSID);
			}
		}
		return networkNames;
	}
	
	// create our own wifi network
	public boolean create(String name) {
		// turn on proxoid stuff
		return true;
	}
	
	// connect to an Airmobs wifi network
	public boolean connect() {
		// turn on transproxy and connect to proxoid of the server.
		ArrayList<String> networkNames = this.search();
		String name = null;
		for (String n : networkNames) {
			if (n.indexOf("Airmobs") > 0) {
				name = n;
				break;
			}
		}
		WifiConfiguration conf = new WifiConfiguration();
		conf.SSID = "\"" + name + "\"";
		int netId = this.wifiManager.addNetwork(conf);
		return this.wifiManager.enableNetwork(netId, true);
	}
	
	// disconnect from the currently connected wifi network
	public boolean disconnect() {
		return this.wifiManager.disconnect();
	}
	
	// 
	public boolean getStatus() {
		return this.status;
	}
	
	public NetworkParams getParams() {
		return this.networkParams;
	}
	
}
