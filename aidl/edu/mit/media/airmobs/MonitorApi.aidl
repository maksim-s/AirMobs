package edu.mit.media.airmobs;

import edu.mit.media.airmobs.MonitorListener;

interface MonitorApi {
 
	int getStatus();
	boolean exit();
	void test(int test_case);
	void addMonitorListener(MonitorListener handler);
	void removeMonitorListener(MonitorListener handler);
}