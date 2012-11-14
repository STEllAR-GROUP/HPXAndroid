package hpx.android.perfcounters;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class QueryStrings {
	private static final String TAG = "QueryStrings";
	public static List<String> buildQueryStrings(int scenario, int subScenario) {
		List<String> items = new ArrayList<String>();
		switch(scenario) {
		case Constants.AGAS:
			break;
		case Constants.LOCALITIES:
			
			break;
		case Constants.LOCALITY:
			switch(subScenario) {
			case Constants.LOCALITY_DATACOUNT:
				items.add("/data{locality#foo/total}/count/sent");
				items.add("/data{locality#foo/total}/count/received");
				break;
			case Constants.LOCALITY_DATATIME:
				items.add("/data{locality#foo/total}/time/sent");
				items.add("/data{locality#foo/total}/time/received");
				break;
			case Constants.LOCALITY_MESSAGECOUNT:
				items.add("/messages{locality#foo/total}/count/sent");
				items.add("/messages{locality#foo/total}/count/received");
				break;
			case Constants.LOCALITY_PARCELSCOUNT:
				items.add("/parcels{locality#foo/total}/count/sent");
				items.add("/parcels{locality#foo/total}/count/received");
				break;
			case Constants.LOCALITY_PARCELQUEUE:
			    items.add("/parcelqueue{locality#foo/total}/length/send");
				items.add("/parcelqueue{locality#foo/total}/length/receive");
				break;
			case Constants.LOCALITY_SERIALIZETIME:
				items.add("/serialize{locality#foo/total}/time/sent");
				items.add("/serialize{locality#foo/total}/time/received");
				break;
			case Constants.LOCALITY_SERIALIZECOUNT:
				items.add("/serialize{locality#foo/total}/count/sent");
				items.add("/serialize{locality#foo/total}/count/received");
				break;
			case Constants.LOCALITY_FULLEMPTYCOUNT:
				items.add("/full_empty{locality#foo/total}/count/constructed");
				items.add("/full_empty{locality#foo/total}/count/destructed");
				items.add("/full_empty{locality#foo/total}/count/read_enqueued");
				items.add("/full_empty{locality#foo/total}/count/read_dequeued");
				items.add("/full_empty{locality#foo/total}/count/fired");
				break;
			case Constants.LOCALITY_DATAFLOWCOUNT:
				items.add("/dataflow{locality#foo/total}/count/initialized");
			    items.add("/dataflow{locality#foo/total}/count/constructed");
				items.add("/dataflow{locality#foo/total}/count/destructed");
				items.add("/dataflow{locality#foo/total}/count/fired");
				break;
			case Constants.LOCALITY_UPTIME:
				items.add("/runtime{locality#foo/total}/uptime");
				break;
			default:
				Log.wtf(TAG, "Not supposed to be here");
			}
			break;
		case Constants.THREADS:
			switch(subScenario) {
//			case Constants.THREADS_EXECUTED:
//				items.add("/threads{locality#foo/total}/count/cumulative");
//				break;
			case Constants.THREADS_STATECOUNT:
				items.add("/threads{locality#foo/total}/count/instantaneous/active");
				items.add("/threads{locality#foo/total}/count/instantaneous/pending");
				items.add("/threads{locality#foo/total}/count/instantaneous/suspended");
//				items.add("/threads{locality#foo/total}/count/instantaneous/terminated");
				break;
			case Constants.THREADS_IDLERATE:
				items.add("/threads{locality#foo/total}/idle-rate");
				break;
//			case Constants.THREADS_UNBINDOPS:
//				Log.e(TAG, "This is currently turned off");
//				items.add("/threads{locality#foo/total}/count/stack-unbinds");
//				break;
//			case Constants.THREADS_RECYCLEOPS:
//				Log.e(TAG, "This is currently turned off");
//				items.add("/threads{locality#foo/total}/count/stack-recycles");
//				break;
			default:
				Log.wtf(TAG, "Not supposed to be here: 2nd stage");
				break;
			}
			break;
		case Constants.THREAD:
			switch(subScenario) {
//			case Constants.THREAD_EXECUTED:
//				items.add("/threads{locality#foo/worker-thread#boo}/count/cumulative");
//				break;
			case Constants.THREAD_STATECOUNT:
				items.add("/threads{locality#foo/worker-thread#boo}/count/instantaneous/active");
				items.add("/threads{locality#foo/worker-thread#boo}/count/instantaneous/pending");
				items.add("/threads{locality#foo/worker-thread#boo}/count/instantaneous/suspended");
				items.add("/threads{locality#foo/worker-thread#boo}/count/instantaneous/terminated");
				break;
			case Constants.THREAD_IDLERATE:
				items.add("/threads{locality#boo/worker-thread#boo}/idle-rate");
				break;
			default:
				Log.wtf(TAG, "Not supposed to be here.");
				break;
			}
			break;
		default: 
			Log.wtf(TAG, "Not supposed to be here");
			break;
		}
			
		return items;
	}
	

	
	public static List<String> buildChartLabels(int scenario, int subScenario) {
		List<String> items = new ArrayList<String>();
		
		switch(scenario) {
		case Constants.LOCALITY:
			switch(subScenario) {
			case Constants.LOCALITY_DATACOUNT:
				items.add("Data Count Sent");
				items.add("Data Count Received");
				break;
			case Constants.LOCALITY_DATATIME:
				items.add("Data Time Sent");
				items.add("Data Time Received");
				break;
			case Constants.LOCALITY_MESSAGECOUNT:
				items.add("Messages Sent");
				items.add("Messages Received");
				break;
			case Constants.LOCALITY_PARCELSCOUNT:
				items.add("Parcels Sent");
				items.add("Parcels Received");
				break;
			case Constants.LOCALITY_PARCELQUEUE:
				items.add("Queued Parcels Sent");
				items.add("Queued Parcels Received");
				break;
			case Constants.LOCALITY_SERIALIZETIME:
				items.add("Serialization Execution Time-Sent");
				items.add("Serialization Execution Time-Received");
				break;
			case Constants.LOCALITY_SERIALIZECOUNT:
				items.add("Serialization Sent Count");
				items.add("Serialization Received Count");
				break;
			case Constants.LOCALITY_FULLEMPTYCOUNT:
				items.add("Constructed");
				items.add("Destructed");
				items.add("Enqueued");
				items.add("Dequeued");
				items.add("Fired");
				break;
			case Constants.LOCALITY_DATAFLOWCOUNT:
				items.add("Initialized");
				items.add("Constructed");
				items.add("Destructed");
				items.add("Fired");
				break;
			case Constants.LOCALITY_UPTIME:
				items.add("Uptime");
				break;
			}
			break;
		case Constants.THREADS:
			switch(subScenario) {
//			case Constants.THREADS_EXECUTED:
//				items.add("Threads Executed");
//				break;
			case Constants.THREADS_STATECOUNT:
				items.add("Active");
				items.add("Pending");
				items.add("Suspended");
				//items.add("Terminated");
				break;
			case Constants.THREADS_IDLERATE:
				items.add("Average Idle Rate");
				break;
//			case Constants.THREADS_UNBINDOPS:
//				items.add("Unbind Operations Performed");
//				break;
//			case Constants.THREADS_RECYCLEOPS:
//				items.add("Recycle Operations Performed");
//				break;
			default:
				Log.wtf(TAG, "Not supposed to be here.");
				break;
			}
			break;
		case Constants.THREAD:
			switch(subScenario) {
//			case Constants.THREAD_EXECUTED:
//				items.add("Threads Executed");
//				break;
			case Constants.THREAD_STATECOUNT:
				items.add("Active");
				items.add("Pending");
				items.add("Suspended");
//				items.add("Terminated");
				break;
			case Constants.THREAD_IDLERATE:
				items.add("Average Idle Rate");
				break;
			default:
				Log.wtf(TAG, "Not supposed to be here.");
				break;
			}
			break;
		default:
			Log.wtf(TAG, "Not supposed to be here");
			break;
		}
		
		return items;
	}

}