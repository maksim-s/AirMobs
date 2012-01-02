package edu.mit.media.airmobs;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
/**
 * create a relay worker per customer connecting usually this will be 1:1 but
 * some cases one provider may relay messages to multiple customers.
 * so each customer will get its own SMS relay
 * @author eyalto
 *
 */
public class AirmobsSmsForward extends Worker {

	
	public AirmobsSmsForward(Socket customer) {
		s = customer;
		try {
			is = new DataInputStream(s.getInputStream());
			mState = Worker.WORKER_STATE.IDLE;
		} catch (IOException e) {
			Log.e(LOG_TAG,"error constucting worker unable to get input stream");
			mState = Worker.WORKER_STATE.FINISHED_ERROR;
			mBoss.workerFinished(this, mState);
		}
	}
	// worker thread loop method
	@Override
	void work() {
		// worker thread loop 
		while(is_working){
			try {
				String message = is.readUTF();
				relayMessage(message);
			} catch (IOException e) {
				Log.e(LOG_TAG,"error reading UTF string message from input stream");
				mState = Worker.WORKER_STATE.FINISHED_ERROR;	
				this.stopWorking();
				// TODO: stop running after exception
				// make sure a new worker relay gets a new socket 
			}
		}
	}

	private void relayMessage(String message){
		Log.i(LOG_TAG,"reaply messge ==> "+message);
		Document msg;
		try {
			msg = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(message.getBytes()));
		} catch (Exception e) {
			Log.e(LOG_TAG,"exception while parsing relay message - message = "+message+" ==> not sent");
			return;
		}
		msg.getDocumentElement().normalize();
		String msgto = msg.getElementsByTagName("msgto").item(0).getTextContent();
		String msgfrom = msg.getElementsByTagName("msgfrom").item(0).getTextContent();
		String text = msg.getElementsByTagName("msgtext").item(0).getTextContent();
		
		SmsManager smsm = SmsManager.getDefault();
		smsm.sendTextMessage(msgto, null, text, null, null);
		
	}
	private Socket s;
	private DataInputStream is;
	private static final String LOG_TAG = "Airmobs::SmsRelayWorker"; 
}
