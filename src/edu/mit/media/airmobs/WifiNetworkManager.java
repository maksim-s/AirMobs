package edu.mit.media.airmobs;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
	private NetworkParams networkParams;
	private WifiManager wifiManager;

	public WifiNetworkManager(Context c) {
		// set the wifi parameters -> ipaddress,
		wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
		status  = wifiManager.isWifiEnabled();
		
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
		return true;
	}
	
	// connect to a wifi network
	public boolean connect(String name) {
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
