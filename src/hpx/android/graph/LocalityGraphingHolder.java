package hpx.android.graph;

import hpx.android.HpxCallback;
import hpx.android.Runtime;
import hpx.android.perfcounters.Constants;

import org.achartengine.GraphicalView;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.XYChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

public class LocalityGraphingHolder {
	private static final String TAG = "Locality Graphing Holder";
	private FragmentActivity _activity;
	private Runtime _runTime;
	private FrameLayout _frame;
	
	
	/* Graph Variables */
	private GraphicalView _graphView;
	
	private XYMultipleSeriesDataset _dataCount;
	private XYMultipleSeriesDataset _dataTime;
	private XYMultipleSeriesDataset _messageCount;
	private XYMultipleSeriesDataset _parcelsCount;
	private XYMultipleSeriesDataset _parcelQueue;
	private XYMultipleSeriesDataset _serializeCount;
	private XYMultipleSeriesDataset _serializeTime;
	private XYMultipleSeriesDataset _fullEmptyCount;
	private XYMultipleSeriesDataset _dataFlowCount;
	private XYMultipleSeriesDataset _runTimeUptime;
	
	private XYMultipleSeriesRenderer _renderer;
	
	private XYSeries dataCountSent;
	private XYSeries dataCountReceived;
	private XYSeries dataTimeSent;
	private XYSeries dataTimeReceived;
	private XYSeries messageCountSent;
	private XYSeries messageCountReceived;
	private XYSeries parcelsCountSent;
	private XYSeries parcelsCountReceived;
	private XYSeries parcelQueueSent;
	private XYSeries parcelQueueReceived;
	private XYSeries serializeCountSent;
	private XYSeries serializeCountReceived;
	private XYSeries serializeTimeSent;
	private XYSeries serializeTimeReceived;
	private XYSeries fullEmptyConstructed;
	private XYSeries fullEmptyDestructed;
	private XYSeries fullEmptyFired;
	private XYSeries fullEmptyEnqueued;
	private XYSeries fullEmptyDequeued;
	private XYSeries dataFlowCountConstructed;
	private XYSeries dataFlowCountDestructed;
	private XYSeries dataFlowCountInitialized;
	private XYSeries dataFlowCountFired;
	private XYSeries runtimeUptime;
	
	private double lastDataCountSentX;
	private double lastDataCountReceivedX;
	private double lastDataTimeSentX;
	private double lastDataTimeReceivedX;
	private double lastMessageCountSentX;
	private double lastMessageCountReceivedX;
	private double lastParcelsCountSentX;
	private double lastParcelsCountReceivedX;
	private double lastParcelQueueSentX;
	private double lastParcelQueueReceivedX;
	private double lastSerializeCountSentX;
	private double lastSerializeCountReceivedX;
	private double lastSerializeTimeSentX;
	private double lastSerializeTimeReceivedX;
	private double lastFullEmptyConstructedX;
	private double lastFullEmptyDestructedX;
	private double lastFullEmptyFiredX;
	private double lastFullEmptyEnqueuedX;
	private double lastFullEmptyDequeuedX;
	private double lastDataFlowConstructedX;
	private double lastDataFlowDestructedX;
	private double lastDataFlowInitializedX;
	private double lastDataFlowFiredX;
	private double lastRuntimeUptimeX;
	
	
	private int currentScenario;
	private int localityNumber;
	
	private double adjustNum;
	
