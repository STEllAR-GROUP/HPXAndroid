package hpx.android.perfcounters;

import hpx.android.R;
import hpx.android.Runtime;
import hpx.android.adapters.AgasListAdapter;
import hpx.android.adapters.LocalitiesListAdapter;
import hpx.android.adapters.LocalityListAdapter;
import hpx.android.adapters.ThreadListAdapter;
import hpx.android.adapters.ThreadsListAdapter;
import hpx.android.graph.LocalityGraphingHolder;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class PerfCounterFragment extends Fragment  {
	
	public static final String ARG_SECTION_NUMBER = "1";
	private static final String TAG = "Performance Counter Fragment";
	
	/* Individual ListViews */
	private ListView _agasList;
	private ListView _localitiesList;
	private ListView _localityList;
	private ListView _threadsList;
	private ListView _threadList;
	
	/* Adapters */
	private AgasListAdapter _agasAdapter;
	private LocalitiesListAdapter _localitiesAdapter;
	private LocalityListAdapter _localityAdapter;
	private ThreadsListAdapter _threadsAdapter;
	private ThreadListAdapter _threadAdapter;
	
	private LocalityGraphingHolder localityHolder;
	
	/* HPX Related */
	private Runtime _runtime;
	private int workerThreads;
	
	/* Action Bar Related */
	private ActionBar bar;
	private TextView actionBarText;
	private int currentStage = 0;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*Initialize our variables here */
		_runtime = new Runtime();
		
		/* Set our Worker Threads Variable right here for now */
		workerThreads = 2;
		
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	
    	//Inflate our XML file containing the view.
    	return inflater.inflate(R.layout.performance_counter_view, container, false);
    			
    }
   
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
        bar = getActivity().getActionBar();
        bar.setDisplayShowCustomEnabled(true);
        
        actionBarText = new TextView(getActivity().getApplicationContext());
        actionBarText.setGravity(Gravity.LEFT);
        actionBarText.setGravity(Gravity.CENTER_HORIZONTAL);
        actionBarText.setGravity(Gravity.CENTER_VERTICAL);
        actionBarText.setTextSize(20);
        actionBarText.setText("Go Back");
        bar.setCustomView(actionBarText);
    	
    	/* Set which initial list we want to display */
		switchListType(Constants.AGAS);
		setListListener(Constants.AGAS);
		setActionBarListener();

    }
	
	@Override
	public void onStart() {
		super.onStart();
		
        String[] args = {
                "--hpx:threads=" + workerThreads
            };
        _runtime.init(args);
        
        _runtime.apply("runHelloWorld", "");

	}
	
	public void setListListener(int scenario) {
	
		switch(scenario) {
		case Constants.AGAS:
			_agasList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> av, View v,
						int position, long id) {
					
					if(position == 9) {
						//Send to localities
						switchListType(Constants.LOCALITIES);
					}
					
				}
			});
			break;
		case Constants.LOCALITIES:
			_localitiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				

				@Override
				public void onItemClick(AdapterView<?> av, View v,
						int position, long id) {
					//Get which locality they clicked on, switch list, and send loc # to graphholder
					switchListType(Constants.LOCALITY);
					localityHolder = new LocalityGraphingHolder(getActivity(), _runtime, 
							getActivity().findViewById(R.id.counter_view_frame), _localityList, position);
					
					
				}
			});
			break;
		case Constants.LOCALITY:
			_localityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> av, View v,
						int position, long id) {
					switch(position) {
					case Constants.LOCALITY_THREADS:
						Log.i(TAG, "Switching list to Threads");
						switchListType(Constants.THREADS);
						localityHolder.disableCallbacks();
					default:
						Log.i(TAG, "Changing Graph to Scenario " + position);
						localityHolder.switchGraph(position);
					}
				}
			});
			break;
		case Constants.THREADS:
			_threadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> av, View v,
						int position, long id) {
					
					
				}
			});
			break;

		default:
			Log.wtf(TAG, "Not supposed to be here.");
		}
		
	
	}
	
	public void switchListType(final int scenario) {
		
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
				_agasList = null;
				_localityList = null;
				_threadsList = null;
				_threadList = null;

				switch(scenario) {
				case Constants.AGAS:
					
					_agasList = (ListView) getActivity().findViewById(R.id.counter_view_list);
					
					_agasAdapter = new AgasListAdapter(getActivity());
					_agasList.setAdapter(_agasAdapter);	
					
					bar.setDisplayShowCustomEnabled(false);
					currentStage = Constants.AGAS;
					setListListener(Constants.AGAS);
					break;
				case Constants.LOCALITIES:
					_localitiesList = (ListView) getActivity().findViewById(R.id.counter_view_list);

							
					//We need to make a call here that will tell us how many localities are running.
					//For now we will send a fake number = 6
					_localitiesAdapter = new LocalitiesListAdapter(getActivity() , 6);
					_localitiesList.setAdapter(_localitiesAdapter);
					
					bar.setDisplayShowCustomEnabled(true);
					currentStage = Constants.LOCALITIES;
					setListListener(Constants.LOCALITIES);
					break;
				case Constants.LOCALITY:
					_localityList = (ListView) getActivity().findViewById(R.id.counter_view_list);
							
					_localityAdapter = new LocalityListAdapter(getActivity());
					_localityList.setAdapter(_localityAdapter);
					
					bar.setDisplayShowCustomEnabled(true);
					currentStage = Constants.LOCALITY;
					setListListener(Constants.LOCALITY);
					break;
				case Constants.THREADS:
					_threadsList = (ListView) getActivity().findViewById(R.id.counter_view_list);
					
					//TODO Need to make a call to see how many threads are running on this
					//Particular locality for now its 4
							
					_threadsAdapter = new ThreadsListAdapter(getActivity(), workerThreads);
					_threadsList.setAdapter(_threadsAdapter);

					bar.setDisplayShowCustomEnabled(true);
					currentStage = Constants.THREADS;
					setListListener(Constants.THREADS);
					break;
				case Constants.THREAD:
					_threadList = (ListView) getActivity().findViewById(R.id.counter_view_list);

					_threadAdapter = new ThreadListAdapter(getActivity());
					_threadList.setAdapter(_threadAdapter);
					
					bar.setDisplayShowCustomEnabled(true);
					currentStage = Constants.THREAD;
					break;
				default:
					Log.wtf(TAG, "Not supposed to be here.");
				}	
			}
		});
		
		
	}
	
	public void setActionBarListener() {
		actionBarText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch(currentStage) {
				case Constants.LOCALITIES:
					switchListType(Constants.AGAS);
					break;
				case Constants.LOCALITY:
					switchListType(Constants.LOCALITIES);
					break;
				case Constants.THREADS:
					switchListType(Constants.LOCALITY);
					break;
				case Constants.THREAD:
					switchListType(Constants.THREADS);
					break;
				default:
					Log.wtf(TAG, "Not supposed to be here.");
					
				}
			}
		});
	}

}
