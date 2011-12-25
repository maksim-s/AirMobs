package edu.mit.media.airmobs;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * This class facilitate a service by handling the notifications and slightly
 * more convenient API - 
 * all common code for Airmobs services should be included in this class
 * @author eyalto
 *
 */
public abstract class ServiceAdapter extends Service {
	/*
	 * to be implemented by the specific service
	 */
	abstract IBinder 	getApiObject();
	abstract String 	getNotificationText();
	abstract void 		created();
	abstract void		destroyed();
	
	public void onCreate() {
		Log.i(LOG_TAG,"onCreate - creating service");
	    // Display a notification about us starting.  We put an icon in the status bar.
		showNotification();	
		created();
	}
	
	public void onDestroy() {
		cancelNotification();
		destroyed();
	}
	
	private void showNotification() {
		//
		 NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	
	    // Set the icon, scrolling text and timestamp
	    Notification notification = new Notification(R.drawable.icon, getNotificationText(),
	            System.currentTimeMillis());
	
	    // The PendingIntent to launch our activity if the user selects this notification
	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	            new Intent(this, AirMobsActivity.class), 0);
	
	    // Set the info for the views that show in the notification panel.
	    notification.setLatestEventInfo(this, getText(R.string.notif_title), getNotificationText(), contentIntent);
	    
	
	    // Send the notification
	    // TODO: notification id - make a resource.
	    mNM.notify(R.string.notif_title, notification);
		
	}
	private void cancelNotification() {
		//
		NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	    // 
		mNM.cancel(R.string.notif_title);
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(LOG_TAG, "Received Start Command start id " + startId + ": " + intent);
		
		return START_STICKY;
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return getApiObject();
	}
	private static final String LOG_TAG = "AirMobs::ServiceAdapter";
}
