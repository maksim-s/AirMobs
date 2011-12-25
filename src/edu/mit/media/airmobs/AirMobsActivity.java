package edu.mit.media.airmobs;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AirMobsActivity extends Activity implements ServiceBinder.BoundCallback{
	
	private static final String LOG_TAG = "AirMobsActivity";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
       started = false;
        
       mBinder = new ServiceBinder();
       started = mBinder.isServiceStarted(MonitorService.class.getName(),this);
       if (!started){
			startService(new Intent(MonitorService.class.getName()));
			started = true;
        }
        mBinder.bindToService(MonitorService.class.getName(), this, this);
        
    }
    
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    if (item.getItemId() == R.id.exit_srv) {
	    	try {
				api.exit();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				Log.e(LOG_TAG,"api exit call failed");
			}
	    	mBinder.unbindToService(MonitorService.class.getName(), this);
	    	stopService(new Intent(MonitorService.class.getName()));
	    	started = false;
	    }	 
	    // start settings activities
	    return true;
	}
    
    
	@Override
	public void serviceBound(IBinder service) {
		api = MonitorApi.Stub.asInterface(service);
		// TODO implement whatever is needed to get the API object
		try {
			Log.i(LOG_TAG,"AirMobsActivity has bound to service"+(new Integer(api.getStatus()).toString()));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Log.e(LOG_TAG,"api call failed");
			e.printStackTrace();
		}
	}    
	
	private MonitorApi api;
    private ServiceBinder mBinder;
    private boolean started;

}