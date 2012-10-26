package hpx.android.perfcounters;

import hpx.android.HpxCallback;
import hpx.android.R;
import hpx.android.Runtime;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v4.app.Fragment;

public class PerfCounterFragment extends Fragment  {
	
	public static final String ARG_SECTION_NUMBER = "1";
	private ListView _counterList;
	private CounterViewListAdapter _counterListAdapter;
	private Runtime _runtime;
	private GraphingViewHolder _graphHolder;
	private static List<Node> _items;

	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*Initialize our variables here */
		_runtime = new Runtime();
		_items = new ArrayList<Node>();
		fakeList();
		

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
    	
    	/* Bind our views*/
		_counterList = (ListView) getActivity().findViewById(R.id.counter_view_list);
		
		/* Initalize our Graphing View Holder here ONLY!!! */
		_graphHolder =  new GraphingViewHolder(getActivity(), getActivity().findViewById(R.id.counter_view_frame));
		
		/* Initialize our ListAdapter */
		_counterListAdapter = new CounterViewListAdapter(getActivity());
		
    	/* Set our List Adapter */
    	_counterList.setAdapter(_counterListAdapter); 
    	_counterListAdapter.setNodeList(_items);
    	
    	setListListener();

    }
	
	@Override
	public void onStart() {
		super.onStart();
		
        String[] args = {
                "--hpx:threads=2"
            };
        _runtime.init(args);
        
        _runtime.apply("runHelloWorld", "");
        
        callbackHandler();
		
	}
	
	public void setListListener() {
		 
		_counterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				switch(_counterListAdapter.getItemViewType()) {
				case Constants.NODE:
					final Node temp = (Node) av.getItemAtPosition(position);
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							_counterListAdapter.setLocalitiesList(temp.localities);
						}
						
					});
					
					break;
				case Constants.LOCALITIES:
					final Locality temploc = (Locality) av.getItemAtPosition(position);
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							_counterListAdapter.setLocality(temploc);
						}
					});
					
					
					break;
				case Constants.LOCALITY:
					
					/** IN THE FUTURE, WE CAN ADD THE CAPABILITY OF CHANGING THE GRAPH FROM THIS RADIAL MENU **/
					Locality locality = (Locality) _counterListAdapter.getItem(position);
					if(locality.hasGPU) {
//				    	  //Create the Widget Object.
//				    	  final RadialMenuWidget radialMenu = new RadialMenuWidget(getActivity());
//				    	  
//				    	  //Create the Items for the Widget.
//				    	  RadialMenuItem gpuMenuItem = new RadialMenuItem("Toggle GPU","Toggle GPU");
//				    	  
//				    	  //Add the items to the Radial Menu
//				    	  radialMenu.addMenuEntry(gpuMenuItem);
//				    	  
//				    	  //Show on top of our view
//				       	  radialMenu.show(v);
//				       	  
//				       	  gpuMenuItem.setOnMenuItemPressed(new RadialMenuItemClickListener() {
//	
//							public void execute() {
//								//TODO Toggle the GPU
//								
//							}
//				       		  
//				       	  });
					}
					break;
				default:
					Log.wtf("List Listener", "Shouldn't be here"); //We should never be here.
					break;
				}
				
			}
			
		});
		
	 _counterListAdapter.notifyDataChangeOver(); //Hack to make the ViewHolder Pattern Hold up.
	}
	
	public void fakeList() {
		Node node;
		for(int i = 0; i < 69; i++) {
			node =  new Node();
			_items.add(node);
		}
	}
	
	public void callbackHandler() {
		_runtime.enablePerfCounterUpdate("/threads{locality#0/total}/count/instantaneous/active"
				,new HpxCallback() {
					
					@Override
					public void apply(final String arg) {
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								_graphHolder.updateActive(arg);
							}
						});
					}
		});
		
		_runtime.enablePerfCounterUpdate("/threads{locality#0/total}/count/instantaneous/pending"
				, new HpxCallback() {

					@Override
					public void apply(final String arg) {
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								_graphHolder.updatePending(arg);
							}
						});
					}
		});
		
		_runtime.enablePerfCounterUpdate("/threads{locality#0/total}/count/instantaneous/suspended"
				, new HpxCallback() {

					@Override
					public void apply(final String arg) {
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								_graphHolder.updateSuspended(arg);
							}
						});
					}
		});
		
		_runtime.enablePerfCounterUpdate("/threads{locality#0/total}/count/instantaneous/terminated"
				, new HpxCallback() {
					
					@Override
					public void apply(final String arg) {
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								_graphHolder.updateTerminated(arg);
							}
						});
					}
		});
	}

}
