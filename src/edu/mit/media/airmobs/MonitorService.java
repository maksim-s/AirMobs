package edu.mit.media.airmobs;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 
 * @author maksim
 *
 * start after boot runs all the other services as needed
 * monitor the state of the networks 
 * decide when to register (or unregister) all the intent handlers
 * content provider for all the airmobs data logged
 *
 */

public class MonitorService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
