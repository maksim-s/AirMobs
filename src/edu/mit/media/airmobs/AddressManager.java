package edu.mit.media.airmobs;

/**
 * 
 * @author maksim
 *
 * maintain all relevant addressing information
 * remote peer address / user id / name 
 * 3G address
 * WiFi local address 
 */
public class AddressManager {
	
	public static AddressManager instance() {
		if (_instance == null){
			_instance = new AddressManager();
		}
		
		return _instance;
	}
	
	public void setProviderAddress(String ipaddr) {mIpAddr = ipaddr;};
	public void setProviderPort(int port) {mPort = port;};
	public String getProviderAddress(){ return mIpAddr;};
	public int getProviderPort() {return mPort;};
		
	
	protected AddressManager() {
		mIpAddr = null;
		mPort = 60021;
	};
	
	private static AddressManager _instance=null;
	private String mIpAddr;
	private int mPort;
}
