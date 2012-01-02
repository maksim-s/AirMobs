package edu.mit.media.airmobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * ServiceBinder class is a helper class that allows multiple client applications
 * bind and start multiple services, a specific service can be bound to multiple activities
 * and a specific activity can bind itself to multiple services.
 * to facilitate this ServiceBinder class keep reference to the services started and the 
 * activities bound to each service
 * 
 * @author eyalto
 *
 */
public class ServiceBinder implements ServiceConnection {
	
	/**
	 * Callback interface to be implemented by activities that request to bind to a service
	 * providing the service object that will be used to get the specific service api object
	 */
	public interface BoundCallback {
		/**
		 * called when the activity is bound to the service in order to extract api object
		 * @param service - the service object after bounding to the activity
		 */
		public void serviceBound(IBinder service);
	}
	
	/**
	 * Constructs a service binder helper class that will be used to bind and create 
	 * services needed by the application 
	 */
	public ServiceBinder(){
		mHandlers = new HashMap<String, ArrayList<BoundCallback>>();
		mServicesBindMap = new HashMap<String,Boolean>();
		started = false;
	}
	
	/**
	 * request the binder to bind to the specified service and return the api though the handler
	 * @param serviceName - the service that the application should bind to 
	 * @param handler - handles the returned api object from the service
	 * @param app - the activity starting the service
	 * @see android.app.Activity#bindService(Intent, ServiceConnection, int)
	 */
	public void bindToService(String serviceName, BoundCallback handler, Context app) {
		boolean bound = app.bindService(new Intent(serviceName),this,0);
		if (bound){
			mServicesBindMap.put(serviceName, new Boolean(bound));
			if (mHandlers.containsKey(serviceName)){
				// add handler to existing handlers list
				ArrayList<BoundCallback> list = mHandlers.get(serviceName);
				list.add(handler);
			}
			else{
				// create a list of handlers and add the first handler
				ArrayList<BoundCallback> list = new ArrayList<BoundCallback>();
				list.add(handler);
				mHandlers.put(serviceName, list);
			}
		}
	}
	
	public void unbindToService(String serviceName, Context app){
		if (isBound(serviceName)) {
			app.unbindService(this);
			mServicesBindMap.put(serviceName, new Boolean(false));
		}
	}
	/**
	 * called when the service is connected
	 * checks if the service reference exists and calls all the activities that are bound to the specific service
	 * by calling their respective callback notification method (serviceBound)
	 * 
	 * @see ServiceBinder.BoundCallback#serviceBound(IBinder)
	 * @see android.content.ServiceConnection#onServiceConnected(android.content.ComponentName, android.os.IBinder)
	 */
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		String classname = name.getClassName();
		if (mHandlers.containsKey(classname)){
			ListIterator<BoundCallback> it = mHandlers.get(classname).listIterator();
			// notify all handlers that the service is bound
			while(it.hasNext()){
				it.next().serviceBound(service);
			}	
		}
	}
	/**
	 * called when the service is disconnected - clears all handler activities bound to the service
	 * TODO: make sure this works right ...
	 * @see android.content.ServiceConnection#onServiceDisconnected(android.content.ComponentName)
	 */
	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO verify connection / disconnection flow
		if (mHandlers.containsKey(name)){
			// remove all reference to handlers for service named name
			mHandlers.get(name).clear();
		}
	}
	/**
	 * checks the internal reference if the service is bound 
	 * @param serviceName - the service that needs to be checked for bound state
	 * @return the binding state of the service specified
	 */
	public boolean isBound(String serviceName) { 
		boolean bound = false;
		
		if (mServicesBindMap.containsKey(serviceName)){
			bound = ((Boolean)mServicesBindMap.get(serviceName)).booleanValue();
		}
		
		return bound;
	}
	/**
	 * checks if the service is already started so it can be bound without starting it again
	 * @param serviceName - the service to test for running state
	 * @param app - the activity that wishes to bind to the service used to access running services list
	 * @return true if service was started already false if service is not running
	 */
	public boolean isServiceStarted(String serviceName, Context app) {
		boolean found = false;
		ActivityManager mgr = (ActivityManager) app.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> servicesInfo = mgr.getRunningServices(Integer.MAX_VALUE);
		for ( Iterator<ActivityManager.RunningServiceInfo> it = servicesInfo.iterator() ; it.hasNext() && ! started ;  ){
			ActivityManager.RunningServiceInfo srvInfo = it.next();
			if (srvInfo.service.getClassName().equals(serviceName)){
				found = true;
				Log.i(LOG_TAG,"control service found . attempting to bind ");
			}
		}
		started = found;
		return found;
	}
	
	private HashMap<String, ArrayList<BoundCallback>> mHandlers;
	private HashMap<String,Boolean> mServicesBindMap;
	private boolean started;
	private static final String LOG_TAG = "AirMobs::ServiceBinder";

}
