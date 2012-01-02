package edu.mit.media.airmobs;

import java.util.ArrayList;

import android.util.Log;

public class Boss {
	
	protected Boss() {
		mWorkForce = new ArrayList<Worker>();
	}
	/**
	 * adds a worker and tell him to start working
	 * @param w
	 */
	protected void addWorker(Worker w) {
		if (!mWorkForce.contains(w)){
			mWorkForce.add(w);
			w.setBoss(this);
			w.startWorking();
		}
	}

	protected void removeWorker(Worker w){
		if (mWorkForce.contains(w)){
			mWorkForce.remove(w);
		}
	}
	
	/**
	 * methods for workers to notify the boss that they are done
	 * @param w - worker that has finished his work
	 */
	public void workerFinished(Worker w, Worker.WORKER_STATE s){
		if (s == Worker.WORKER_STATE.FINISHED_OK) {
			Log.i(LOG_TAG,"worker finished ok ... removing from list");
		}
		else if (s == Worker.WORKER_STATE.FINISHED_ERROR){
			Log.e(LOG_TAG,"woker finished with error ... removing from list");
		}
		removeWorker(w);
	}

	protected ArrayList<Worker> mWorkForce;	
	private static final String LOG_TAG = "Airmobs::Boss";
}