	private int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.CYAN, Color.YELLOW, Color.RED };
	private PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND,
											 PointStyle.TRIANGLE, PointStyle.SQUARE,
											 PointStyle.POINT};
	
	private XYChart _chart;
	
	
	public LocalityGraphingHolder(FragmentActivity activity, Runtime runtime, View view, int locnum) {
		_activity = activity;
		_runTime = runtime;
		_frame = (FrameLayout) view;
		localityNumber = locnum;
		
		adjustNum = 5.0;
		
		initializeDataSets();
		
		
		currentScenario = Constants.LOCALITY_DATACOUNT;
		switchGraph(Constants.LOCALITY_DATACOUNT);
		
		
		
	}
	
	/* When user clicks on a different list item, this will switch out the graph with the one 
	 * specific to that list item.
	 */
	public void switchGraph(final int scenario) {
		
		_activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				disableCallbacks();
				currentScenario = scenario;
				_chart = null;
				_frame.removeAllViews();
				_renderer.removeAllRenderers();
				switch(scenario) {
				case Constants.LOCALITY_DATACOUNT: 
					
                    /*
					setSeriesRenderer(_dataCount);
					_chart = new LineChart(_dataCount, _renderer);
					_graphView = new GraphicalView(_activity, _chart);
					_frame.addView(_graphView);
					
					_runTime.enablePerfCounterUpdate("/data{locality#" + localityNumber + "/total}/count/sent", 
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastDataCountSentX += adjustNum; 
							dataCountSent.add(lastDataCountSentX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
					
					_runTime.enablePerfCounterUpdate("/data{locality#" + localityNumber + "/total}/count/received", 
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastDataCountReceivedX += adjustNum;
							dataCountReceived.add(lastDataCountReceivedX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
                    */
					break;
				case Constants.LOCALITY_DATATIME:
					/*
					setSeriesRenderer(_dataTime);
					_chart = new LineChart(_dataTime, _renderer);
					_graphView = new GraphicalView(_activity, _chart);
					_frame.addView(_graphView);
					
					_runTime.enablePerfCounterUpdate("/data{locality#" + localityNumber + "/total}/time/sent", 
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastDataTimeSentX += adjustNum;
							dataTimeSent.add(lastDataTimeSentX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
					
					_runTime.enablePerfCounterUpdate("/data{locality#" + localityNumber + "/total}/time/received", 
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastDataTimeReceivedX += adjustNum;
							dataTimeReceived.add(lastDataTimeReceivedX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
                    */
					break;
				case Constants.LOCALITY_MESSAGECOUNT:
					/*
					setSeriesRenderer(_messageCount);
					_chart = new LineChart(_messageCount, _renderer);
					_graphView = new GraphicalView(_activity, _chart);
					_frame.addView(_graphView);
					
					_runTime.enablePerfCounterUpdate("/messages{locality#" + localityNumber + "/total}/count/sent",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastMessageCountSentX += adjustNum;
							messageCountSent.add(lastMessageCountSentX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
					
					_runTime.enablePerfCounterUpdate("/messages{locality#" + localityNumber + "/total}/count/received",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastMessageCountReceivedX += adjustNum;
							messageCountReceived.add(lastMessageCountReceivedX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
                    */
					break;
				case Constants.LOCALITY_PARCELSCOUNT:
					/*
					setSeriesRenderer(_parcelsCount);
					_chart = new LineChart(_parcelsCount, _renderer);
					_graphView = new GraphicalView(_activity, _chart);
					_frame.addView(_graphView);
					
					_runTime.enablePerfCounterUpdate("/parcels{locality#" + localityNumber + "/total}/count/sent",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastParcelsCountSentX += adjustNum;
							parcelsCountSent.add(lastParcelsCountSentX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
					
					_runTime.enablePerfCounterUpdate("/parcels{locality#" + localityNumber + "/total}/count/received",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastParcelsCountReceivedX += adjustNum;
							parcelsCountReceived.add(lastParcelsCountReceivedX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
                    */
					break;
				case Constants.LOCALITY_PARCELQUEUE:
					/*
					setSeriesRenderer(_parcelQueue);
					_chart = new LineChart(_parcelQueue, _renderer);
					_graphView = new GraphicalView(_activity, _chart);
					_frame.addView(_graphView);
					
					_runTime.enablePerfCounterUpdate("/parcelqueue{locality#" + localityNumber + "/total}/length/send",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastParcelQueueSentX += adjustNum;
							parcelQueueSent.add(lastParcelQueueSentX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
					
					_runTime.enablePerfCounterUpdate("/parcelqueue{locality#" + localityNumber + "/total}/length/receive",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastParcelQueueReceivedX += adjustNum;
							parcelQueueReceived.add(lastParcelQueueReceivedX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
                    */
					break;
				case Constants.LOCALITY_SERIALIZECOUNT:
					/*
					setSeriesRenderer(_serializeCount);
					_chart = new LineChart(_serializeCount, _renderer);
					_graphView = new GraphicalView(_activity, _chart);
					_frame.addView(_graphView);
					
					_runTime.enablePerfCounterUpdate("/serialize{locality#" + localityNumber + "/total}/count/sent",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastSerializeCountSentX += adjustNum;
							serializeCountSent.add(lastSerializeCountSentX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
					
					_runTime.enablePerfCounterUpdate("/serialize{locality#" + localityNumber + "/total}/count/received",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastSerializeCountReceivedX += adjustNum;
							serializeCountReceived.add(lastSerializeCountReceivedX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
                    */
					break;
				case Constants.LOCALITY_SERIALIZETIME:
					/*
					setSeriesRenderer(_serializeTime);
					_chart = new LineChart(_serializeTime, _renderer);
					_graphView = new GraphicalView(_activity, _chart);
					_frame.addView(_graphView);
					
					_runTime.enablePerfCounterUpdate("/serialize{locality#" + localityNumber + "/total}/time/sent",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastSerializeTimeSentX += adjustNum;
							serializeTimeSent.add(lastSerializeTimeSentX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
					
					_runTime.enablePerfCounterUpdate("/serialize{locality#" + localityNumber + "/total}/time/received",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastSerializeTimeReceivedX += adjustNum;
							serializeTimeReceived.add(lastSerializeTimeReceivedX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
                    */
					break;
				case Constants.LOCALITY_FULLEMPTYCOUNT:
					/*
					setSeriesRenderer(_fullEmptyCount);
					_chart = new LineChart(_fullEmptyCount, _renderer);
					_graphView = new GraphicalView(_activity, _chart);
					_frame.addView(_graphView);
					
					_runTime.enablePerfCounterUpdate("/full_empty{locality#" + localityNumber + "/total}/count/constructed",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastFullEmptyConstructedX += adjustNum;
							fullEmptyConstructed.add(lastFullEmptyConstructedX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
					
					_runTime.enablePerfCounterUpdate("/full_empty{locality#" + localityNumber + "/total}/count/destructed",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastFullEmptyDestructedX += adjustNum;
							fullEmptyDestructed.add(lastFullEmptyDestructedX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
					
					_runTime.enablePerfCounterUpdate("/full_empty{locality#" + localityNumber +"/total}/count/fired",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastFullEmptyFiredX += adjustNum;
							fullEmptyFired.add(lastFullEmptyFiredX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
					
					_runTime.enablePerfCounterUpdate("/full_empty{locality#" + localityNumber + "/total}/count/read_dequeued",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastFullEmptyDequeuedX += adjustNum;
							fullEmptyDequeued.add(lastFullEmptyDequeuedX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
					
					_runTime.enablePerfCounterUpdate("/full_empty{locality#" + localityNumber + "/total}/count/read_enqueued",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastFullEmptyEnqueuedX += adjustNum;
							fullEmptyEnqueued.add(lastFullEmptyEnqueuedX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
                    */
					break;
				case Constants.LOCALITY_DATAFLOWCOUNT:
					/*
					setSeriesRenderer(_dataFlowCount);
					_chart = new LineChart(_dataFlowCount, _renderer);
					_graphView = new GraphicalView(_activity, _chart);
					_frame.addView(_graphView);
					
					_runTime.enablePerfCounterUpdate("/dataflow{locality#" + localityNumber + "/total}/count/constructed",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastDataFlowConstructedX += adjustNum;
							dataFlowCountConstructed.add(lastDataFlowConstructedX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
					
					_runTime.enablePerfCounterUpdate("/dataflow{locality#" + localityNumber + "/total}/count/destructed",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastDataFlowDestructedX += adjustNum;
							dataFlowCountDestructed.add(lastDataFlowDestructedX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
					
					_runTime.enablePerfCounterUpdate("/dataflow{locality#" + localityNumber + "/total}/count/fired",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastDataFlowFiredX += adjustNum;
							dataFlowCountFired.add(lastDataFlowFiredX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
					
					_runTime.enablePerfCounterUpdate("/dataflow{locality#" + localityNumber + "/total}/count/initialized",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastDataFlowInitializedX += adjustNum;
							dataFlowCountInitialized.add(lastDataFlowInitializedX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
                    */
					break;
				case Constants.LOCALITY_UPTIME:
					/*
					setSeriesRenderer(_runTimeUptime);
					_chart = new LineChart(_runTimeUptime, _renderer);
					_graphView = new GraphicalView(_activity, _chart);
					_frame.addView(_graphView);
					
					_runTime.enablePerfCounterUpdate("/runtime{locality#" + localityNumber + "/total}/uptime",
							new HpxCallback() {
						@Override
						public void apply(String arg) {
							lastRuntimeUptimeX += adjustNum;
							runtimeUptime.add(lastRuntimeUptimeX, Double.parseDouble(arg));
							_graphView.repaint();
						}
					});
                    */
					break;
				default:
					Log.wtf(TAG, "Not supposed to be here");

				}
				
			}
			
		});
		
	}
	
	public void disableCallbacks() {
		switch(currentScenario) {
		case Constants.LOCALITY_DATACOUNT: 
			_runTime.disablePerfCounterUpdate("/data{locality#" + localityNumber + "/total}/count/sent");
			_runTime.disablePerfCounterUpdate("/data{locality#" + localityNumber + "/total}/count/received");
			break;
		case Constants.LOCALITY_DATATIME:
			_runTime.disablePerfCounterUpdate("/data{locality#" + localityNumber + "/total}/time/sent");
			_runTime.disablePerfCounterUpdate("/data{locality#" + localityNumber + "/total}/time/received");
			break;
		case Constants.LOCALITY_MESSAGECOUNT:
			_runTime.disablePerfCounterUpdate("/messages{locality#" + localityNumber + "/total}/count/sent");
			_runTime.disablePerfCounterUpdate("/messages{locality#" + localityNumber + "/total}/count/received");
			break;
		case Constants.LOCALITY_PARCELSCOUNT:
			_runTime.disablePerfCounterUpdate("/parcels{locality#" + localityNumber + "/total}/count/sent");
			_runTime.disablePerfCounterUpdate("/parcels{locality#" + localityNumber + "/total}/count/received");
			break;
		case Constants.LOCALITY_PARCELQUEUE:
			_runTime.disablePerfCounterUpdate("/parcelqueue{locality#" + localityNumber + "/total}/length/send");
			_runTime.disablePerfCounterUpdate("/parcelqueue{locality#" + localityNumber + "/total}/length/receive");
			break;
		case Constants.LOCALITY_SERIALIZECOUNT:
			_runTime.disablePerfCounterUpdate("/serialize{locality#" + localityNumber + "/total}/count/sent");
			_runTime.disablePerfCounterUpdate("/serialize{locality#" + localityNumber + "/total}/count/received");
			break;
		case Constants.LOCALITY_SERIALIZETIME:
			_runTime.disablePerfCounterUpdate("/serialize{locality#" + localityNumber + "/total}/time/sent");
			_runTime.disablePerfCounterUpdate("/serialize{locality#" + localityNumber + "/total}/time/received");
			break;
		case Constants.LOCALITY_FULLEMPTYCOUNT:
			_runTime.disablePerfCounterUpdate("/full_empty{locality#" + localityNumber + "/total}/count/constructed");
			_runTime.disablePerfCounterUpdate("/full_empty{locality#" + localityNumber + "/total}/count/destructed");
			_runTime.disablePerfCounterUpdate("/full_empty{locality#" + localityNumber + "/total}/count/fired");
			_runTime.disablePerfCounterUpdate("/full_empty{locality#" + localityNumber + "/total}/count/read_dequeued");
			_runTime.disablePerfCounterUpdate("/full_empty{locality#" + localityNumber + "/total}/count/read_enqueued");
			break;
		case Constants.LOCALITY_DATAFLOWCOUNT:
			_runTime.disablePerfCounterUpdate("/dataflow{locality#" + localityNumber + "/total}/count/constructed");
			_runTime.disablePerfCounterUpdate("/dataflow{locality#" + localityNumber + "/total}/count/destructed");
			_runTime.disablePerfCounterUpdate("/dataflow{locality#" + localityNumber + "/total}/count/fired");
			_runTime.disablePerfCounterUpdate("/dataflow{locality#" + localityNumber + "/total}/count/initialized");
			break;
		case Constants.LOCALITY_UPTIME:
			_runTime.disablePerfCounterUpdate("/runtime{locality#" + localityNumber + "/total}/uptime");
			break;
		default:
			Log.wtf(TAG, "Not supposed to be here");

		}		
	}
	
	/* Initialize all of our variables that will be used for the graph */
	private void initializeDataSets() {
		
		_dataCount = new XYMultipleSeriesDataset();
		_dataTime = new XYMultipleSeriesDataset();
		_messageCount = new XYMultipleSeriesDataset();
		_parcelsCount = new XYMultipleSeriesDataset();
		_parcelQueue = new XYMultipleSeriesDataset();
		_serializeCount = new XYMultipleSeriesDataset();
		_serializeTime = new XYMultipleSeriesDataset();
		_fullEmptyCount = new XYMultipleSeriesDataset();
		_dataFlowCount = new XYMultipleSeriesDataset();
		_runTimeUptime = new XYMultipleSeriesDataset();
		
		_renderer = new XYMultipleSeriesRenderer();
		
		dataCountSent = new XYSeries("Data Count Sent");
		dataCountReceived = new XYSeries("Data Count Received");
		dataTimeSent = new XYSeries("Data Time Sent");
		dataTimeReceived = new XYSeries("Data Time Received");
		messageCountSent = new XYSeries("Message Count Sent");
		messageCountReceived = new XYSeries("Message Count Received");
		parcelsCountSent = new XYSeries("Sent Parcels");
		parcelsCountReceived = new XYSeries("Received Parcels");
		parcelQueueSent = new XYSeries("Parcel Queue Sent");
		parcelQueueReceived = new XYSeries("Parcel Queue Received");
		serializeCountSent = new XYSeries("Serialization Sent Count");
		serializeCountReceived = new XYSeries("Serialization Received Count");
		serializeTimeSent = new XYSeries("Serialize Execution Time Sent");
		serializeTimeReceived = new XYSeries("Serialize Execution Time Received");
		fullEmptyConstructed = new XYSeries("Full Empty Operations Constructed");
		fullEmptyDestructed = new XYSeries("Full Empty Operations Destructed");
		fullEmptyFired = new XYSeries("Full Empty Fired Operations");
		fullEmptyEnqueued = new XYSeries("Full Empty Enqueued Operations");
		fullEmptyDequeued = new XYSeries("Full Empty Dequeued Operations");
		dataFlowCountConstructed = new XYSeries("Data Flow Constructed Operations");
		dataFlowCountDestructed = new XYSeries("Data Flow Destructed Operations");
		dataFlowCountInitialized = new XYSeries("Data Flow Initialized Operations");
		dataFlowCountFired = new XYSeries("Data Flow Fired Operations");
		runtimeUptime = new XYSeries("Locality Runtime Uptime");
		
		lastDataCountSentX = 0.0;
		lastDataCountReceivedX = 0.0;
		lastDataTimeSentX = 0.0;
		lastDataTimeReceivedX = 0.0;
		lastMessageCountSentX = 0.0;
		lastMessageCountReceivedX = 0.0;
		lastParcelsCountSentX = 0.0;
		lastParcelsCountReceivedX = 0.0;
		lastParcelQueueSentX = 0.0;
		lastParcelQueueReceivedX = 0.0;
		lastSerializeCountSentX = 0.0;
		lastSerializeCountReceivedX = 0.0;
		lastSerializeTimeSentX = 0.0;
		lastSerializeTimeReceivedX = 0.0;
		lastFullEmptyConstructedX = 0.0;
		lastFullEmptyDestructedX = 0.0;
		lastFullEmptyFiredX = 0.0;
		lastFullEmptyEnqueuedX = 0.0;
		lastFullEmptyDequeuedX = 0.0;
		lastDataFlowConstructedX = 0.0;
		lastDataFlowDestructedX = 0.0;
		lastDataFlowInitializedX = 0.0;
		lastDataFlowFiredX = 0.0;
		
		_renderer.setAxisTitleTextSize(16);
		_renderer.setChartTitleTextSize(20);
		_renderer.setLabelsTextSize(15);
		_renderer.setLegendTextSize(15);
		_renderer.setPointSize(5f);
		_renderer.setShowGridX(true);
		_renderer.setGridColor(Color.DKGRAY);
		_renderer.setDisplayValues(true);
		
		/* Initialize all of our values to 0 for the series */
		dataCountSent.add(lastDataCountSentX, 0.0);
		dataCountReceived.add(lastDataCountReceivedX, 0.0);
		
		dataTimeSent.add(lastDataTimeSentX, 0.0);
		dataTimeReceived.add(lastDataTimeReceivedX, 0.0);
		
		messageCountSent.add(lastMessageCountSentX, 0.0);
		messageCountReceived.add(lastMessageCountReceivedX, 0.0);
		
		parcelsCountSent.add(lastParcelsCountSentX, 0.0);
		parcelsCountReceived.add(lastParcelsCountReceivedX, 0.0);
		
		parcelQueueSent.add(lastParcelQueueSentX, 0.0);
		parcelQueueReceived.add(lastParcelQueueReceivedX, 0.0);
		
		serializeCountSent.add(lastSerializeCountSentX, 0.0);
		serializeCountReceived.add(lastSerializeCountReceivedX, 0.0);
		
		serializeTimeSent.add(lastSerializeTimeSentX, 0.0);
		serializeTimeReceived.add(lastSerializeTimeReceivedX, 0.0);
		
		fullEmptyConstructed.add(lastFullEmptyConstructedX, 0.0);
		fullEmptyDestructed.add(lastFullEmptyDestructedX, 0.0);
		fullEmptyEnqueued.add(lastFullEmptyEnqueuedX, 0.0);
		fullEmptyDequeued.add(lastFullEmptyDequeuedX, 0.0);
		fullEmptyFired.add(lastFullEmptyFiredX, 0.0);
		
		dataFlowCountConstructed.add(lastDataFlowConstructedX, 0.0);
		dataFlowCountDestructed.add(lastDataFlowDestructedX, 0.0);
		dataFlowCountInitialized.add(lastDataFlowInitializedX, 0.0);
		dataFlowCountFired.add(lastDataFlowFiredX, 0.0);
		
		runtimeUptime.add(lastRuntimeUptimeX, 0.0);
		
		/* Add our individual series to the Multiple Series */
		_dataCount.addSeries(dataCountSent);
		_dataCount.addSeries(dataCountReceived);
		
		_dataTime.addSeries(dataTimeSent);
		_dataTime.addSeries(dataTimeReceived);
		
		_messageCount.addSeries(messageCountSent);
		_messageCount.addSeries(messageCountReceived);
		
		_parcelsCount.addSeries(parcelsCountSent);
		_parcelsCount.addSeries(parcelsCountReceived);
		
		_parcelQueue.addSeries(parcelQueueSent);
		_parcelQueue.addSeries(parcelQueueReceived);
		
		_serializeCount.addSeries(serializeCountSent);
		_serializeCount.addSeries(serializeCountReceived);
		
		_serializeTime.addSeries(serializeTimeSent);
		_serializeTime.addSeries(serializeTimeReceived);
		
		_fullEmptyCount.addSeries(fullEmptyConstructed);
		_fullEmptyCount.addSeries(fullEmptyDestructed);
		_fullEmptyCount.addSeries(fullEmptyEnqueued);
		_fullEmptyCount.addSeries(fullEmptyDequeued);
		_fullEmptyCount.addSeries(fullEmptyFired);
		
		_dataFlowCount.addSeries(dataFlowCountConstructed);
		_dataFlowCount.addSeries(dataFlowCountDestructed);
		_dataFlowCount.addSeries(dataFlowCountInitialized);
		_dataFlowCount.addSeries(dataFlowCountFired);
		
		_runTimeUptime.addSeries(runtimeUptime);
		
	}
	
	private void setSeriesRenderer(XYMultipleSeriesDataset set) {
		for(int i = 0; i < set.getSeriesCount(); i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			_renderer.addSeriesRenderer(r);
		}
	}
	
	
}
