package edu.mit.media.airmobs;

import android.util.Log;

public abstract class Worker implements Runnable{

	public enum WORKER_STATE {FINISHED_OK, FINISHED_ERROR, WORKING, IDLE} ;
	
	public Worker() {
		is_working = false;
		mState = WORKER_STATE.IDLE;
	}
	
	abstract void work();
	
	public void run() {
		try {
			work();
		}
		catch (Exception e){
			Log.e(LOG_TAG, "worker thread exception while working");
			mState = WORKER_STATE.FINISHED_ERROR;
			stopWorking();
		}
	}
	
	public void startWorking(){
		is_working = true;
		mState = WORKER_STATE.WORKING;
		mThread = new Thread(this);
		mThread.start();
	};
	
	public void stopWorking(){
		is_working = false;
		mThread.interrupt();
		if (mState == WORKER_STATE.WORKING)
			mState = WORKER_STATE.FINISHED_OK;
		mBoss.workerFinished(this,mState);
	};
	
	public void setBoss(Boss b){
		mBoss = b;
	}
	
	protected Thread mThread;
	protected boolean is_working;
	protected WORKER_STATE mState;
	protected Boss mBoss; 
	private static final String LOG_TAG = "Airmobs::Worker";
}
