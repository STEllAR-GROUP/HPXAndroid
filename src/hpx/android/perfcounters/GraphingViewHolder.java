package hpx.android.perfcounters;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

public class GraphingViewHolder { 
	private GraphicalView _graphicalView; 
	private XYMultipleSeriesDataset _data; 
	private XYMultipleSeriesRenderer _renderer; //For tweaking the settings of the graph
	
	private Context _context;
	private FrameLayout _view;
	
	private double lastActiveX;
	private double lastPendingX;
	private double lastSuspendedX;
	private double lastTerminatedX;
	
	private XYSeries activeSeries;
	private XYSeries pendingSeries;
	private XYSeries suspendingSeries;
	private XYSeries terminatedSeries;
	
	
	
	public GraphingViewHolder(Context context, View view) {
		
		/* Initialize our last valeus of X for all types */
		lastActiveX = 0.0;
		lastPendingX = 0.0;
		lastSuspendedX = 0.0;
		lastTerminatedX = 0.0;
		
		/* Cast our view into our FrameLayout */
		_view = (FrameLayout) _view;
		
		/* Initialize our Data Set and retrieve our LineChartView */
		initializeDataSet();
		_graphicalView = ChartFactory.getLineChartView(_context, _data, _renderer);
		
		/* Add our view to the frame */
		_view.addView(_graphicalView);
		
	}
	
	
	private void initializeDataSet() {
		
		/* Initialize all of our Series at a and y value 0 */
		activeSeries.add(lastActiveX, 0);
		pendingSeries.add(lastPendingX, 0);
		suspendingSeries.add(lastSuspendedX, 0);
		terminatedSeries.add(lastTerminatedX, 0);
										
		/* Add all of our series to the Data Set */
		_data.addSeries(activeSeries);
		_data.addSeries(pendingSeries);
		_data.addSeries(suspendingSeries);
		_data.addSeries(terminatedSeries);
		
		/*Set all Render values for the chart */
		
	}
	
	public void updateActive(String arg) {
		double num = Double.parseDouble(arg);
		lastActiveX += 5; //MAY NEED TO TWEAK THIS VALUE;
		_data.putValueToSeriesAt(Constants.ACTIVE, lastActiveX, num);
		//activeSeries.add(lastActiveX, num);
		_graphicalView.repaint();
	}
	
	public void updatePending(String arg) {
		double num = Double.parseDouble(arg);
		lastPendingX += 5;
		_data.putValueToSeriesAt(Constants.PENDING, lastPendingX, num);
		//pendingSeries.add(lastPendingX, num);
		_graphicalView.repaint();
	}
	
	public void updateSuspended(String arg) {
		double num = Double.parseDouble(arg);
		lastSuspendedX += 5;
		_data.putValueToSeriesAt(Constants.SUSPENDING, lastSuspendedX, num);
		//suspendingSeries.add(lastSuspendedX, num);
		_graphicalView.repaint();
	}
	
	public void updateTerminated(String arg) {
		double num = Double.parseDouble(arg);
		lastTerminatedX += 5;
		_data.putValueToSeriesAt(Constants.TERMINATED, lastTerminatedX, num);
		//terminatedSeries.add(lastTerminatedX, num);
		_graphicalView.repaint();
	}
}
