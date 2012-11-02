package hpx.android.graph;

import hpx.android.HpxCallback;
import hpx.android.Runtime;
import hpx.android.perfcounters.Constants;
import hpx.android.perfcounters.QueryStrings;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.XYChart;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class HPXGraphBuilder {
	private static final String TAG = "HPXGraphBuilder";
	private FragmentActivity activity;
	private Runtime runtime;
	private FrameLayout frame;
	private double adjustNumberX;
	private boolean callbacksRunning;
	
	private XYMultipleSeriesRenderer renderer;
	private XYMultipleSeriesDataset data;
	private XYChart chart;
	
	private List<XYSeries> series;
	private List<String> labels;
	private List<String> queryStrings;
	
	private GraphicalView view;
	
	private int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.CYAN, Color.YELLOW, Color.RED };
	private PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND,
											 PointStyle.TRIANGLE, PointStyle.SQUARE,
											 PointStyle.POINT};
	
	public HPXGraphBuilder(FragmentActivity activity, Runtime runtime, View view) {
		this.activity = activity;
		this.runtime = runtime;
		this.frame = (FrameLayout) view;
		
		renderer = new XYMultipleSeriesRenderer();
		
		adjustNumberX = 5.0;
		
		callbacksRunning  = false;
		
	}
	
	
	public void buildGraph(int stringNumber, int scenario, int subscenario, int chartType) {
		disableCallbacks();
		clearFrame();
		
		labels = QueryStrings.buildChartLabels(scenario, subscenario);
		queryStrings = QueryStrings.buildQueryStrings(scenario, subscenario);
	
		fixString(stringNumber);
		
		initializeData();
		
		enableCallbacks();
		
		determineChart(chartType);
		
		view = new GraphicalView(activity, chart);
		callbacksRunning = true;
		
		loadFrame();
		
		
	}
	
	public void buildGraph(int localityNumber, int scenario, int subScenario, int workerNumber, int chartType) {
		disableCallbacks();
		clearFrame();
		
		labels = QueryStrings.buildChartLabels(scenario, subScenario);
		queryStrings = QueryStrings.buildQueryStrings(scenario, subScenario);
		
		fixString(localityNumber, workerNumber);
		
		initializeData();
		
		enableCallbacks();
		
		determineChart(scenario);
		
		view = new GraphicalView(activity, chart);
		callbacksRunning =  true;
		
		loadFrame();
	}
	
	private void determineChart(int scenario) {
		switch(scenario) {
		case Constants.CHART_LINE:
			renderer.setAxesColor(Color.BLACK);
			renderer.setYLabelsAlign(Align.RIGHT);
			renderer.setLabelsColor(Color.BLACK);
			renderer.setAxisTitleTextSize(16);
			renderer.setChartTitleTextSize(20);
			renderer.setLabelsTextSize(15);
			renderer.setLegendTextSize(15);
			renderer.setPointSize(5f);
			renderer.setShowGridX(true);
			renderer.setGridColor(Color.DKGRAY);
			renderer.setDisplayValues(true);
			renderer.setPanEnabled(false);
			renderer.setZoomEnabled(false);
			chart = new LineChart(data, renderer);
			break;
		case Constants.CHART_BAR:
			Type mType = Type.DEFAULT;
			renderer.setAxesColor(Color.BLACK);

			
			
			renderer.setAxisTitleTextSize(16);
			renderer.setChartTitleTextSize(20);
			renderer.setLabelsTextSize(15);
			renderer.setLegendTextSize(15);
			renderer.setShowGridX(true);
			renderer.setGridColor(Color.DKGRAY);
			renderer.setDisplayValues(true);
			renderer.setPanEnabled(false);
			renderer.setZoomEnabled(false);
			chart = new BarChart(data, renderer, mType);
			break;
		default: 
			Log.wtf(TAG, "Not supposed to be here.");
			break;
		}
	}
	
	private void disableCallbacks() {
		if(callbacksRunning) {
			for(int i = 0; i < queryStrings.size(); i++) {
				Log.i(TAG, "Disabling Callback: " + queryStrings.get(i));
				runtime.disablePerfCounterUpdate(queryStrings.get(i));
			}
		}
	}
	
	private void enableCallbacks() {
		for(int i = 0; i < queryStrings.size(); i++) {
			Log.i(TAG, "Enabling Callback: " + queryStrings.get(i));
			runtime.enablePerfCounterUpdate(queryStrings.get(i), new ExtendedCallback(i));
		}
	}
	
	private void initializeData() {
		
		data = new XYMultipleSeriesDataset();
		series = new ArrayList<XYSeries>();
		
		for(int i = 0; i < data.getSeriesCount(); i++) {
			data.removeSeries(0);
		}
		
		XYSeries temp;
		for(int i = 0; i < labels.size(); i++) {
			temp = new XYSeries(labels.get(i));
			series.add(temp); //Add it to our list of XYSeries
			data.addSeries(temp); //Add it to multiple series dataset
		}
		
		for(int i = 0; i < data.getSeriesCount(); i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			renderer.addSeriesRenderer(r);
		}
//		
//		for(int i = 0; i < series.size(); i++) {
//			double tempX = 0.0;
//			series.get(i).add(tempX, 0.0);
//		}
//		
		
	}
	
	/* For changing the string specific to our call */
	private void fixString(int stringNumber) {
		for(int i = 0; i < queryStrings.size(); i++) {
			String temp = queryStrings.get(i).replace("foo", "" + stringNumber);
			queryStrings.set(i, temp);
		}
	}
	
	/*Special fixString for the Worker Thread Calls */
	private void fixString(int localityNumber, int workerNumber) {
		for(int i = 0; i < queryStrings.size(); i++) {
			queryStrings.get(i).replaceAll("foo", "" + localityNumber);
			queryStrings.get(i).replaceAll("boo", "" + workerNumber);
		}
	}
	
	private void clearFrame() {

		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if(null != frame) {
					frame.removeAllViews();
				}
				
				if(null != renderer) {
					renderer.removeAllRenderers();
				}
				
				if(null != view) {
					view.invalidate();
				}
				
				if(null != data) {
					data.removeAllSeries();
				}
				
				if(null != series) {
					series.clear();
				}
				
				if(null != labels) {
					labels.clear();
				}
				
				if(null != series) {
					series.clear();
				}
				
				if(null != queryStrings) {
					queryStrings.clear();
				}
			}
		});
	}
	
	private void loadFrame() {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				frame.addView(view);
			}
		});
	}
	
	public void sleep() {
		disableCallbacks();
		clearFrame();
	}
	
	/* This is a special class of of Hpx callback that stores the index that 
	 * at which 
	 */
	private class ExtendedCallback implements HpxCallback {
		private int index;
		private double lastX;
		
		public ExtendedCallback(int index) {
			this.index = index;
			lastX = 0.0;
		}
		@Override
		public void apply(final String arg) {
			lastX += adjustNumberX;
			series.get(index).add(lastX, Double.parseDouble(arg));
			view.repaint();
		}
		
	}

}
