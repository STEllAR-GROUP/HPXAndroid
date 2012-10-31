package hpx.android.graph;

import org.achartengine.chart.BarChart;
import org.achartengine.chart.XYChart;

import android.support.v4.app.FragmentActivity;

public class AgasGraphingHolder {
	private FragmentActivity _activity; 
	private Runtime _runTime;
	
	/* Graph Variables */
	private XYChart _chart;
	
	
	
	
	public AgasGraphingHolder(FragmentActivity activity, Runtime runtime) {
		_activity = activity;
		_runTime = runtime;
		
		//_chart = new BarChart();
		
	}

}
