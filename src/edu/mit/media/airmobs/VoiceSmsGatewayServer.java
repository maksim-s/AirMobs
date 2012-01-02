package edu.mit.media.airmobs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import edu.mit.media.airmobs.MonitorService.AirmobsRole;

import android.util.Log;

public class VoiceSmsGatewayServer extends Boss implements Runnable {

	public static final int PORT = 60021;
	private AirmobsRole mMode;
	
	public void start(MonitorService.AirmobsRole mode){
		try {
			mMode = mode;
			s = new ServerSocket(PORT);
			mThread = new Thread(this);
			mRunning = true;
			mThread.start();
		} catch (IOException e) {
			Log.e(LOG_TAG,"error creating server socket for Voice Sms Gateway");
		}
	};
	
	public void exit(){
		mRunning = false;
	};
	
	public boolean isRunning() {return mRunning;};
	
	public MonitorService.AirmobsRole getServerMode() {return mMode; }
	
	@Override
	public void run() {
		
		while (mRunning){
			try {
				Socket ws = s.accept();
				Worker w = null;
				if (mMode == MonitorService.AirmobsRole.PROVIDER) {
					w = new AirmobsSmsForward(ws);
				}
				else if (mMode == MonitorService.AirmobsRole.CUSTOMER){
					w = new AirmobsSmsReply(ws);
				}
				addWorker(w); // adds to worker list -> and start the worker thread
			} catch (IOException e) {
				Log.e(LOG_TAG,"error accepting gw socket for Voice Sms Gateway");
				e.printStackTrace();
			}
			
		}
		
		try {
			s.close();
		} catch (IOException e) {
			Log.e(LOG_TAG,"error closing server exiting Voice Sms Gateway");
		}
	}

	private ServerSocket s;
	private Thread mThread;
	private boolean mRunning;
	private static final String LOG_TAG = "Airmobs::VoiceSmsGatewayServer";
}
