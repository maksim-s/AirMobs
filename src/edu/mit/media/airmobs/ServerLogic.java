package edu.mit.media.airmobs;

import android.os.IBinder;
import android.os.RemoteException;

public class ServerLogic implements MonitorListener {

	
	
	@Override
	public IBinder asBinder() {
		return null;
	}

	@Override
	public void monitorRoleProvider() throws RemoteException {
		// gw server should be running in provider mode
		if (msrv != null) {
			if (msrv.isRunning() && msrv.getServerMode() == MonitorService.AirmobsRole.CUSTOMER){
				msrv.exit();
				msrv = null;
			}
		}
		// gw server running in provider mode
		if (msrv == null){
			msrv = new VoiceSmsGatewayServer();
			msrv.start(MonitorService.AirmobsRole.PROVIDER);
		}
	}

	@Override
	public void monitorRoleCustomer() throws RemoteException {
		// gw server should be running in customer mode
		if (msrv != null) {
			if (msrv.isRunning() && msrv.getServerMode() == MonitorService.AirmobsRole.PROVIDER){
				msrv.exit();
				msrv = null;
			}
		}
		if (msrv == null){
			// run server in customer mode
			msrv = new VoiceSmsGatewayServer();
			msrv.start(MonitorService.AirmobsRole.CUSTOMER);
		}
		
	}

	@Override
	public void monitorRoleIdle() throws RemoteException {
		// gw server is not needed in idle mode should shut down
		if (msrv != null) {
			if (msrv.isRunning()){
				msrv.exit();
				msrv = null;
			}
		}
	}

	VoiceSmsGatewayServer msrv;
}
